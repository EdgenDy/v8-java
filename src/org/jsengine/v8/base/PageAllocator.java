package org.jsengine.v8.base;

import org.jsengine.utils.Pointer;
import org.jsengine.Globals;
import org.jsengine.v8.internal.Address;

public class PageAllocator extends org.jsengine.v8.PageAllocator {
    private long allocate_page_size_;
    private long commit_page_size_;
    
    public PageAllocator() {
    	allocate_page_size_ = OS.allocatePageSize();
    	commit_page_size_ = OS.commitPageSize();
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
	
	public boolean setPermissions(Pointer<Object> address, long size, PageAllocator.Permission access) {
		return OS.setPermissions(address, size, access);
	}
}
