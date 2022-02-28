package org.jsengine.utils;

public class Var<T> {
	private T value_;
	private int id_;
	private static int id = 0;
	
	@SuppressWarnings("unchecked")
	public Var(Object value) {
		if(value instanceof Var<?>) {
			this.value_ = (T) ((Var<?>) value).value_;
			this.id_ = ((Var<?>) value).id_;
		}
		else {
			this.value_ = (T) value;
			this.id_ = id++;
		}
	}
	
	public T getValue() {
		return this.value_;
	}
	
	public void setValue(T newValue) {
		this.value_ = newValue;
	}
	
	@Override
	public boolean equals(Object object) {
		if(object instanceof Var<?>) {
			Var<?> var = (Var<?>) object;
			return var.id_ == this.id_;
		}
		return false;
	}
	
	@Override
	public String toString() {
		return this.value_ == null ? "null" : this.value_.toString();
	}
}