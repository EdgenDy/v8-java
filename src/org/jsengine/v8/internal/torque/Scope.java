package org.jsengine.v8.internal.torque; 

import org.jsengine.v8.internal.Torque;
import java.util.HashMap;
import java.util.Vector;

public class Scope extends Declarable {
	private HashMap<String, Vector<Declarable>> declarations_ = new HashMap<String, Vector<Declarable>>();
	
	public Scope(Declarable.Kind kind) {
		super(kind);
	}
	
	public Vector<Declarable> lookupShallow(QualifiedName name) {
		if (name.namespace_qualification.size() == 0) return declarations_.get(name.name);
		Scope child = null;
		for (Declarable declarable : declarations_.get(name.namespace_qualification.firstElement())) {
			Scope scope = Scope.dynamicCast(declarable);
			if (scope == null) {
				if (child != null) {
					Torque.reportError("ambiguous reference to scope " + name.namespace_qualification.firstElement());
				}
				child = scope;
			}
		}
		if (child == null) return new Vector<Declarable>();
		Vector<String> vector = new Vector<String>();
		for (int index = 1, end = name.namespace_qualification.size(); index < end; index++)
			vector.add(name.namespace_qualification.get(index));
		return child.lookupShallow(new QualifiedName(vector, name.name));
	}
	
	public Vector<Declarable> lookup(QualifiedName name) {
		Vector<Declarable> result = new Vector<Declarable>();
		if (parentScope() != null) {
			result = parentScope().lookup(name);
		}
		for (Declarable declarable : lookupShallow(name)) {
			result.add(declarable);
		}
		return result;
	}
	
	public static Scope dynamicCast(Declarable declarable) {
		if (declarable == null) return null;
		if (!declarable.isScope()) return null;
		return (Scope) declarable;
	}

	public <T> T addDeclarable(String name, T declarable) {
		declarations_.get(name).add((Declarable) declarable);
		return declarable;
	}
}