package org.jsengine.v8.internal.torque; 

public class CurrentSourceFile {
	public static ThreadLocal<Scope> top_ = new ThreadLocal<Scope>();
	
	public static class Scope {
		public SourceId value_;
		public Scope previous_;
		
		public Scope(SourceId value) {
			value_ = value;
			previous_ = top();
			top(this);
		}
		
		public SourceId value() {
			return value_;
		}
	}
	
	public static Scope top() {
		return top_.get();
	}
		
	public static void top(Scope scope) {
		top_.set(scope);
	}
	
	public static SourceId get() {
		return top().value();
	}
}