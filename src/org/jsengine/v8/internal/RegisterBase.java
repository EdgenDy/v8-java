package org.jsengine.v8.internal;

public class RegisterBase {
	protected int reg_code_ = 0;
	
	protected RegisterBase(int code) {
		reg_code_ = code;
	}
	
	public boolean is_valid() { 
		return reg_code_ != kCode_no_reg;
	}
	
	public int code() {
		return reg_code_;
	}
	
	public RegList bit() {
		return is_valid() ? new RegList(1 << code()) : new RegList(0);
	}
	
	public static int kCode_no_reg = -1;
}