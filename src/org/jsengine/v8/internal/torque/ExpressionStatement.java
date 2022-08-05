package org.jsengine.v8.internal.torque;

public class ExpressionStatement extends Statement {
    public static Kind kKind = Kind.kExpressionStatement;
    private Expression expression;

    public ExpressionStatement(SourcePosition pos, Expression expression) {
        super(kKind, pos);
    }
}
