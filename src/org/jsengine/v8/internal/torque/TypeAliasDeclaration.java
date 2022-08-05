package org.jsengine.v8.internal.torque; 

public class TypeAliasDeclaration extends TypeDeclaration {
	public static Kind kKind = Kind.kTypeAliasDeclaration;
	private TypeExpression type;
	
	public TypeAliasDeclaration(SourcePosition pos, Identifier name, TypeExpression type) {
		super(kKind, pos, name);
		this.type = type;
	}
  
	
	public static TypeAliasDeclaration cast(AstNode node) {
		return (TypeAliasDeclaration) node;
	}        
}