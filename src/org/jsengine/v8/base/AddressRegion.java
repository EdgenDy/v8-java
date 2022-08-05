package org.jsengine.v8.base;

import org.jsengine.v8.internal.Address;
import org.jsengine.v8.Internal;

public class AddressRegion {
	private Address address_ = Internal.kNullAddress;
	private long size_ = 0;
	
	public AddressRegion(Address address, long size) {
		address_ = address;
		size_ = size;
	}
	
	public AddressRegion() {
		
	}
	
	public Address begin() {
		return address_;
	}
	
	public Address end() {
		return new Address(address_.value() + size_);
	}
	
	public long size() {
		return size_;
	}
	
	public void set_size(long size) {
		size_ = size;
	}
	
	public boolean contains(Address address) {
		return (address.value() - begin().value()) < size();
	}
	
	public boolean contains(Address address, long size) {
		long offset = address.value() - begin().value();
		return (offset < size_) && (offset + size <= size_);
	}
}