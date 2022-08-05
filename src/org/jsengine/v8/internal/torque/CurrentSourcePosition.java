package org.jsengine.v8.internal.torque; 

public class CurrentSourcePosition {
	public static ThreadLocal<Scope> top_ = new ThreadLocal<Scope>();
	
	public static class Scope {
		public SourcePosition value_;
		public Scope previous_;
		
		public Scope(SourcePosition value) {
			value_ = value;
			previous_ = top();
			top(this);
		}
		
		public SourcePosition value() {
			return value_;
		}
		
		public void value(SourcePosition val) {
			value_ = val;
		}
	}
	
	public static Scope top() {
		return top_.get();
	}
		
	public static void top(Scope scope) {
		top_.set(scope);
	}
	
	public static SourcePosition get() {
		return top().value();
	}
	
	public static void get(SourcePosition val) {
		top().value(val);
	}
	
	public static boolean hasScope() {
		return top() != null;
	}
}