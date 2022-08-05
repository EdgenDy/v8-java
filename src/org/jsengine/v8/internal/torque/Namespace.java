package org.jsengine.v8.internal.torque;

public class Namespace extends Scope {
	private String name_ = null;
	public Namespace(String name) {
		super(Declarable.Kind.kNamespace);
		name_ = name;
	}

	public static Namespace dynamicCast(Declarable declarable) {
		if (declarable == null) return null;
		if (!declarable.isNamespace()) return null;
		return (Namespace) declarable;
	}
}