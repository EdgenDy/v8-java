package org.jsengine.v8;

public abstract class PageAllocator {
    public abstract int allocatePageSize();
    public abstract int commitPageSize();
    public abstract void setRandomMmapSeed(int seed);
}
