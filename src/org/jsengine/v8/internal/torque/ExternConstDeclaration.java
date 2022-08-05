package org.jsengine.v8.internal.torque; 

public class ExternConstDeclaration extends Declaration {
	public static Kind kKind = Kind.kExternConstDeclaration;  
	public Identifier name;
	public TypeExpression type;
	public String literal;
	
	public ExternConstDeclaration(SourcePosition pos, Identifier name,
                         TypeExpression type, String literal) {
		super(kKind, pos);
        name = name;
        type = type;
        literal = literal;
	}
	
	public static ExternConstDeclaration cast(AstNode node) {
		return (ExternConstDeclaration) node;
	}      
};