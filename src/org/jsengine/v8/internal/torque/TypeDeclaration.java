package org.jsengine.v8.internal.torque; 

public class TypeDeclaration extends Declaration {
	public Identifier name;
	
	public TypeDeclaration(Kind kKind, SourcePosition pos, Identifier name) {
		super(kKind, pos);
		this.name = name;
	}
};