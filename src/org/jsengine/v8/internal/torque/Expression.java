package org.jsengine.v8.internal.torque; 

public class Expression extends AstNode {
	public Expression(Kind kind, SourcePosition pos) {
		super(kind, pos);
	}
	
	public static Expression cast(AstNode node) {
		return (Expression) node;
	}            
};