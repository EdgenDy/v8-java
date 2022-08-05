package org.jsengine.v8.internal.torque;

public class ExternalMacroDeclaration extends MacroDeclaration {
    public String external_assembler_name;
    public static Kind kKind = Kind.kExternalMacroDeclaration;

    public ExternalMacroDeclaration(SourcePosition pos, boolean transitioning,
                                    String external_assembler_name,
                                    Identifier name, String op,
                                    ParameterList parameters,
                                    TypeExpression return_type,
                                    LabelAndTypesVector labels) {
        super(kKind, pos, transitioning, name, op,
                parameters, return_type, labels);
        this.external_assembler_name = external_assembler_name;
    }

    public static ExternalMacroDeclaration cast(AstNode node) {
        return (ExternalMacroDeclaration) node;
    }
}
