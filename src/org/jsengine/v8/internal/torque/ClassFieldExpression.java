package org.jsengine.v8.internal.torque;

import java.util.Vector;

public class ClassFieldExpression {
	public NameAndTypeExpression name_and_type;
	public String index;
	public Vector<ConditionalAnnotation> conditions;
	public boolean weak;
	public boolean const_qualified;
	public boolean generate_verify;
};