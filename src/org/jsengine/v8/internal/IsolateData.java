package org.jsengine.v8.internal;

public class IsolateData {
	private StackGuard stack_guard_;
	
	public IsolateData(Isolate isolate) {
		stack_guard_ = new StackGuard(isolate);
	}
}