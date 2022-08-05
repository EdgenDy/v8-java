package org.jsengine.v8.internal.torque;

import java.util.Vector;

public class TorqueMessages {
	public static ThreadLocal<Scope> top_ = new ThreadLocal<Scope>();
	
	public static class Scope {
		public Vector<TorqueMessage> value_;
		public Scope previous_;
		
		public Scope(Vector<TorqueMessage> value) {
			value_ = value;
			previous_ = top();
			top(this);
		}
		
		public Vector<TorqueMessage> value() {
			return value_;
		}
	}
	
	public static Scope top() {
		return top_.get();
	}
		
	public static void top(Scope scope) {
		top_.set(scope);
	}
	
	public static Vector<TorqueMessage> get() {
		return top().value();
	}
}