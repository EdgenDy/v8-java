package org.jsengine.v8.base;

import org.jsengine.v8.PageAllocator;
import org.jsengine.v8.internal.Address;
import org.jsengine.utils.Pointer;

public class BoundedPageAllocator extends PageAllocator {
	private long allocate_page_size_;
	private long commit_page_size_;
	private PageAllocator page_allocator_;
	private RegionAllocator region_allocator_;
	
	public BoundedPageAllocator(PageAllocator page_allocator, Address start, long size, long allocate_page_size) {
		allocate_page_size_ = allocate_page_size;
		commit_page_size_ = page_allocator.commitPageSize();
		page_allocator_ = page_allocator;
		region_allocator_ = new RegionAllocator(start, size, allocate_page_size_);
	}
	
	public long allocatePageSize() {
        return allocate_page_size_;
    }
    
    public long commitPageSize() {
        return commit_page_size_;
    }

    public void setRandomMmapSeed(int seed) {
        OS.setRandomMmapSeed(seed);
    }

	public Pointer<Object> getRandomMmapAddr() {
		return OS.getRandomMmapAddr();
	}
	
	public Pointer<Object> allocatePages(Pointer<Object> hint, long size, long alignment, Permission access) {
		return OS.allocate(hint, size, alignment, access);
	}
	
	
	public boolean freePages(Pointer<Object> address, long size) {
		return OS.free(address, size);
	}
	
	public boolean allocatePagesAt(Address address, long size, PageAllocator.Permission access) {
		if (!region_allocator_.allocateRegionAt(address, size)) {
			return false;
		}
		return true;
	}
	
	public boolean setPermissions(Pointer<Object> address, long size, Permission access) {
		return page_allocator_.setPermissions(address, size, access);
	}
}