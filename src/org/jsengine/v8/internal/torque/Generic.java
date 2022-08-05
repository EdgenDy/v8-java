package org.jsengine.v8.internal.torque;

import java.util.Vector;

public class Generic extends Declarable {
    private String name_;
    private GenericDeclaration generic_declaration_;
    private SpecializationMap<Callable> specializations_ = new SpecializationMap<Callable>();

    public Generic(String name, GenericDeclaration generic_declaration) {
        super(Kind.kGeneric);
        name_ = name;
        generic_declaration_ = generic_declaration;
    }

    public String name() {
        return name_;
    }

    public CallableDeclaration declaration() {
        return generic_declaration_.declaration;
    }

    public Vector<Identifier> generic_parameters() {
        return generic_declaration_.generic_parameters;
    }

    public SpecializationMap<Callable> specializations() {
        return specializations_;
    }

    public static Generic dynamicCast(Declarable declarable) {
        if (declarable == null) return null;
        if (!declarable.isGeneric()) return null;
        return (Generic) declarable;
    }

    public Statement callableBody() {
        TorqueMacroDeclaration decl = TorqueMacroDeclaration.dynamicCast(declaration());
        TorqueBuiltinDeclaration decl0 = null;
        if (decl != null) {
            return decl.body;
        } else if ((decl0 = TorqueBuiltinDeclaration.dynamicCast(declaration())) != null) {
            return decl0.body;
        } else {
            return null;
        }
    }

    public TypeArgumentInference inferSpecializationTypes(TypeVector explicit_specialization_types,
                                                            TypeVector arguments) {
        int implicit_count = declaration().parameters.implicit_count;
        Vector<TypeExpression> parameters = declaration().parameters.types;
        Vector<TypeExpression> explicit_parameters = new Vector<TypeExpression>();

        for(int index = implicit_count; index < parameters.size(); index++) {
            explicit_parameters.add(parameters.get(index));
        }

        CurrentScope.Scope generic_scope = new CurrentScope.Scope(parentScope());
        TypeArgumentInference inference = new TypeArgumentInference(generic_parameters(),
                explicit_specialization_types,
                explicit_parameters, arguments);
        return inference;
    }
}
