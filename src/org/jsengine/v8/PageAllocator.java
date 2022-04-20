package org.jsengine.v8;

public abstract class PageAllocator {
    public abstract int allocatePageSize();
    public abstract int commitPageSize();
    public abstract void setRandomMmapSeed(int seed);
    public abstract Object getRandomMmapAddr();
    
    public static enum Permission {
		kNoAccess,
		kRead,
		kReadWrite,
		kReadWriteExecute,
		kReadExecute
	};
}
