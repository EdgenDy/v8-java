package org.jsengine.v8.internal;

import org.jsengine.V8.MemoryPressureLevel;

import java.util.concurrent.atomic.AtomicReference;
import java.util.HashMap;

public class Heap {
	private Isolate isolate_ = null;
	private AtomicReference<MemoryPressureLevel> memory_pressure_level_;
	
	public Heap(Isolate isolate) {
		isolate_ = isolate;
		memory_pressure_level_ = new AtomicReference<MemoryPressureLevel>(MemoryPressureLevel.kNone);
	}
	
	public Isolate isolate() {
		return isolate_;
	}
	
	public static boolean shouldZapGarbage() {
		return false;
	}
	/*
	public class PretenuringFeedbackMap extends HashMap<AllocationSite, Long> {
		public PretenuringFeedbackMap() {
			
		}
	}*/
}