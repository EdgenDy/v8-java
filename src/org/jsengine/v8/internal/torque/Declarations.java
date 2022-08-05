package org.jsengine.v8.internal.torque; 

import org.jsengine.v8.internal.Torque;

import java.util.function.Function;
import java.util.Vector;

public class Declarations {
	public static Type lookupType(Identifier name) {
		TypeAlias alias = lookupTypeAlias(new QualifiedName(name.value));
		if (GlobalContext.collect_language_server_data()) {
			LanguageServerData.addDefinition(name.pos,
                          alias.getDeclarationPosition());
		}
		return alias.type();
	}
	
	public static TypeAlias lookupTypeAlias(QualifiedName name) {
		Function<Declarable, TypeAlias> function = new Function<Declarable, TypeAlias>() {
			@Override
			public TypeAlias apply(Declarable declarable) {
				return TypeAlias.dynamicCast(declarable);
			}
		};
		TypeAlias declaration = Torque.ensureUnique(Torque.filterDeclarables(lookup(name), function), name, "type");
		return declaration;
	}
	
	public static Vector<Declarable> lookup(QualifiedName name) {
		Vector<Declarable> d = tryLookup(name);
		if (d.size() == 0) {
			Torque.reportError("cannot find \"", name, "\"");
		}
		return d;
	}
	
	public static Vector<Declarable> tryLookup(QualifiedName name) {
    	return CurrentScope.get().lookup(name);
	}

	public static Vector<Generic> lookupGeneric(String name) {
		Function<Declarable, Generic> function = new Function<Declarable, Generic>() {
			@Override
			public Generic apply(Declarable declarable) {
				return Generic.dynamicCast(declarable);
			}
		};
		return Torque.ensureNonempty(Torque.filterDeclarables(lookup(new QualifiedName(name)), function),
				name, "generic");
	}

	public static GenericStructType lookupUniqueGenericStructType(QualifiedName name) {
		Function<Declarable, GenericStructType> function = new Function<Declarable, GenericStructType>() {
			@Override
			public GenericStructType apply(Declarable declarable) {
				return GenericStructType.dynamicCast(declarable);
			}
		};
		return Torque.ensureUnique(Torque.filterDeclarables(lookup(name), function), name, "generic struct");
	}

	public static TypeAlias declareType(Identifier name, Type type) {
		//<TypeAlias>
		Torque.checkAlreadyDeclared(name.value, "type", new Function<Declarable, TypeAlias>() {
			@Override
			public TypeAlias apply(Declarable declarable) {
				return TypeAlias.dynamicCast(declarable);
			}
		});
		return declare(name.value, new TypeAlias(type, true, name.pos), true);
	}

	public static Vector<Declarable> tryLookupShallow(QualifiedName name) {
		return CurrentScope.get().lookupShallow(name);
	}

	public static <T> T declare(String name, T d, boolean unique) {
		return CurrentScope.get().addDeclarable(name, Torque.registerDeclarable(d));
	}

	public static Type lookupGlobalType(String name) {
		Function<Declarable, TypeAlias> function = new Function<Declarable, TypeAlias>() {
			@Override
			public TypeAlias apply(Declarable declarable) {
				return TypeAlias.dynamicCast(declarable);
			}
		};

		TypeAlias declaration = Torque.ensureUnique(
				Torque.filterDeclarables(lookupGlobalScope(name), function), name, "type");
		return declaration.type();
	}

	public static Vector<Declarable> lookupGlobalScope(String name) {
		Vector<Declarable> d = GlobalContext.getDefaultNamespace().lookup(new QualifiedName(name));
		if (d.isEmpty()) {
			Torque.reportError("cannot find \"" + name + "\" in global scope");
		}
		return d;
	}

	public static String getGeneratedCallableName(String name, TypeVector specialized_types) {
		String result = name;
		for (Type type : specialized_types) {
			String type_string = type.mangledName();
			result += type_string.length() + type_string;
		}
		return result;
	}

	public static TorqueMacro createTorqueMacro(String external_name,
										 String readable_name, boolean exported_to_csa,
										 Signature signature, Statement body,
										 boolean is_user_defined) {
		external_name += "_" + GlobalContext.freshId();
		return Torque.registerDeclarable((new TorqueMacro(
				external_name, readable_name, signature,
				body, is_user_defined, exported_to_csa)));
	}

	public static void declareExternConstant(Identifier name, Type type, String value) {
		Torque.checkAlreadyDeclared(name.value, "constant", new Function<Declarable, Value>() {
			@Override
			public Value apply(Declarable declarable) {
				return Value.dynamicCast(declarable);
			}
		});
		ExternConstant result = new ExternConstant(name, type, value);
		declare(name.value, (Declarable) result);
	}

	public static <T> T declare(String name, T d) {
		return CurrentScope.get().addDeclarable(name, Torque.registerDeclarable(d));
	}

	public static Namespace declareNamespace(String name) {
		return declare(name, new Namespace(name));
	}

	public static NamespaceConstant declareNamespaceConstant(Identifier name, Type type,
															  Expression body) {
		Torque.checkAlreadyDeclared(name.value, "constant", new Function<Declarable, Value>() {
			@Override
			public Value apply(Declarable declarable) {
				return Value.dynamicCast(declarable);
			}
		});
		String external_name = name.value + "_" + GlobalContext.freshId();
		NamespaceConstant result = new NamespaceConstant(name, external_name, type, body);
		declare(name.value, (Declarable) result);
		return result;
	}

	public static Macro declareMacro(String name, boolean accessible_from_csa,
										String external_assembler_name,
									 	Signature signature, Statement body,
										String op, boolean is_user_defined) {
		if (tryLookupMacro(name, signature.getExplicitTypes()) != null) {
			Torque.reportError("cannot redeclare macro ", name,
					" with identical explicit parameters");
		}
		Macro macro = null;
		if (external_assembler_name != null) {
			macro = createExternMacro(name, external_assembler_name, signature);
		} else {
			macro = createTorqueMacro(name, name, accessible_from_csa, signature, body,
					is_user_defined);
		}
		declare(name, macro);
		if (op != null) {
			if (tryLookupMacro(op, signature.getExplicitTypes()) != null) {
				Torque.reportError("cannot redeclare operator ", name,
						" with identical explicit parameters");
			}
			declareOperator(op, macro);
		}
		return macro;
	}

	public static Macro declareMacro(String name, boolean accessible_from_csa,
									 String external_assembler_name,
									 Signature signature, Statement body,
									 String op) {
		return declareMacro(name, accessible_from_csa, external_assembler_name,
				signature, body, op);
	}

	public static Macro tryLookupMacro(String name, TypeVector types) {
		Vector<Macro> macros = tryLookup(new QualifiedName(name), new Function<Declarable, Macro>() {
			@Override
			public Macro apply(Declarable declarable) {
				return Macro.dynamicCast(declarable);
			}
		});

		for (Macro m : macros) {
			TypeVector signature_types = m.signature().getExplicitTypes();
			if (signature_types == types && !m.signature().parameter_types.var_args) {
				return m;
			}
		}
		return null;
	}

	public static <T> Vector<T> tryLookup(QualifiedName name, Function<Declarable, T> function) {
		return Torque.filterDeclarables(tryLookup(name), function);
	}

	public static ExternMacro createExternMacro(String name, String external_assembler_name,
													Signature signature) {
		return Torque.registerDeclarable(new ExternMacro(name, external_assembler_name,
				signature));
	}

	public static Macro declareOperator(String name, Macro m) {
		GlobalContext.getDefaultNamespace().addDeclarable(name, m);
		return m;
	}

	public static Builtin createBuiltin(String external_name,
                                                       String readable_name,
                                                       Builtin.Kind kind, Signature signature,
                                                       Statement body) {
        return Torque.registerDeclarable(new Builtin(external_name, readable_name, kind,
                signature, body));
    }

    public static RuntimeFunction declareRuntimeFunction(String name, Signature signature) {
		Torque.checkAlreadyDeclared(name, "runtime function", new Function<Declarable, RuntimeFunction>() {
			@Override
			public RuntimeFunction apply(Declarable declarable) {
				return RuntimeFunction.dynamicCast(declarable);
			}
		});
		return declare(name, Torque.registerDeclarable(new RuntimeFunction(name, signature)));
	}

	public static Intrinsic declareIntrinsic(String name, Signature signature) {
		Intrinsic result = createIntrinsic(name, signature);
		declare(name, result);
		return result;
	}

	public static Intrinsic createIntrinsic(String name, Signature signature) {
		Intrinsic result = Torque.registerDeclarable(new Intrinsic(name, signature));
		return result;
	}

	public static Builtin tryLookupBuiltin(QualifiedName name) {
		Vector<Builtin> builtins = tryLookup(name, new Function<Declarable, Builtin>() {
			@Override
			public Builtin apply(Declarable declarable) {
				return Builtin.dynamicCast(declarable);
			}
		});

		if (builtins.isEmpty()) return null;
		return Torque.ensureUnique(builtins, name.name, "builtin");
	}

	public static Generic lookupUniqueGeneric(QualifiedName name) {
		return Torque.ensureUnique(Torque.filterDeclarables(lookup(name), new Function<Declarable, Generic>() {
					@Override
					public Generic apply(Declarable declarable) {
						return Generic.dynamicCast(declarable);
					}
				}), name,
				"generic");
	}

	public static Value lookupValue(QualifiedName name) {
		return Torque.ensureUnique(Torque.filterDeclarables(lookup(name), new Function<Declarable, Value>() {
			@Override
			public Value apply(Declarable declarable) {
				return Value.dynamicCast(declarable);
			}
		}), name, "value");
	}

	public static Method createMethod(AggregateType container_type, String name, Signature signature,
													 Statement body) {
		String generated_name = container_type.getGeneratedMethodName(name);
		Method result = Torque.registerDeclarable(new Method(container_type,
				container_type.getGeneratedMethodName(name),
						name, signature, body));
		container_type.registerMethod(result);
		return result;
	}
}