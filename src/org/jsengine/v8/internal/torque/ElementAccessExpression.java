package org.jsengine.v8.internal.torque;

public class ElementAccessExpression extends LocationExpression {
    public static Kind kKind = Kind.kElementAccessExpression;
    public Expression array;
    public Expression index;

    public ElementAccessExpression(SourcePosition pos, Expression array,
                                   Expression index) {
        super(kKind, pos);
        this.array = array;
        this.index = index;
    }
}
