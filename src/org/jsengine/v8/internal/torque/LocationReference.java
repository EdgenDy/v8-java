package org.jsengine.v8.internal.torque;

public class LocationReference {
    private VisitResult variable_ = null;
    private VisitResult temporary_ = null;
    private String temporary_description_ = null;
    private Binding<LocalValue> binding_ = null;
    private VisitResult heap_slice_ = null;
    private VisitResult heap_reference_ = null;
    private String eval_function_ = null;
    private VisitResultVector call_arguments_;

    public static LocationReference variableAccess(VisitResult variable,
                                                   Binding<LocalValue> binding) {
        LocationReference result = new LocationReference();
        result.variable_ = variable;
        result.binding_ = binding;
        return result;
    }

    public static LocationReference variableAccess(VisitResult variable) {
        return variableAccess(variable, null);
    }

    public static LocationReference temporary(VisitResult temporary, String description) {
        LocationReference result = new LocationReference();
        result.temporary_ = temporary;
        result.temporary_description_ = description;
        return result;
    }

    public VisitResult temporary() {
        return temporary_;
    }

    public boolean isVariableAccess() {
        return variable_ != null;
    }

    public VisitResult getVisitResult() {
        if (isVariableAccess()) return variable();
        if (isHeapSlice()) return heap_slice();
        return temporary();
    }

    public VisitResult variable() {
        return variable_;
    }

    public VisitResult heap_slice() {
        return heap_slice_;
    }

    public boolean isHeapSlice() {
        return heap_slice_ != null;
    }

    public boolean isTemporary() {
        return temporary_ != null;
    }

    public String temporary_description() {
        return temporary_description_;
    }

    public boolean isHeapReference() { return heap_reference_ != null; }

    public VisitResult heap_reference() {
        return heap_reference_;
    }

    public Type referencedType() {
        if (isHeapReference()) {
            return StructType.matchUnaryGeneric(heap_reference().type(),
                    TypeOracle.getReferenceGeneric());
        } else if (isHeapSlice()) {
            return StructType.matchUnaryGeneric(heap_slice().type(),
                    TypeOracle.getSliceGeneric());
        }
        return getVisitResult().type();
    }

    public String eval_function() {
        return eval_function_;
    }

    public VisitResultVector call_arguments() {
        return call_arguments_;
    }
}
