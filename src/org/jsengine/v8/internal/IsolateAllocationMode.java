package org.jsengine.v8.internal;

public enum IsolateAllocationMode {
	kInCppHeap(0),
	kInV8Heap(1),
	
	// compress pointer
	kDefault(1);
	
	int value_ = 0;
	IsolateAllocationMode(int value) {
		this.value_ = value;
	}
	
	public int value() {
		return value_;
	}
}