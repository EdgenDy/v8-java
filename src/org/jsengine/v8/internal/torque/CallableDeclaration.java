package org.jsengine.v8.internal.torque; 

public class CallableDeclaration extends Declaration {
	public boolean transitioning;
	public Identifier name;
	public ParameterList parameters;
	public TypeExpression return_type;
	public LabelAndTypesVector labels;
  
	public CallableDeclaration(AstNode.Kind kind, SourcePosition pos,
                      boolean transitioning, Identifier name,
                      ParameterList parameters, TypeExpression return_type,
                      LabelAndTypesVector labels) {
		super(kind, pos);
        this.transitioning = transitioning;
        this.name = name;
        this.parameters = parameters;
        this.return_type = return_type;
        this.labels = labels;
	}
};