package org.jsengine.v8.internal;

public class Vector<T> {
	private Object start_;
	private int length_;
	private _Type _type;
	
	public Vector() {
		start_ = null;
		length_ = 0;
	}
	
	public Vector(T[] data, int length) {
		start_ = data;
		length_ = length;
		_type = _Type._Wrapper;
	}
	
	public Vector(char[] data, int length) {
		start_ = data;
		length_ = length;
		_type = _Type._CharArray;
	}
	
	public Object begin() {
		return start_;
	}
	
	public int length() {
		return length_;
	}
	
	public void set(int index, T data) {
		T[] t = (T[]) start_;
		t[index] = (T) data;
	}
	
	public void set(int index, char data) {
		char[] ch = (char[]) start_;
		ch[index] = data;
	}
	
	public T get(int index) {
		if(_type == _Type._CharArray) {
			char[] ch = (char[]) start_;
			Character chr = (Character) ch[index];
			return ((T) chr);
		}
		else {
			T[] t = (T[]) start_;
			return t[index];
		}
	}
	
	private enum _Type {
		_CharArray,
		_Wrapper
	}
}