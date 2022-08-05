package org.jsengine.v8.internal.torque;

import org.jsengine.utils.OrderedSet;
import org.jsengine.v8.internal.Torque;
import org.jsengine.utils.Set;

import java.util.HashSet;
import java.util.function.Function;

public class UnionType extends Type {
    public OrderedSet<Type> types_ = new OrderedSet<Type>();
    public UnionType(Type t) {
        super(Kind.kUnionType, t);
        types_.add(t);

        HashSet<Type> hashset = new HashSet<Type>();
    }

    public Type getSingleMember() {
        if (types_.size() == 1) {
            return types_.begin();
        }
        return null;
    }

    public String toExplicitString() {
        StringBuilder result = new StringBuilder();
        result.append("(");
        boolean first = true;
        for (Type t : types_) {
            if (!first) {
                result.append(" | ");
            }
            first = false;
            result.append(t);
        }
        result.append(")");
        return result.toString();
    }

    public static UnionType fromType(Type t) {
        UnionType union_type = UnionType.dynamicCast(t);
        return union_type != null ? union_type : new UnionType(t);
    }

    public static UnionType dynamicCast(TypeBase declarable) {
        if (declarable == null) return null;
        if (!declarable.isUnionType()) return null;
        return (UnionType) declarable;
    }

    public void extend(Type t) {
        UnionType union_type = UnionType.dynamicCast(t);
        if (union_type != null) {
            for (Type member : union_type.types_) {
                extend(member);
            }
        } else {
            if (t.isSubtypeOf(this)) return;
            set_parent(commonSupertype(parent(), t));
            Torque.eraseIf(types_, new Function<Type, Boolean>() {
                @Override
                public Boolean apply(Type member) {
                    return member.isSubtypeOf(t);
                }
            });
            types_.add(t);
        }
    }

    public boolean isSupertypeOf(Type other) {
        for (Type member : types_) {
            if (other.isSubtypeOf(member)) {
                return true;
            }
        }
        return false;
    }

    public String mangledName() {
        StringBuilder result = new StringBuilder();
        result.append("UT");
        for (Type t : types_) {
            String arg_type_string = t.mangledName();
            result.append(arg_type_string.length()).append(arg_type_string);
        }
        return result.toString();
    }

    @Override
    public boolean isTransient() {
        for (Type member : types_) {
            if (member.isTransient()) {
                return true;
            }
        }
        return false;
    }

    @Override
    public String getGeneratedTypeNameImpl() {
        return "compiler::TNode<" + getGeneratedTNodeTypeName() + ">";
    }

    @Override
    public String getGeneratedTNodeTypeNameImpl() {
        if (types_.size() <= 3) {
            Set<String> members = new Set<String>();
            for (Type t : types_) {
                members.insert(t.getGeneratedTNodeTypeName());
            }
            if (members.equals(new Set<String>("Smi", "HeapNumber"))) {
                return "Number";
            }
            if (members.equals(new Set<String>("Smi", "HeapNumber", "BigInt"))) {
                return "Numeric";
            }
        }
        return parent().getGeneratedTNodeTypeName();
    }
}
