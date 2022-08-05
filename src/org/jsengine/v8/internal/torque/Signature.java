package org.jsengine.v8.internal.torque;

public class Signature {
    public NameVector parameter_names;
    public String arguments_variable;
    public ParameterTypes parameter_types;
    public int implicit_count = 0;
    public Type return_type;
    public LabelDeclarationVector labels;
    public boolean transitioning = false;

    public Signature(NameVector n, String arguments_variable,
                     ParameterTypes p, int i, Type r, LabelDeclarationVector l,
                     boolean transitioning) {
        this.parameter_names = n;
        this.arguments_variable = arguments_variable;
        this.parameter_types = p;
        this.implicit_count = i;
        this.return_type = r;
        this.labels = l;
        this.transitioning = transitioning;
    }

    public Signature() {

    }

    public TypeVector types() {
        return parameter_types.types;
    }

    public boolean hasSameTypesAs(Signature other, ParameterMode mode) {
        TypeVector compare_types = types();
        TypeVector other_compare_types = other.types();
        if (mode == ParameterMode.kIgnoreImplicit) {
            compare_types = getExplicitTypes();
            other_compare_types = other.getExplicitTypes();
        }
        if (!(compare_types == other_compare_types &&
                parameter_types.var_args == other.parameter_types.var_args &&
                return_type == other.return_type)) {
            return false;
        }
        if (labels.size() != other.labels.size()) {
            return false;
        }
        int i = 0;
        for (LabelDeclaration l : labels) {
            if (l.types != other.labels.get(i++).types) {
                return false;
            }
        }
        return true;
    }

    public TypeVector getExplicitTypes() {
        return new TypeVector(parameter_types.types, implicit_count);
    }
}
