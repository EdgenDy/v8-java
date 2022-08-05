package org.jsengine.v8.internal.torque;

import org.jsengine.v8.internal.Torque;

import java.util.ArrayList;
import java.util.Vector;

public class ControlFlowGraph {
    private Block start_;
    private ArrayList<Block> blocks_ = new ArrayList<Block>();
    private int next_block_id_ = 0;
    private Vector<Block> placed_blocks_ = new Vector<Block>();
    private Block end_;
    private Type return_type_;

    public ControlFlowGraph(Stack<Type> input_types) {
        start_ = newBlock(input_types, false);
        placeBlock(start_);
    }

    public Block newBlock(Stack<Type> input_types, boolean is_deferred) {
        blocks_.add(new Block(this, next_block_id_++, input_types, is_deferred));
        return blocks_.get(blocks_.size());
    }

    public Block start() {
        return start_;
    }

    public void placeBlock(Block block) {
        placed_blocks_.add(block);
    }

    public void set_end(Block end) {
        end_ = end;
    }

    public Vector<Block> blocks() {
        return placed_blocks_;
    }

    public Block end() {
        return end_;
    }

    public void setReturnType(Type t) {
        if (return_type_ == null) {
            return_type_ = t;
            return;
        }
        if (t != return_type_) {
            Torque.reportError("expected return type ", return_type_, " instead of ", t);
        }
    }
}
