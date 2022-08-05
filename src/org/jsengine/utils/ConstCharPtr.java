package org.jsengine.utils;

public class ConstCharPtr {
	protected String string_ = null;
	protected int index_ = 0;
	
	public ConstCharPtr(String string) {
		this.string_ = string;
	}
	
	public char deref() {
		if (index_ >= string_.length())
			return 0;
		return string_.charAt(index_);
	}
	
	public void deref(char c) {
		this.string_ = (c + "");
		this.index_ = 0;
	}
	
	public void set(ConstCharPtr const_char_ptr) {
		this.string_ = const_char_ptr.string_;
		this.index_ = const_char_ptr.index_;
	}
	
	public ConstCharPtr clone() {
		ConstCharPtr const_char_ptr = new ConstCharPtr(string_);
		const_char_ptr.index_ = index_;
		return const_char_ptr;
	}
	
	public ConstCharPtr increase(int num) {
		index_ += num;
		return this;
	}
	
	public ConstCharPtr increase() {
		index_++;
		return this;
	}
	
	public ConstCharPtr incLeft() {
		++index_;
		return this;
	}
	
	public ConstCharPtr add(int num) {
		ConstCharPtr ret_val = new ConstCharPtr(string_);
		ret_val.index_ = (index_ + num);
		return ret_val;
	}
	
	public boolean notEquals(ConstCharPtr other) {
		return index_ != other.index_;
	}
	
	public boolean greaterThan(ConstCharPtr other) {
		return index_ > other.index_;
	}
	
	public String toString() {
		if (index_ >= string_.length() || index_ < 0)
			return null;
		return string_.substring(index_);
	}
	
	public String toString(int end) {
		if (index_ >= string_.length() || index_ < 0)
			return null;
		return string_.substring(index_, end);
	}
	
	public int toInt() {
		return index_;
	}
}