package org.jsengine.v8.base;

public class Bits {
	public static int RotateRight32(int value, int shift) {
		if (shift == 0) return value;
		return (value >> shift) | (value << (32 - shift));
	}
}