package org.jsengine.v8.internal.torque;

import org.jsengine.v8.internal.Torque;
import org.jsengine.utils.Optional;

import java.util.Vector;

public class CfgAssembler {
    public Stack<Type> current_stack_;
    public ControlFlowGraph cfg_;
    public Block current_block_;

    public CfgAssembler(Stack<Type> input_types) {
        current_stack_ = input_types;
        cfg_ = new ControlFlowGraph(current_stack_);
        current_block_ = cfg_.start();
    }

    public Block newBlock(Stack<Type> input_types, boolean is_deferred) {
        return cfg_.newBlock(input_types, is_deferred);
    }

    public Block newBlock(Stack<Type> input_types) {
        return newBlock(input_types, false);
    }

    public Block newBlock() {
        return newBlock(null, false);
    }

    public void emit(Instruction instruction) {
        instruction.typeInstruction(current_stack_, cfg_);
        current_block_.add(instruction);
    }

    public Stack<Type> currentStack() {
        return current_stack_;
    }

    public boolean CurrentBlockIsComplete() {
        return current_block_.isComplete();
    }

    public boolean currentBlockIsComplete() {
        return current_block_.isComplete();
    }

    public void deleteRange(StackRange range) {
        if (range.size() == 0) return;
        emit(new Instruction(new DeleteRangeInstruction(range), DeleteRangeInstruction.kKind));
    }

    public void dropTo(BottomOffset new_level) {
        deleteRange(new StackRange(new_level, currentStack().aboveTop()));
    }

    public StackRange topRange(int slot_count) {
        return currentStack().topRange(slot_count);
    }

    public StackRange peek(StackRange range, Type type) {
        Vector<Type> lowered_types = new Vector<Type>();
        if (type != null) {
            lowered_types = Torque.lowerType(type);
        }
        for (int i = 0; i < range.size(); ++i) {
            PeekInstruction peekInstruction = new PeekInstruction(range.begin().add(i),
                    type != null ? new Optional<Type>(lowered_types.get(i)) : new Optional<Type>());
            Instruction instruction = new Instruction(peekInstruction, PeekInstruction.kKind);
            emit(instruction);
        }
        return topRange(range.size());
    }

    public void bind(Block block) {
        current_block_ = block;
        current_stack_ = block.inputTypes();
        cfg_.placeBlock(block);
    }

    public void Goto(Block block) {
        if (block.hasInputTypes()) {
            dropTo(block.inputTypes().aboveTop());
        }
        emit(new Instruction(new GotoInstruction(block), InstructionKind.kGotoInstruction));
    }

    public StackRange Goto(Block block, int preserved_slots) {
        emit(new Instruction(new DeleteRangeInstruction(
            new StackRange(block.inputTypes().aboveTop().subtract(preserved_slots),
                    currentStack().aboveTop().subtract(preserved_slots))), InstructionKind.kDeleteRangeInstruction));
        StackRange preserved_slot_range = topRange(preserved_slots);
        emit(new Instruction(new GotoInstruction(block), InstructionKind.kGotoInstruction));
        return preserved_slot_range;
    }

    public ControlFlowGraph result() {
        if (!CurrentBlockIsComplete()) {
            cfg_.set_end(current_block_);
        }
        return cfg_;
    }
}
