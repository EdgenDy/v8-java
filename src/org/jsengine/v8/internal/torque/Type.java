package org.jsengine.v8.internal.torque; 

import org.jsengine.v8.internal.Torque;
import org.jsengine.utils.OrderedSet;

public abstract class Type extends TypeBase {
	private Type parent_;
	private OrderedSet<String> aliases_ = new OrderedSet<String>();
	protected Type(TypeBase.Kind kind, Type parent) {
		super(kind);
		parent_ = parent;
	}

	public abstract String toExplicitString();

	public boolean isSubtypeOf(Type supertype) {
		if (supertype.isTopType()) return true;
		if (isNever()) return true;
		UnionType union_type = UnionType.dynamicCast(supertype);
		if (union_type != null) {
			return union_type.isSupertypeOf(this);
		}
		Type subtype = this;
		while (subtype != null) {
			if (subtype == supertype) return true;
			subtype = subtype.parent();
		}
		return false;
	}

	public String toString() {
		if (aliases_.size() == 0) return toExplicitString();
		if (aliases_.size() == 1) return aliases_.begin();
		StringBuilder result = new StringBuilder();
		int i = 0;
		for (String alias : aliases_) {
			if (i == 0) {
				result.append(alias).append(" (aka. ");
			} else if (i == 1) {
				result.append(alias);
			} else {
				result.append(", ").append(alias);
			}
			++i;
		}
		result.append(")");
		return result.toString();
	}

	public boolean isNever() {
		return isAbstractName(Torque.NEVER_TYPE_STRING);
	}

	public boolean isConstexpr() {
		//if (parent() != null) DCHECK(!parent()->IsConstexpr());
		return false;
	}

	protected Type parent() {
		return parent_;
	}

	public boolean isAbstractName(String name) {
		if (!isAbstractType()) return false;
		return AbstractType.cast(this).name() == name;
	}

	public void set_parent(Type t) {
		parent_ = t;
	}

	public Type commonSupertype(Type a, Type b) {
		int diff = a.depth() - b.depth();
		Type a_supertype = a;
		Type b_supertype = b;
		for (; diff > 0; --diff) a_supertype = a_supertype.parent();
		for (; diff < 0; ++diff) b_supertype = b_supertype.parent();
		while (a_supertype != null && b_supertype != null) {
			if (a_supertype == b_supertype) return a_supertype;
			a_supertype = a_supertype.parent();
			b_supertype = b_supertype.parent();
		}
		Torque.reportError("types " + a.toString() + " and " + b.toString() +
				" have no common supertype");
		return null;
	}

	public int depth() {
		int result = 0;
		for (Type current = parent_; current != null; current = current.parent_) {
			++result;
		}
		return result;
	}

	public abstract String mangledName();

	public boolean isVoidOrNever() {
		return isVoid() || isNever();
	}

	public boolean isVoid() {
		return isAbstractName(Torque.VOID_TYPE_STRING);
	}

	public String getGeneratedTypeName() {
		String result = getGeneratedTypeNameImpl();
		if (result.length() == 0 || result == "compiler::TNode<>") {
			Torque.reportError("Generated type is required for type '", toString(),
					"'. Use 'generates' clause in definition.");
		}
		return result;
	}

	protected abstract String getGeneratedTypeNameImpl();

	protected abstract String getGeneratedTNodeTypeNameImpl();

	public String getGeneratedTNodeTypeName() {
		String result = getGeneratedTNodeTypeNameImpl();
		if (result.length() == 0 || isConstexpr()) {
			Torque.reportError("Generated TNode type is required for type '", toString(),
					"'. Use 'generates' clause in definition.");
		}
		return result;
	}

	public boolean isTransient() {
		return false;
	}

	public ClassType classSupertype() {
		for (Type t = this; t != null; t = t.parent()) {
			ClassType class_type = ClassType.dynamicCast(t);
			if (class_type != null) return class_type;
		}
		return null;
	}
};