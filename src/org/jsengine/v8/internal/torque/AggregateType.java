package org.jsengine.v8.internal.torque;

import org.jsengine.v8.internal.Torque;

import java.util.Vector;
import java.util.Map;
import java.util.HashMap;

public abstract class AggregateType extends Type {
    protected boolean is_finalized_;
    private Namespace namespace_;
    private String name_;
    private Vector<Method> methods_ = new Vector<Method>();

    protected Vector<Field> fields_;

    public AggregateType(Kind kind, Type parent, Namespace nspace, String name) {
        super(kind, parent);
        this.is_finalized_ = false;
        this.namespace_ = nspace;
        this.name_ = name;
    }

    public String mangledName() {
        return name_;
    }

    public Namespace nspace() {
        return namespace_;
    }

    public String name() {
        return name_;
    }

    public abstract String toExplicitString();

    public abstract void Finalize();

    public Vector<Field> fields() {
        if (!is_finalized_) Finalize();
        return fields_;
    }

    @Override
    public String getGeneratedTypeNameImpl() {
        throw new RuntimeException("UNREACHABLE");
    }

    @Override
    public String getGeneratedTNodeTypeNameImpl() {
        throw new RuntimeException("UNREACHABLE");
    }

    public Field lookupField(String name) {
        if (!is_finalized_) Finalize();
        return lookupFieldInternal(name);
    }

    public Field lookupFieldInternal(String name) {
        for (Field field : fields_) {
            if (field.name_and_type.name == name) return field;
        }
        if (parent() != null) {
            ClassType parent_class = ClassType.dynamicCast(parent());
            if (parent_class != null) {
                return parent_class.lookupField(name);
            }
        }
        Torque.reportError("no field ", name, " found in ", this.toString());
        throw new RuntimeException("no field " + name + " found in " + this.toString());
    }

    public Field registerField(Field field) {
        fields_.add(field);
        return fields_.lastElement();
    }

    public void checkForDuplicateFields() {
        Vector<AggregateType> hierarchy = getHierarchy();
        Map<String, AggregateType> field_names = new HashMap<String, AggregateType>();
        for (AggregateType aggregate_type : hierarchy) {
            for (Field field : aggregate_type.fields()) {
                String field_name = field.name_and_type.name;
                AggregateType i = field_names.get(field_name);
                if (i != null) {
                    CurrentSourcePosition.Scope current_source_position
                            = new CurrentSourcePosition.Scope(field.pos);
                    String aggregate_type_name =
                            aggregate_type.isClassType() ? "class" : "struct";
                    if (i == this) {
                        Torque.reportError(aggregate_type_name, " '", name(),
                                "' declares a field with the name '", field_name,
                                "' more than once");
                    } else {
                        Torque.reportError(aggregate_type_name, " '", name(),
                                "' declares a field with the name '", field_name,
                                "' that masks an inherited field from class '",
                                i.name(), "'");
                    }
                }
                field_names.put(field_name, aggregate_type);
            }
        }
    }

    public Vector<AggregateType> getHierarchy() {
        if (!is_finalized_) Finalize();
        Vector<AggregateType> hierarchy = new Vector<AggregateType>();
        AggregateType current_container_type = this;
        while (current_container_type != null) {
            hierarchy.add(current_container_type);
            current_container_type = current_container_type.isClassType()
                            ? ClassType.cast(current_container_type).getSuperClass()
            : null;
        }
        Vector<AggregateType> reversed_hierarchy = new Vector<AggregateType>();
        for(AggregateType atype : hierarchy) {
            reversed_hierarchy.add(atype);
        }
        hierarchy = reversed_hierarchy;
        reversed_hierarchy = null;
        return hierarchy;
    }

    public String getGeneratedMethodName(String name) {
        return "_method_" + name_ + "_" + name;
    }

    public void registerMethod(Method method) {
        methods_.add(method);
    }

    public boolean hasField(String name) {
        if (!is_finalized_) Finalize();
        for (Field field : fields_) {
            if (field.name_and_type.name == name) return true;
        }
        if (parent() != null) {
            ClassType parent_class = ClassType.dynamicCast(parent());
            if (parent_class != null) {
                return parent_class.hasField(name);
            }
        }
        return false;
    }
}
