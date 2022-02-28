package org.jsengine.v8.base;

public class MutexGuard extends LockGuard {
	public MutexGuard(Mutex mutex) {
		super(mutex);
	}
}