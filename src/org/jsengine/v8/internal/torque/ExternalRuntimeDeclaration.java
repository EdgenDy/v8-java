package org.jsengine.v8.internal.torque;

public class ExternalRuntimeDeclaration extends CallableDeclaration {
    public static Kind kKind = Kind.kExternalRuntimeDeclaration;

    public ExternalRuntimeDeclaration(SourcePosition pos, boolean transitioning,
                                      Identifier name, ParameterList parameters,
                                      TypeExpression return_type) {
        super(kKind, pos, transitioning, name, parameters,
                return_type, new LabelAndTypesVector());
    }

    public static ExternalRuntimeDeclaration cast(AstNode node) {
        return (ExternalRuntimeDeclaration) node;
    }
}
