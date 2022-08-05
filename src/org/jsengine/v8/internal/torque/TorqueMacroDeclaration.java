package org.jsengine.v8.internal.torque;

public class TorqueMacroDeclaration extends MacroDeclaration {
    public static Kind kKind = Kind.kTorqueMacroDeclaration;
    public boolean export_to_csa;
    public Statement body;
    public TorqueMacroDeclaration(SourcePosition pos, boolean transitioning,
                                  Identifier name, String op,
                                  ParameterList parameters, TypeExpression return_type,
                                  LabelAndTypesVector labels, boolean export_to_csa,
                                  Statement body) {
        super(kKind, pos, transitioning, name, op,
                parameters, return_type, labels);
        export_to_csa = export_to_csa;
        body = body;
    }

    public static TorqueMacroDeclaration cast(AstNode node) {
        return (TorqueMacroDeclaration) node;
    }

    public static TorqueMacroDeclaration dynamicCast(AstNode node) {
        if (node == null) return null;
        if (node.kind != kKind) return null;
        return (TorqueMacroDeclaration) node;
    }
}
