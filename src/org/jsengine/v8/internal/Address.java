package org.jsengine.v8.internal;

public class Address {
	private int value_ = 0;
	private long longValue_ = 0;
	
	public Address(int value) {
		this.value_ = value;
	}
	
	public Address(long value) {
		this.longValue_ = value;
	}
	
	public int value() {
		return value_;
	}
	
	public long getLongValue() {
		return longValue_;
	}
};