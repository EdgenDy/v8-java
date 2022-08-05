package org.jsengine.v8.internal.torque; 

public class Statement extends AstNode {
	public Statement(Kind kind, SourcePosition pos) {
		super(kind, pos);
	}
  
	public static Statement cast(AstNode node) {
		return (Statement) node;
	}       
};