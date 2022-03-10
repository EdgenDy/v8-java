package org.jsengine.v8.base;

import org.jsengine.Globals.Handle;
import org.jsengine.Globals.Dword;
import org.jsengine.Globals;

import org.jsengine.v8.Base;
import org.jsengine.utils.Var;

import java.util.concurrent.Semaphore;

//src\base\platform\platform.h:312
public abstract class Thread {
	private int stack_size_;
	private Semaphore start_semaphore_;
	private PlatformData data_;
	private String name_; // TODO: convert this to char array.
	
	public abstract void run();
	
	public static class LocalStorageKey extends Var<Integer> {
		public LocalStorageKey(int value) {
			super(value);
		}
	}
	
	public static class Options {
		private String name_;
		private int stack_size_ = 0;
		
		public Options(String name, int stack_size) {
			this.name_ = name;
			this.stack_size_ = stack_size;
		}
		
		public Options(String name) {
			this(name, 0);
		}
		
		public Options() {
			this.name_ = "v8:<unknown>";
			this.stack_size_ = 0;
		}
		
		public String name() {
			return this.name_;
		}
		
		public int stack_size() {
			return this.stack_size_;
		}
	}
	
	// src\base\platform\platform-win32.cc:1332
	public Thread(Options options) {
		this.stack_size_ = options.stack_size();
		this.start_semaphore_ = null;
		this.data_ = new PlatformData(Base.kNoThread);
		set_name(options.name());
	}
	
	public void set_name(String name) {
		this.name_ = name; // TODO: convert this String to char[]
	}
	
	public static class PlatformData {
		public Handle thread_;
		public int thread_id_;
		public PlatformData(Handle thread) {
			this.thread_ = thread;
		}
	}
	
	public static LocalStorageKey createThreadLocalKey() {
		Dword result = Globals.tlsAlloc();
		return new LocalStorageKey(result.getValue());
	}
}