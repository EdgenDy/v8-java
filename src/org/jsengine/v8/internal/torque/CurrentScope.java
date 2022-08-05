package org.jsengine.v8.internal.torque; 

public class CurrentScope {
	public static ThreadLocal<Scope> top_ = new ThreadLocal<Scope>();
	
	public static class Scope {
		public org.jsengine.v8.internal.torque.Scope value_;
		public Scope previous_;
		
		public Scope(org.jsengine.v8.internal.torque.Scope value) {
			value_ = value;
			previous_ = top();
			top(this);
		}
		
		public org.jsengine.v8.internal.torque.Scope value() {
			return value_;
		}
	}
	
	public static Scope top() {
		return top_.get();
	}
		
	public static void top(Scope scope) {
		top_.set(scope);
	}
	
	public static org.jsengine.v8.internal.torque.Scope get() {
		return top().value();
	}
}