package org.jsengine.v8.internal.torque;

public class TorqueBuiltinDeclaration extends BuiltinDeclaration {
    public static Kind kKind = Kind.kTorqueBuiltinDeclaration;
    public Statement body;
    public TorqueBuiltinDeclaration(SourcePosition pos, boolean transitioning,
                                    boolean javascript_linkage, Identifier name,
                                    ParameterList parameters,
                                    TypeExpression return_type,
                                    Statement body) {
        super(kKind, pos, javascript_linkage, transitioning, name,
                parameters, return_type);
        this.body = body;
    }

    public static TorqueBuiltinDeclaration cast(AstNode node) {
        return (TorqueBuiltinDeclaration) node;
    }

    public static TorqueBuiltinDeclaration dynamicCast(AstNode node) {
        if (node == null) return null;
        if (node.kind != kKind) return null;
        return (TorqueBuiltinDeclaration) node;
    }
}
