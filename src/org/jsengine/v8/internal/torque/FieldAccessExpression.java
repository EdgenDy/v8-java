package org.jsengine.v8.internal.torque;

public class FieldAccessExpression extends LocationExpression {
    public static Kind kKind = Kind.kFieldAccessExpression;
    public Expression object;
    public Identifier field;

    public FieldAccessExpression(SourcePosition pos, Expression object, Identifier field) {
        super(kKind, pos);
        this.object = object;
        this.field = field;
    }
}
