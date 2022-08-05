package org.jsengine.v8.internal;

import org.jsengine.v8.PageAllocator;
import org.jsengine.utils.Pointer;
import org.jsengine.Globals;
import org.jsengine.v8.Internal;
import org.jsengine.v8.base.AddressRegion;

public class VirtualMemory {
	private PageAllocator page_allocator_ = null;
	private AddressRegion region_;
	
	public VirtualMemory(PageAllocator page_allocator, long size, Pointer<Object> hint, long alignment) {
		page_allocator_ = page_allocator;
		long page_size = page_allocator_.allocatePageSize();
		alignment = Globals.roundUp(alignment, page_size);
		Address address = new Address(Internal.allocatePages(page_allocator_, hint, Globals.roundUp(size, page_size), alignment,
                    PageAllocator.Permission.kNoAccess).getValue());
                    
		if (address.value() != Internal.kNullAddress.value()) {
			region_ = new AddressRegion(address, size);
		}
	}
	
	public VirtualMemory(PageAllocator page_allocator, long size, Pointer<Object> hint) {
		this(page_allocator, size, hint, 1);         	
	}
	
	public boolean isReserved() { 
		return region_.begin().value() != Internal.kNullAddress.value();
	}
	
	public Address address() {
		return region_.begin();
	}
	
	public boolean inVM(Address address, long size) {
		return region_.contains(address, size);
	}
	
	public void reset() {
		page_allocator_ = null;
		region_ = new AddressRegion();
	}
	
	public void free() {
		PageAllocator page_allocator = page_allocator_;
		AddressRegion region = region_;
		reset();
		Internal.freePages(page_allocator, new Pointer<Object>(region.begin().value()), Globals.roundUp(region.size(), page_allocator.allocatePageSize()));
	}
	
	public boolean setPermissions(Address address, long size, PageAllocator.Permission access) {
		boolean result = Internal.setPermissions(page_allocator_, Pointer.from(address), size, access);
		return result;
	}
}