package org.jsengine.v8.internal.torque; 

import org.jsengine.v8.internal.Torque;
import org.jsengine.v8.Internal;

import java.util.Vector;

public class TypeOracle {
	public static ThreadLocal<Scope> top_ = new ThreadLocal<Scope>();
	private Deduplicator<BuiltinPointerType> function_pointer_types_ = new Deduplicator<BuiltinPointerType>();
	private Vector<BuiltinPointerType> all_builtin_pointer_types_ = new Vector<BuiltinPointerType>();
	private Vector<AggregateType> aggregate_types_ = new Vector<AggregateType>();
	private Vector<Namespace> struct_namespaces_ = new Vector<Namespace>();
	private Deduplicator<UnionType> union_types_ = new Deduplicator<UnionType>();
	private Vector<Type> top_types_ = new Vector<Type>();
	
	public static class Scope {
		public TypeOracle value_;
		public Scope previous_;
		
		public Scope() {
			value_ = new TypeOracle();
			previous_ = top();
			top(this);
		}
		
		public TypeOracle value() {
			return value_;
		}
	}
	
	public TypeOracle() {
	}
	
	public static Scope top() {
		return top_.get();
	}
		
	public static void top(Scope scope) {
		top_.set(scope);
	}
	
	public static TypeOracle get() {
		return top().value();
	}

	public static StructType getGenericStructTypeInstance(GenericStructType generic_struct, TypeVector arg_types) {
        Vector<Identifier> params = generic_struct.generic_parameters();
        SpecializationMap<StructType> specializations = generic_struct.specializations();

        if (params.size() != arg_types.size()) {
            Torque.reportError("Generic struct takes ", params.size(), " parameters, but ",
                    arg_types.size(), " were given");
        }

		StructType specialization = specializations.get(arg_types);
        if (specialization != null) {
            return specialization;
        } else {
            CurrentScope.Scope generic_scope = new CurrentScope.Scope(generic_struct.parentScope());
			StructType struct_type = TypeVisitor.computeType(generic_struct.declaration(),
                    new StructType.MaybeSpecializationKey(generic_struct, arg_types));
            specializations.add(arg_types, struct_type);
            return struct_type;
        }
	}

	public static StructType getStructType(StructDeclaration decl, StructType.MaybeSpecializationKey specialized_from) {
		Namespace nspace = new Namespace(Torque.STRUCT_NAMESPACE_STRING);
		StructType result = new StructType(nspace, decl, specialized_from);
		get().aggregate_types_.add(result);
		get().struct_namespaces_.add(nspace);
		return result;
	}

	public static Type getUnionType(UnionType type) {
		Type single = type.getSingleMember();
		if (single != null) {
			return single;
		}
		return get().union_types_.add(type);
	}

	public static Type getUnionType(Type a, Type b) {
		if (a.isSubtypeOf(b)) return b;
		if (b.isSubtypeOf(a)) return a;
		UnionType result = UnionType.fromType(a);
		result.extend(b);
		return getUnionType(result);
	}

	public static BuiltinPointerType getBuiltinPointerType(TypeVector argument_types, Type return_type) {
		TypeOracle self = get();
		Type builtin_type = self.getBuiltinType(Torque.BUILTIN_POINTER_TYPE_STRING);
		BuiltinPointerType result = self.function_pointer_types_.add(
				new BuiltinPointerType(builtin_type, argument_types, return_type,
						self.all_builtin_pointer_types_.size()));
		if (result.function_pointer_type_id() ==
				self.all_builtin_pointer_types_.size()) {
			self.all_builtin_pointer_types_.add(result);
		}
		return result;
	}

	public Type getBuiltinType(String name) {
		return Declarations.lookupGlobalType(name);
	}

	public static Type getJSAnyType() {
		return get().getBuiltinType(Torque.JSANY_TYPE_STRING);
	}

	public static Type getTaggedType() {
		return get().getBuiltinType(Torque.TAGGED_TYPE_STRING);
	}

	public static Type getContextType() {
		return get().getBuiltinType(Torque.CONTEXT_TYPE_STRING);
	}

	public static Type getObjectType() {
		return get().getBuiltinType(Torque.OBJECT_TYPE_STRING);
	}

	public static Type getVoidType() {
		return get().getBuiltinType(Torque.VOID_TYPE_STRING);
	}

	public static Type getNeverType() {
		return get().getBuiltinType(Torque.NEVER_TYPE_STRING);
	}

	public static void finalizeAggregateTypes() {
		for (AggregateType p : get().aggregate_types_) {
			p.Finalize();
		}
	}

	public static Type getRawPtrType() {
		return get().getBuiltinType(Torque.RAWPTR_TYPE_STRING);
	}

	public static Type getInt8Type() {
		return get().getBuiltinType(Torque.INT8_TYPE_STRING);
	}

	public static Type getUint8Type() {
		return get().getBuiltinType(Torque.UINT8_TYPE_STRING);
	}

	public static Type getInt16Type() {
		return get().getBuiltinType(Torque.INT16_TYPE_STRING);
	}

	public static Type getUint16Type() {
		return get().getBuiltinType(Torque.UINT16_TYPE_STRING);
	}

	public static Type getInt32Type() {
		return get().getBuiltinType(Torque.INT32_TYPE_STRING);
	}

	public static Type getUint32Type() {
		return get().getBuiltinType(Torque.UINT32_TYPE_STRING);
	}

	public static Type getFloat64Type() {
		return get().getBuiltinType(Torque.FLOAT64_TYPE_STRING);
	}

	public static Type getIntPtrType() {
		return get().getBuiltinType(Torque.INTPTR_TYPE_STRING);
	}

	public static Type getUIntPtrType() {
		return get().getBuiltinType(Torque.UINTPTR_TYPE_STRING);
	}

	public static Type getHeapObjectType() {
		return get().getBuiltinType(Torque.HEAP_OBJECT_TYPE_STRING);
	}

	public static GenericStructType getReferenceGeneric() {
		Vector<String> vector_string = new Vector<String>();
		vector_string.add(Torque.TORQUE_INTERNAL_NAMESPACE_STRING);
		return Declarations.lookupUniqueGenericStructType(new QualifiedName(
				vector_string, Torque.REFERENCE_TYPE_STRING));
	}

	public static GenericStructType getSliceGeneric() {
		Vector<String> vector_string = new Vector<String>();
		vector_string.add(Torque.TORQUE_INTERNAL_NAMESPACE_STRING);
		return Declarations.lookupUniqueGenericStructType(
				new QualifiedName(vector_string, Torque.SLICE_TYPE_STRING));
	}

	public static boolean isImplicitlyConvertableFrom(Type to, Type from) {
		for (Generic from_constexpr : Declarations.lookupGeneric(Torque.kFromConstexprMacroName)) {
			Callable specialization = from_constexpr.specializations().get(new TypeVector(to, from));
			if (specialization != null) {
				if (specialization.signature().getExplicitTypes() == new TypeVector(from)) {
					return true;
				}
			}
		}
		return false;
	}

	public static TopType getTopType(String reason, Type source_type) {
		TopType result = new TopType(reason, source_type);
		get().top_types_.add(result);
		return result;
	}

	public static Type getBoolType() {
		return get().getBuiltinType(Torque.BOOL_TYPE_STRING);
	}

	public static Type getSmiType() {
		return get().getBuiltinType(Torque.SMI_TYPE_STRING);
	}

	public static Type getNumberType() {
		return get().getBuiltinType(Torque.NUMBER_TYPE_STRING);
	}

	public static Type getStringType() {
		return get().getBuiltinType(Torque.STRING_TYPE_STRING);
	}

	public static Type getJSObjectType() {
		return get().getBuiltinType(Torque.JSOBJECT_TYPE_STRING);
	}
}