package org.jsengine.v8.internal.torque; 

public enum ClassFlag {
	kNone(0),
	kExtern(1 << 0),
	kGeneratePrint(1 << 1),
	kGenerateVerify(1 << 2),
	kTransient(1 << 3),
	kAbstract(1 << 4),
	kInstantiatedAbstractClass(1 << 5),
	kHasSameInstanceTypeAsParent(1 << 6),
	kGenerateCppClassDefinitions(1 << 7),
	kHasIndexedField(1 << 8);
	
	int value_;
	
	ClassFlag(int value) {
		value_ = value;
	}
	
	public int value() {
		return value_;
	}
};