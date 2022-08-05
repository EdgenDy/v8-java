package org.jsengine.v8.internal.torque;

public enum InstructionKind {
    kPeekInstruction,
    kPokeInstruction,
    kDeleteRangeInstruction,
    kPushUninitializedInstruction,
    kPushBuiltinPointerInstruction,
    kCreateFieldReferenceInstruction,
    kLoadReferenceInstruction,
    kStoreReferenceInstruction,
    kCallCsaMacroInstruction,
    kCallIntrinsicInstruction,
    kNamespaceConstantInstruction,
    kCallCsaMacroAndBranchInstruction,
    kCallBuiltinInstruction,
    kCallRuntimeInstruction,
    kCallBuiltinPointerInstruction,
    kBranchInstruction,
    kConstexprBranchInstruction,
    kGotoInstruction,
    kGotoExternalInstruction,
    kReturnInstruction,
    kPrintConstantStringInstruction,
    kAbortInstruction,
    kUnsafeCastInstruction
}
