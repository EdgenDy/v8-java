package org.jsengine.v8.internal.torque; 

public class CurrentAst {
	public static ThreadLocal<Scope> top_ = new ThreadLocal<Scope>();
	
	public static class Scope {
		public Ast value_;
		public Scope previous_;
		
		public Scope(Ast value) {
			value_ = value;
			previous_ = top();
			top(this);
		}
		
		public Ast value() {
			return value_;
		}
	}
	
	public static Scope top() {
		return top_.get();
	}
		
	public static void top(Scope scope) {
		top_.set(scope);
	}
	
	public static Ast get() {
		return top().value();
	}
}