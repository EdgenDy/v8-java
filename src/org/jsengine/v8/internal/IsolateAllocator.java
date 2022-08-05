package org.jsengine.v8.internal;

import org.jsengine.v8.base.BoundedPageAllocator;
import org.jsengine.v8.PageAllocator;
import org.jsengine.v8.Internal;
import org.jsengine.utils.Pointer;
import org.jsengine.Globals;

public class IsolateAllocator {
	private Pointer<Object> isolate_memory_ = Globals.nullptr;
	private VirtualMemory reservation_;
	private BoundedPageAllocator page_allocator_instance_;
	private PageAllocator page_allocator_ = null;
	
	public IsolateAllocator(IsolateAllocationMode mode) {
		if(mode.value() == IsolateAllocationMode.kInV8Heap.value()) {
			Address heap_reservation_address = initReservation();
			commitPagesForIsolate(heap_reservation_address);
			System.out.println("IsolateAllocator::heap_reservation_address: " + heap_reservation_address.value());
		}
	}
	
	public Pointer<Object> isolate_memory() {
		return isolate_memory_;
	}
	
	private Address initReservation() {
		PageAllocator platform_page_allocator = Internal.getPlatformPageAllocator();
		
		long reservation_size = Internal.kPtrComprHeapReservationSize;
		long base_alignment = Internal.kPtrComprIsolateRootAlignment;
		
		int kMaxAttempts = 4;
		
		for (int attempt = 0; attempt < kMaxAttempts; ++attempt) {
			//System.out.println("IsolateAllocator::initReservation::Address->value() " + new Address(Globals.roundDown(platform_page_allocator.getRandomMmapAddr().getValue(), base_alignment) + Internal.kPtrComprIsolateRootBias).value());
			Address hint = new Address(Globals.roundDown(platform_page_allocator.getRandomMmapAddr().getValue(), base_alignment) + Internal.kPtrComprIsolateRootBias);
			
			//System.out.println("IsolateAllocator::initReservation::hint " + hint.value());
			System.out.println("IsolateAllocator::initReservation::Address->value() " + new Address(Globals.roundDown(platform_page_allocator.getRandomMmapAddr().getValue(), base_alignment) + Internal.kPtrComprIsolateRootBias).value());
			VirtualMemory padded_reservation = new VirtualMemory(platform_page_allocator, reservation_size * 2, new Pointer<Object>(hint));
			
			if (!padded_reservation.isReserved()) break;
			
			Address address = new Address(Globals.roundUp(padded_reservation.address().value() + Internal.kPtrComprIsolateRootBias, base_alignment) - Internal.kPtrComprIsolateRootBias);
			
			boolean overreserve = (attempt == kMaxAttempts - 1);
			
			if (overreserve) {
				if (padded_reservation.inVM(address, reservation_size)) {
					reservation_ = padded_reservation;
					return address;
				}
			}
			else {
				padded_reservation.free();

				VirtualMemory reservation = new VirtualMemory(platform_page_allocator, reservation_size, new Pointer<Object>(address));
				if (!reservation.isReserved()) break;

				Address aligned_address = new Address(Globals.roundUp(reservation.address().value() + Internal.kPtrComprIsolateRootBias, base_alignment) - Internal.kPtrComprIsolateRootBias);

				if (reservation.address().value() == aligned_address.value()) {
					reservation_ = reservation;
					return aligned_address;
				}
			}
		}
		return Internal.kNullAddress;
	}
	
	public void commitPagesForIsolate(Address heap_address) {
		Address isolate_root = new Address(heap_address.value() + Internal.kPtrComprIsolateRootBias);
		PageAllocator platform_page_allocator = Internal.getPlatformPageAllocator();
		
		long page_size = Globals.roundUp(1L << Globals.kPageSizeBits, platform_page_allocator.allocatePageSize());
		page_allocator_instance_ = new BoundedPageAllocator(platform_page_allocator, heap_address, Internal.kPtrComprHeapReservationSize, page_size);
		page_allocator_ = page_allocator_instance_;
		
		Address isolate_address = new Address(isolate_root.value() - Isolate.isolate_root_bias());
		Address isolate_end = new Address(isolate_address.value() + 50); // sizeof(Isolate);
		
		Address reserved_region_address = new Address(Globals.roundDown(isolate_address.value(), page_size));
		long reserved_region_size = Globals.roundUp(isolate_end.value(), page_size) - reserved_region_address.value();

		page_allocator_instance_.allocatePagesAt(reserved_region_address, reserved_region_size, PageAllocator.Permission.kNoAccess);
		
		long commit_page_size = platform_page_allocator.commitPageSize();
		Address committed_region_address = new Address(Globals.roundDown(isolate_address.value(), commit_page_size));
		
		long committed_region_size = Globals.roundUp(isolate_end.value(), commit_page_size) - committed_region_address.value();
		
		reservation_.setPermissions(committed_region_address, committed_region_size, PageAllocator.Permission.kReadWrite);
		
		if (Heap.shouldZapGarbage()) {
			for (Address address = committed_region_address; address.value() < committed_region_size; address.add(Internal.kSystemPointerSize)) {
				throw new RuntimeException("not yet implemented at IsolateAllocator::commitPagesForIsolate().");
				//base::Memory<Address>(address) = static_cast<Address>(kZapValue);
			}
		}
		
		isolate_memory_ = Pointer.from(isolate_address);
	}
}