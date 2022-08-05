package org.jsengine.v8.internal.torque; 

public class TypeBase {
	public static enum Kind {
		kTopType,
		kAbstractType,
		kBuiltinPointerType,
		kUnionType,
		kStructType,
		kClassType
	};
	
	private Kind kind_;
	
	public TypeBase(Kind kind) {
		kind_ = kind;
	}
	
	public Kind kind() {
		return kind_;
	}

	public boolean isTopType() {
		return kind() == Kind.kTopType;
	}

	public boolean isAbstractType() {
		return kind() == Kind.kAbstractType;
	}

	public boolean isUnionType() {
		return kind() == Kind.kUnionType;
	}

	public boolean isStructType() {
		return kind() == Kind.kStructType;
	}

	public boolean isClassType() {
		return kind() == Kind.kClassType;
	}

	public boolean isBuiltinPointerType() {
		return kind() == Kind.kBuiltinPointerType;
	}
}