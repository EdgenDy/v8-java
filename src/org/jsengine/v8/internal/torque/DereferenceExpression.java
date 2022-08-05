package org.jsengine.v8.internal.torque;

public class DereferenceExpression extends LocationExpression {
    public static Kind kKind = Kind.kDereferenceExpression;
    public Expression reference;

    public DereferenceExpression(SourcePosition pos, Expression reference) {
        super(kKind, pos);
        this.reference = reference;
    }
}
