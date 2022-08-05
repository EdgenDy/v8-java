package org.jsengine.v8.internal.torque;

import org.jsengine.utils.ConstCharPtr;

public class InputPosition extends ConstCharPtr {
	public InputPosition(String value) {
		super(value);
	}
	
	@Override
	public InputPosition clone() {
		InputPosition const_char_ptr = new InputPosition(string_);
		const_char_ptr.index_ = index_;
		return const_char_ptr;
	}
	
	public InputPosition add(int num) {
		InputPosition ret_val = new InputPosition(string_);
		ret_val.index_ = (index_ + num);
		return ret_val;
	}
}