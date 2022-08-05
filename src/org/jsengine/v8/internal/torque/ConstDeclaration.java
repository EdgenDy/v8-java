package org.jsengine.v8.internal.torque; 

public class ConstDeclaration extends Declaration {
	public static Kind kKind = Kind.kConstDeclaration;
	public Identifier name;
	public TypeExpression type;
	public Expression expression;
	
	public ConstDeclaration(SourcePosition pos, Identifier name, TypeExpression type,
                   Expression expression) {
		super(kKind, pos);
        name = name;
        type = type;
        expression = expression;
	}
	
	public static ConstDeclaration cast(AstNode node) {
		return (ConstDeclaration) node;
	}     
};