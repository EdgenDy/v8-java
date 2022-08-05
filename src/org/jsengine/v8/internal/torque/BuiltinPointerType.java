package org.jsengine.v8.internal.torque;

import org.jsengine.v8.internal.Torque;

public class BuiltinPointerType extends Type {
    private TypeVector parameter_types_;
    private Type  return_type_;
    private int function_pointer_type_id_;

    public BuiltinPointerType(Type parent, TypeVector parameter_types,
                              Type return_type, int function_pointer_type_id) {
        super(Kind.kBuiltinPointerType, parent);
        this.parameter_types_ = parameter_types;
        this.return_type_ = return_type;
        this.function_pointer_type_id_ = function_pointer_type_id;
    }

    public String toExplicitString() {
        StringBuilder result = new StringBuilder();
        result.append("builtin (");
        Torque.printCommaSeparatedList(result, parameter_types_, null);
        result.append(") => ").append(return_type_);
        return result.toString();
    }

    public int function_pointer_type_id() {
        return function_pointer_type_id_;
    }

    public String mangledName() {
        StringBuilder result = new StringBuilder();
        result.append("FT");
        for (Type t : parameter_types_) {
            String arg_type_string = t.mangledName();
            result.append(arg_type_string.length()).append(arg_type_string);
        }
        String return_type_string = return_type_.mangledName();
        result.append(return_type_string.length()).append(return_type_string);
        return result.toString();
    }

    @Override
    public String getGeneratedTypeNameImpl() {
        return parent().getGeneratedTypeName();
    }

    @Override
    public String getGeneratedTNodeTypeNameImpl() {
        return parent().getGeneratedTNodeTypeName();
    }

    public static BuiltinPointerType dynamicCast(TypeBase declarable) {
        if (declarable == null) return null;
        if (!declarable.isBuiltinPointerType()) return null;
        return (BuiltinPointerType) declarable;
    }

    public TypeVector parameter_types() {
        return parameter_types_;
    }

    public Type return_type() {
        return return_type_;
    }
}
