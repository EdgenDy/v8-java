package org.jsengine.v8.internal;

import org.jsengine.v8.Internal;
import org.jsengine.v8.base.OS;

class Isolate {
	private int id_;
	Isolate() {
		
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