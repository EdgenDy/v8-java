package org.jsengine.v8.internal.torque;

import org.jsengine.v8.internal.Torque;

public class BuiltinDeclaration extends CallableDeclaration {
    public boolean javascript_linkage;
    public BuiltinDeclaration(AstNode.Kind kind, SourcePosition pos,
                              boolean javascript_linkage, boolean transitioning,
                              Identifier name, ParameterList parameters,
                              TypeExpression return_type) {
        super(kind, pos, transitioning, name, parameters,
                return_type, new LabelAndTypesVector());
        this.javascript_linkage = javascript_linkage;

        if (parameters.implicit_kind == ImplicitKind.kJSImplicit && !javascript_linkage) {
            Torque.error("\"js-implicit\" is for implicit parameters passed according to the " +
                    "JavaScript calling convention. Use \"implicit\" instead.");
        }
        if (parameters.implicit_kind == ImplicitKind.kImplicit && javascript_linkage) {
            Torque.error("The JavaScript calling convention implicitly passes a fixed set of " +
                    "values. Use \"js-implicit\" to refer to those.")
                    .position(parameters.implicit_kind_pos);
        }
    }
}
