package org.jsengine.v8.internal.torque; 

public abstract class Declarable {
	public static enum Kind {
		kNamespace,
		kTorqueMacro,
		kExternMacro,
		kMethod,
		kBuiltin,
		kRuntimeFunction,
		kIntrinsic,
		kGeneric,
		kGenericStructType,
		kTypeAlias,
		kExternConstant,
		kNamespaceConstant
	};
	
	private Scope parent_scope_ = CurrentScope.get();
	private SourcePosition position_ = CurrentSourcePosition.get();
	private SourcePosition identifier_position_ = SourcePosition.invalid();
	private Kind kind_;
	private boolean is_user_defined_ = true;
	
	
	public Declarable(Kind kind) {
		kind_ = kind;
	}
	
	public boolean isMacro() {
		return isTorqueMacro() || isExternMacro();
	}
	
	public boolean isMethod() {
		return kind() == Kind.kMethod;
	}
	
	public boolean isTorqueMacro() {
		return kind() == Kind.kTorqueMacro || isMethod();
	}
	
	public boolean isExternMacro() {
		return kind() == Kind.kExternMacro;
	}
	
	public boolean isNamespaceConstant() {
		return kind() == Kind.kNamespaceConstant;
	}
	
	public boolean isIntrinsic() {
		return kind() == Kind.kIntrinsic;
	}
	
	public boolean isBuiltin() {
		return kind() == Kind.kBuiltin;
	}
	
	public boolean isRuntimeFunction() {
		return kind() == Kind.kRuntimeFunction;
	}
	
	public boolean isNamespace() {
		return kind() == Kind.kNamespace;
	}
	
	public boolean isTypeAlias() {
		return kind() == Kind.kTypeAlias;
	}

	public boolean isExternConstant() {
		return kind() == Kind.kExternConstant;
	}

	public boolean isValue() {
		return isExternConstant() || isNamespaceConstant();
	}
	
	public boolean isScope() {
		return isNamespace() || isCallable();
	}
	
	public boolean isCallable() {
		return isMacro() || isBuiltin() || isRuntimeFunction() || isIntrinsic() || isMethod();
	}

	public boolean isGeneric() {
		return kind() == Kind.kGeneric;
	}

	public boolean isGenericStructType() {
		return kind() == Kind.kGenericStructType;
	}
	
	public Kind kind() {
		return kind_;
	}
	
	public SourcePosition position() {
		return position_;
	}
	
	public Scope parentScope() {
		return parent_scope_;
	}

	public SourcePosition identifierPosition() {
		return identifier_position_.source.isValid() ? identifier_position_
				: position_;
	}

	public void setPosition(SourcePosition position) {
		position_ = position;
	}

	public void setIsUserDefined(boolean is_user_defined) {
		is_user_defined_ = is_user_defined;
	}
}