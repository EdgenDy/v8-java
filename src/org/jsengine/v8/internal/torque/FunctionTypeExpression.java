package org.jsengine.v8.internal.torque;

import java.util.Vector;

public class FunctionTypeExpression extends TypeExpression {
    public static Kind kKind = Kind.kFunctionTypeExpression;
    public Vector<TypeExpression> parameters;
    public TypeExpression return_type;

    public FunctionTypeExpression(SourcePosition pos,
                                  Vector<TypeExpression> parameters,
                                  TypeExpression return_type) {
        super(kKind, pos);
        this.parameters = parameters;
        this.return_type = return_type;
    }

    public static FunctionTypeExpression cast(AstNode node) {
        return (FunctionTypeExpression) node;
    }
}
