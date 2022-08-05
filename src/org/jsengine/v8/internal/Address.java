package org.jsengine.v8.internal;

public class Address {
	private long value_ = 0L;
	
	public Address(long value) {
		this.value_ = value;
	}
	
	public long value() {
		return value_;
	}
	
	public boolean equals(Address address) {
		return value_ == address.value_;
	}
	
	public boolean notEquals(Address address) {
		return value_ != address.value_;
	}
	
	public boolean greaterThan(Address address) {
		return value_ > address.value_;
	}
	
	public boolean lessThan(Address address) {
		return value_ < address.value_;
	}
	
	public void add(long num) {
		value_ += num;
	}
	
	public void subtract(long num) {
		value_ -= num;
	}
};