package org.jsengine.v8.internal.torque; 

import org.jsengine.v8.internal.Torque;

public class AbstractTypeDeclaration extends TypeDeclaration {
	public static Kind kKind = Kind.kAbstractTypeDeclaration;
	private boolean is_constexpr;
	private boolean transient_;
	private Identifier extends_;
	private String generates = null;
  
	public static AbstractTypeDeclaration cast(AstNode node) {
		return (AbstractTypeDeclaration) node;
	}
	
	public AbstractTypeDeclaration(SourcePosition pos, Identifier name, boolean transient_, Identifier extends_, String generates) {
		super(kKind, pos, name);
		this.is_constexpr = Torque.isConstexprName(name.value);
		this.transient_ = transient_;
		this.extends_ = extends_;
        this.generates = generates;
	}
  
}