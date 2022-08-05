package org.jsengine.v8.internal.torque;

public class StructType extends AggregateType {
    private StructDeclaration decl_;
    private MaybeSpecializationKey specialized_from_;
    public StructType(Namespace nspace, StructDeclaration decl) {
        this(nspace, decl, null);
    }

    public StructType(Namespace nspace, StructDeclaration decl, MaybeSpecializationKey specialized_from) {
        super(Kind.kStructType, null, nspace,
                computeName(decl.name.value, specialized_from));
        decl_ = decl;
        specialized_from_ = specialized_from;
    }

    public static class MaybeSpecializationKey extends SpecializationKey<GenericStructType> {
        public MaybeSpecializationKey(GenericStructType generic, TypeVector specialized_types) {
            super(generic, specialized_types);
        }
    }

    public static String computeName(String basename, MaybeSpecializationKey specialized_from) {
        if (specialized_from == null) return basename;
        StringBuilder s = new StringBuilder();
        s.append(basename).append("<");
        boolean first = true;
        for (Type t : specialized_from.specialized_types) {
            if (!first) {
                s.append(", ");
            }
            s.append(t.toString());
            first = false;
        }
        s.append(">");
        return s.toString();
    }

    public String toExplicitString() {
        StringBuilder result = new StringBuilder();
        result.append("struct ").append(name());
        return result.toString();
    }

    public String mangledName() {
        StringBuilder result = new StringBuilder();
        result.append(decl_.name.value);
        if (specialized_from_ != null) {
            for (Type t : specialized_from_.specialized_types) {
                String arg_type_string = t.mangledName();
                result.append(arg_type_string.length()).append(arg_type_string);
            }
        }
        return result.toString();
    }

    public static StructType dynamicCast(TypeBase declarable) {
        if (declarable == null) return null;
        if (!declarable.isStructType()) return null;
        return (StructType) declarable;
    }

    public static StructType cast(TypeBase declarable) {
        return (StructType) declarable;
    }

    public void Finalize() {
        // TODO: implement this method
        throw new RuntimeException("METHOD UNIMPLEMENTED");
    }

    public static Type matchUnaryGeneric(Type type, GenericStructType generic) {
        StructType struct_type = StructType.dynamicCast(type);
        if (struct_type != null) {
            return matchUnaryGeneric(struct_type, generic);
        }
        return null;
    }

    public MaybeSpecializationKey getSpecializedFrom() {
        return specialized_from_;
    }
}
