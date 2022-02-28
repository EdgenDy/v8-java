package org.jsengine.v8.internal;

public class MaybeBoolFlag {
	public boolean has_value;
	public boolean value;
	public static MaybeBoolFlag create(boolean has_value, boolean value) {
		MaybeBoolFlag flag = new MaybeBoolFlag();
		flag.has_value = has_value;
		flag.value = value;
		return flag;
	}
	
	public boolean notEquals(MaybeBoolFlag other) {
		return other.has_value != this.has_value || other.value != this.value;
	}
}