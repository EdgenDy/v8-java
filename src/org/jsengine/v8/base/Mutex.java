package org.jsengine.v8.base;

import org.jsengine.v8.Base;
import java.util.concurrent.Semaphore;

public class Mutex {
	public NativeHandle native_handle_ = null;
	public Mutex() {
		Base.InitializeNativeHandle(this);
	}
	
	public void lock() {
		native_handle_.lock();
	}
	
	public void unlock() {
		native_handle_.release();
	}
	
	public boolean tryLock() {
		return native_handle_.tryAcquire();
	}
	
	public NativeHandle native_handle() {
		return native_handle_;
	}
	
	public static class NativeHandle extends Semaphore {
		public NativeHandle() {
			super(1);
		}

		public void lock() {
			try {
				this.acquire();
			} catch(InterruptedException e) {
				e.printStackTrace();
			}
		}
		
		public void unlock() {
			this.release();
		}
	}
}