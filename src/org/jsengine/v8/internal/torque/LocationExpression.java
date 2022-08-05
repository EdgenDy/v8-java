package org.jsengine.v8.internal.torque;

public class LocationExpression extends Expression {
    public LocationExpression(Kind kind, SourcePosition pos) {
        super(kind, pos);
    }

    public static LocationExpression dynamicCast(AstNode node) {
        if (node == null) return null;
        if (node instanceof LocationExpression) return null;
        return (LocationExpression) node;
    }
}
