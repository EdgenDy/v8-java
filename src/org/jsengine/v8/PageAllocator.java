package org.jsengine.v8;

import org.jsengine.utils.Pointer;
import org.jsengine.v8.internal.Address;

public abstract class PageAllocator {
    public abstract long allocatePageSize();
    public abstract long commitPageSize();
    public abstract void setRandomMmapSeed(int seed);
    public abstract Pointer<Object> getRandomMmapAddr();
    public abstract Pointer<Object> allocatePages(Pointer<Object> address, long length, long alignment, Permission permissions);
    public abstract boolean freePages(Pointer<Object> address, long size);
    public abstract boolean setPermissions(Pointer<Object> address, long length, Permission permissions);
    
    public static enum Permission {
		kNoAccess,
		kRead,
		kReadWrite,
		kReadWriteExecute,
		kReadExecute
	};
}
