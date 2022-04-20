package org.jsengine.v8;

public class Isolate {
	private Isolate(org.jsengine.v8.internal.Isolate isolate) {
		
	}
	
	public static class CreateParams {
		public ArrayBuffer.Allocator array_buffer_allocator;
		
		public CreateParams() {
			array_buffer_allocator = null;
		}
	}
	
	// src\api\api.cc:7875
	// include\v8.h:7887
	public static Isolate allocate() {
		return new Isolate(org.jsengine.v8.internal.Isolate.newInstance());
	}
	
	// include\v8.h:7903
	public static Isolate newInstance(CreateParams params) {
		Isolate isolate = allocate();
		//initialize(isolate, params);
		return isolate;
	}
} 