package org.jsengine.v8.internal.torque; 

import java.util.Vector;

public class GenericDeclaration extends Declaration {
	public static Kind kKind = Kind.kGenericDeclaration;
	
	public Vector<Identifier> generic_parameters;
	public CallableDeclaration declaration;
	
	public GenericDeclaration(SourcePosition pos,
                     Vector<Identifier> generic_parameters,
                     CallableDeclaration declaration) {
		super(kKind, pos);
		this.generic_parameters = generic_parameters;
		this.declaration = declaration;
	}
	
	public static GenericDeclaration cast(AstNode node) {
		return (GenericDeclaration) node;
	}        
}