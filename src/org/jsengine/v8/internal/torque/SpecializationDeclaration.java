package org.jsengine.v8.internal.torque; 

import java.util.Vector;

public class SpecializationDeclaration extends CallableDeclaration {
	public static Kind kKind = Kind.kSpecializationDeclaration;
	public Vector<TypeExpression> generic_parameters;
	public Statement body;
	
	public SpecializationDeclaration(SourcePosition pos, boolean transitioning, Identifier name,
                            Vector<TypeExpression> generic_parameters,
                            ParameterList parameters,
                            TypeExpression return_type,
                            LabelAndTypesVector labels, Statement body) {
		super(kKind, pos, transitioning, name,
                            parameters, return_type, labels);
        generic_parameters = generic_parameters;
        body = body;
	}
	
	public static SpecializationDeclaration cast(AstNode node) {
		return (SpecializationDeclaration) node;
	}         
};