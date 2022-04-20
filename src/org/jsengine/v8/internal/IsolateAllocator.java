package org.jsengine.v8.internal;

import org.jsengine.v8.PageAllocator;
import org.jsengine.v8.Internal;

import org.jsengine.Globals;

public class IsolateAllocator {
	public IsolateAllocator(IsolateAllocationMode mode) {
		if(mode.value() == IsolateAllocationMode.kInV8Heap.value()) {
			Address heap_reservation_address = initReservation();
		}
	}
	
	private Address initReservation() {
		PageAllocator platform_page_allocator = Internal.getPlatformPageAllocator();
		
		long reservation_size = Internal.kPtrComprHeapReservationSize;
		long base_alignment = Internal.kPtrComprIsolateRootAlignment;
		
		int kMaxAttempts = 4;
		
		for (int attempt = 0; attempt < kMaxAttempts; ++attempt) {
			Address hint = new Address(Globals.roundDown((long) platform_page_allocator.getRandomMmapAddr(), base_alignment) + Internal.kPtrComprIsolateRootBias);
			VirtualMemory padded_reservation = new VirtualMemory(platform_page_allocator, reservation_size * 2, hint);
		}
		
		return null;
	}
}