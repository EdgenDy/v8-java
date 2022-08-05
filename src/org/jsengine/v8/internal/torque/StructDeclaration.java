package org.jsengine.v8.internal.torque; 

import java.util.Vector;

public class StructDeclaration extends TypeDeclaration {
	public static Kind kKind = Kind.kStructDeclaration;
	public Vector<Declaration> methods;
	public Vector<StructFieldExpression> fields;
	public Vector<Identifier> generic_parameters;
  
	public StructDeclaration(SourcePosition pos, Identifier name,
                    Vector<Declaration> methods,
                    Vector<StructFieldExpression> fields,
                    Vector<Identifier> generic_parameters) {
		super(kKind, pos, name);
        this.methods = methods;
        this.fields = fields;
        this.generic_parameters = generic_parameters;
	}

	boolean isGeneric() { return !(generic_parameters.size() == 0); }
	
	public static StructDeclaration cast(AstNode node) {
		return (StructDeclaration) node;
	}        
}