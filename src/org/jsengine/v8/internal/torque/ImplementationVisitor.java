package org.jsengine.v8.internal.torque;

import org.jsengine.utils.Std;
import org.jsengine.v8.internal.Torque;
import org.jsengine.utils.Tuple;

import java.util.function.Function;
import java.util.Vector;

public class ImplementationVisitor {
    private CfgAssembler assembler_;
    private boolean is_dry_run_;

    public void setDryRun(boolean is_dry_run) {
        is_dry_run_ = is_dry_run;
    }

    public void beginCSAFiles() {
        for (SourceId file : SourceFileMap.allSources()){
            StringBuilder source = GlobalContext.generatedPerFile(file).csa_ccfile;
            StringBuilder header = GlobalContext.generatedPerFile(file).csa_headerfile;

            for (String include_path : GlobalContext.cppIncludes()) {
                source.append("#include ").append(Torque.stringLiteralQuote(include_path)).append("\n");
            }

            for (SourceId file0 : SourceFileMap.allSources()) { // TODO: watch this file0 (file)
                source.append("#include \"torque-generated/" +
                        SourceFileMap.pathFromV8RootWithoutExtension(file0) +
                        "-tq-csa.h\"\n");
            }
            source.append("\n");

            source.append("namespace v8 {\n")
                    .append("namespace internal {\n")
                    .append("\n");

            String headerDefine =
                    "V8_GEN_TORQUE_GENERATED_" +
                            Torque.underlinifyPath(SourceFileMap.pathFromV8Root(file)) + "_H_";
            header.append("#ifndef ").append(headerDefine).append("\n");
            header.append("#define ").append(headerDefine).append("\n\n");
            header.append("#include \"src/compiler/code-assembler.h\"\n");
            header.append("#include \"src/codegen/code-stub-assembler.h\"\n");
            header.append("#include \"src/utils/utils.h\"\n");
            header.append("#include \"torque-generated/field-offsets-tq.h\"\n");
            header.append("#include \"torque-generated/csa-types-tq.h\"\n");
            header.append("\n");

            header.append("namespace v8 {\n")
                   .append("namespace internal {\n")
                   .append("\n");
        }
    }

    public void visitAllDeclarables() {
        CurrentCallable.Scope current_callable = new CurrentCallable.Scope(null);
        Vector<Declarable> all_declarables = GlobalContext.allDeclarables();
        // This has to be an index-based loop because all_declarables can be extended
        // during the loop.
        for (int i = 0; i < all_declarables.size(); ++i) {
            try {
                visit(all_declarables.get(i));
            } catch (TorqueAbortCompilation e) {
                // Recover from compile errors here. The error is recorded already.
            }
        }
    }

    public void visit(Declarable declarable) {
        CurrentScope.Scope current_scope = new CurrentScope.Scope(declarable.parentScope());
        CurrentSourcePosition.Scope current_source_position = new CurrentSourcePosition.Scope(declarable.position());
        CurrentFileStreams.Scope current_file_streams = new CurrentFileStreams.Scope(
                GlobalContext.generatedPerFile(declarable.position().source));
        Callable callable = Callable.dynamicCast(declarable);
        if (callable != null) {
            if (!callable.shouldGenerateExternalCode())
                CurrentFileStreams.get(null);
        }
        
        switch (declarable.kind()) {
            case kExternMacro:
                visit(ExternMacro.cast(declarable));
            case kTorqueMacro:
                visit(TorqueMacro.cast(declarable));
            case kMethod:
                visit(Method.cast(declarable));
            case kBuiltin:
                visit(Builtin.cast(declarable));
            case kTypeAlias:
                visit(TypeAlias.cast(declarable));
            case kNamespaceConstant:
                visit(NamespaceConstant.cast(declarable));
            case kRuntimeFunction:
            case kIntrinsic:
            case kExternConstant:
            case kNamespace:
            case kGeneric:
            case kGenericStructType:
                return;
        }
    }

    public void visit(ExternMacro macro) {}

    public void visit(TorqueMacro macro) {
        visitMacroCommon(macro);
    }

    public void visitMacroCommon(Macro macro) {
        CurrentCallable.Scope current_callable = new CurrentCallable.Scope(macro);
        Signature signature = macro.signature();
        Type return_type = macro.signature().return_type;
        boolean can_return = return_type != TypeOracle.getNeverType();
        boolean has_return_value =
                can_return && return_type != TypeOracle.getVoidType();

        generateMacroFunctionDeclaration(header_out(), "", macro);
        header_out().append(";\n");

        generateMacroFunctionDeclaration(source_out(), "", macro);
        source_out().append(" {\n");
        source_out().append("  compiler::CodeAssembler ca_(state_);\n");

        Stack<String> lowered_parameters = new Stack<String>();
        Stack<Type> lowered_parameter_types = new Stack<Type>();

        Vector<VisitResult> arguments = new Vector<VisitResult>();

        LocationReference this_reference = null;
        Method method = Method.dynamicCast(macro);
        if (method != null) {
            Type this_type = method.aggregate_type();
            lowerParameter(this_type, externalParameterName(Torque.kThisParameterName), lowered_parameters);
            StackRange range = lowered_parameter_types.pushMany(Torque.lowerType(this_type));
            VisitResult this_result = new VisitResult(this_type, range);

            this_reference = (this_type.isClassType())
                            ? LocationReference.temporary(this_result, "this parameter")
            : LocationReference.variableAccess(this_result);
        }

        for (int i = 0; i < macro.signature().parameter_names.size(); ++i) {
            if (this_reference != null && i == macro.signature().implicit_count) continue;
            String name = macro.parameter_names().get(i).value;
            String external_name = externalParameterName(name);
            Type type = macro.signature().types().get(i);

            if (type.isConstexpr()) {
                arguments.add(new VisitResult(type, external_name));
            } else {
                lowerParameter(type, external_name, lowered_parameters);
                StackRange range = lowered_parameter_types.pushMany(Torque.lowerType(type));
                arguments.add(new VisitResult(type, range));
            }
        }

        assembler_ = new CfgAssembler(lowered_parameter_types);

        Vector<Block> label_blocks = new Vector<Block>();
        for (LabelDeclaration label_info : signature.labels) {
            Stack<Type> label_input_stack = new Stack<Type>();
            for (Type type : label_info.types) {
                label_input_stack.pushMany(Torque.lowerType(type));
            }
            Block block = assembler().newBlock(label_input_stack);
            label_blocks.add(block);
        }

        VisitResult return_value =
                inlineMacro(macro, this_reference, arguments, label_blocks);
        Block end = assembler().newBlock();
        if (return_type != TypeOracle.getNeverType()) {
            assembler().Goto(end);
        }

        for (int i = 0; i < label_blocks.size(); ++i) {
            Block label_block = label_blocks.get(i);
            LabelDeclaration label_info = signature.labels.get(i);
            assembler().bind(label_block);
            Vector<String> label_parameter_variables = new Vector<String>();
            for (int i0 = 0; i0 < label_info.types.size(); ++i0) {
                lowerLabelParameter(label_info.types.get(i0),
                        externalLabelParameterName(label_info.name.value, i0),
                          label_parameter_variables);
            }
            assembler().emit(new Instruction(new GotoExternalInstruction(
                externalLabelName(label_info.name.value), label_parameter_variables),
                    GotoExternalInstruction.kKind));
        }

        if (return_type != TypeOracle.getNeverType()) {
            assembler().bind(end);
        }

        CSAGenerator csa_generator = new CSAGenerator(assembler().result(), source_out());
        Stack<String> values =
                csa_generator.emitGraph(lowered_parameters);

        assembler_ = null;

        if (has_return_value) {
            source_out().append("  return ");
            CSAGenerator.eEmitCSAValue(return_value, values, source_out());
            source_out().append(";\n");
        }
        source_out().append("}\n\n");
    }

    public StringBuilder header_out() {
        GlobalContext.PerFileStreams streams = CurrentFileStreams.get();
        if (streams != null) {
            return streams.csa_headerfile;
        }
        return null;
    }

    public void generateMacroFunctionDeclaration(StringBuilder o, String macro_prefix, Macro macro) {
        generateFunctionDeclaration(o, macro_prefix, macro.externalName(),
                macro.signature(), macro.parameter_names());
    }

    public Vector<String> generateFunctionDeclaration(StringBuilder o, String macro_prefix, String name,
                                                      Signature signature, NameVector parameter_names) {
        return generateFunctionDeclaration(o, macro_prefix, name, signature, parameter_names, true);
    }

    public Vector<String> generateFunctionDeclaration(StringBuilder o, String macro_prefix, String name,
            Signature signature, NameVector parameter_names, boolean pass_code_assembler_state) {
        Vector<String> generated_parameter_names = new Vector<String>();
        if (signature.return_type.isVoidOrNever()) {
            o.append("void");
        } else {
            o.append(signature.return_type.getGeneratedTypeName());
        }
        o.append(" ").append(macro_prefix).append(name).append("(");

        boolean first = true;
        if (pass_code_assembler_state) {
            first = false;
            o.append("compiler::CodeAssemblerState* state_");
        }

        for (int i = 0; i < signature.types().size(); ++i) {
            if (!first) o.append(", ");
            first = false;
            Type parameter_type = signature.types().get(i);
            String generated_type_name = parameter_type.getGeneratedTypeName();

            generated_parameter_names.add(externalParameterName(
                    i < parameter_names.size() ? parameter_names.get(i).value
            : (i + "")));
            o.append(generated_type_name).append(" ").append(generated_parameter_names.lastElement());
        }

        for (LabelDeclaration label_info : signature.labels) {
            if (!first) o.append(", ");
            first = false;
            generated_parameter_names.add(
                    externalLabelName(label_info.name.value));
            o.append("compiler::CodeAssemblerLabel* ").append(generated_parameter_names.lastElement());
            int i = 0;
            for (Type type : label_info.types) {
                String generated_type_name;
                if (type.isStructType()) {
                    generated_type_name = "\n#error no structs allowed in labels\n";
                } else {
                    generated_type_name = "compiler::TypedCodeAssemblerVariable<";
                    generated_type_name += type.getGeneratedTNodeTypeName();
                    generated_type_name += ">*";
                }
                o.append(", ");
                generated_parameter_names.add(externalLabelParameterName(label_info.name.value, i));
                o.append(generated_type_name).append(" ").append(generated_parameter_names.lastElement());
                ++i;
            }
        }

        o.append(")");
        return generated_parameter_names;
    }

    public String externalParameterName(String name) {
        return "p_" + name;
    }

    public String externalLabelName(String label_name) {
        return "label_" + label_name;
    }

    public String externalLabelParameterName(String label_name, int i) {
        return "label_" + label_name + "_parameter_" + i;
    }

    public StringBuilder source_out() {
        GlobalContext.PerFileStreams streams = CurrentFileStreams.get();
        if (streams != null) {
            return streams.csa_ccfile;
        }
        return null;
    }
    
    public StackRange lowerParameter(Type type, String parameter_name, Stack<String> lowered_parameters) {
        StructType struct_type = StructType.dynamicCast(type);
        if (struct_type != null) {
            StackRange range = lowered_parameters.topRange(0);
            for (Field field : struct_type.fields()) {
                StackRange parameter_range = lowerParameter(field.name_and_type.type,
                        parameter_name + "." + field.name_and_type.name, lowered_parameters);
                range.extend(parameter_range);
            }
            return range;
        } else {
            lowered_parameters.push(parameter_name);
            return lowered_parameters.topRange(1);
        }
    }

    public CfgAssembler assembler() {
        return assembler_;
    }

    public VisitResult inlineMacro(Macro macro, LocationReference this_reference,
                                        Vector<VisitResult> arguments, Vector<Block> label_blocks) {
        CurrentScope.Scope current_scope = new CurrentScope.Scope(macro);
        BindingsManagersScope bindings_managers_scope;
        CurrentCallable.Scope current_callable = new CurrentCallable.Scope(macro);
        CurrentReturnValue.Scope current_return_value;
        Signature signature = macro.signature();
        Type return_type = macro.signature().return_type;
        boolean can_return = return_type != TypeOracle.getNeverType();

        BlockBindings<LocalValue> parameter_bindings = new BlockBindings<LocalValue>(ValueBindingsManager.get());
        BlockBindings<LocalLabel> label_bindings = new BlockBindings<LocalLabel>(LabelBindingsManager.get());

        if (this_reference != null) {
            LocalValue this_value = new LocalValue(!this_reference.isVariableAccess(),
                    this_reference.getVisitResult());
            parameter_bindings.add(Torque.kThisParameterName, this_value, true);
        }

        int i = 0;
        for (VisitResult arg : arguments) {
            if (this_reference != null && i == signature.implicit_count) i++;
            boolean mark_as_used = signature.implicit_count > i;
            Identifier name = macro.parameter_names().get(i++);
            parameter_bindings.add(name, new LocalValue(true, arg), mark_as_used);
        }

        for (int i0 = 0; i < signature.labels.size(); ++i0) {
            LabelDeclaration label_info = signature.labels.get(i0);
            label_bindings.add(label_info.name,
                    new LocalLabel(label_blocks.get(i0), label_info.types));
        }

        Block macro_end = null;
        Binding<LocalLabel> macro_end_binding;
        if (can_return) {
            Stack<Type> stack = assembler().currentStack();
            Vector<Type> lowered_return_types = Torque.lowerType(return_type);
            stack.pushMany(lowered_return_types);
            if (!return_type.isConstexpr()) {
                setReturnValue(new VisitResult(return_type,
                        stack.topRange(lowered_return_types.size())));
            }

            for(int i1 = 0; i1 < stack.size(); i1++) {
                if(stack.elements_.get(i1).isTopType()) {
                    stack.elements_.set(i1, TopType.cast(stack.elements_.get(i1)).source_type());
                }
            }
            macro_end = assembler().newBlock(stack);
            Vector<Type> return_type_vector = new Vector<Type>();
            return_type_vector.add(return_type);
            macro_end_binding = new Binding(LabelBindingsManager.get(), Torque.kMacroEndLabelName,
                    new LocalLabel(macro_end, return_type_vector));
        } else {
            setReturnValue(VisitResult.neverResult());
        }

        Type result = visit(macro.body());

        if (result.isNever()) {
            if (!return_type.isNever() && !macro.hasReturns()) {
                StringBuilder s = new StringBuilder();
                s.append("macro ").append(macro.readableName())
                        .append(" that never returns must have return type never");
                Torque.reportError(s.toString());
            }
        } else {
            if (return_type.isNever()) {
                StringBuilder s = new StringBuilder();
                s.append("macro ").append(macro.readableName())
                        .append(" has implicit return at end of its declartion but return type never");
                Torque.reportError(s.toString());
            } else if (!macro.signature().return_type.isVoid()) {
                StringBuilder s;
                s.append("macro ").append(macro.readableName())
                        .append(" expects to return a value but doesn't on all paths");
                Torque.reportError(s.toString());
            }
        }
        if (!result.isNever()) {
            assembler().Goto(macro_end);
        }

        if (macro.hasReturns() || !result.isNever()) {
            assembler().bind(macro_end);
        }

        return getAndClearReturnValue();
    }

    public static class ValueBindingsManager {
        public static ThreadLocal<Scope> top_ = new ThreadLocal<Scope>();

        public static class Scope {
            public BindingsManager<LocalValue> value_;
            public Scope previous_;

            public Scope(BindingsManager<LocalValue> value) {
                value_ = value;
                previous_ = top();
                top(this);
            }

            public BindingsManager<LocalValue> value() {
                return value_;
            }
        }

        public static Scope top() {
            return top_.get();
        }

        public static void top(Scope scope) {
            top_.set(scope);
        }

        public static BindingsManager<LocalValue> get() {
            return top().value();
        }
    }

    public static class LabelBindingsManager {
        public static ThreadLocal<Scope> top_ = new ThreadLocal<Scope>();

        public static class Scope {
            public BindingsManager<LocalLabel> value_;
            public Scope previous_;

            public Scope(BindingsManager<LocalLabel> value) {
                value_ = value;
                previous_ = top();
                top(this);
            }

            public BindingsManager<LocalLabel> value() {
                return value_;
            }
        }

        public static Scope top() {
            return top_.get();
        }

        public static void top(Scope scope) {
            top_.set(scope);
        }

        public static BindingsManager<LocalLabel> get() {
            return top().value();
        }
    }

    public static class CurrentReturnValue {
        public static ThreadLocal<Scope> top_ = new ThreadLocal<Scope>();

        public static class Scope {
            public VisitResult value_;
            public Scope previous_;

            public Scope(VisitResult value) {
                value_ = value;
                previous_ = top();
                top(this);
            }

            public VisitResult value() {
                return value_;
            }

            public void value(VisitResult value) {
                value_ = value;
            }
        }

        public static Scope top() {
            return top_.get();
        }

        public static void top(Scope scope) {
            top_.set(scope);
        }

        public static VisitResult get() {
            return top().value();
        }

        public static void get(VisitResult value) {
            top().value(value);
        }
    }

    private static class BindingsManagersScope {
        public ValueBindingsManager.Scope value_bindings_manager;
        public LabelBindingsManager.Scope label_bindings_manager;
    }

    public VisitResult getAndClearReturnValue() {
        VisitResult return_value = CurrentReturnValue.get();
        CurrentReturnValue.get(null);
        return return_value;
    }

    public void setReturnValue(VisitResult return_value) {
        VisitResult current_return_value = CurrentReturnValue.get();
        current_return_value = return_value;
    }

    public Type visit(Statement stmt) {
        CurrentSourcePosition.Scope scope = new CurrentSourcePosition.Scope(stmt.pos);
        StackScope stack_scope = new StackScope(this);
        Type result;
        switch (stmt.kind) {
            case kBlockStatement:
                result = visit(BlockStatement.cast(stmt));
                break;
            case kExpressionStatement:
                result = visit(ExpressionStatement.cast(stmt));
                break;
            case kIfStatement:
                result = visit(IfStatement.cast(stmt));
                break;
            case kWhileStatement:
                result = visit(WhileStatement.cast(stmt));
                break;
            case kForLoopStatement:
                result = visit(ForLoopStatement.cast(stmt));
                break;
            case kBreakStatement:
                result = visit(BreakStatement.cast(stmt));
                break;
            case kContinueStatement:
                result = visit(ContinueStatement.cast(stmt));
                break;
            case kReturnStatement:
                result = visit(ReturnStatement.cast(stmt));
                break;
            case kDebugStatement:
                result = visit(DebugStatement.cast(stmt));
                break;
            case kAssertStatement:
                result = visit(AssertStatement.cast(stmt));
                break;
            case kTailCallStatement:
                result = visit(TailCallStatement.cast(stmt));
                break;
            case kVarDeclarationStatement:
                result = visit(VarDeclarationStatement.cast(stmt));
                break;
            case kGotoStatement:
                result = visit(GotoStatement.cast(stmt));
                break;
            default:
                throw new RuntimeException("UNREACHABLE");
        }
        return result;
    }

    public Type visit(BlockStatement block) {
        BlockBindings<LocalValue> block_bindings = new BlockBindings<LocalValue>(ValueBindingsManager.get());
        Type type = TypeOracle.getVoidType();
        for (Statement s : block.statements) {
            CurrentSourcePosition.Scope source_position = new CurrentSourcePosition.Scope(s.pos);
            if (type.isNever()) {
                Torque.reportError("statement after non-returning statement");
            }
            VarDeclarationStatement var_declaration = VarDeclarationStatement.dynamicCast(s);
            if (var_declaration != null) {
                type = visit(var_declaration, block_bindings);
            } else {
                type = visit(s);
            }
        }
        return type;
    }

    public Type visit(VarDeclarationStatement stmt, BlockBindings<LocalValue> block_bindings) {
        if (stmt.const_qualified && stmt.initializer == null) {
            Torque.reportError("local constant \"", stmt.name, "\" is not initialized.");
        }

        Type type = null;
        if (stmt.type != null) {
            type = TypeVisitor.computeType(stmt.type);
        }
        VisitResult init_result;
        if (stmt.initializer != null) {
            StackScope scope = new StackScope(this);
            init_result = visit(stmt.initializdeleteRangeer);
            if (type != null) {
                init_result = generateImplicitConvert(type, init_result);
            }
            type = init_result.type();
            if (type.isConstexpr() && !stmt.const_qualified) {
                Torque.error("Use 'const' instead of 'let' for variable '", stmt.name.value,
                        "' of constexpr type '", type.toString(), "'.").position(stmt.name.pos).Throw();
            }
            init_result = scope.yield(init_result);
        } else {
            if (type.isConstexpr()) {
                Torque.reportError("constexpr variables need an initializer");
            }
            TypeVector lowered_types = Torque.lowerType(type);
            for (Type type0 : lowered_types) {
                assembler().emit(new Instruction(new PushUninitializedInstruction(TypeOracle.getTopType(
                        "uninitialized variable '" + stmt.name.value + "' of type " +
                                type.toString() + " originally defined at " +
                                        Torque.positionAsString(stmt.pos), type)), PushUninitializedInstruction.kKind));
            }
            init_result =
                    new VisitResult(type, assembler().topRange(lowered_types.size()));
        }
        block_bindings.add(stmt.name,
                new LocalValue(stmt.const_qualified, init_result));
        return TypeOracle.getVoidType();
    }

    public VisitResult visit(Expression expr) {
        CurrentSourcePosition.Scope scope = new CurrentSourcePosition.Scope(expr.pos);
        switch (expr.kind) {
            case AstNode.Kind.kCallExpression:
                return visit(CallExpression.cast(expr));
            case AstNode.Kind.kCallMethodExpression:
                return visit(CallMethodExpression.cast(expr));
            case AstNode.Kind.kIntrinsicCallExpression:
                return visit(IntrinsicCallExpression.cast(expr));
            case AstNode.Kind.kStructExpression:
                return visit(StructExpression.cast(expr));
            case AstNode.Kind.kLogicalOrExpression:
                return visit(LogicalOrExpression.cast(expr));
            case AstNode.Kind.kLogicalAndExpression:
                return visit(LogicalAndExpression.cast(expr));
            case AstNode.Kind.kSpreadExpression:
                return visit(SpreadExpression.cast(expr));
            case AstNode.Kind.kConditionalExpression:
                return visit(ConditionalExpression.cast(expr));
            case AstNode.Kind.kIdentifierExpression:
                return visit(IdentifierExpression.cast(expr));
            case AstNode.Kind.kStringLiteralExpression:
                return visit(StringLiteralExpression.cast(expr));
            case AstNode.Kind.kNumberLiteralExpression:
                return visit(NumberLiteralExpression.cast(expr));
            case AstNode.Kind.kFieldAccessExpression:
                return visit(FieldAccessExpression.cast(expr));
            case AstNode.Kind.kElementAccessExpression:
                return visit(ElementAccessExpression.cast(expr));
            case AstNode.Kind.kDereferenceExpression:
                return visit(DereferenceExpression.cast(expr));
            case AstNode.Kind.kAssignmentExpression:
                return visit(AssignmentExpression.cast(expr));
            case AstNode.Kind.kIncrementDecrementExpression:
                return visit(IncrementDecrementExpression.cast(expr));
            case AstNode.Kind.kNewExpression:
                return visit(NewExpression.cast(expr));
            case AstNode.Kind.kAssumeTypeImpossibleExpression:
                return visit(AssumeTypeImpossibleExpression.cast(expr));
            case AstNode.Kind.kStatementExpression:
                return visit(StatementExpression.cast(expr));
            case AstNode.Kind.kTryLabelExpression:
                return visit(TryLabelExpression.cast(expr));
            default:
                throw new RuntimeException("UNREACHABLE");
        }
    }

    public VisitResult visit(CallExpression expr, boolean is_tailcall) {
        StackScope scope = new StackScope(this);

        if (expr.callee.name.value == "&" && expr.arguments.size() == 1) {
            LocationExpression loc_expr = LocationExpression.dynamicCast(expr.arguments.get(0));
            if (loc_expr != null) {
                LocationReference ref = getLocationReference(loc_expr);
                if (ref.isHeapReference()) return scope.yield(ref.heap_reference());
                if (ref.isHeapSlice()) return scope.yield(ref.heap_slice());
            }
            Torque.reportError("Unable to create a heap reference.");
        }

        Arguments arguments = new Arguments();
        QualifiedName name = new QualifiedName(expr.callee.namespace_qualification,
                expr.callee.name.value);
        TypeVector specialization_types =
                TypeVisitor.computeTypeVector(expr.callee.generic_arguments);
        boolean has_template_arguments = !specialization_types.isEmpty();
        for (Expression arg : expr.arguments)
        arguments.parameters.add(new Visit(arg));
        arguments.labels = LabelsFromIdentifiers(expr.labels);
        if (!has_template_arguments && name.namespace_qualification.isEmpty() &&
                tryLookupLocalValue(name.name) != null) {
            return scope.yield(
                    GeneratePointerCall(expr.callee, arguments, is_tailcall));
        } else {
            if (GlobalContext.collect_language_server_data()) {
                Callable callable = lookupCallable(name, Declarations.lookup(name),
                        arguments, specialization_types);
                LanguageServerData::AddDefinition(expr.callee.name.pos,
                        callable.identifierPosition());
            }
            return scope.yield(
                    generateCall(name, arguments, specialization_types, is_tailcall));
        }
    }

    public VisitResult visit(CallExpression expr) {
        return visit(expr, false);
    }

    public LocationReference getLocationReference(Expression location) {
        switch (location.kind) {
            case kIdentifierExpression:
                return getLocationReference((IdentifierExpression) location);
            case kFieldAccessExpression:
                return getLocationReference((FieldAccessExpression) location);
            case kElementAccessExpression:
                return getLocationReference((ElementAccessExpression) location);
            case kDereferenceExpression:
                return getLocationReference((DereferenceExpression) location);
            default:
                return LocationReference.temporary(visit(location), "expression");
        }
    }

    public LocationReference getLocationReference(IdentifierExpression expr) {
        if (expr.namespace_qualification.isEmpty()) {
            Binding<LocalValue> value = tryLookupLocalValue(expr.name.value);
            if (value != null) {
                if (GlobalContext.collect_language_server_data()) {
                    LanguageServerData.addDefinition(expr.name.pos,
                            value.declaration_position());
                }
                if (expr.generic_arguments.size() != 0) {
                    Torque.reportError("cannot have generic parameters on local name ",
                            expr.name);
                }
                if (value.super_class.is_const) {
                    return LocationReference.temporary(
                            value.super_class.value, "constant value " + expr.name.value);
                }
                return LocationReference.variableAccess(value.super_class.value, value);
            }
        }

        if (expr.isThis()) {
            Torque.reportError("\"this\" cannot be qualified");
        }
        QualifiedName name = new QualifiedName(expr.namespace_qualification, expr.name.value);
        Builtin builtin = Declarations.tryLookupBuiltin(name);
        if (builtin != null) {
            if (GlobalContext.collect_language_server_data()) {
                LanguageServerData.addDefinition(expr.name.pos, builtin.position());
            }
            return LocationReference.temporary(getBuiltinCode(builtin),
                    "builtin " + expr.name.value);
        }
        if (expr.generic_arguments.size() != 0) {
            Generic generic = Declarations.lookupUniqueGeneric(name);
            Callable specialization =
                    Torque.getOrCreateSpecialization(new SpecializationKey<Generic>(
                            generic, TypeVisitor.computeTypeVector(expr.generic_arguments)));

            Builtin builtin0 = Builtin.dynamicCast(specialization);
            if (builtin0 != null) {
                return LocationReference.temporary(getBuiltinCode(builtin0),
                        "builtin " + expr.name.value);
            } else {
                Torque.reportError("cannot create function pointer for non-builtin ",
                        generic.name());
            }
        }
        Value value = Declarations.lookupValue(name);
        if (GlobalContext.collect_language_server_data()) {
            LanguageServerData.addDefinition(expr.name.pos, value.name().pos);
        }
        NamespaceConstant constant = NamespaceConstant.dynamicCast(value);
        if (constant != null) {
            if (constant.type().isConstexpr()) {
                return LocationReference.temporary(
                        new VisitResult(constant.type(), constant.external_name() + "(state_)"),
                        "namespace constant " + expr.name.value);
            }
            assembler().emit(new Instruction(new NamespaceConstantInstruction(constant), NamespaceConstantInstruction.kKind));
            StackRange stack_range = assembler().topRange(Torque.loweredSlotCount(constant.type()));
            return LocationReference.temporary(
                    new VisitResult(constant.type(), stack_range),
                    "namespace constant " + expr.name.value);
        }
        ExternConstant constant0 = ExternConstant.cast(value);
        return LocationReference.temporary(constant0.value(),
                "extern value " + expr.name.value);
    }

    public Binding<LocalValue> tryLookupLocalValue(String name) {
        return ValueBindingsManager.get().tryLookup(name);
    }

    public VisitResult getBuiltinCode(Builtin builtin) {
        if (builtin.isExternal() || builtin.kind_ != Builtin.Kind.kStub) {
            Torque.reportError(
                    "creating function pointers is only allowed for internal builtins with " +
                    "stub linkage");
        }
        Type type = TypeOracle.getBuiltinPointerType(builtin.signature().parameter_types.types,
                builtin.signature().return_type);
        PushBuiltinPointerInstruction pushBuiltinPointerInstruction = new PushBuiltinPointerInstruction(builtin.externalName(), type);
        assembler().emit(new Instruction(pushBuiltinPointerInstruction, PushBuiltinPointerInstruction.kKind));
        return new VisitResult(type, assembler().topRange(1));
    }

    public LocationReference getLocationReference(FieldAccessExpression expr) {
        String fieldname = expr.field.value;
        LocationReference reference = getLocationReference(expr.object);
        if (reference.isVariableAccess() && reference.variable().type().isStructType()) {
            StructType type = StructType.cast(reference.variable().type());
            Field field = type.lookupField(fieldname);
            if (GlobalContext.collect_language_server_data()) {
                LanguageServerData.addDefinition(expr.field.pos, field.pos);
            }
            if (field.const_qualified) {
                VisitResult t_value = Torque.projectStructField(reference.variable(), fieldname);
                return LocationReference.temporary(
                        t_value, "for constant field '" + field.name_and_type.name + "'");
            } else {
                return LocationReference.variableAccess(
                        Torque.projectStructField(reference.variable(), fieldname));
            }
        }
        if (reference.isTemporary() && reference.temporary().type().isStructType()) {
            if (GlobalContext.collect_language_server_data()) {
                StructType type = StructType.cast(reference.temporary().type());
                Field field = type.lookupField(fieldname);
                LanguageServerData.addDefinition(expr.field.pos, field.pos);
            }
            return LocationReference.temporary(
                    Torque.projectStructField(reference.temporary(), fieldname),
                    reference.temporary_description());
        }
        VisitResult object_result = generateFetchFromLocation(reference);
        ClassType class_type = object_result.type().classSupertype();
        if (class_type != null) {
            boolean has_explicit_overloads = testLookupCallable(
                    new QualifiedName("." + fieldname), new TypeVector(object_result.type()));
            if (class_type.hasField(fieldname) && !has_explicit_overloads) {
                Field field = class_type.lookupField(fieldname);
                if (GlobalContext.collect_language_server_data()) {
                    LanguageServerData.addDefinition(expr.field.pos, field.pos);
                }
                if (field.index != null) {
                    assembler().emit(
                            new CreateFieldReferenceInstruction(object_result.type(), fieldname));
                    // Fetch the length from the object
                    {
                        StackScope length_scope(this);
                        // Get a reference to the length
          const Field* index_field = field.index.value();
                        GenerateCopy(object_result);
                        assembler().Emit(CreateFieldReferenceInstruction{
                        object_result.type(), index_field->name_and_type.name});
                        VisitResult length_reference(
                            TypeOracle::GetReferenceType(index_field->name_and_type.type),
                            assembler().TopRange(2));

                        // Load the length from the reference and convert it to intptr
                        VisitResult length = GenerateFetchFromLocation(
                                LocationReference::HeapReference(length_reference));
                        VisitResult converted_length =
                                GenerateCall("Convert", {{length}, {}},
                                        {TypeOracle::GetIntPtrType(), length.type()}, false);
                        DCHECK_EQ(converted_length.stack_range().Size(), 1);
                        length_scope.Yield(converted_length);
                    }
        const Type* slice_type =
                            TypeOracle::GetSliceType(field.name_and_type.type);
                    return LocationReference::HeapSlice(
                            VisitResult(slice_type, assembler().TopRange(3)));
                } else {
                    assembler().rmit(
                            new CreateFieldReferenceInstruction(class_type, fieldname));
        const Type* reference_type =
                            TypeOracle::GetReferenceType(field.name_and_type.type);
                    return LocationReference::HeapReference(
                            VisitResult(reference_type, assembler().TopRange(2)));
                }
            }
        }
        return LocationReference.fieldAccess(object_result, fieldname);
    }

    public VisitResult generateFetchFromLocation(LocationReference reference) {
        if (reference.isTemporary()) {
            return generateCopy(reference.temporary());
        } else if (reference.isVariableAccess()) {
            return generateCopy(reference.variable());
        } else if (reference.isHeapReference()) {
            generateCopy(reference.heap_reference());
            LoadReferenceInstruction loadReferenceInstruction = new LoadReferenceInstruction(reference.referencedType());
            assembler().emit(new Instruction(loadReferenceInstruction, LoadReferenceInstruction.kKind));
            return new VisitResult(reference.referencedType(), assembler().topRange(1));
        } else {
            if (reference.isHeapSlice()) {
                Torque.reportError(
                        "fetching a value directly from an indexed field isn't allowed");
            }
            return generateCall(reference.eval_function(),
                    new Arguments(reference.call_arguments(), new Vector<Binding<LocalLabel>>()));
        }
    }

    public VisitResult generateCopy(VisitResult to_copy) {
        if (to_copy.isOnStack()) {
            return new VisitResult(to_copy.type(),
                    assembler().peek(to_copy.stack_range(), to_copy.type()));
        }
        return to_copy;
    }

    public VisitResult generateCall(String callable_name, Arguments parameters,
                                    TypeVector specialization_types, boolean tail_call) {
        return generateCall(new QualifiedName(callable_name), parameters, specialization_types, tail_call);
    }

    public VisitResult generateCall(String callable_name, Arguments parameters) {
        return generateCall(callable_name, parameters, new TypeVector(), false);
    }

    public VisitResult generateCall(QualifiedName callable_name, Arguments arguments,
                                        TypeVector specialization_types, boolean is_tailcall) {
        Callable callable = lookupCallable(callable_name, Declarations.lookup(callable_name),
                arguments, specialization_types);
        return generateCall(callable, null, arguments, specialization_types, is_tailcall);
    }

    public <Container extends Iterable<Declarable>> Callable lookupCallable(QualifiedName name, Container declaration_container,
                            Arguments arguments, TypeVector specialization_types) {
        return lookupCallable(name, declaration_container,
                arguments.parameters.computeTypeVector(),
                arguments.labels, specialization_types);
    }

    public <Container extends Iterable<Declarable>> Callable lookupCallable(QualifiedName name, Container declaration_container,
                                                                            TypeVector parameter_types,
                                                                            Vector<Binding<LocalLabel>> labels,
                                                                            TypeVector specialization_types) {
        return lookupCallable(name, declaration_container,
                parameter_types, labels, specialization_types, false);
    }
    
    public <Container extends Iterable<Declarable>> Callable lookupCallable(QualifiedName name, Container declaration_container,
                                                    TypeVector parameter_types,
                                                    Vector<Binding<LocalLabel>> labels,
                                                    TypeVector specialization_types, boolean silence_errors) {
        Callable result = null;

        Vector<Declarable> overloads = new Vector<Declarable>();
        Vector<Signature> overload_signatures = new Vector<Signature>();
        Vector<Tuple<Generic, String>> inapplicable_generics = new Vector<Tuple<Generic, String>>();
        for (Declarable declarable : declaration_container) {
            Generic generic = Generic.dynamicCast(declarable);
            Callable callable = null;
            if (generic != null) {
                TypeArgumentInference inference = generic.inferSpecializationTypes(
                        specialization_types, parameter_types);
                if (inference.hasFailed()) {
                    inapplicable_generics.add(
                            Tuple.makeTuple(generic, inference.getFailureReason()));
                    continue;
                }
                overloads.add(generic);
                overload_signatures.add(
                        DeclarationVisitor.makeSpecializedSignature(
                        new SpecializationKey<Generic>(generic, inference.getResult())));
            } else if ((callable = Callable.dynamicCast(declarable)) != null) {
                overloads.add(callable);
                overload_signatures.add(callable.signature());
            }
        }

        Vector<Integer> candidates = new Vector<Integer>();
        for (int i = 0; i < overloads.size(); ++i) {
            Signature signature = overload_signatures.get(i);
            if (isCompatibleSignature(signature, parameter_types, labels.size())) {
                candidates.add(i);
            }
        }

        if (overloads.isEmpty() && inapplicable_generics.isEmpty()) {
            if (silence_errors) return null;
            StringBuilder stream = new StringBuilder();
            stream.append("no matching declaration found for ").append(name);
            Torque.reportError(stream.toString());
        } else if (candidates.isEmpty()) {
            if (silence_errors) return null;
            Torque.failCallableLookup("cannot find suitable callable with name", name,
                    parameter_types, labels, overload_signatures,
                    inapplicable_generics);
        }

        Function<Std.Arguments<Integer>, Boolean> is_better_candidate = new Function<Std.Arguments<Integer>, Boolean>() {
            @Override
            public Boolean apply(Std.Arguments<Integer> args) {
                int a = args.get(0), b = args.get(1);
                return new ParameterDifference(overload_signatures.get(a).getExplicitTypes(),
                        parameter_types)
                        .strictlyBetterThan(new ParameterDifference(
                                overload_signatures.get(b).getExplicitTypes(), parameter_types));
            }
        };

        int best = Std.min_element(candidates, is_better_candidate);

        for (int candidate : candidates) {
            if (candidate != best && !is_better_candidate.apply(new Std.Arguments(best, candidate))) {
                Vector<Signature> candidate_signatures = new Vector<Signature>();
                for (int i : candidates) {
                    candidate_signatures.add(overload_signatures.get(i));
                }
                Torque.failCallableLookup("ambiguous callable ", name, parameter_types, labels,
                        candidate_signatures, inapplicable_generics);
            }
        }

        Generic generic = Generic.dynamicCast(overloads.get(best));
        if (generic != null) {
            TypeArgumentInference inference = generic.inferSpecializationTypes(
                    specialization_types, parameter_types);
            result = Torque.getOrCreateSpecialization(
                    new SpecializationKey<Generic>(generic, inference.getResult()));
        } else {
            result = Callable.cast(overloads.get(best));
        }

        int caller_size = parameter_types.size();
        int callee_size = result.signature().types().size() - result.signature().implicit_count;
        if (caller_size != callee_size &&
                !result.signature().parameter_types.var_args) {
            StringBuilder stream = new StringBuilder();
            stream.append("parameter count mismatch calling ").append(result).append(" - expected ")
                    .append(callee_size).append(", found ")
                    .append(caller_size);
            Torque.reportError(stream.toString());
        }

        return result;
    }

    public boolean isCompatibleSignature(Signature sig, TypeVector types, int label_count) {
        int i = sig.implicit_count; //sig.parameter_types.types.begin() + sig.implicit_count;
        if ((sig.parameter_types.types.size() - sig.implicit_count) > types.size())
            return false;
        if (sig.labels.size() != label_count) return false;
        for (Type current : types) {
            if (i == sig.parameter_types.types.size()) {
                if (!sig.parameter_types.var_args) return false;
                if (!Torque.isAssignableFrom(TypeOracle.getObjectType(), current)) return false;
            } else {
                if (!Torque.isAssignableFrom(sig.parameter_types.types.get(i++), current)) return false;
            }
        }
        return true;
    }

    public VisitResult generateCall(Callable callable, LocationReference this_reference,
                        Arguments arguments, TypeVector specialization_types, boolean is_tailcall) {
        Type return_type = callable.signature().return_type;

        if (is_tailcall) {
            Builtin builtin = Builtin.dynamicCast(CurrentCallable.get());
            if (builtin != null) {
                Type outer_return_type = builtin.signature().return_type;
                if (!return_type.isSubtypeOf(outer_return_type)) {
                    Torque.error("Cannot tailcall, type of result is ", return_type,
                            " but should be a subtype of ", outer_return_type, ".");
                }
            } else {
                Torque.error("Tail calls are only allowed from builtins");
            }
        }

        Vector<VisitResult> converted_arguments = new Vector<VisitResult>();
        StackRange argument_range = assembler().topRange(0);
        Vector<String> constexpr_arguments = new Vector<String>();

        int current = 0;
        for (; current < callable.signature().implicit_count; ++current) {
            String implicit_name = callable.signature().parameter_names.get(current).value;
            Binding<LocalValue> val = tryLookupLocalValue(implicit_name);
            if (val == null) {
                Torque.reportError("implicit parameter '", implicit_name,
                        "' required for call to '", callable.readableName(),
                        "' is not defined");
            }
            addCallParameter(callable, val.super_class.value,
                    callable.signature().parameter_types.types.get(current),
                     converted_arguments, argument_range,
                     constexpr_arguments);
        }

        if (this_reference != null) {
            Method method = Method.cast(callable);
            VisitResult this_value = this_reference.getVisitResult();
            if (method.shouldBeInlined()) {
                if (!this_value.type().isSubtypeOf(method.aggregate_type())) {
                    Torque.reportError("this parameter must be a subtype of ",
                            method.aggregate_type(), " but it is of type ",
                            this_value.type());
                }
            } else {
                addCallParameter(callable, this_value, method.aggregate_type(),
                        converted_arguments, argument_range,
                       constexpr_arguments);
            }
            ++current;
        }

        for (VisitResult arg : arguments.parameters) {
            Type to_type = (current >= callable.signature().types().size())
                    ? TypeOracle.getObjectType()
                              : callable.signature().types().get(current++);
            addCallParameter(callable, arg, to_type, converted_arguments,
                     argument_range, constexpr_arguments);
        }

        int label_count = callable.signature().labels.size();
        if (label_count != arguments.labels.size()) {
            StringBuilder s = new StringBuilder();
            s.append("unexpected number of otherwise labels for ")
                    .append(callable.readableName()).append(" (expected ")
                    .append(label_count).append(" found ")
                    .append(arguments.labels.size()).append(")");
            Torque.reportError(s.toString());
        }

        if (callable.isTransitioning()) {
            if (!CurrentCallable.get().isTransitioning()) {
                StringBuilder s = new StringBuilder();
                s.append(CurrentCallable.get())
                        .append(" isn't marked transitioning but calls the transitioning ")
                        .append(callable);
                Torque.reportError(s.toString());
            }
        }

        Builtin builtin = Builtin.dynamicCast(callable);
        Macro macro = null;
        RuntimeFunction runtime_function = null;
        Intrinsic intrinsic = null;
        if (builtin != null) {
            Block catch_block = getCatchBlock();
            assembler().emit(new Instruction(new CallBuiltinInstruction(
                is_tailcall, builtin, argument_range.size(), catch_block), InstructionKind.kCallBuiltinInstruction));
            generateCatchBlock(catch_block);
            if (is_tailcall) {
                return VisitResult.neverResult();
            } else {
                int slot_count = Torque.loweredSlotCount(return_type);
                return new VisitResult(return_type, assembler().topRange(slot_count));
            }
        } else if ((macro = Macro.dynamicCast(callable)) != null) {
            if (is_tailcall) {
                Torque.reportError("can't tail call a macro");
            }
            macro.setUsed();
            if (return_type.isConstexpr()) {
                StringBuilder result = new StringBuilder();
                result.append("(");
                boolean first = true;
                ExternMacro extern_macro = null;
                if ((extern_macro = ExternMacro.dynamicCast(macro)) != null) {
                    result.append(extern_macro.external_assembler_name()).append("(state_).")
                            .append(extern_macro.externalName()).append("(");
                } else {
                    result.append(macro.externalName()).append("(state_");
                    first = false;
                }
                for (VisitResult arg : arguments.parameters) {
                    if (!first) {
                        result.append(", ");
                    }
                    first = false;
                    result.append(arg.constexpr_value());
                }
                result.append("))");
                return new VisitResult(return_type, result.toString());
            } else if (macro.shouldBeInlined()) {
                Vector<Block> label_blocks = new Vector<Block>();
                for (Binding<LocalLabel> label : arguments.labels) {
                    label_blocks.add(label.super_class.block);
                }
                return inlineMacro(macro, this_reference, converted_arguments, label_blocks);
            } else if (arguments.labels.isEmpty() &&
                    return_type != TypeOracle.getNeverType()) {
                Block catch_block = getCatchBlock();
                assembler().emit(new Instruction(
                        new CallCsaMacroInstruction(macro, constexpr_arguments, catch_block), CallCsaMacroInstruction.kKind));
                generateCatchBlock(catch_block);
                int return_slot_count = Torque.loweredSlotCount(return_type);
                return new VisitResult(return_type, assembler().topRange(return_slot_count));
            } else {
                Block return_continuation = null;
                if (return_type != TypeOracle.getNeverType()) {
                    return_continuation = assembler().newBlock();
                }

                Vector<Block> label_blocks = new Vector<Block>();

                for (int i = 0; i < label_count; ++i) {
                    label_blocks.add(assembler().newBlock());
                }
                Block catch_block = getCatchBlock();
                assembler().emit(new Instruction(new CallCsaMacroAndBranchInstruction(
                    macro, constexpr_arguments, return_continuation, label_blocks,
                            catch_block), InstructionKind.kCallCsaMacroAndBranchInstruction));
                generateCatchBlock(catch_block);

                for (int i = 0; i < label_count; ++i) {
                    Binding<LocalLabel> label = arguments.labels.get(i);
                    int callee_label_parameters =
                            callable.signature().labels.get(i).types.size();
                    if (label.super_class.parameter_types.size() != callee_label_parameters) {
                        StringBuilder s = new StringBuilder();
                        s.append("label ").append(label.name())
                                .append(" doesn't have the right number of parameters (found ")
                                .append(label.super_class.parameter_types.size()).append(" expected ")
                                .append(callee_label_parameters).append(")");
                        Torque.reportError(s.toString());
                    }
                    assembler().bind(label_blocks.get(i));
                    assembler().Goto(
                            label.super_class.block,
                            Torque.lowerParameterTypes(callable.signature().labels.get(i).types).size());

                    int j = 0;
                    for (Type t : callable.signature().labels.get(i).types) {
                        Type parameter_type = label.super_class.parameter_types.get(j);
                        if (!t.isSubtypeOf(parameter_type)) {
                            Torque.reportError("mismatch of label parameters (label expects ",
                                    parameter_type, " but macro produces ", t,
                                    " for parameter ", i + 1, ")");
                        }
                        j++;
                    }
                }

                if (return_continuation != null) {
                    assembler().bind(return_continuation);
                    int return_slot_count = Torque.loweredSlotCount(return_type);
                    return new VisitResult(return_type,
                            assembler().topRange(return_slot_count));
                } else {
                    return VisitResult.neverResult();
                }
            }
        } else if ((runtime_function = RuntimeFunction.dynamicCast(callable)) != null) {
            Block catch_block = getCatchBlock();
            assembler().emit(new Instruction(new CallRuntimeInstruction(
                is_tailcall, runtime_function, argument_range.size(), catch_block), InstructionKind.kCallRuntimeInstruction));
            generateCatchBlock(catch_block);
            if (is_tailcall || return_type == TypeOracle.getNeverType()) {
                return VisitResult.neverResult();
            } else {
                int slot_count = Torque.loweredSlotCount(return_type);
                return new VisitResult(return_type, assembler().topRange(slot_count));
            }
        } else if ((intrinsic = Intrinsic.dynamicCast(callable)) != null) {
            if (intrinsic.externalName() == "%RawConstexprCast") {
                if (intrinsic.signature().parameter_types.types.size() != 1 ||
                        constexpr_arguments.size() != 1) {
                    Torque.reportError(
                            "%RawConstexprCast must take a single parameter with constexpr ",
                            "type");
                }
                if (!return_type.isConstexpr()) {
                    StringBuilder s = new StringBuilder();
                    s.append(return_type)
                            .append(" return type for %RawConstexprCast is not constexpr");
                    Torque.reportError(s.toString());
                }
                StringBuilder result = new StringBuilder();
                result.append("static_cast<").append(return_type.getGeneratedTypeName()).append(">(");
                result.append(constexpr_arguments.get(0));
                result.append(")");
                return new VisitResult(return_type, result.toString());
            } else {
                assembler().emit(new Instruction(new CallIntrinsicInstruction(intrinsic, specialization_types,
                        constexpr_arguments), InstructionKind.kCallIntrinsicInstruction));
                int return_slot_count =
                        Torque.loweredSlotCount(intrinsic.signature().return_type);
                return new VisitResult(return_type, assembler().topRange(return_slot_count));
            }
        } else {
            throw new RuntimeException("UNREACHABLE");
        }
    }

    public void addCallParameter(Callable callable, VisitResult parameter, Type parameter_type,
                                    Vector<VisitResult> converted_arguments, StackRange argument_range,
                                    Vector<String> constexpr_arguments) {
        VisitResult converted = generateImplicitConvert(parameter_type, parameter);
        converted_arguments.add(converted);
        if (!callable.shouldBeInlined()) {
            if (converted.isOnStack()) {
                argument_range.extend(converted.stack_range());
            } else {
                constexpr_arguments.add(converted.constexpr_value());
            }
        }
    }

    public VisitResult generateImplicitConvert(Type destination_type, VisitResult source) {
        StackScope scope = new StackScope(this);
        if (source.type() == TypeOracle.getNeverType()) {
            Torque.reportError("it is not allowed to use a value of type never");
            throw new RuntimeException("it is not allowed to use a value of type never");
        }

        if (destination_type == source.type()) {
            return scope.yield(generateCopy(source));
        }

        if (TypeOracle.isImplicitlyConvertableFrom(destination_type, source.type())) {
            return scope.yield(generateCall(Torque.kFromConstexprMacroName, new Arguments(new VisitResultVector(source), new Vector<Binding<LocalLabel>>()),
                    new TypeVector(destination_type, source.type()), false));
        } else if (Torque.isAssignableFrom(destination_type, source.type())) {
            source.setType(destination_type);
            return scope.yield(generateCopy(source));
        } else {
            StringBuilder s = new StringBuilder();
            s.append("cannot use expression of type ").append(source.type())
                    .append(" as a value of type ").append(destination_type);
            Torque.reportError(s.toString());
            throw new RuntimeException(s.toString());
        }
    }

    public Block getCatchBlock() {
        Block catch_block = null;
        Binding<LocalLabel> catch_handler = tryLookupLabel(Torque.kCatchLabelName);
        if (catch_handler != null) {
            catch_block = assembler().newBlock(null, true);
        }
        return catch_block;
    }

    public Binding<LocalLabel> tryLookupLabel(String name) {
        return LabelBindingsManager.get().tryLookup(name);
    }

    public void generateCatchBlock(Block catch_block) {
        if (catch_block != null) {
            Binding<LocalLabel> catch_handler = tryLookupLabel(Torque.kCatchLabelName);
            if (assembler().CurrentBlockIsComplete()) {
                assembler().bind(catch_block);
                assembler().Goto(catch_handler.super_class.block, 1);
            } else {
                CfgAssemblerScopedTemporaryBlock temp = new CfgAssemblerScopedTemporaryBlock(assembler(), catch_block);
                assembler().Goto(catch_handler.super_class.block, 1);
            }
        }
    }

    public boolean testLookupCallable(QualifiedName name, TypeVector parameter_types) {
        return lookupCallable(name, Declarations.tryLookup(name), parameter_types,
                new Vector<Binding<LocalLabel>>(), new TypeVector(), true) != null;
    }

    public void lowerLabelParameter(Type type, String parameter_name, Vector<String> lowered_parameters) {
        StructType struct_type = StructType.dynamicCast(type);
        if (struct_type != null) {
            for (Field field : struct_type.fields()) {
                lowerLabelParameter(
                        field.name_and_type.type,
                        "&((*" + parameter_name + ")." + field.name_and_type.name + ")",
                        lowered_parameters);
            }
        } else {
            lowered_parameters.add(parameter_name);
        }
    }
}
