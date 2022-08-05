package org.jsengine.v8.internal.torque; 

import org.jsengine.v8.Internal;

import java.util.HashMap;

public class BuildFlags {
	public static ThreadLocal<Scope> top_ = new ThreadLocal<Scope>();
	private HashMap<String, Boolean> build_flags_ = new HashMap<String, Boolean>();
	
	public BuildFlags() {
		build_flags_.put("V8_SFI_HAS_UNIQUE_ID", Internal.V8_SFI_HAS_UNIQUE_ID);
		build_flags_.put("TAGGED_SIZE_8_BYTES", Internal.TAGGED_SIZE_8_BYTES);
		build_flags_.put("V8_DOUBLE_FIELDS_UNBOXING", Internal.V8_DOUBLE_FIELDS_UNBOXING);
		build_flags_.put("TRUE_FOR_TESTING", true);
		build_flags_.put("FALSE_FOR_TESTING", false);
	}
	
	public static class Scope {
		public BuildFlags value_;
		public Scope previous_;
		
		public Scope(BuildFlags value) {
			value_ = value;
			previous_ = top();
			top(this);
		}
		
		public BuildFlags value() {
			return value_;
		}
	}
	
	public static Scope top() {
		return top_.get();
	}
		
	public static void top(Scope scope) {
		top_.set(scope);
	}
	
	public static BuildFlags get() {
		return top().value();
	}
}