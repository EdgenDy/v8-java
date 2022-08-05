package org.jsengine.v8.internal.torque;

import org.jsengine.v8.internal.Torque;
import org.jsengine.utils.Std;

import java.util.Vector;
import java.util.function.Function;

public class CSAGenerator {
    private ControlFlowGraph cfg_;
    private StringBuilder out_;
    private int fresh_id_ = 0;
    private Builtin.Kind linkage_;
    private SourcePosition previous_position_;

    public CSAGenerator(ControlFlowGraph cfg, StringBuilder out, Builtin.Kind linkage) {
        this.cfg_ = cfg;
        this.out_ = out;
        this.linkage_ = linkage;
        this.previous_position_ = SourcePosition.invalid();
    }

    public CSAGenerator(ControlFlowGraph cfg, StringBuilder out) {
        this(cfg, out, null);
    }

    public Stack<String> emitGraph(Stack<String> parameters) {
        for (Block block : cfg_.blocks()) {
            out_.append("  compiler::CodeAssemblerParameterizedLabel<");
            Torque.printCommaSeparatedList(out_, block.inputTypes(), new Function<Type, String>() {
                @Override
                public String apply(Type t) {
                    return t.getGeneratedTNodeTypeName();
                }
            });

            out_.append("> ").append(blockName(block)).append("(&ca_, compiler::CodeAssemblerLabel::")
                    .append(block.isDeferred() ? "kDeferred" : "kNonDeferred").append(");\n");
        }

        emitInstruction(new GotoInstruction(cfg_.start()), parameters);
        for (Block block : cfg_.blocks()) {
            if (cfg_.end() != null && cfg_.end() == block) continue;
            out_.append("\n  if (").append(blockName(block)).append(".is_used()) {\n");
            emitBlock(block);
            out_.append("  }\n");
        }
        if (cfg_.end() != null) {
            out_.append("\n");
            return emitBlock(cfg_.end());
        }
        return null;
    }

    public String blockName(Block block) {
        return "block" + block.id();
    }

    public void emitInstruction(GotoInstruction instruction, Stack<String> stack) {
        out_.append("    ca_.Goto(&").append(blockName(instruction.destination));
        for (String value : stack) {
            out_.append(", ").append(value);
        }
        out_.append(");\n");
    }

    public Stack<String> emitBlock(Block block) {
        Stack<String> stack = new Stack<String>();
        for (Type t : block.inputTypes()) {
            stack.push(freshNodeName());
            out_.append("    compiler::TNode<").append(t.getGeneratedTNodeTypeName()).append("> ")
                    .append(stack.top()).append(";\n");
        }
        out_.append("    ca_.Bind(&").append(blockName(block));
        for (String name : stack) {
            out_.append(", &").append(name);
        }
        out_.append(");\n");
        for (Instruction instruction : block.instructions()) {
            emitInstruction(instruction, stack);
        }
        return stack;
    }

    public String freshNodeName() {
        return "tmp" + (fresh_id_++);
    }

    public void emitInstruction(Instruction instruction, Stack<String> stack) {
        emitSourcePosition(instruction.instruction_.pos);
        switch (instruction.kind()) {
            case kPeekInstruction:
                emitInstruction((PeekInstruction) instruction.cast(PeekInstruction.class), stack);
                return;
            case kPokeInstruction:
                emitInstruction((PokeInstruction) instruction.cast(PokeInstruction.class), stack);
                return;
            case kDeleteRangeInstruction:
                emitInstruction((DeleteRangeInstruction) instruction.cast(DeleteRangeInstruction.class), stack);
                return;
            case kPushUninitializedInstruction:
                emitInstruction((PushUninitializedInstruction) instruction.cast(PushUninitializedInstruction.class), stack);
                return;
            case kPushBuiltinPointerInstruction:
                emitInstruction((PushBuiltinPointerInstruction) instruction.cast(PushBuiltinPointerInstruction.class), stack);
                return;
            case kCreateFieldReferenceInstruction:
                emitInstruction((CreateFieldReferenceInstruction) instruction.cast(CreateFieldReferenceInstruction.class), stack);
                return;
            case kLoadReferenceInstruction:
                emitInstruction((LoadReferenceInstruction) instruction.cast(LoadReferenceInstruction.class), stack);
                return;
            case kStoreReferenceInstruction:
                emitInstruction((StoreReferenceInstruction) instruction.cast(StoreReferenceInstruction.class), stack);
                return;
            case kCallCsaMacroInstruction:
                emitInstruction((CallCsaMacroInstruction) instruction.cast(CallCsaMacroInstruction.class), stack);
                return;
            case kCallIntrinsicInstruction:
                emitInstruction((CallIntrinsicInstruction) instruction.cast(CallIntrinsicInstruction.class), stack);
                return;
            case kNamespaceConstantInstruction:
                emitInstruction((NamespaceConstantInstruction) instruction.cast(NamespaceConstantInstruction.class), stack);
                return;
            case kCallCsaMacroAndBranchInstruction:
                emitInstruction((CallCsaMacroAndBranchInstruction) instruction.cast(CallCsaMacroAndBranchInstruction.class), stack);
                return;
            case kCallBuiltinInstruction:
                emitInstruction((CallBuiltinInstruction) instruction.cast(CallBuiltinInstruction.class), stack);
                return;
            case kCallRuntimeInstruction:
                emitInstruction((CallRuntimeInstruction) instruction.cast(CallRuntimeInstruction.class), stack);
                return;
            case kCallBuiltinPointerInstruction:
                emitInstruction((CallBuiltinPointerInstruction) instruction.cast(CallBuiltinPointerInstruction.class), stack);
                return;
            case kBranchInstruction:
                emitInstruction((BranchInstruction) instruction.cast(BranchInstruction.class), stack);
                return;
            case kConstexprBranchInstruction:
                emitInstruction((ConstexprBranchInstruction) instruction.cast(ConstexprBranchInstruction.class), stack);
                return;
            case kGotoInstruction:
                emitInstruction(((GotoInstruction) instruction.cast(GotoInstruction.class)), stack);
                return;
            case kGotoExternalInstruction:
                emitInstruction((GotoExternalInstruction) instruction.cast(GotoExternalInstruction.class), stack);
                return;
            case kReturnInstruction:
                emitInstruction((ReturnInstruction) instruction.cast(ReturnInstruction.class), stack);
                return;
            case kPrintConstantStringInstruction:
                emitInstruction((PrintConstantStringInstruction) instruction.cast(PrintConstantStringInstruction.class), stack);
                return;
            case kAbortInstruction:
                emitInstruction((AbortInstruction) instruction.cast(AbortInstruction.class), stack);
                return;
            case kUnsafeCastInstruction:
                emitInstruction((UnsafeCastInstruction) instruction.cast(UnsafeCastInstruction.class), stack);
                return;
        }
    }

    public void emitSourcePosition(SourcePosition pos, boolean always_emit) {
        String file = SourceFileMap.absolutePath(pos.source);
        if (always_emit || !previous_position_.compareStartIgnoreColumn(pos)) {
            out_.append("    ca_.SetSourcePosition(\"").append(file).append("\", ")
                    .append(pos.start.line + 1).append(");\n");
            previous_position_ = pos;
        }
    }

    public void emitSourcePosition(SourcePosition pos) {
        emitSourcePosition(pos, false);
    }

    public void emitInstruction(PeekInstruction instruction, Stack<String> stack) {
        stack.push(stack.peek(instruction.slot));
    }

    public void emitInstruction(PokeInstruction instruction, Stack<String> stack) {
        stack.poke(instruction.slot, stack.top());
        stack.pop();
    }

    public void emitInstruction(DeleteRangeInstruction instruction, Stack<String> stack) {
        stack.deleteRange(instruction.range);
    }

    public void emitInstruction(PushUninitializedInstruction instruction, Stack<String> stack) {
        stack.push("ca_.Uninitialized<" + instruction.type.getGeneratedTNodeTypeName() + ">()");
    }

    public void emitInstruction(PushBuiltinPointerInstruction instruction, Stack<String> stack) {
        stack.push("ca_.UncheckedCast<BuiltinPtr>(ca_.SmiConstant(Builtins::k" +
                instruction.external_name + "))");
    }

    public void emitInstruction(CreateFieldReferenceInstruction instruction, Stack<String> stack) {
        ClassType class_type = instruction.type.classSupertype();
        if (class_type != null) {
            Torque.reportError("Cannot create field reference of type ", instruction.type,
                    " which does not inherit from a class type");
        }

        Field field = class_type.lookupField(instruction.field_name);
        String offset_name = freshNodeName();
        stack.push(offset_name);

        out_.append("    compiler::TNode<IntPtrT> ").append(offset_name)
                .append(" = ca_.IntPtrConstant(");
        out_.append(field.aggregate.getGeneratedTNodeTypeName()).append("::k")
                .append(Torque.camelifyString(field.name_and_type.name)).append("Offset");
        out_.append(");\n")
                .append("    USE(").append(stack.top()).append(");\n");
    }

    public void emitInstruction(LoadReferenceInstruction instruction, Stack<String> stack) {
        String result_name = freshNodeName();

        String offset = stack.pop();
        String object = stack.pop();
        stack.push(result_name);

        out_.append("    ").append(instruction.type.getGeneratedTypeName()).append(result_name)
                .append(" = CodeStubAssembler(state_).LoadReference<")
                .append(instruction.type.getGeneratedTNodeTypeName())
                .append(">(CodeStubAssembler::Reference{").append(object).append(", ").append(offset)
                .append("});\n");
    }

    public void emitInstruction(StoreReferenceInstruction instruction, Stack<String> stack) {
        String value = stack.pop();
        String offset = stack.pop();
        String object = stack.pop();

        out_.append("    CodeStubAssembler(state_).StoreReference(CodeStubAssembler::")
            .append("Reference{")
            .append(object).append(", ").append(offset).append("}, ").append(value).append(");\n");
    }

    public void emitInstruction(CallCsaMacroInstruction instruction, Stack<String> stack) {
        Vector<String> constexpr_arguments = instruction.constexpr_arguments;
        Vector<String> args = new Vector<String>();
        TypeVector parameter_types = instruction.macro.signature().parameter_types.types;
        processArgumentsCommon(parameter_types, args, constexpr_arguments, stack);

        Stack<String> pre_call_stack = stack;
        Type return_type = instruction.macro.signature().return_type;
        Vector<String> results = new Vector<String>();
        for (Type type : Torque.lowerType(return_type)) {
            results.add(freshNodeName());
            stack.push(results.lastElement());
            out_.append("    compiler::TNode<").append(type.getGeneratedTNodeTypeName()).append("> ")
                    .append(stack.top()).append(";\n");
            out_.append("    USE(").append(stack.top()).append(");\n");
        }
        String catch_name = preCallableExceptionPreparation(instruction.catch_block);
        out_.append("    ");
        boolean needs_flattening = return_type.isStructType();
        if (needs_flattening) {
            out_.append("std::tie(");
            Torque.printCommaSeparatedList(out_, results);
            out_.append(") = ");
        } else {
            if (results.size() == 1) {
                out_.append(results.get(0)).append(" = ");
            } else {
                //DCHECK_EQ(0, results.size());
            }
        }

        ExternMacro extern_macro = ExternMacro.dynamicCast(instruction.macro);
        if (extern_macro != null) {
            out_.append(extern_macro.external_assembler_name()).append("(state_).");
        } else {
            args.insertElementAt("state_", 0);
        }
        out_.append(instruction.macro.externalName()).append("(");
        Torque.printCommaSeparatedList(out_, args);
        if (needs_flattening) {
            out_.append(").Flatten();\n");
        } else {
            out_.append(");\n");
        }
        postCallableExceptionPreparation(catch_name, return_type, instruction.catch_block, pre_call_stack);
    }

    public void processArgumentsCommon(TypeVector parameter_types, Vector<String> args,
                                            Vector<String> constexpr_arguments, Stack<String> stack) {
        for (Type type : parameter_types) {
            VisitResult arg = new VisitResult();
            if (type.isConstexpr()) {
                args.add(constexpr_arguments.lastElement());
                constexpr_arguments.remove(constexpr_arguments.size() - 1);
            } else {
                StringBuilder s = new StringBuilder();
                int slot_count = Torque.loweredSlotCount(type);
                arg = new VisitResult(type, stack.topRange(slot_count));
                emitCSAValue(arg, stack, s);
                args.add(s.toString());
                stack.popMany(slot_count);
            }
        }
        args = Std.reverse(args);
    }

    public void emitCSAValue(VisitResult result, Stack<String> values,
                                            StringBuilder out) {
        StructType struct_type = null;
        if (!result.isOnStack()) {
            out.append(result.constexpr_value());
        } else if ((struct_type = StructType.dynamicCast(result.type())) != null) {
            out.append(struct_type.getGeneratedTypeName()).append("{");
            boolean first = true;
            for (Field field : struct_type.fields()) {
                if (!first) {
                    out.append(", ");
                }
                first = false;
                emitCSAValue(Torque.projectStructField(result, field.name_and_type.name), values, out);
            }
            out.append("}");
        } else {
            out.append("compiler::TNode<").append(result.type().getGeneratedTNodeTypeName())
                    .append(">{").append(values.peek(result.stack_range().begin())).append("}");
        }
    }

    public String preCallableExceptionPreparation(Block catch_block) {
        String catch_name = new String();
        if (catch_block != null) {
            catch_name = freshCatchName();
            out_.append("    compiler::CodeAssemblerExceptionHandlerLabel ").append(catch_name)
                    .append("__label(&ca_, compiler::CodeAssemblerLabel::kDeferred);\n");
            out_.append("    { compiler::CodeAssemblerScopedExceptionHandler s(&ca_, &")
                    .append(catch_name).append("__label);\n");
        }
        return catch_name;
    }

    public String freshCatchName() {
        return "catch" + (fresh_id_++);
    }

    public void postCallableExceptionPreparation(String catch_name, Type return_type,
                                                   Block catch_block, Stack<String> stack) {
        if (catch_block != null) {
            String block_name = blockName(catch_block);
            out_.append("    }\n");
            out_.append("    if (").append(catch_name).append("__label.is_used()) {\n");
            out_.append("      compiler::CodeAssemblerLabel ").append(catch_name)
                    .append("_skip(&ca_);\n");
            if (!return_type.isNever()) {
                out_.append("      ca_.Goto(&").append(catch_name).append("_skip);\n");
            }
            out_.append("      compiler::TNode<Object> ").append(catch_name)
                    .append("_exception_object;\n");
            out_.append("      ca_.Bind(&").append(catch_name).append("__label, &").append(catch_name)
                    .append("_exception_object);\n");
            out_.append("      ca_.Goto(&").append(block_name);
            for (int i = 0; i < stack.size(); ++i) {
                out_.append(", ").append(stack.begin().value());
            }
            out_.append(", ").append(catch_name).append("_exception_object);\n");
            if (!return_type.isNever()) {
                out_.append("      ca_.Bind(&").append(catch_name).append("_skip);\n");
            }
            out_.append("    }\n");
        }
    }

    public void emitInstruction(CallIntrinsicInstruction instruction, Stack<String> stack) {
        Vector<String> constexpr_arguments = instruction.constexpr_arguments;
        Vector<String> args = new Vector<String>();
        TypeVector parameter_types = instruction.intrinsic.signature().parameter_types.types;
        processArgumentsCommon(parameter_types, args, constexpr_arguments, stack);

        Stack<String> pre_call_stack = stack;
        Type return_type = instruction.intrinsic.signature().return_type;
        Vector<String> results = new Vector<String>();
        for (Type type : Torque.lowerType(return_type)) {
            results.add(freshNodeName());
            stack.push(results.lastElement());
            out_.append("    compiler::TNode<").append(type.getGeneratedTNodeTypeName()).append("> ")
                    .append(stack.top()).append(";\n");
            out_.append("    USE(").append(stack.top()).append(");\n");
        }
        out_.append("    ");

        if (return_type.isStructType()) {
            out_.append("std::tie(");
            Torque.printCommaSeparatedList(out_, results);
            out_.append(") = ");
        } else {
            if (results.size() == 1) {
                out_.append(results.get(0)).append(" = ");
            }
        }

        if (instruction.intrinsic.externalName() == "%RawDownCast") {
            if (parameter_types.size() != 1) {
                Torque.reportError("%RawDownCast must take a single parameter");
            }
            if (!return_type.isSubtypeOf(parameter_types.get(0))) {
                Torque.reportError("%RawDownCast error: ", return_type, " is not a subtype of ",
                  parameter_types.get(0));
            }
            if (return_type.isSubtypeOf(TypeOracle.getTaggedType())) {
                if (return_type.getGeneratedTNodeTypeName() !=
                        parameter_types.get(0).getGeneratedTNodeTypeName()) {
                    out_.append("TORQUE_CAST");
                }
            }
        } else if (instruction.intrinsic.externalName() == "%FromConstexpr") {
            if (parameter_types.size() != 1 || !parameter_types.get(0).isConstexpr()) {
                Torque.reportError(
                        "%FromConstexpr must take a single parameter with constexpr ","type");
            }
            if (return_type.isConstexpr()) {
                Torque.reportError("%FromConstexpr must return a non-constexpr type");
            }
            if (return_type.isSubtypeOf(TypeOracle.getSmiType())) {
                out_.append("ca_.SmiConstant");
            } else if (return_type.isSubtypeOf(TypeOracle.getNumberType())) {
                out_.append("ca_.NumberConstant");
            } else if (return_type.isSubtypeOf(TypeOracle.getStringType())) {
                out_.append("ca_.StringConstant");
            } else if (return_type.isSubtypeOf(TypeOracle.getObjectType())) {
                Torque.reportError(
                        "%FromConstexpr cannot cast to subclass of HeapObject unless it's a ",
                        "String or Number");
            } else if (return_type.isSubtypeOf(TypeOracle.getIntPtrType())) {
                out_.append("ca_.IntPtrConstant");
            } else if (return_type.isSubtypeOf(TypeOracle.getUIntPtrType())) {
                out_.append("ca_.UintPtrConstant");
            } else if (return_type.isSubtypeOf(TypeOracle.getInt32Type())) {
                out_.append("ca_.Int32Constant");
            } else {
                StringBuilder s = new StringBuilder();
                s.append("%FromConstexpr does not support return type ").append(return_type);
                Torque.reportError(s.toString());
            }
        } else if (instruction.intrinsic.externalName() == "%GetAllocationBaseSize") {
            if (instruction.specialization_types.size() != 1) {
                Torque.reportError(
                        "incorrect number of specialization classes for ",
                        "%GetAllocationBaseSize (should be one)");
            }
            ClassType class_type = ClassType.cast(instruction.specialization_types.get(0));

            if (class_type != TypeOracle.getJSObjectType()) {
                out_.append("CodeStubAssembler(state_).IntPtrConstant((");
                args.setElementAt(class_type.size() + "", 0);
            } else {
                out_.append("CodeStubAssembler(state_).TimesTaggedSize(CodeStubAssembler(" +
                "state_).LoadMapInstanceSizeInWords(");
            }
        } else if (instruction.intrinsic.externalName() == "%Allocate") {
            out_.append("ca_.UncheckedCast<").append(return_type.getGeneratedTNodeTypeName())
                    .append(">(CodeStubAssembler(state_).Allocate");
        } else if (instruction.intrinsic.externalName() == "%GetStructMap") {
            out_.append("CodeStubAssembler(state_).GetStructMap");
        } else {
            Torque.reportError("no built in intrinsic with name " + instruction.intrinsic.externalName());
        }

        out_.append("(");
        Torque.printCommaSeparatedList(out_, args);
        if (instruction.intrinsic.externalName() == "%Allocate") out_.append(")");
        if (instruction.intrinsic.externalName() == "%GetAllocationBaseSize")
        out_.append("))");
        if (return_type.isStructType()) {
            out_.append(").Flatten();\n");
        } else {
            out_.append(");\n");
        }
        if (instruction.intrinsic.externalName() == "%Allocate") {
            out_.append("    CodeStubAssembler(state_).InitializeFieldsWithRoot(")
                    .append(results.get(0)).append(", ");
            out_.append("CodeStubAssembler(state_).IntPtrConstant(")
                    .append(ClassType.cast(return_type).size()).append("), ");
            Torque.printCommaSeparatedList(out_, args);
            out_.append(", RootIndex::kUndefinedValue);\n");
        }
    }

    public void emitInstruction(NamespaceConstantInstruction instruction, Stack<String> stack) {
        Type type = instruction.constant.type();
        Vector<String> results = new Vector<String>();
        for (Type lowered : Torque.lowerType(type)) {
            results.add(freshNodeName());
            stack.push(results.lastElement());
            out_.append("    compiler::TNode<").append(lowered.getGeneratedTNodeTypeName())
                    .append("> ").append(stack.top()).append(";\n");
            out_.append("    USE(").append(stack.top()).append(");\n");
        }
        out_.append("    ");
        if (type.isStructType()) {
            out_.append("std::tie(");
            Torque.printCommaSeparatedList(out_, results);
            out_.append(") = ");
        } else if (results.size() == 1) {
            out_.append(results.get(0)).append(" = ");
        }
        out_.append(instruction.constant.external_name()).append("(state_)");
        if (type.isStructType()) {
            out_.append(".Flatten();\n");
        } else {
            out_.append(";\n");
        }
    }

    public void emitInstruction(CallCsaMacroAndBranchInstruction instruction, Stack<String> stack) {
        Vector<String> constexpr_arguments = instruction.constexpr_arguments;
        Vector<String> args = new Vector<String>();
        TypeVector parameter_types = instruction.macro.signature().parameter_types.types;
        processArgumentsCommon(parameter_types, &args, constexpr_arguments, stack);

        Stack<String> pre_call_stack = stack;
        Vector<String> results = new Vector<String>();
        Type return_type = instruction.macro.signature().return_type;
        if (return_type != TypeOracle.getNeverType()) {
            for (Type type : Torque.lowerType(instruction.macro.signature().return_type)) {
                results.add(freshNodeName());
                out_.append("    compiler::TNode<").append(type.getGeneratedTNodeTypeName())
                        .append("> ").append(results.lastElement()).append(";\n");
                out_.append("    USE(").append(results.lastElement()).append(");\n");
            }
        }

        Vector<String> label_names = new Vector<String>();
        Vector<Vector<String>> var_names = new Vector<Vector<String>>();
        LabelDeclarationVector labels = instruction.macro.signature().labels;

        for (int i = 0; i < labels.size(); ++i) {
            TypeVector label_parameters = labels.get(i).types;
            label_names.add("label" + i);
            var_names.add(new Vector<String>());
            for (int j = 0; j < label_parameters.size(); ++j) {
                var_names.get(i).add("result_" + i + "_" + j);
                out_.append("    compiler::TypedCodeAssemblerVariable<")
                        .append(label_parameters.get(j).getGeneratedTNodeTypeName()).append("> ")
                        .append(var_names.get(i).get(j)).append("(&ca_);\n");
            }
            out_.append("    compiler::CodeAssemblerLabel ").append(label_names.get(i))
                    .append("(&ca_);\n");
        }

        String catch_name = preCallableExceptionPreparation(instruction.catch_block);
        out_.append("    ");
        if (results.size() == 1) {
            out_.append(results.get(0)).append(" = ");
        } else if (results.size() > 1) {
            out_.append("std::tie(");
            Torque.printCommaSeparatedList(out_, results);
            out_.append(") = ");
        }
        ExternMacro extern_macro = null;
        if ((extern_macro = ExternMacro.dynamicCast(instruction.macro)) != null) {
            out_.append(extern_macro.external_assembler_name()).append("(state_).");
        } else {
            args.insertElementAt( "state_", 0);
        }
        out_.append(instruction.macro.externalName()).append("(");
        Torque.printCommaSeparatedList(out_, args);
        boolean first = args.isEmpty();
        for (int i = 0; i < label_names.size(); ++i) {
            if (!first) out_.append(", ");
            out_.append("&").append(label_names.get(i));
            first = false;
            for (int j = 0; j < var_names.get(i).size(); ++j) {
                out_.append(", &").append(var_names.get(i).get(j));
            }
        }
        if (return_type.isStructType()) {
            out_.append(").Flatten();\n");
        } else {
            out_.append(");\n");
        }

        postCallableExceptionPreparation(catch_name, return_type,
                instruction.catch_block, pre_call_stack);

        if (instruction.return_continuation != null) {
            out_.append("    ca_.Goto(&").append(blockName(instruction.return_continuation));
            for (String value : stack) {
                out_.append(", ").append(value);
            }
            for (String result : results) {
                out_.append(", ").append(result);
            }
            out_.append(");\n");
        }
        for (int i = 0; i < label_names.size(); ++i) {
            out_.append("    if (").append(label_names.get(i)).append(".is_used()) {\n");
            out_.append("      ca_.Bind(&").append(label_names.get(i)).append(");\n");
            out_.append("      ca_.Goto(&").append(blockName(instruction.label_blocks.get(i)));
            for (String value : stack) {
                out_.append(", ").append(value);
            }
            for (String var : var_names.get(i)) {
                out_.append(", ").append(var).append(".value()");
            }
            out_.append(");\n");

            out_.append("    }\n");
        }
    }
}
