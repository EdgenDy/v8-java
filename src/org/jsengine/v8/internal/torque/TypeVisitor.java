package org.jsengine.v8.internal.torque; 

import org.jsengine.v8.internal.Torque;
import org.jsengine.utils.Tuple;

import java.util.Vector;
import java.util.Iterator;

public class TypeVisitor {
	public static Type computeType(TypeDeclaration decl) {
		CurrentSourcePosition.Scope scope = new CurrentSourcePosition.Scope(decl.pos);
		switch (decl.kind) {
			case kAbstractTypeDeclaration:
				return computeType(AbstractTypeDeclaration.cast(decl));
			case kTypeAliasDeclaration:
				return computeType(TypeAliasDeclaration.cast(decl));
			case kClassDeclaration:
				return computeType(ClassDeclaration.cast(decl));
			case kStructDeclaration:
				return computeType(StructDeclaration.cast(decl));
			default:
				throw new RuntimeException("unimplemented");
		}
	}

	public static Signature makeSignature(CallableDeclaration declaration) {
		LabelDeclarationVector definition_vector = new LabelDeclarationVector();
		for (LabelAndTypes label : declaration.labels) {
			LabelDeclaration def = new LabelDeclaration(label.name, computeTypeVector(label.types));
			definition_vector.add(def);
		}
        String arguments_variable = null;
        if (declaration.parameters.has_varargs)
            arguments_variable = declaration.parameters.arguments_variable;

        Signature result = new Signature(new NameVector(declaration.parameters.names),
                arguments_variable,
                new ParameterTypes(computeTypeVector(declaration.parameters.types),
                        declaration.parameters.has_varargs),
                declaration.parameters.implicit_count,
                computeType(declaration.return_type),
                definition_vector,
                declaration.transitioning);
        return result;
	}

	public static TypeVector computeTypeVector(Vector<TypeExpression> v) {
		TypeVector result = new TypeVector();
		for (TypeExpression t : v) {
			result.add(computeType(t));
		}
		return result;
	}

	public static Type computeType(TypeExpression type_expression) {
		BasicTypeExpression basic = BasicTypeExpression.dynamicCast(type_expression);
        UnionTypeExpression union_type = null;
		if (basic != null) {
			QualifiedName qualified_name = new QualifiedName(basic.namespace_qualification, basic.name);
			Vector<TypeExpression> args = basic.generic_arguments;
    		Type type;
			SourcePosition pos = SourcePosition.invalid();

			if (args.size() == 0) {
				TypeAlias alias = Declarations.lookupTypeAlias(qualified_name);
				type = alias.type();
				pos = alias.getDeclarationPosition();
			} else {
				GenericStructType generic_struct = Declarations.lookupUniqueGenericStructType(qualified_name);
				type = TypeOracle.getGenericStructTypeInstance(generic_struct,
						computeTypeVector(args));
				pos = generic_struct.declaration().name.pos;
			}

            if (GlobalContext.collect_language_server_data()) {
                LanguageServerData.addDefinition(type_expression.pos, pos);
            }
            return type;
		} else if((union_type = UnionTypeExpression.dynamicCast(type_expression)) != null) {
            return TypeOracle.getUnionType(computeType(union_type.a),
                    computeType(union_type.b));
        } else {
			FunctionTypeExpression function_type_exp = FunctionTypeExpression.cast(type_expression);
			TypeVector argument_types = new TypeVector();
			for (TypeExpression type_exp : function_type_exp.parameters) {
				argument_types.add(computeType(type_exp));
			}
			return TypeOracle.getBuiltinPointerType(
					argument_types, computeType(function_type_exp.return_type));
		}
	}

	public static StructType computeType(StructDeclaration decl, StructType.MaybeSpecializationKey specialized_from) {
		StructType struct_type = TypeOracle.getStructType(decl, specialized_from);
		CurrentScope.Scope struct_namespace_scope = new CurrentScope.Scope(struct_type.nspace());
		CurrentSourcePosition.Scope position_activator = new CurrentSourcePosition.Scope(decl.pos);

		if (specialized_from != null) {
			Vector<Identifier> params = specialized_from.generic.generic_parameters();
			Iterator<Type> arg_types_iterator = specialized_from.specialized_types.iterator();
			for (Identifier param : params) {
				TypeAlias alias = Declarations.declareType(param, arg_types_iterator.next());
				alias.setIsUserDefined(false);
			}
		}

        int offset = 0;
        for (StructFieldExpression field : decl.fields) {
            CurrentSourcePosition.Scope position_activator0 = new CurrentSourcePosition.Scope(
                    field.name_and_type.type.pos);
            Type field_type = TypeVisitor.computeType(field.name_and_type.type);
            if (field_type.isConstexpr()) {
                Torque.reportError("struct field \"", field.name_and_type.name.value,
                        "\" carries constexpr type \"", field_type, "\"");
            }
        }
        return struct_type;
	}

	public static StructType computeType(StructDeclaration decl) {
		return computeType(decl, null);
	}

	public static void visitClassFieldsAndMethods(ClassType class_type, ClassDeclaration class_declaration) {
		ClassType super_class = class_type.getSuperClass();
		int class_offset = super_class != null ? super_class.size() : 0;
		boolean seen_indexed_field = false;
		for (ClassFieldExpression field_expression : class_declaration.fields) {
			CurrentSourcePosition.Scope position_activator = new CurrentSourcePosition.Scope(
					field_expression.name_and_type.type.pos);
			Type field_type = computeType(field_expression.name_and_type.type);
			if (!((class_declaration.flags.mask_ & ClassFlag.kExtern.value_) <= 0)) {
				if (!field_type.isSubtypeOf(TypeOracle.getTaggedType())) {
					Torque.reportError("non-extern classes do not support untagged fields");
				}
				if (field_expression.weak) {
					Torque.reportError("non-extern classes do not support weak fields");
				}
			}
			if (field_expression.index != null) {
				if (seen_indexed_field ||
						(super_class != null && super_class.hasIndexedField())) {
					Torque.reportError(
							"only one indexable field is currently supported per class");
				}
				seen_indexed_field = true;
				Field index_field = class_type.lookupFieldInternal(field_expression.index);
				class_type.registerField(
						new Field(field_expression.name_and_type.name.pos,
						class_type,
						index_field,
						new NameAndType(field_expression.name_and_type.name.value, field_type),
				class_offset,
						field_expression.weak,
						field_expression.const_qualified,
						field_expression.generate_verify));
			} else {
				if (seen_indexed_field) {
					Torque.reportError("cannot declare non-indexable field \"",
							field_expression.name_and_type.name,
							"\" after an indexable field ",
							"declaration");
				}
				Field field = class_type.registerField(
						new Field(field_expression.name_and_type.name.pos,
						class_type,
						null,
						new NameAndType(field_expression.name_and_type.name.value, field_type),
				class_offset,
						field_expression.weak,
						field_expression.const_qualified,
						field_expression.generate_verify));
				int field_size;
				String size_string;
				String machine_type;
				Tuple<Integer, String> tuple = field.getFieldSizeInformation();
				field_size = tuple.first;
				size_string = tuple.second;

				int alignment = Math.min(TargetArchitecture.taggedSize(), field_size);
				if (alignment > 0 && class_offset % alignment != 0) {
					Torque.reportError("field ", field_expression.name_and_type.name,
							" at offset ", class_offset, " is not ", alignment,
							"-byte aligned.");
				}
				class_offset += field_size;
			}
		}
		class_type.setSize(class_offset);
		class_type.generateAccessors();
		Torque.declareMethods(class_type, class_declaration.methods);
	}
}