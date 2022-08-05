package org.jsengine.v8.internal.torque;

public class ExternalBuiltinDeclaration extends BuiltinDeclaration {
    public static Kind kKind = Kind.kExternalBuiltinDeclaration;

    public ExternalBuiltinDeclaration(SourcePosition pos, boolean transitioning,
                                    boolean javascript_linkage, Identifier name,
                                    ParameterList parameters,
                                    TypeExpression return_type) {
        super(kKind, pos, javascript_linkage, transitioning, name,
                parameters, return_type);
    }

    public static ExternalBuiltinDeclaration cast(AstNode node) {
        return (ExternalBuiltinDeclaration) node;
    }
}
