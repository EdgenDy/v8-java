package org.jsengine.v8.internal.torque;

import org.jsengine.v8.internal.Torque;

public class MacroDeclaration extends CallableDeclaration {
    public String op;
    public MacroDeclaration(AstNode.Kind kind, SourcePosition pos, boolean transitioning,
                            Identifier name, String op,
                            ParameterList parameters, TypeExpression return_type,
                            LabelAndTypesVector labels) {
        super(kind, pos, transitioning, name,
                parameters, return_type, labels);
        this.op = op;
        if (parameters.implicit_kind == ImplicitKind.kJSImplicit) {
            Torque.error("Cannot use \"js-implicit\" with macros, use \"implicit\" instead.")
                    .position(parameters.implicit_kind_pos);
        }
    }

    public static MacroDeclaration dynamicCast(AstNode node) {
        if (node == null) return null;
        if (!(node instanceof MacroDeclaration)) return null;
        return (MacroDeclaration) node;
    }
}
