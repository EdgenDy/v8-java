package org.jsengine.v8.internal.torque; 

public class CppIncludeDeclaration extends Declaration {
	public static Kind kKind = Kind.kCppIncludeDeclaration;
	public String include_path;
  
	public CppIncludeDeclaration(SourcePosition pos, String include_path) {
		super(kKind, pos); 
		include_path = include_path;
	}
	
	static CppIncludeDeclaration cast(AstNode node) {
		return (CppIncludeDeclaration) node;
	}     
};