package org.jsengine.v8.internal;

import org.jsengine.v8.Internal;
import org.jsengine.v8.base.OS;
import org.jsengine.v8.base.Thread;

class Isolate {
	public static Thread.LocalStorageKey per_isolate_thread_data_key_;
	public static Thread.LocalStorageKey isolate_key_;
	private int id_;
	
	public static void initializeOncePerProcess() {
		isolate_key_ = Thread.createThreadLocalKey();
		per_isolate_thread_data_key_ = Thread.createThreadLocalKey();
		
		System.out.println(isolate_key_ + " " + per_isolate_thread_data_key_);
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
}