package org.jsengine.v8.internal;

public class StackGuard {
	private Isolate isolate_;
	
	public StackGuard(Isolate isolate) {
		isolate_ = isolate;
	}
}