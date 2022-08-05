package org.jsengine.v8.internal.torque; 

import org.jsengine.v8.internal.Torque;

public class TypeAlias extends Declarable {
	private Type type_ = null;
	private boolean redeclaration_;
	private SourcePosition declaration_position_ = null;
	private TypeDeclaration delayed_;
	private boolean being_resolved_ = false;
	private boolean is_user_defined_ = true;
	
	public TypeAlias(Type type, boolean redeclaration,
						SourcePosition declaration_position) {
		super(Declarable.Kind.kTypeAlias);
        type_ = type;
        redeclaration_ = redeclaration;
        declaration_position_ = declaration_position;
	}
	
	public TypeAlias(Type type, boolean redeclaration) {
		this(type, redeclaration, SourcePosition.invalid());
	}
	
	public TypeAlias(TypeDeclaration type, boolean redeclaration,
						SourcePosition declaration_position) {
		super(Declarable.Kind.kTypeAlias);
        delayed_ = type;
        redeclaration_ = redeclaration;
        declaration_position_ = declaration_position;
	}
	
	public TypeAlias(TypeDeclaration type, boolean redeclaration) {
		this(type, redeclaration, SourcePosition.invalid());
	}
	
	public static TypeAlias dynamicCast(Declarable declarable) {
		if (declarable == null) return null;
		if (!declarable.isTypeAlias()) return null;
		return (TypeAlias) declarable;
	}
	
	public Type resolve() {
		if (type_ == null) {
			CurrentScope.Scope scope_activator = new CurrentScope.Scope(parentScope());
			CurrentSourcePosition.Scope position_activator = new CurrentSourcePosition.Scope(position());
			TypeDeclaration decl = delayed_;
			if (being_resolved_) {
				StringBuilder s = new StringBuilder();
				s.append("Cannot create type ");
				s.append(decl.name.value);
				s.append(" due to circular dependencies.");
				Torque.reportError(s.toString());
			}
			type_ = TypeVisitor.computeType(decl);
		}
		return type_;
	}
	
	public SourcePosition getDeclarationPosition() {
		return declaration_position_;
	}
	
	public Type type() {
		if (type_ != null) return type_;
		return resolve();
	}

	public void setIsUserDefined(boolean is_user_defined) {
		is_user_defined_ = is_user_defined;
	}

	public static TypeAlias cast(Declarable declarable) {
		return (TypeAlias) declarable;
	}
}