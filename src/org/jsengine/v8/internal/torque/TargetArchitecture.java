package org.jsengine.v8.internal.torque; 

import org.jsengine.v8.internal.Torque;
import org.jsengine.v8.Internal;

public class TargetArchitecture {
	private int tagged_size_;
	private int raw_ptr_size_;
	public static ThreadLocal<Scope> top_ = new ThreadLocal<Scope>();
	public static class Scope {
		public TargetArchitecture value_;
		public Scope previous_;
		
		public Scope(boolean value) {
			value_ = new TargetArchitecture(value);
			previous_ = top();
			top(this);
		}
		
		public TargetArchitecture value() {
			return value_;
		}
	}
	
	public TargetArchitecture(boolean force_32bit) {
		tagged_size_ = force_32bit ? 4 : Internal.kTaggedSize;
		raw_ptr_size_ = force_32bit ? 4 : Internal.kSystemPointerSize;
	}
	
	public static Scope top() {
		return top_.get();
	}
		
	public static void top(Scope scope) {
		top_.set(scope);
	}
	
	public static TargetArchitecture get() {
		return top().value();
	}

	public static int taggedSize() {
		return get().tagged_size_;
	}

	public static int rawPtrSize() {
		return get().raw_ptr_size_;
	}
}