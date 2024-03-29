package org.jsengine.utils;

import org.jsengine.v8.internal.Address;
import org.jsengine.Memory;

public class Pointer<T> {
	private long address = 0x00;
	public Pointer(long address) {
		this.address = address;
	}
	
	public Pointer(Address address) {
		this(address.value());
	}
	
	public T dereference() {
		if(address == 0)
			throw new RuntimeException("Null pointer.");
		Object data = Memory.load(address);
		return (T) data;
	}
	
	public Pointer<T> dereference(T data) {
		Memory.store(address, data);
		return this;
	}
	
	public void setValue(long address) {
		this.address = address;
	}
	
	public long getValue() {
		return this.address;
	}
	
	public Pointer<T> increment(int num) {
		address += num;
		return this;
	}
	
	public Pointer<T> decrement(int num) {
		address -= num;
		return this;
	}
	
	public Pointer<T> increment() {
		return increment(1);
	}
	
	public Pointer<T> decrement() {
		return decrement(1);
	}
	
	public Pointer<T> add(int num) {
		address += num;
		return this;
	}
	
	public Pointer<T> subtract(int num) {
		address -= num;
		return this;
	}
	
	public Pointer<T> multiply(int num) {
		address *= num;
		return this;
	}
	
	public Pointer<T> divide(int num) {
		address /= num;
		return this;
	}
	
	public Pointer<T> or(int num) {
		address |= num;
		return this;
	}
	
	public Pointer<T> and(int num) {
		address &= num;
		return this;
	}
	
	public Pointer<T> shiftRight(int num) {
		address >>= num;
		return this;
	}
	
	public Pointer<T> shiftLeft(int num) {
		address <<= num;
		return this;
	}
	
	public boolean isGreaterThan(int num) {
		return address > num;
	}
	
	public boolean isLessThan(int num) {
		return address < num;
	}
	
	public boolean isEquals(int num) {
		return address == num;
	}
	
	public boolean isEquals(Pointer<T> num) {
		return address == num.address;
	}
	
	public static <T> Pointer<T> init(T data) {
		return Memory.allocate(data);
	}
	
	public static Pointer<Object> from(long address) {
		return new Pointer<Object>(address);
	}
	
	public static Pointer<Object> from(Address address) {
		return new Pointer<Object>(address);
	}
}