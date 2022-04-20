package org.jsengine.v8.internal;

public class Register extends RegisterBase {
	private Register(int code) {
		super(code);
	}
	
	public static Register from_code(int code) {
		return new Register(code);
	}
	
	public static int kNumRegisters = RegisterCode.kRegAfterLast.ordinal();
	
	public static Register no_reg() {
		return new Register(kCode_no_reg);
	}
}