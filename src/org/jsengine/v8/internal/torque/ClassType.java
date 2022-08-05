package org.jsengine.v8.internal.torque;

import org.jsengine.v8.internal.Torque;
import java.util.Vector;

public class ClassType extends AggregateType {
    public static ClassFlags kInternalFlags = new ClassFlags(ClassFlag.kHasIndexedField.ordinal());
    public int size_;
    public ClassFlags flags_;
    public String generates_;
    public ClassDeclaration decl_;
    public TypeAlias alias_;

    public ClassType(Type parent, Namespace nspace, String name,
                            ClassFlags flags, String generates,
                                ClassDeclaration decl, TypeAlias alias) {
        super(Kind.kClassType, parent, nspace, name);
        size_ = 0;
        flags_ = new ClassFlags(flags.mask_ & ~(kInternalFlags.mask_));
        generates_= generates;
        decl_ = decl;
        alias_ = alias;
    }

    @Override
    public String toExplicitString() {
        StringBuilder result = new StringBuilder();
        result.append("class ").append(name());
        return result.toString();
    }

    @Override
    public void finalize() {
        if (is_finalized_) return;
        CurrentScope.Scope scope_activator = new CurrentScope.Scope(alias_.parentScope());
        CurrentSourcePosition.Scope position_activator = new CurrentSourcePosition.Scope(decl_.pos);
        if (parent() != null) {
            ClassType super_class = ClassType.dynamicCast(parent());
            if (super_class != null) {
                if (super_class.hasIndexedField()) flags_.mask_ |= ClassFlag.kHasIndexedField.ordinal();
                if (!super_class.isAbstract() && !hasSameInstanceTypeAsParent()) {
                    Torque.error(
                            "Super class must either be abstract (annotate super class with ",
                            "@abstract) ",
                            "or this class must have the same instance type as the super class ",
                            "(annotate this class with @hasSameInstanceTypeAsParent).")
                            .position(this.decl_.name.pos);
                }
            }
        }
        TypeVisitor.visitClassFieldsAndMethods(this, this.decl_);
        is_finalized_ = true;
        if (generateCppClassDefinitions() || !isExtern()) {
            for (Field f : fields()) {
                if (f.is_weak) {
                    Torque.error("Generation of C++ class for Torque class ", name(),
                            " is not supported yet, because field ", f.name_and_type.name,
                            ": ", f.name_and_type.type, " is a weak field.")
            .position(f.pos);
                }
            }
        }
        checkForDuplicateFields();
    }

    public static ClassType dynamicCast(TypeBase declarable) {
        if (declarable == null) return null;
        if (!declarable.isClassType()) return null;
        return (ClassType) declarable;
    }

    public boolean hasIndexedField() {
        if (!is_finalized_) Finalize();
        return (flags_.mask_ & ClassFlag.kHasIndexedField.ordinal()) >= 1;
    }

    public boolean isAbstract() {
        return (flags_.mask_ & ClassFlag.kAbstract.ordinal()) >= 1;
    }

    public boolean hasSameInstanceTypeAsParent() {
        return (flags_.mask_ & ClassFlag.kHasSameInstanceTypeAsParent.ordinal()) >= 1;
    }

    public ClassType getSuperClass() {
        if (parent() == null) return null;
        return parent().isClassType() ? ClassType.dynamicCast(parent()) : null;
    }

    public int size() {
        return size_;
    }

    public Field registerField(Field field) {
        if (field.index != null) {
            flags_.mask_ |= ClassFlag.kHasIndexedField.ordinal();
        }
        return super.registerField(field);
    }

    public boolean generateCppClassDefinitions() {
        return ((flags_.mask_ & ClassFlag.kGenerateCppClassDefinitions.ordinal()) >= 1) || !isExtern();
    }

    public boolean isExtern() {
        return (flags_.mask_ & ClassFlag.kExtern.ordinal()) >= 1;
    }

    public static ClassType cast(TypeBase declarable) {
        return (ClassType) declarable;
    }

    public void setSize(int size) { size_ = size; }

    public void generateAccessors() {
        for (Field field : fields_) {
            if (field.index != null || field.name_and_type.type == TypeOracle.getVoidType()) {
                continue;
            }
            CurrentSourcePosition.Scope position_activator = new CurrentSourcePosition.Scope(field.pos);
            IdentifierExpression parameter =
                    Torque.makeNode(new IdentifierExpression(CurrentSourcePosition.get(),
                            Torque.makeNode(new Identifier(CurrentSourcePosition.get(), "o"))));

            // Load accessor
            String camel_field_name = Torque.camelifyString(field.name_and_type.name);
            String load_macro_name = "Load" + this.name() + camel_field_name;
            Signature load_signature = new Signature();
            load_signature.parameter_names.add(Torque.makeNode(new Identifier(CurrentSourcePosition.get(), "o")));
            load_signature.parameter_types.types.add(this);
            load_signature.parameter_types.var_args = false;
            load_signature.return_type = field.name_and_type.type;
            Statement load_body =
                    Torque.makeNode(new ReturnStatement(CurrentSourcePosition.get(), Torque.makeNode(
                            new FieldAccessExpression(CurrentSourcePosition.get(),
                                parameter, Torque.makeNode(new Identifier(CurrentSourcePosition.get(), field.name_and_type.name))))));
            Declarations.declareMacro(load_macro_name, true, null,
                    load_signature, load_body, null);


            IdentifierExpression value = Torque.makeNode(new IdentifierExpression(CurrentSourcePosition.get(),
                    new Vector<String>(), Torque.makeNode(new Identifier(CurrentSourcePosition.get(), "v"))));
            String store_macro_name = "Store" + this.name() + camel_field_name;
            Signature store_signature = new Signature();
            store_signature.parameter_names.add(Torque.makeNode(new Identifier(CurrentSourcePosition.get(),"o")));
            store_signature.parameter_names.add(Torque.makeNode(new Identifier(CurrentSourcePosition.get(),"v")));
            store_signature.parameter_types.types.add(this);
            store_signature.parameter_types.types.add(field.name_and_type.type);
            store_signature.parameter_types.var_args = false;

            store_signature.return_type = TypeOracle.getVoidType();
            Statement store_body =
                    Torque.makeNode(new ExpressionStatement(CurrentSourcePosition.get(),
                            Torque.makeNode(new AssignmentExpression(CurrentSourcePosition.get(),
                                Torque.makeNode(new FieldAccessExpression(CurrentSourcePosition.get(),
                                    parameter, Torque.makeNode(new Identifier(CurrentSourcePosition.get(),
                                            field.name_and_type.name)))), value))));
            Declarations.declareMacro(store_macro_name, true, null,
                    store_signature, store_body, null, false);
        }
    }

    public void Finalize() {
        if (is_finalized_) return;
        CurrentScope.Scope scope_activator = new CurrentScope.Scope(alias_.parentScope());
        CurrentSourcePosition.Scope position_activator = new CurrentSourcePosition.Scope(decl_.pos);
        if (parent() != null) {
            ClassType super_class = ClassType.dynamicCast(parent());
            if (super_class != null) {
                if (super_class.hasIndexedField()) flags_.mask_ |= ClassFlag.kHasIndexedField.ordinal();
                if (!super_class.isAbstract() && !hasSameInstanceTypeAsParent()) {
                    Torque.error(
                            "Super class must either be abstract (annotate super class with ",
                            "@abstract) ",
                            "or this class must have the same instance type as the super class ",
                            "(annotate this class with @hasSameInstanceTypeAsParent).")
                            .position(this.decl_.name.pos);
                }
            }
        }
        TypeVisitor.visitClassFieldsAndMethods((ClassType) this, this.decl_);
        is_finalized_ = true;
        if (generateCppClassDefinitions() || !isExtern()) {
            for (Field f : fields()) {
                if (f.is_weak) {
                    Torque.error("Generation of C++ class for Torque class ", name(),
                            " is not supported yet, because field ", f.name_and_type.name,
                            ": ", f.name_and_type.type, " is a weak field.").position(f.pos);
                }
            }
        }
        checkForDuplicateFields();
    }

    @Override
    public boolean isTransient() {
        return (flags_.mask_ & ClassFlag.kTransient.ordinal()) > 0;
    }
}
