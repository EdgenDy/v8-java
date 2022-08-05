package org.jsengine.v8.internal; 

import org.jsengine.v8.internal.torque.*;
import org.jsengine.v8.base.OS;

import org.jsengine.utils.UnorderedSet;
import org.jsengine.utils.Optional;
import org.jsengine.utils.Tuple;

import org.jsengine.Globals;

import java.util.AbstractMap;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Vector;
import java.util.Set;
import java.util.function.Function;

public class Torque {
	public static int WrappedMain(String[] args) {
		TorqueCompilerOptions options = new TorqueCompilerOptions();
		options.collect_language_server_data = false;
		options.force_assert_statements = false;
		
		Vector<String> files = new Vector<String>();
		
		for (int i = 1; i < args.length; ++i) {
			String argument = args[i];
			
			if (argument == "-o") {
				options.output_directory = args[++i];
			}
			else if (argument == "-v8-root") {
				options.v8_root = args[++i];
			}
			else if (argument == "-m32") {
				options.force_32bit_output = true;
			}
			else {
				files.add(argument);
				if (!argument.endsWith(".tq")) {
					System.err.println("Unexpected command-line argument \"" + argument + "\", expected a .tq file.\n");
					OS.abort();
				}
			}
		}
		
		TorqueCompilerResult result = compileTorque(files, options);
		
		return 0;
	}
	
	public static TorqueCompilerResult compileTorque(String source, TorqueCompilerOptions options) {
		return null;
	}
	
	public static TorqueCompilerResult compileTorque(Vector<String> files, TorqueCompilerOptions options) {
		SourceFileMap.Scope source_map_scope = new SourceFileMap.Scope(new SourceFileMap(options.v8_root));
		CurrentSourceFile.Scope no_file_scope = new CurrentSourceFile.Scope(SourceFileMap.addSource("dummy-filename.tq"));
		CurrentAst.Scope ast_scope = new CurrentAst.Scope(new Ast());
		TorqueMessages.Scope messages_scope = new TorqueMessages.Scope(new Vector<TorqueMessage>());
		LanguageServerData.Scope server_data_scope = new LanguageServerData.Scope(new LanguageServerData());
		
		TorqueCompilerResult result = new TorqueCompilerResult();
		try {
			for (String path : files) {
				readAndParseTorqueFile(path);
			}
			compileCurrentAst(options);
		} 
		catch (TorqueAbortCompilation tac) {
			
		}
		return null;
	}
	
	public static void parseTorque(String input) {
		BuildFlags.Scope build_flags_scope = new BuildFlags.Scope(new BuildFlags());
		new TorqueGrammar().parse(input);
	}
	
	public static Action defaultAction = new Action() {
		@Override
		public ParseResult apply(ParseResultIterator child_results) {
			if (!child_results.hasNext()) return null;
			return child_results.next();
		}
	};
	
	
	public static void readAndParseTorqueFile(String path) {
		SourceId source_id = SourceFileMap.addSource(path);
		CurrentSourceFile.Scope source_id_scope = new CurrentSourceFile.Scope(source_id);
		
		String maybe_content = readFile(SourceFileMap.absolutePath(source_id));
		if (maybe_content == null) {
			String maybe_path = fileUriDecode(path);
			if (maybe_path != null) {
				maybe_content = readFile(maybe_path);
			}
		}
		
		if (maybe_content != null) {
			throw new RuntimeException("Cannot open file path/uri: " + path);
		}

		parseTorque(maybe_content);
	}
	
	public static void compileCurrentAst(TorqueCompilerOptions options) {
		GlobalContext.Scope global_context = new GlobalContext.Scope(CurrentAst.get());
		if (options.collect_language_server_data) {
			GlobalContext.setCollectLanguageServerData();
		}

		if (options.force_assert_statements) {
			GlobalContext.setForceAssertStatements();
		}
		TargetArchitecture.Scope target_architecture = new TargetArchitecture.Scope(options.force_32bit_output);
		TypeOracle.Scope type_oracle = new TypeOracle.Scope();
		
		PredeclarationVisitor.predeclare(GlobalContext.ast());
		PredeclarationVisitor.resolvePredeclarations();
		
		DeclarationVisitor.visit(GlobalContext.ast());

        TypeOracle.finalizeAggregateTypes();

		String output_directory = options.output_directory;

		ImplementationVisitor implementation_visitor = new ImplementationVisitor();
		implementation_visitor.setDryRun(output_directory.length() == 0);

		implementation_visitor.beginCSAFiles();

        implementation_visitor.visitAllDeclarables();
	}
	
	public static String readFile(String file) {
		return Globals.readFile(file);
	}
	
	public static String fileUriDecode(String uri) {
		if (uri != null)
			throw new RuntimeException("Torque::fileUriDecode not yet implemented.");
		return null;
	}
	
	public static Action processTorqueImportDeclaration = new Action() {
		@Override
		public ParseResult apply(ParseResultIterator child_results) {
			String import_path = (String) ((ParseResultHolder) (child_results.next().value_)).value_;
			if (!SourceFileMap.fileRelativeToV8RootExists(import_path)) {
				throw new RuntimeException("File '" + import_path + "' not found.");
			}
			
			SourceId import_id = SourceFileMap.getSourceId(import_path);
			if (!import_id.isValid()) {
				throw new RuntimeException("File '" + import_path + "'is not part of the source set.");
			}

			CurrentAst.get().declareImportForCurrentFile(import_id);

			return null;
		}
	};
	
	public static Action addGlobalDeclarations = new Action() {
		@Override
		public ParseResult apply(ParseResultIterator child_results) {
			Vector<Declaration> declarations = (Vector<Declaration>) ((ParseResultHolder) (child_results.next().value_)).value_;
			
			for (Declaration declaration : declarations) {
				CurrentAst.get().declarations().add(declaration);
			}
			return null;
		}
	};
	
	public static Action stringLiteralUnquoteAction = new Action() {
		@Override
		public ParseResult apply(ParseResultIterator child_results) {
			return new ParseResult(stringLiteralUnquote(child_results.nextAs(String.class)));
		}
	};
	
	public static String stringLiteralUnquote(String s) {
		StringBuilder result = new StringBuilder();
		for (int i = 1; i < s.length() - 1; ++i) {
			if (s.charAt(i) == '\\') {
				switch (s.charAt(++i)) {
					case 'n':
						result.append('\n');
						break;
					case 'r':
						result.append('\r');
						break;
					case 't':
						result.append('\t');
						break;
					case '\'':
					case '"':
					case '\\':
						result.append(s.charAt(i));
						break;
					default:
						throw new RuntimeException("Unreachable");
				}
			}
			else {
				result.append(s.charAt(i));
			}
		}
		return result.toString();
	}
	
	public static ParseResult parseTokens(Symbol start, LexerResult tokens) {
		UnorderedSet<Item> table = new UnorderedSet<Item>();
		Item final_item = runEarleyAlgorithm(start, tokens, table);
		return start.runAction(final_item, tokens);
	}
	
	public static Item runEarleyAlgorithm(Symbol start, LexerResult tokens, UnorderedSet<Item> processed) {
		Vector<Item> worklist = new Vector<Item>();
		Vector<Item> future_items = new Vector<Item>();
		CurrentSourcePosition.Scope source_position = new CurrentSourcePosition.Scope(new SourcePosition(CurrentSourceFile.get(), new LineAndColumn(0, 0), new LineAndColumn(0, 0)));
		Vector<Item> completed_items = new Vector<Item>();
		HashMap<AbstractMap.Entry<Integer, Symbol>, Set<Item>> waiting = new HashMap<AbstractMap.Entry<Integer, Symbol>, Set<Item>>();

		Vector<Item> debug_trace = new Vector<Item>();
		
		Symbol top_level = new Symbol();
		top_level.addRule(new Rule(new Symbol[] {start}));
		worklist.add(new Item(top_level.rule(0), 0, 0, 0));

		int input_length = tokens.token_symbols.size();
		
		for (int pos = 0; pos <= input_length; ++pos) {
			while (worklist.size() != 0) {
				boolean insert_result = processed.add(worklist.lastElement());
				Item item = worklist.lastElement();
				
				MatchedInput last_token = tokens.token_contents.get(pos);
				CurrentSourcePosition.get(last_token.pos);
				
				boolean is_new = insert_result;
				if (!is_new) item.checkAmbiguity(worklist.lastElement(), tokens);
				worklist.remove(worklist.size() - 1);
				if (!is_new) continue;
				
				debug_trace.add(item);
				if (item.isComplete()) {
					for (Item parent : waiting.get(new AbstractMap.SimpleEntry(item.start(), item.left()))) {
						worklist.add(parent.advance(pos, item));
					}
				} else {
					Symbol next = item.nextSymbol();
					
					if (pos < tokens.token_symbols.size() && tokens.token_symbols.get(pos) == next) {
						future_items.add(item.advance(pos + 1, null));
					}
					
					if (!next.isTerminal()) {
						waiting.get(new AbstractMap.SimpleEntry(pos, next)).add(item);
					}
					for (int i = 0; i < next.rule_number(); ++i) {
						Rule rule = next.rule(i);
						Item already_completed = processed.find(new Item(rule, rule.right().size(), pos, pos));
						
						if (already_completed != null) {
							worklist.add(item.advance(pos, already_completed));
						} else {
							worklist.add(new Item(rule, 0, pos, pos));
						}
					}
				}
			}
			
			Vector<Item> temp = worklist;
			worklist = future_items;
			future_items = temp;
			temp = null;
		}
		
		Item final_item = processed.find(new Item(top_level.rule(0), 1, 0, input_length));
		if (final_item != null) {
			return final_item.children().get(0);
		}
		String reason;
		Item last_item = debug_trace.lastElement();
		if (last_item.pos() < tokens.token_symbols.size()) {
			String next_token = tokens.token_contents.get(last_item.pos()).toString();
			reason = "unexpected token \"" + next_token + "\"";
		} else {
			reason = "unexpected end of input";
		}
		throw new RuntimeException("Parser Error: " + reason);
	}
	
	public static <A> MessageBuilder message(TorqueMessage.Kind kind, A... args) {
		StringBuilder builder = new StringBuilder();
		for (int index = 0; index < args.length; index++)
			builder.append(args[index]);
		return new MessageBuilder(builder.toString(), kind);
	}
	
	public static <A> MessageBuilder error(A... args) {
		return message(TorqueMessage.Kind.kError, args);
	}
	
	public static String kBaseNamespaceName = "base";
	
	public static String CONSTEXPR_TYPE_PREFIX = "constexpr ";
	public static String NEVER_TYPE_STRING = "never";
	public static String CONSTEXPR_BOOL_TYPE_STRING = "constexpr bool";
	public static String CONSTEXPR_INTPTR_TYPE_STRING = "constexpr intptr";
	public static String CONSTEXPR_INSTANCE_TYPE_TYPE_STRING = "constexpr InstanceType";
	public static String BOOL_TYPE_STRING = "bool";
	public static String VOID_TYPE_STRING = "void";
	public static String ARGUMENTS_TYPE_STRING = "Arguments";
	public static String CONTEXT_TYPE_STRING = "Context";
	public static String JS_FUNCTION_TYPE_STRING = "JSFunction";
	public static String MAP_TYPE_STRING = "Map";
	public static String OBJECT_TYPE_STRING = "Object";
	public static String HEAP_OBJECT_TYPE_STRING = "HeapObject";
	public static String JSANY_TYPE_STRING = "JSAny";
	public static String JSOBJECT_TYPE_STRING = "JSObject";
	public static String SMI_TYPE_STRING = "Smi";
	public static String TAGGED_TYPE_STRING = "Tagged";
	public static String UNINITIALIZED_TYPE_STRING = "Uninitialized";
	public static String RAWPTR_TYPE_STRING = "RawPtr";
	public static String CONST_STRING_TYPE_STRING = "constexpr string";
	public static String STRING_TYPE_STRING = "String";
	public static String NUMBER_TYPE_STRING = "Number";
	public static String BUILTIN_POINTER_TYPE_STRING = "BuiltinPtr";
	public static String INTPTR_TYPE_STRING = "intptr";
	public static String UINTPTR_TYPE_STRING = "uintptr";
	public static String INT32_TYPE_STRING = "int32";
	public static String UINT32_TYPE_STRING = "uint32";
	public static String INT16_TYPE_STRING = "int16";
	public static String UINT16_TYPE_STRING = "uint16";
	public static String INT8_TYPE_STRING = "int8";
	public static String UINT8_TYPE_STRING = "uint8";
	public static String FLOAT64_TYPE_STRING = "float64";
	public static String CONST_INT31_TYPE_STRING = "constexpr int31";
	public static String CONST_INT32_TYPE_STRING = "constexpr int32";
	public static String CONST_FLOAT64_TYPE_STRING = "constexpr float64";
	public static String TORQUE_INTERNAL_NAMESPACE_STRING = "torque_internal";
	public static String REFERENCE_TYPE_STRING = "Reference";
	public static String SLICE_TYPE_STRING = "Slice";
	public static String STRUCT_NAMESPACE_STRING = "_struct";
	
	public static boolean isConstexprName(String name) {
		return name.substring(0, CONSTEXPR_TYPE_PREFIX.length()) == CONSTEXPR_TYPE_PREFIX;
	}
	
	public static <A> void reportError(A... args) throws RuntimeException {
		error(args).Throw();
	}
	
	public static <T> Vector<T> filterDeclarables(Vector<Declarable> list, Function<Declarable, T> function) {
		Vector<T> result = new Vector<T>();
		for (Declarable declarable : list) {
			T t = function.apply(declarable);
			if (t != null) {
				result.add(t);
			}
		}
		return result;
	}
	
	public static <T, N> T ensureUnique(Vector<T> list, N name, String kind) {
		if (list.size() == 0) {
			reportError("there is no ", kind, " named ", name);
		}
		if (list.size() >= 2) {
			reportError("ambiguous reference to ", kind, " ", name);
		}
		return list.firstElement();
	}

	public static <T> Vector<T> ensureNonempty(Vector<T> list, String name, String kind) {
		if (list.size() == 0) {
			reportError("there is no ", kind, " named ", name);
		}
		return list;
	}

	public static <T> void checkAlreadyDeclared(String name, String new_type, Function<Declarable, T> function) {
		Function<Declarable, T> function0 = new Function<Declarable, T>() {
			@Override
			public T apply(Declarable declarable) {
				return function.apply(declarable);
			}
		};

		Vector<T> declarations = filterDeclarables(Declarations.tryLookupShallow(new QualifiedName(name)), function0);
		if (declarations.size() != 0) {
			Scope scope = CurrentScope.get();
			reportError("cannot redeclare ", name, " (type ", new_type, scope, ")");
		}
	}

	public static <T> T registerDeclarable(T d) {
		return GlobalContext.get().registerDeclarable(d);
	}

	public static <C extends Function, T extends Vector> void printCommaSeparatedList(StringBuilder os, T list, C transform) {
		boolean first = true;
		for (Object e : list) {
			if (first) {
				first = false;
			} else {
				os.append(", ");
			}
			if(transform != null)
				os.append(transform.apply(e));
		}
	}

	public static <T extends Vector> void printCommaSeparatedList(StringBuilder os, T list) {
		boolean first = true;
		for (Object e : list) {
			if (first) {
				first = false;
			} else {
				os.append(", ");
			}
			os.append(e);
		}
	}

	public static <C extends Set, F extends Function<Type, Boolean>> void eraseIf(C container, F f) {
		Iterator<Type> iterator = container.iterator();
		while(iterator.hasNext()) {
			Type o = iterator.next();
			if(f.apply(o))
				container.remove(o);
		}
	}

	public static Namespace getOrCreateNamespace(String name) {
		Vector<Namespace> existing_namespaces = filterDeclarables(
				Declarations.tryLookupShallow(new QualifiedName(name)), new Function<Declarable, Namespace>() {
					@Override
					public Namespace apply(Declarable declarable) {
						return Namespace.dynamicCast(declarable);
					}
				});
		if (existing_namespaces.isEmpty()) {
			return Declarations.declareNamespace(name);
		}

		return existing_namespaces.firstElement();
	}

	public static String stringLiteralQuote(String s) {
		StringBuilder result = new StringBuilder();
		result.append('"');
		for (int i = 0; i < s.length(); ++i) {
			switch (s.charAt(i)) {
				case '\n':
					result.append("\\n");
					break;
				case '\r':
					result.append("\\r");
					break;
				case '\t':
					result.append("\\t");
					break;
				case '"':
				case '\\':
					result.append("\\").append(s.charAt(i));
					break;
				default:
					result.append(s.charAt(i));
			}
		}
		result.append('"');
		return result.toString();
	}

	public static boolean stringEndsWith(String s, String suffix) {
		if (s.length() < suffix.length()) return false;
		return s.substring(s.length() - suffix.length()) == suffix;
	}

	public static String underlinifyPath(String path) {
        path = path.replace("-", "_");
        path = path.replace("/", "_");
        path = path.replace("\\", "_");
        path = path.replace(".", "_");
        path = path.toUpperCase();
        return path;
    }

    public static String kThisParameterName = "this";

	public static void appendLoweredTypes(Type type, Vector<Type> result) {
		if (type.isConstexpr()) return;
		if (type == TypeOracle.getVoidType()) return;
		StructType s = StructType.dynamicCast(type);
		if (s != null) {
			for (Field field : s.fields()) {
				appendLoweredTypes(field.name_and_type.type, result);
			}
		} else {
			result.add(type);
		}
	}

	public static TypeVector lowerType(Type type) {
		TypeVector result = new TypeVector();
		appendLoweredTypes(type, result);
		return result;
	}

	public static String kFromConstexprMacroName = "FromConstexpr";
	public static String kMacroEndLabelName = "__macro_end";
	public static String kBreakLabelName = "__break";
	public static String kContinueLabelName = "__continue";
	public static String kCatchLabelName = "__catch";
	public static String kNextCaseLabelName = "__NextCase";

	public static Callable getOrCreateSpecialization(SpecializationKey<Generic> key) {
		Callable specialization = key.generic.specializations().get(key.specialized_types);
		if (specialization != null) {
			return specialization;
		}
		return DeclarationVisitor.specializeImplicit(key);
	}

	public static int loweredSlotCount(Type type) {
		return lowerType(type).size();
	}

	public static <T> T makeNode(T args) {
		return CurrentAst.get().addNode(args);
	}

	public static void declareMethods(AggregateType container_type, Vector<Declaration> methods) {
		Vector<Integer> vector = new Vector<Integer>();
		for (Declaration declaration : methods) {
			CurrentSourcePosition.Scope pos_scope = new CurrentSourcePosition.Scope(declaration.pos);
			TorqueMacroDeclaration method = TorqueMacroDeclaration.dynamicCast(declaration);
			Signature signature = TypeVisitor.makeSignature(method);
			signature.parameter_names.insertElementAt(
					Torque.makeNode(new Identifier(CurrentSourcePosition.get(), kThisParameterName)),
					signature.implicit_count);
			Statement body = method.body;
			String method_name = new String(method.name.value);
			signature.parameter_types.types.insertElementAt(container_type, signature.implicit_count);
			Declarations.createMethod(container_type, method_name, signature, body);
		}
	}

	public static String camelifyString(String underscore_string) {
        String result = "";
        boolean word_beginning = true;
        for(int index = 0, end = underscore_string.length(); index < end; index++) {
            char current = underscore_string.charAt(index);

            if (current == '_' || current == '-') {
                word_beginning = true;
                continue;
            }
            if (word_beginning) {
                current = String.valueOf(current).toUpperCase().charAt(0);
            }
            result += current;
            word_beginning = false;
        }
        return result;
    }

    public static VisitResult projectStructField(VisitResult structure, String fieldname) {
		BottomOffset begin = structure.stack_range().begin();
		StructType type = StructType.cast(structure.type());
		Vector<Field> fields = type.fields();
		for (Field field : fields) {
			BottomOffset end = begin.add(loweredSlotCount(field.name_and_type.type));
			if (field.name_and_type.name == fieldname) {
				return new VisitResult(field.name_and_type.type, new StackRange(begin, end));
			}
			begin = end;
		}

		Torque.reportError("struct '", type.name(), "' doesn't contain a field '",
				fieldname, "'");
		throw new RuntimeException("struct '" + type.name() + "' doesn't contain a field '" +
				fieldname + "'");
	}

	public static void expectSubtype(Type subtype, Optional<Type> supertype) {
		if (!subtype.isSubtypeOf(supertype.get())) {
			reportError("type ", subtype, " is not a subtype of ", supertype);
		}
	}

	public static void expectType(Type expected, Type actual) {
        if (expected != actual) {
            reportError("expected type ", expected, " but found ", actual);
        }
    }

    public static void expectSubtype(Type subtype, Type supertype) {
        if (!subtype.isSubtypeOf(supertype)) {
            reportError("type ", subtype, " is not a subtype of ", supertype);
        }
    }

    public static boolean isAssignableFrom(Type to, Type from) {
		if (to == from) return true;
		if (from.isSubtypeOf(to)) return true;
		return TypeOracle.isImplicitlyConvertableFrom(to, from);
	}

	public static void failCallableLookup(String reason, QualifiedName name,
										  	TypeVector parameter_types,
										  	Vector<Binding<LocalLabel>> labels,
										  	Vector<Signature> candidates,
										  	Vector<Tuple<Generic, String>> inapplicable_generics) {
		StringBuilder stream = new StringBuilder();
		stream.append("\n").append(reason).append(": \n  ").append(name).append("(").append(parameter_types).append(")");
		if (labels.size() != 0) {
			stream.append(" labels ");
			for (int i = 0; i < labels.size(); ++i) {
				stream.append(labels.get(i).name()).append("(").append(labels.get(i).super_class.parameter_types).append(")");
			}
		}
		stream.append("\ncandidates are:");
		for (Signature signature : candidates) {
			stream.append("\n  ").append(name);
			printSignature(stream, signature, false);
		}
		if (inapplicable_generics.size() != 0) {
			stream.append("\nfailed to instantiate all of these generic declarations:");
			for (Tuple<Generic, String> failure : inapplicable_generics) {
				Generic generic = null;
				String reason0 = null;
				generic = failure.first;
				reason0 = failure.second;
				stream.append("\n  ").append(generic.name()).append(" defined at ")
						.append(generic.position()).append(":\n    ").append(reason0).append("\n");
			}
		}
		Torque.reportError(stream.toString());
	}

	public static void printSignature(StringBuilder os, Signature sig, boolean with_names) {
		os.append("(");
		for (int i = 0; i < sig.parameter_types.types.size(); ++i) {
			if (i == 0 && sig.implicit_count != 0) os.append("implicit ");
			if (sig.implicit_count > 0 && sig.implicit_count == i) {
				os.append(")(");
			} else {
				if (i > 0) os.append(", ");
			}
			if (with_names && !sig.parameter_names.isEmpty()) {
				if (i < sig.parameter_names.size()) {
					os.append(sig.parameter_names.get(i));
					os.append(": ");
				}
			}
			os.append(sig.parameter_types.types.get(i));
		}
		if (sig.parameter_types.var_args) {
			if (sig.parameter_names.size() > 0) os.append(", ");
			os.append("...");
		}
		os.append(")");
		os.append(": ");
		os.append(sig.return_type);

		if (sig.labels.isEmpty()) return;

		os.append(" labels ");
		for (int i = 0; i < sig.labels.size(); ++i) {
			if (i > 0) os.append(", ");
			os.append(sig.labels.get(i).name);
			if (sig.labels.get(i).types.size() > 0) os.append("(" + sig.labels.get(i).types + ")");
		}
	}

	public static String positionAsString(SourcePosition pos) {
        return SourceFileMap.pathFromV8Root(pos.source) + ":" +
                (pos.start.line + 1) + ":" + (pos.start.column + 1);
    }

    public static TypeVector lowerParameterTypes(TypeVector parameters) {
		TypeVector result = new TypeVector();
		for (Type t : parameters) {
			appendLoweredTypes(t, result);
		}
		return result;
	}

	public static TypeVector lowerParameterTypes(ParameterTypes parameter_types, int arg_count) {
		TypeVector result = lowerParameterTypes(parameter_types.types);
		for (int i = parameter_types.types.size(); i < arg_count; ++i) {
			appendLoweredTypes(TypeOracle.getObjectType(), result);
		}
		return result;
	}

	public static TypeVector lowerParameterTypes(ParameterTypes parameter_types) {
		return  lowerParameterTypes(parameter_types, 0);
	}

	public static <C extends Function, E, T extends Iterable<E>> void printCommaSeparatedList(
									StringBuilder os, T list, C transform) {
		boolean first = true;
		for (E e : list) {
			if (first) {
				first = false;
			} else {
				os.append(", ");
			}
			os.append(transform.apply(e));
		}
	}
}