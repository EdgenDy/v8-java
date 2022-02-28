package org.jsengine.v8.base;

public class OS {
	public static int allocate_alignment = 0;
	public static int page_size = 0;
	
	public static boolean g_hard_abort;

	public static int pid = 0xff;

	public static LazyMutex rng_mutex = new LazyMutex();
	
	public static void initialize(boolean hard_abort, String gc_fake_mmap) {
		g_hard_abort = hard_abort;
	}
	
	public static int allocatePageSize() {
		if(allocate_alignment == 0) {
			allocate_alignment = 65536;
		}
		return allocate_alignment;
	}
	
	public static int commitPageSize() {
		if(page_size == 0) {
			page_size = 4096;
		}
		return page_size;
	}

	public static void setRandomMmapSeed(int seed) {
		if(seed != 0) {
			MutexGuard guard = new MutexGuard(rng_mutex.pointer().getValue());
			// TODO: implement this method
		}
	}
	
	public static int getCurrentProcessId() {
		return pid;
	}
}