package org.jsengine.v8.internal.torque;

import org.jsengine.utils.UnorderedMap;
import org.jsengine.v8.internal.Torque;
import java.util.Vector;

public class TypeArgumentInference {
    public int num_explicit_ = 0;
    public UnorderedMap<String, Integer> type_parameter_from_name_;
    public Vector<Type> inferred_;
    public String failure_reason_;

    public TypeArgumentInference(NameVector type_parameters,
                                    TypeVector explicit_type_arguments,
                                    Vector<TypeExpression> term_parameters,
                                    TypeVector term_argument_types) {
        num_explicit_ = explicit_type_arguments.size();
        type_parameter_from_name_ = new UnorderedMap<String, Integer>();
        inferred_ = new Vector<Type>();

        if (num_explicit_ > type_parameters.size()) {
            fail("more explicit type arguments than expected");
            return;
        }
        if (term_parameters.size() != term_argument_types.size()) {
            fail("number of term parameters does not match number of term arguments!");
            return;
        }

        for (int i = 0; i < type_parameters.size(); i++) {
            type_parameter_from_name_.put(type_parameters.get(i).value, i);
        }
        for (int i = 0; i < num_explicit_; i++) {
            inferred_.setElementAt(explicit_type_arguments.get(i), i);
        }

        for (int i = 0; i < term_parameters.size(); i++) {
            match(term_parameters.get(i), term_argument_types.get(i));
            if (hasFailed()) return;
        }

        for (int i = 0; i < type_parameters.size(); i++) {
            if (inferred_.get(i) == null) {
                fail("failed to infer arguments for all type parameters");
                return;
            }
        }
    }

    public void fail(String reason) {
        failure_reason_ = reason;
    }

    public boolean hasFailed() {
        return failure_reason_ != null;
    }

    public void match(TypeExpression parameter, Type argument_type) {
        BasicTypeExpression basic = BasicTypeExpression.dynamicCast(parameter);
        if (basic != null) {
            if (basic.namespace_qualification.isEmpty() && !basic.is_constexpr) {
                Integer result = type_parameter_from_name_.get(basic.name);
                if (result != null) {
                    int type_parameter_index = result;
                    if (type_parameter_index < num_explicit_) {
                        return;
                    }
                    Type maybe_inferred = inferred_.get(type_parameter_index);
                    if (maybe_inferred != null && maybe_inferred != argument_type) {
                        fail("found conflicting types for generic parameter");
                    } else {
                        inferred_.setElementAt(argument_type, type_parameter_index);
                    }
                    return;
                }
            }

            if (!basic.generic_arguments.isEmpty()) {
                StructType argument_struct_type = StructType.dynamicCast(argument_type);
                if (argument_struct_type != null) {
                    matchGeneric(basic, argument_struct_type);
                }
            }

        } else {
            // TODO(gsps): Perform inference on function and union types
        }
    }

    public void matchGeneric(BasicTypeExpression parameter, StructType argument_type) {
        QualifiedName qualified_name = new QualifiedName(parameter.namespace_qualification, parameter.name);
        GenericStructType generic_struct = Declarations.lookupUniqueGenericStructType(qualified_name);
        StructType.MaybeSpecializationKey specialized_from = argument_type.getSpecializedFrom();
        if (specialized_from == null || specialized_from.generic != generic_struct) {
            fail("found conflicting generic type constructors");
            return;
        }
        Vector<TypeExpression> parameters = parameter.generic_arguments;
        TypeVector argument_types = specialized_from.specialized_types;
        if (parameters.size() != argument_types.size()) {
            Torque.error(
                    "cannot infer types from generic-struct-typed parameter with ",
                    "incompatible number of arguments")
                    .position(parameter.pos).Throw();
        }
        for (int i = 0; i < parameters.size(); i++) {
            match(parameters.get(i), argument_types.get(i));
            if (hasFailed()) return;
        }
    }

    public String getFailureReason() {
        return failure_reason_;
    }

    public TypeVector getResult() {
        TypeVector result = new TypeVector();
        for(Type type : inferred_) {
            result.add(type);
        }
        return result;
    }
}
