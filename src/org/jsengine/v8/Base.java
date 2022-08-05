package org.jsengine.v8;

import org.jsengine.v8.base.OS;
import org.jsengine.v8.base.Mutex;
import org.jsengine.v8.base.AtomicWord;
import org.jsengine.v8.base.Bits;
import org.jsengine.v8.base.RandomNumberGenerator;
import org.jsengine.v8.base.LeakyObject;

import org.jsengine.v8.PageAllocator;

import org.jsengine.Globals;
import org.jsengine.Globals.Handle;
import org.jsengine.utils.Var;
import org.jsengine.utils.Pointer;

import java.util.function.Function;
import java.util.concurrent.atomic.AtomicInteger;

public class Base {
    public static Method g_print_stack_trace;

    // src\base\logging.h:57
    public static void setPrintStackTrace(Base.Method print_stack_trace) {
        g_print_stack_trace = print_stack_trace;
    }
    public static interface Method {
        public void invoke();
    }
	
	
	public static void InitializeNativeHandle(Mutex mutex) {
		mutex.native_handle_ = new Mutex.NativeHandle();
	}
	
	public static enum NullBehavior { 
		kRequireNotNull, 
		kIgnoreIfNull
	};
	
	public static Handle kNoThread = new Handle();
	
	public static int relaxedLoad(AtomicWord ptr) {
		return ptr.get();
	}
	
	public static int acquireLoad(AtomicWord ptr) {
		return ptr.get();
	}
	
	public static class OnceType extends AtomicInteger {
		public OnceType() {
			super(0);
		}
	}
	
	public static enum OnceState {
		ONCE_STATE_UNINITIALIZED(0),
		ONCE_STATE_EXECUTING_FUNCTION(1),
		ONCE_STATE_DONE(2);
		
		int value_;
		OnceState(int value) {
			value_ = value;
		}
		
		public int value() {
			return value_;
		}
	};

	public static void callOnce(OnceType once, Function init_func) {
		if(once.get() != OnceState.ONCE_STATE_DONE.value()) {
			callOnceImpl(once, init_func);
		}
	}

	public static void callOnce(OnceType once, final Function init_func, final Object arg) {
		if(once.get() != OnceState.ONCE_STATE_DONE.value()) {
			callOnceImpl(once, new Function<Object, Void>() {
				public Void apply(Object object) {
					init_func.apply(arg);
					return null;
				}
			});
		}
	}
	
	public static void callOnceImpl(OnceType once, Function init_func) {
		if (once.get() == OnceState.ONCE_STATE_DONE.value()) {
			return;
		}
		int expected = OnceState.ONCE_STATE_UNINITIALIZED.value();
		if (once.get() == expected) {
			once.set(OnceState.ONCE_STATE_EXECUTING_FUNCTION.value());
			init_func.apply(null);
			once.set(OnceState.ONCE_STATE_DONE.value());
		}
		else {
			while(once.get() == OnceState.ONCE_STATE_EXECUTING_FUNCTION.value()) {
				try {
					Thread.sleep(0);
				} catch(Exception e) {
					e.printStackTrace();
				}
			}
		}
	}
	
	public static int hash_range(int first, int last) {
		int seed = 0;
		for (; first != last; ++first) {
			seed = hash_combine(seed, first);
		}
		return seed;
	}
	
	public static int hash_combine(int seed, int value) {
		/**int m = 0xC6A4A7935BD1E995;
		int r = 47;

		value *= m;
		value ^= value >> r;
		value *= m;

		seed ^= value;
		seed *= m;**/
		
		// we use hashing for arch32 bit instead of 64
		// long can't hold 0xC6A4A7935BD1E995 as a long value
		
		int c1 = 0xCC9E2D51;
		int c2 = 0x1B873593;

		value *= c1;
		value = Bits.RotateRight32(value, 15);
		value *= c2;

		seed ^= value;
		seed = Bits.RotateRight32(seed, 13);
		seed = seed * 5 + 0xE6546B64;
		
		return seed;
	}
	
	public static LeakyObject<RandomNumberGenerator> object;
	public static RandomNumberGenerator getPlatformRandomNumberGenerator() {
		if(object == null) object = new LeakyObject<RandomNumberGenerator>(new RandomNumberGenerator());
		return object.get();
	}
	
	public static long getProtectionFromMemoryPermission(PageAllocator.Permission access) {
		if (access == PageAllocator.Permission.kNoAccess)
			return Globals.PAGE_NOACCESS;
		if (access == PageAllocator.Permission.kRead)
			return Globals.PAGE_READONLY;
		if (access == PageAllocator.Permission.kReadWrite)
			return Globals.PAGE_READWRITE;
		if (access == PageAllocator.Permission.kReadWriteExecute)
			return Globals.PAGE_EXECUTE_READWRITE;
		if (access == PageAllocator.Permission.kReadExecute)
			return Globals.PAGE_EXECUTE_READ;
		throw new RuntimeException("unreachable");
	}
	
	public static Pointer<Object> randomizedVirtualAlloc(long size, long flags, long protect, Pointer<Object> hint) {
		Pointer<Object> base = Globals.nullptr;

		if (protect != Globals.PAGE_READWRITE) {
			base = Globals.virtualAlloc(hint, size, flags, protect);
		}
		
		return base;
	}
	
	public static double kMaxLoadFactorForRandomization = 0.40;
}
