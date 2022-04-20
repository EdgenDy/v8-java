package org.jsengine.v8.base;

public class PageAllocator extends org.jsengine.v8.PageAllocator {
    private int allocate_page_size_;
    private int commit_page_size_;
    
    public PageAllocator() {
    	allocate_page_size_ = OS.allocatePageSize();
    	commit_page_size_ = OS.commitPageSize();
    }

    public int allocatePageSize() {
        return allocate_page_size_;
    }
    public int commitPageSize() {
        return commit_page_size_;
    }

    public void setRandomMmapSeed(int seed) {
        OS.setRandomMmapSeed(seed);
    }

	public Object getRandomMmapAddr() {
		return OS.getRandomMmapAddr();
	}

}
