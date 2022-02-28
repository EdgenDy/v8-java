package org.jsengine.v8.platform;

public class DelayedTaskQueue {
	private DefaultPlatform.TimeFunction time_function_;
	public DelayedTaskQueue(DefaultPlatform.TimeFunction time_function) {
		this.time_function_ = time_function;
	}
}