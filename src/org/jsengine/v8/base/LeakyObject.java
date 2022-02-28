package org.jsengine.v8.base;

// src\base\lazy-instance.h
public class LeakyObject<T> {
	private T storage_;
	public LeakyObject(T storage) {
		storage_ = storage;
	}
	
	public T get() {
		return storage_;
	}
}