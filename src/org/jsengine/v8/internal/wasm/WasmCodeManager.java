package org.jsengine.v8.internal.wasm;

import java.util.concurrent.atomic.AtomicInteger;

public class WasmCodeManager {
	private WasmMemoryTracker memory_tracker_;
	private int max_committed_code_space_;
	private AtomicInteger critical_committed_code_space_;
	
	public WasmCodeManager(WasmMemoryTracker memory_tracker, int max_committed) {
		memory_tracker_ = memory_tracker;
		max_committed_code_space_ = max_committed;
		critical_committed_code_space_ = new AtomicInteger(max_committed / 2);
	}
}