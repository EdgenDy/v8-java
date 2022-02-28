package org.jsengine.v8.base;

import org.jsengine.v8.Base;

// template for Mutex and NullBehavior enum
public class LockGuard {
	private Mutex mutex_;
	protected Base.NullBehavior Behavior = Base.NullBehavior.kRequireNotNull;
	
	public LockGuard(Mutex mutex) {
		mutex_ = mutex;
		if(has_mutex())
			mutex_.lock();
	}
	
	public void destroy() {
		if(has_mutex())
			mutex_.unlock();
	}
	
	public boolean has_mutex() {
		return Behavior == Base.NullBehavior.kRequireNotNull || mutex_ != null;
	}
}