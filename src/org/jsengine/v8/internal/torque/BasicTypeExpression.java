package org.jsengine.v8.internal.torque;

import org.jsengine.v8.internal.Torque;
import java.util.Vector;

public class BasicTypeExpression extends TypeExpression {
    public static Kind kKind = Kind.kBasicTypeExpression;
    public Vector<String> namespace_qualification;
    public boolean is_constexpr;
    public String name;
    public Vector<TypeExpression> generic_arguments;

    public BasicTypeExpression(SourcePosition pos,
                               Vector<String> namespace_qualification,
                               String name,
                               Vector<TypeExpression> generic_arguments) {
        super(kKind, pos);
        this.namespace_qualification = namespace_qualification;
        this.is_constexpr = Torque.isConstexprName(name);
        this.name = name;
        this.generic_arguments = generic_arguments;
    }

    static BasicTypeExpression dynamicCast(AstNode node) {
        if (node == null) return null;
        if (node.kind != kKind) return null;
        return (BasicTypeExpression) node;
    }
}
