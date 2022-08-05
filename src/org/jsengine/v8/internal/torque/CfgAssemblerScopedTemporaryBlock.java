package org.jsengine.v8.internal.torque;

public class CfgAssemblerScopedTemporaryBlock {
    private CfgAssembler assembler_;
    private Stack<Type> saved_stack_;
    private Block saved_block_;

    public CfgAssemblerScopedTemporaryBlock(CfgAssembler assembler, Block block) {
        this.assembler_ = assembler;
        this.saved_block_ = block;

        saved_stack_ = block.inputTypes();

        Block saved_block_$ = saved_block_;
        saved_block_ = assembler.current_block_;
        assembler.current_block_ = saved_block_$;


        Stack<Type> saved_stack_$ = saved_stack_;
        saved_stack_ = assembler.current_stack_;
        assembler.current_stack_ = saved_stack_$;

        assembler.cfg_.placeBlock(block);
    }
}
