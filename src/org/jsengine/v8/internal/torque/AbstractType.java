package org.jsengine.v8.internal.torque;

public abstract class AbstractType extends Type {
    private boolean transient_;
    private String name_;
    private String generated_type_;
    private Type non_constexpr_version_;

    public AbstractType(Type parent, boolean transient_, String name,
               String generated_type, Type non_constexpr_version) {
        super(Kind.kAbstractType, parent);
        this.transient_ = transient_;
        this.name_ = name;
        this.generated_type_ = generated_type;
        this.non_constexpr_version_ = non_constexpr_version;
    }

    public String name() {
        return name_;
    }

    public abstract String toExplicitString();

    public static AbstractType cast(TypeBase declarable) {
        return (AbstractType) declarable;
    }

    public static AbstractType dynamicCast(TypeBase declarable) {
        if (declarable == null) return null;
        if (!declarable.isAbstractType()) return null;
        return (AbstractType) declarable;
    }

    @Override
    public boolean isTransient() {
        return transient_;
    }
}
