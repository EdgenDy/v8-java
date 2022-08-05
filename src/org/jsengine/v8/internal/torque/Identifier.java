package org.jsengine.v8.internal.torque;

public class Identifier extends AstNode {
	public static Kind kKind = Kind.kIdentifier;
	public String value;
	
	public Identifier(SourcePosition pos, String identifier) {
		super(kKind, pos);
		value = identifier;
	}
}