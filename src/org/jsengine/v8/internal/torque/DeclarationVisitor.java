package org.jsengine.v8.internal.torque;

import org.jsengine.v8.internal.Torque;

import java.util.Vector;

public class DeclarationVisitor {
	public static void visit(Ast ast) {
		CurrentScope.Scope current_namespace = new CurrentScope.Scope(GlobalContext.getDefaultNamespace());
		for (Declaration child : ast.declarations()) visit(child);
	}
	
	public static void visit(Declaration decl) {
		CurrentSourcePosition.Scope scope = new CurrentSourcePosition.Scope(decl.pos);
		switch (decl.kind) {
			case kAbstractTypeDeclaration:
				visit(AbstractTypeDeclaration.cast(decl));
			case kTypeAliasDeclaration:
				visit(TypeAliasDeclaration.cast(decl));
			case kClassDeclaration:
				visit(ClassDeclaration.cast(decl));
			case kStructDeclaration:
				visit(StructDeclaration.cast(decl));
			case kGenericDeclaration:
				visit(GenericDeclaration.cast(decl));
			case kSpecializationDeclaration:
				visit(SpecializationDeclaration.cast(decl));
			case kExternConstDeclaration:
				visit(ExternConstDeclaration.cast(decl));
			case kNamespaceDeclaration:
				visit(NamespaceDeclaration.cast(decl));
			case kConstDeclaration:
				visit(ConstDeclaration.cast(decl));
			case kCppIncludeDeclaration:
				visit(CppIncludeDeclaration.cast(decl));
			case kTorqueMacroDeclaration:
				visit(TorqueMacroDeclaration.cast(decl));
			case kTorqueBuiltinDeclaration:
				visit(TorqueBuiltinDeclaration.cast(decl));
			case kExternalMacroDeclaration:
				visit(ExternalMacroDeclaration.cast(decl));
			case kExternalBuiltinDeclaration:
				visit(ExternalBuiltinDeclaration.cast(decl));
			case kExternalRuntimeDeclaration:
				visit(ExternalRuntimeDeclaration.cast(decl));
			case kIntrinsicDeclaration:
				visit(IntrinsicDeclaration.cast(decl));
			default:
				throw new RuntimeException("unimplemented");
		}
	}
	
	public static void visit(TypeDeclaration decl) {
		Declarations.lookupType(decl.name);
	}

	public static void visit(GenericDeclaration decl) {
        // The PredeclarationVisitor already handled this case.
    }

    public static void visit(SpecializationDeclaration decl) {
        Vector<Generic> generic_list = Declarations.lookupGeneric(decl.name.value);
		// Find the matching generic specialization based on the concrete parameter
		// list.
		Generic matching_generic = null;
		Signature signature_with_types = TypeVisitor.makeSignature(decl);

		for (Generic generic : generic_list) {
			Signature generic_signature_with_types =
					makeSpecializedSignature(new SpecializationKey<Generic>(
							generic, TypeVisitor.computeTypeVector(decl.generic_parameters)));

			if (signature_with_types.hasSameTypesAs(generic_signature_with_types,
														ParameterMode.kIgnoreImplicit)) {
				if (matching_generic != null) {
					StringBuilder stream = new StringBuilder();
					stream.append("specialization of ").append(decl.name)
							.append(" is ambigous, it matches more than one generic declaration (")
							.append(matching_generic).append(" and ").append(generic).append(")");
					Torque.reportError(stream.toString());
				}
				matching_generic = generic;
			}
		}

		if (matching_generic == null) {
			StringBuilder stream = new StringBuilder();
			if (generic_list.size() == 0) {
				stream.append("no generic defined with the name ").append(decl.name);
				Torque.reportError(stream.toString());
			}
			stream.append("specialization of ").append(decl.name)
					.append(" doesn't match any generic declaration\n");
			stream.append("specialization signature:");
			stream.append("\n  ").append(signature_with_types);
			stream.append("\ncandidates are:");
			for (Generic generic : generic_list) {
				stream.append("\n  ")
						.append(makeSpecializedSignature(new SpecializationKey<Generic>(
						generic,
						TypeVisitor.computeTypeVector(decl.generic_parameters))));
			}
			Torque.reportError(stream.toString());
		}

		if (GlobalContext.collect_language_server_data()) {
			LanguageServerData.addDefinition(decl.name.pos,
					matching_generic.identifierPosition());
		}

		CallableDeclaration generic_declaration = matching_generic.declaration();

		specialize(new SpecializationKey<Generic>(matching_generic,
						TypeVisitor.computeTypeVector(
						decl.generic_parameters)),
				generic_declaration, decl, decl.body, decl.pos);
    }

    public static Signature makeSpecializedSignature(SpecializationKey<Generic> key) {
		CurrentScope.Scope generic_scope = new CurrentScope.Scope(key.generic.parentScope());
		Namespace tmp_namespace = new Namespace("_tmp");
		CurrentScope.Scope tmp_namespace_scope = new CurrentScope.Scope(tmp_namespace);
		declareSpecializedTypes(key);
		return TypeVisitor.makeSignature(key.generic.declaration());
	}

	public static void declareSpecializedTypes(SpecializationKey<Generic> key) {
		int i = 0;
		int generic_parameter_count = key.generic.generic_parameters().size();
		if (generic_parameter_count != key.specialized_types.size()) {
			StringBuilder stream = new StringBuilder();
			stream.append("Wrong generic argument count for specialization of \"")
				.append(key.generic.name()).append("\", expected: ").append(generic_parameter_count)
					.append(", actual: ").append(key.specialized_types.size());
			Torque.reportError(stream.toString());
		}

		for (Type type : key.specialized_types) {
			Identifier generic_type_name = key.generic.generic_parameters().get(i++);
			TypeAlias alias = Declarations.declareType(generic_type_name, type);
			alias.setIsUserDefined(false);
		}
	}

	public static Callable specialize(SpecializationKey<Generic> key, CallableDeclaration declaration,
									SpecializationDeclaration explicit_specialization,
									Statement body, SourcePosition position) {
        CurrentSourcePosition.Scope pos_scope = new CurrentSourcePosition.Scope(position);
        int generic_parameter_count = key.generic.generic_parameters().size();
        if (generic_parameter_count != key.specialized_types.size()) {
            StringBuilder stream = new StringBuilder();
            stream.append("number of template parameters (")
                    .append(key.specialized_types.size())
                    .append(") to intantiation of generic ").append(declaration.name)
                    .append(" doesnt match the generic's declaration (")
                    .append(generic_parameter_count).append(")");
            Torque.reportError(stream.toString());
        }
        if (key.generic.specializations().get(key.specialized_types) != null) {
			Torque.reportError("cannot redeclare specialization of ", key.generic.name(),
                    " with types <", key.specialized_types, ">");
        }

		Signature type_signature =
				explicit_specialization != null
						? TypeVisitor.makeSignature(explicit_specialization)
          : makeSpecializedSignature(key);

		String generated_name = Declarations.getGeneratedCallableName(
				declaration.name.value, key.specialized_types);
		StringBuilder readable_name = new StringBuilder();
		readable_name.append(declaration.name.value).append("<");
		boolean first = true;
		for (Type t : key.specialized_types) {
			if (!first) readable_name.append(", ");
			readable_name.append(t);
			first = false;
		}
		readable_name.append(">");
		Callable callable;
		if (MacroDeclaration.dynamicCast(declaration) != null) {
			callable =
					Declarations.createTorqueMacro(generated_name, readable_name.toString(),
					false, type_signature, body, true);
		}
		return null;
	}

	public static void visit(ExternConstDeclaration decl) {
		Type type = TypeVisitor.computeType(decl.type);
		if (!type.isConstexpr()) {
			StringBuilder stream = new StringBuilder();
			stream.append("extern constants must have constexpr type, but found: \"")
					.append(type).append("\"\n");
			Torque.reportError(stream.toString());
		}

		Declarations.declareExternConstant(decl.name, type, decl.literal);
	}

	public static void visit(NamespaceDeclaration decl) {
		CurrentScope.Scope current_scope = new CurrentScope.Scope(Torque.getOrCreateNamespace(decl.name));
		for (Declaration child : decl.declarations) visit(child);
	}

	public static void visit(ConstDeclaration decl) {
		Declarations.declareNamespaceConstant(
				decl.name, TypeVisitor.computeType(decl.type), decl.expression);
	}

	public static void visit(CppIncludeDeclaration decl) {
		GlobalContext.addCppInclude(decl.include_path);
	}

	public static void visit(TorqueMacroDeclaration decl) {
		Macro macro = Declarations.declareMacro(
				decl.name.value, decl.export_to_csa, null,
				TypeVisitor.makeSignature(decl), decl.body, decl.op);
		macro.setPosition(decl.pos);
	}

	public static void visit(TorqueBuiltinDeclaration decl) {
		Declarations.declare(
				decl.name.value, createBuiltin(decl, decl.name.value, decl.name.value,
						TypeVisitor.makeSignature(decl), decl.body));
	}

	public static Builtin createBuiltin(BuiltinDeclaration decl,
											   String external_name, String readable_name,
											   Signature signature, Statement body) {
		boolean javascript = decl.javascript_linkage;
		boolean varargs = decl.parameters.has_varargs;
		Builtin.Kind kind = !javascript ? Builtin.Kind.kStub
				: varargs ? Builtin.Kind.kVarArgsJavaScript
				: Builtin.Kind.kFixedArgsJavaScript;

		if (varargs && !javascript) {
			Torque.error("Rest parameters require ", decl.name,
					" to be a JavaScript builtin");
		}

		if (javascript) {
			if (!signature.return_type.isSubtypeOf(TypeOracle.getJSAnyType())) {
				Torque.error("Return type of JavaScript-linkage builtins has to be JSAny.")
						.position(decl.return_type.pos);
			}
			for (int i = signature.implicit_count; i < signature.parameter_types.types.size(); ++i) {
				Type parameter_type = signature.parameter_types.types.get(i);
				if (parameter_type != TypeOracle.getJSAnyType()) {
					Torque.error("Parameters of JavaScript-linkage builtins have to be JSAny.")
							.position(decl.parameters.types.get(i).pos);
				}
			}
		}

		for (int i = 0; i < signature.types().size(); ++i) {
			StructType type = StructType.dynamicCast(signature.types().get(i));
			if (type != null) {
				Torque.error("Builtin '", decl.name, "' uses the struct '", type.name(),
						"' as argument '", signature.parameter_names.get(i),
						"', which is not supported.");
			}
		}

		if (TorqueBuiltinDeclaration.dynamicCast(decl) != null) {
			for (int i = 0; i < signature.types().size(); ++i) {
				Type type = signature.types().get(i);
				if (!type.isSubtypeOf(TypeOracle.getTaggedType())) {
					Identifier id = signature.parameter_names.size() > i
							? signature.parameter_names.get(i)
							: null;
					Torque.error("Untagged argument ", id != null ? (id.value + " ") : "", "at position ",
							i, " to builtin ", decl.name, " is not supported.")
							.position(id != null ? id.pos : decl.pos);
				}
			}
		}

		StructType struct_type = StructType.dynamicCast(signature.return_type);
		if (struct_type != null) {
			Torque.error("Builtins ", decl.name, " cannot return structs ",
					struct_type.name());
		}

		return Declarations.createBuiltin(external_name,
				readable_name, kind,
				signature, body);
	}

	public static void visit(ExternalMacroDeclaration decl) {
		Declarations.declareMacro(
				decl.name.value, true, decl.external_assembler_name,
				TypeVisitor.makeSignature(decl), null, decl.op);
	}

	public static void visit(ExternalBuiltinDeclaration decl) {
		Declarations.declare(decl.name.value,
				createBuiltin(decl, decl.name.value, decl.name.value,
						TypeVisitor.makeSignature(decl), null));
	}

	public static void visit(ExternalRuntimeDeclaration decl) {
		Signature signature = TypeVisitor.makeSignature(decl);
		if (signature.parameter_types.types.size() == 0 ||
				!(signature.parameter_types.types.get(0) == TypeOracle.getContextType())) {
			Torque.reportError(
					"first parameter to runtime functions has to be the context and have ",
					"type Context, but found type ",
					signature.parameter_types.types.get(0));
		}
		if (!(signature.return_type.isSubtypeOf(TypeOracle.getObjectType()) ||
		signature.return_type == TypeOracle.getVoidType() ||
				signature.return_type == TypeOracle.getNeverType())) {
			Torque.reportError(
					"runtime functions can only return tagged values, but found type ",
					signature.return_type);
		}
		for (Type parameter_type : signature.parameter_types.types) {
			if (!parameter_type.isSubtypeOf(TypeOracle.getObjectType())) {
				Torque.reportError(
						"runtime functions can only take tagged values as parameters, but ",
						"found type ", parameter_type);
			}
		}

		Declarations.declareRuntimeFunction(decl.name.value, signature);
	}

	public static void visit(IntrinsicDeclaration decl) {
		Declarations.declareIntrinsic(decl.name.value,
				TypeVisitor.makeSignature(decl));
	}

	public static Callable specializeImplicit(SpecializationKey<Generic> key) {
		Statement body = key.generic.callableBody();
		if (body == null && IntrinsicDeclaration.dynamicCast(key.generic.declaration()) == null) {
			Torque.reportError("missing specialization of ", key.generic.name(),
					" with types <", key.specialized_types, "> declared at ",
					key.generic.position());
		}
		CurrentScope.Scope generic_scope = new CurrentScope.Scope(key.generic.parentScope());
		Callable result = specialize(key, key.generic.declaration(), null, body, CurrentSourcePosition.get());
		result.setIsUserDefined(false);
		CurrentScope.Scope callable_scope = new CurrentScope.Scope(result);
		declareSpecializedTypes(key);
		return result;
	}
}