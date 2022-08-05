package org.jsengine.v8.internal.torque; 

import java.util.Vector;

public class ParameterList {
	public Vector<Identifier> names;
	public Vector<TypeExpression> types;
	public ImplicitKind implicit_kind = ImplicitKind.kNoImplicit;
	public SourcePosition implicit_kind_pos = SourcePosition.invalid();
	public int implicit_count = 0;
	public boolean has_varargs = false;
	public String arguments_variable = "";

	public static ParameterList empty() {
		return new ParameterList();
	}

	public Vector<TypeExpression> getImplicitTypes() {
		Vector<TypeExpression> vector = new Vector<TypeExpression>();
		for (int index = 0, limit = implicit_count; index < limit; index++)
			vector.add(types.get(index));
		return vector;
	}
	
	public Vector<TypeExpression> getExplicitTypes() {
		Vector<TypeExpression> vector = new Vector<TypeExpression>();
		for (int index = implicit_count, limit = vector.size(); index < limit; index++)
			vector.add(types.get(index));
		return vector;
	}
};