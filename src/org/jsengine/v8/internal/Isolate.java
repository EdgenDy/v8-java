package org.jsengine.v8.internal;

import org.jsengine.v8.Internal;
import org.jsengine.v8.base.OS;
import org.jsengine.v8.base.Thread;

import org.jsengine.utils.Pointer;

public class Isolate {
	public static Thread.LocalStorageKey per_isolate_thread_data_key_;
	public static Thread.LocalStorageKey isolate_key_;
	
	private IsolateData isolate_data_;
	private IsolateAllocator isolate_allocator_;
	private Heap heap_;
	private int id_;
	
	private AccountingAllocator allocator_ = null;
	
	public Isolate(IsolateAllocator isolate_allocator) {
		heap_ = new Heap(this);
		
		isolate_data_ = new IsolateData(this);
		isolate_allocator_ = isolate_allocator;
		id_ = Internal.isolate_counter.getAndAdd(1);
		allocator_ = Internal.FLAG_trace_zone_stats.getValue()
			? new VerboseAccountingAllocator(heap_, 256 * Internal.KB)
			: new AccountingAllocator();
	}
	
	public static void initializeOncePerProcess() {
		isolate_key_ = Thread.createThreadLocalKey();
		per_isolate_thread_data_key_ = Thread.createThreadLocalKey();
	}
	
	// src\execution\isolate.h:512
	// src\execution\isolate.cc:2815
	public static Isolate newInstance(IsolateAllocationMode mode) {
		IsolateAllocator isolate_allocator = new IsolateAllocator(mode);
		Pointer<Object> isolate_ptr = isolate_allocator.isolate_memory();
		
		Isolate isolate = new Isolate(isolate_allocator);
		isolate_ptr.dereference(isolate);
		return null;
	}
	public static Isolate newInstance() {
		return newInstance(IsolateAllocationMode.kDefault);
	}
	
	
	public int id() {
		return id_;
	}
	
	public static String getTurboCfgFileName(Isolate isolate) {
		if(Internal.FLAG_trace_turbo_cfg_file.getValue() == null) {
			StringBuilder os = new StringBuilder();
			os.append("turbo-");
			os.append(OS.getCurrentProcessId());
			os.append("-");
			
			if (isolate != null) {
				os.append(isolate.id());
			}
			else {
				os.append("any");
			}
			
    		os.append(".cfg");
			return os.toString();
		}
		else {
			return Internal.FLAG_trace_turbo_cfg_file.getValue();
		}
	}
	
	// address of isolate_data_
	public static long isolate_root_bias() {
		return 1L;
	}
}