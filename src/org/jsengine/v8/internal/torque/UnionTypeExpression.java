package org.jsengine.v8.internal.torque;

public class UnionTypeExpression extends TypeExpression {
    public static Kind kKind = Kind.kUnionTypeExpression;
    public TypeExpression a;
    public TypeExpression b;
    public UnionTypeExpression(SourcePosition pos, TypeExpression a, TypeExpression b) {
        super(kKind, pos);
        this.a = a;
        this.b = b;
    }

    public static UnionTypeExpression dynamicCast(AstNode node) {
        if (node == null) return null;
        if (node.kind != kKind) return null;
        return (UnionTypeExpression) node;
    }
}
