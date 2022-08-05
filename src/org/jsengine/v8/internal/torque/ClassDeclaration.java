package org.jsengine.v8.internal.torque; 

import java.util.Vector;

public class ClassDeclaration extends TypeDeclaration {
	public static Kind kKind = Kind.kClassDeclaration;
	public ClassFlags flags;
	private TypeExpression super_;
	private String generates;
	public Vector<Declaration> methods;
	public Vector<ClassFieldExpression> fields;
  
	public ClassDeclaration(SourcePosition pos, Identifier name, ClassFlags flags,
					TypeExpression super_, String generates, Vector<Declaration> methods, Vector<ClassFieldExpression> fields) {
		super(kKind, pos, name);
		this.flags = flags;
		this.super_ = super_;
		this.generates = generates;
		this.methods = methods;
		this.fields = fields;
	}
	
	public static ClassDeclaration cast(AstNode node) {
		return (ClassDeclaration) node;
	}        
}