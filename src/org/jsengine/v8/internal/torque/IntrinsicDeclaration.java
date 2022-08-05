package org.jsengine.v8.internal.torque;

import org.jsengine.v8.internal.Torque;

public class IntrinsicDeclaration extends CallableDeclaration {
    public static Kind kKind = Kind.kIntrinsicDeclaration;

    public IntrinsicDeclaration(SourcePosition pos, Identifier name,
                                ParameterList parameters, TypeExpression return_type) {
        super(kKind, pos, false, name, parameters,
                return_type, new LabelAndTypesVector());
        if (parameters.implicit_kind != ImplicitKind.kNoImplicit) {
            Torque.error("Intinsics cannot have implicit parameters.");
        }
    }

    public static IntrinsicDeclaration cast(AstNode node) {
        return (IntrinsicDeclaration) node;
    }

    public static IntrinsicDeclaration dynamicCast(AstNode node) {
        if (node == null) return null;
        if (node.kind != kKind) return null;
        return (IntrinsicDeclaration) node;
    }
}
