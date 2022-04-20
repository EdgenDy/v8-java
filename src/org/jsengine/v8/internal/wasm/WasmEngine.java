package org.jsengine.v8.internal.wasm;

import org.jsengine.v8.base.LeakyObject;
import org.jsengine.v8.Internal;

public class WasmEngine {
	private WasmCodeManager code_manager_;
	private WasmMemoryTracker memory_tracker_ = new WasmMemoryTracker();
	
	public WasmEngine() {
		code_manager_ = new WasmCodeManager(memory_tracker_, Internal.FLAG_wasm_max_code_space.getValue() * Internal.MB);
	}
	
	public static LeakyObject<WasmEngine> object = null;
	public static WasmEngine getSharedWasmEngine() {
		if(object == null) object = new LeakyObject<WasmEngine>(new WasmEngine());
		return object.get();
	}
	
	public static void initializeOncePerProcess() {
		if (!Internal.FLAG_wasm_shared_engine.getValue()) return;
		getSharedWasmEngine();
	}
}