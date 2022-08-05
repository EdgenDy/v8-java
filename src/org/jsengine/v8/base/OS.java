package org.jsengine.v8.base;

import org.jsengine.utils.Pointer;

import org.jsengine.v8.PageAllocator;
import org.jsengine.v8.Base;
import org.jsengine.Globals;
import org.jsengine.Globals.Dword;
import org.jsengine.v8.internal.Address;

public class OS {
	public static long allocate_alignment = 0;
	public static long page_size = 0;
	
	public static boolean g_hard_abort;

	public static int pid = 0xff;

	public static LazyMutex rng_mutex = new LazyMutex();
	
	public static long kAllocationRandomAddressMin = 2147483648L;
	public static long kAllocationRandomAddressMax = 4398046445568L;
	
	public static void initialize(boolean hard_abort, String gc_fake_mmap) {
		g_hard_abort = hard_abort;
	}
	
	public static long allocatePageSize() {
		if(allocate_alignment == 0) {
			allocate_alignment = 65536;
		}
		return allocate_alignment;
	}
	
	public static long commitPageSize() {
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
	
	public static Pointer<Object> getRandomMmapAddr() {
		long address = 0;
		byte address_bytes[] = new byte[8];
		MutexGuard guard = new MutexGuard(rng_mutex.pointer().getValue());
		Base.getPlatformRandomNumberGenerator().nextBytes(address_bytes, address_bytes.length);
		guard.destroy();
		
		address = ((long) address_bytes[7] << 56 ) 
			| ((long) address_bytes[6] & 0xff) << 48
			| ((long) address_bytes[5] & 0xff) << 40
			| ((long) address_bytes[4] & 0xff) << 32
			| ((long) address_bytes[3] & 0xff) << 24
			| ((long) address_bytes[2] & 0xff) << 16
			| ((long) address_bytes[1] & 0xff) << 8
			| ((long) address_bytes[0] & 0xff);
		
		address <<= Globals.kPageSizeBits;
		address += kAllocationRandomAddressMin;
		address &= kAllocationRandomAddressMax;
		//System.out.println("OS::GetRandomMmapAddr(): " + address + " " + (address < kAllocationRandomAddressMax && address > kAllocationRandomAddressMin));
		return new Pointer<Object>(address);
	}
	
	public static enum MemoryPermission {
		kNoAccess,
		kRead,
		kReadWrite,
		kReadWriteExecute,
		kReadExecute
	};
	
	public static Pointer<Object> allocate(Pointer<Object> hint, long size, long alignment, PageAllocator.Permission access) {
		long page_size = allocatePageSize();
		System.out.println("OS::allocate::hint " + hint.getValue());
		hint = Globals.alignedAddress(hint, alignment);
		System.out.println("OS::allocate::hint " + hint.getValue());
		long flags = (access == PageAllocator.Permission.kNoAccess)
                    ? Globals.MEM_RESERVE
                    : Globals.MEM_RESERVE | Globals.MEM_COMMIT;
                    
		long protect = Base.getProtectionFromMemoryPermission(access);
		
		Pointer<Object> base = Base.randomizedVirtualAlloc(size, flags, protect, hint);
		if (base == Globals.nullptr) return Globals.nullptr;
		
		long aligned_base = Globals.roundUp(base.getValue(), alignment);
		
		if (base.getValue() == aligned_base) return base;
		System.out.println("OS::allocate::base " + base.getValue() + " OS::allocate::aligned_base " + aligned_base);
		
		return base;
	}
	
	public static boolean free(Pointer<Object> address, long size) {
		return Globals.virtualFree(address, size, Globals.MEM_RELEASE) != false;
	}
	
	public static boolean setPermissions(Pointer<Object> address, long size, PageAllocator.Permission access) {
		if (access == PageAllocator.Permission.kNoAccess) {
			return Globals.virtualFree(address, size, Globals.MEM_DECOMMIT) != false;
		}
		long protect = Base.getProtectionFromMemoryPermission(access);
		return Globals.virtualAlloc(address, size, Globals.MEM_COMMIT, protect) != null;
	}
	
	public static void abort() {
		System.exit(1);
	}
}