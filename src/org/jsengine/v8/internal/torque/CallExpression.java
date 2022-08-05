package org.jsengine.v8.internal.torque;

import java.util.Vector;

public class CallExpression extends Expression {
    public static Kind kKind = Kind.kCallExpression;
    public IdentifierExpression callee;
    public Vector<Expression> arguments;
    public Vector<Identifier> labels;

    public CallExpression(SourcePosition pos, IdentifierExpression callee,
                          Vector<Expression> arguments,
                          Vector<Identifier> labels) {
        super(kKind, pos);
        this.callee = callee;
        this.arguments = arguments;
        this.labels = labels;
    }
}
