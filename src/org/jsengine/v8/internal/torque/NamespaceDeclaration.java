package org.jsengine.v8.internal.torque; 

import java.util.Vector;

public class NamespaceDeclaration extends Declaration {
	public static Kind kKind = Kind.kNamespaceDeclaration;
	public Vector<Declaration> declarations;
	public String name;
	
	public NamespaceDeclaration(SourcePosition pos, String name,
								Vector<Declaration> declarations) {
		super(kKind, pos);
		this.declarations = declarations;
		this.name = name;
	}
	
	public static NamespaceDeclaration cast(AstNode node) {
		return (NamespaceDeclaration) node;
	}        
}