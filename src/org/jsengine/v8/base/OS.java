package org.jsengine.v8.base;

import org.jsengine.v8.Base;
import org.jsengine.Globals;

public class OS {
	public static int allocate_alignment = 0;
	public static int page_size = 0;
	
	public static boolean g_hard_abort;

	public static int pid = 0xff;

	public static LazyMutex rng_mutex = new LazyMutex();
	
	//TODO: use BigInt instead
	//public static int kAllocationRandomAddressMin = 0x0000000080000000;
	//public static int kAllocationRandomAddressMax = 0x000003FFFFFF0000;
	
	public static int kAllocationRandomAddressMin = 0x04000000;
	public static int kAllocationRandomAddressMax = 0x3FFF0000;
	
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
			Base.getPlatformRandomNumberGenerator().setSeed(seed);
		}
	}
	
	public static int getCurrentProcessId() {
		return pid;
	}
	
	public static int vSNPrintF(char[] str, int length, String format, Object ...args) {
		int n = Globals._vsnprintf_s(str, length, 0, format, args);
		
		return n;
	}
	
	public static Object getRandomMmapAddr() {
		long address = 0;
		byte address_bytes[] = new byte[8];
		MutexGuard guard = new MutexGuard(rng_mutex.pointer().getValue());
		Base.getPlatformRandomNumberGenerator().nextBytes(address_bytes, address_bytes.length);
		guard.destroy();
		
		address = ((long) address_bytes[7] << 56 ) 
			| ((long) address_bytes[6] & 0xff) << 48
			| ((long) address_bytes[5] & 0xff) << 40
			| ((long) address_bytes[7] & 0xff) << 32
			| ((long) address_bytes[7] & 0xff) << 24
			| ((long) address_bytes[7] & 0xff) << 16
			| ((long) address_bytes[7] & 0xff) << 8
			| ((long) address_bytes[7] & 0xff);
		
		address <<= Globals.kPageSizeBits;
		address += kAllocationRandomAddressMin;
		address &= kAllocationRandomAddressMax;
		return (Object) address;
	}
	
	public static Object allocate(Object hint, int size, int alignment, MemoryPermission access) {
		return null;
	} 
	
	public static enum MemoryPermission {
		kNoAccess,
		kRead,
		kReadWrite,
		kReadWriteExecute,
		kReadExecute
	};
}