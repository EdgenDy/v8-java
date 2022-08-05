package org.jsengine.v8.internal.torque;

import org.jsengine.v8.internal.Torque;

import java.util.Vector;

public class IdentifierExpression extends LocationExpression {
    public static Kind kKind = Kind.kIdentifierExpression;
    public Vector<String> namespace_qualification = new Vector<String>();
    public Identifier name;
    public Vector<TypeExpression> generic_arguments = new Vector<TypeExpression>();

    public IdentifierExpression(SourcePosition pos,
                                Vector<String> namespace_qualification,
                                Identifier name, Vector<TypeExpression> args) {
        super(kKind, pos);
        this.namespace_qualification = namespace_qualification;
        this.name = name;
        this.generic_arguments = args;
    }

    public IdentifierExpression(SourcePosition pos,
                                Vector<String> namespace_qualification,
                                Identifier name) {
        this(pos, namespace_qualification, name, new Vector<TypeExpression>());
    }

    public IdentifierExpression(SourcePosition pos, Identifier name,
                                Vector<TypeExpression> args) {
        this(pos, new Vector<String>(), name, args);
    }

    public IdentifierExpression(SourcePosition pos, Identifier name) {
        this(pos, name, new Vector<TypeExpression>());
    }

    public boolean isThis(){
        return name.value == Torque.kThisParameterName;
    }
}
