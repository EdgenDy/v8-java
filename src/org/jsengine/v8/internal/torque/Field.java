package org.jsengine.v8.internal.torque;

import org.jsengine.v8.internal.Torque;
import org.jsengine.utils.Tuple;

public class Field {
    public SourcePosition pos;
    public AggregateType aggregate;
    public Field index;
    public NameAndType name_and_type;
    public int offset;
    public boolean is_weak;
    public boolean const_qualified;
    public boolean generate_verify;

    public Field(SourcePosition pos, AggregateType aggregate, Field index,
                 NameAndType name_and_type, int offset, boolean is_weak,
                 boolean const_qualified, boolean generate_verify) {
        this.pos = pos;
        this.aggregate = aggregate;
        this.index = index;
        this.name_and_type = name_and_type;
        this.offset = offset;
        this.is_weak = is_weak;
        this.const_qualified = const_qualified;
        this.generate_verify = generate_verify;
    }

    public Tuple<Integer, String> getFieldSizeInformation() {
        String size_string = "#no size";
        Type field_type = this.name_and_type.type;
        int field_size = 0;
        if (field_type.isSubtypeOf(TypeOracle.getTaggedType())) {
            field_size = TargetArchitecture.taggedSize();
            size_string = "kTaggedSize";
        } else if (field_type.isSubtypeOf(TypeOracle.getRawPtrType())) {
            field_size = TargetArchitecture.rawPtrSize();
            size_string = "kSystemPointerSize";
        } else if (field_type.isSubtypeOf(TypeOracle.getVoidType())) {
            field_size = 0;
            size_string = "0";
        } else if (field_type.isSubtypeOf(TypeOracle.getInt8Type())) {
            field_size = org.jsengine.v8.Internal.kUInt8Size;
            size_string = "kUInt8Size";
        } else if (field_type.isSubtypeOf(TypeOracle.getUint8Type())) {
            field_size = org.jsengine.v8.Internal.kUInt8Size;
            size_string = "kUInt8Size";
        } else if (field_type.isSubtypeOf(TypeOracle.getInt16Type())) {
            field_size = org.jsengine.v8.Internal.kUInt16Size;
            size_string = "kUInt16Size";
        } else if (field_type.isSubtypeOf(TypeOracle.getUint16Type())) {
            field_size = org.jsengine.v8.Internal.kUInt16Size;
            size_string = "kUInt16Size";
        } else if (field_type.isSubtypeOf(TypeOracle.getInt32Type())) {
            field_size = org.jsengine.v8.Internal.kInt32Size;
            size_string = "kInt32Size";
        } else if (field_type.isSubtypeOf(TypeOracle.getUint32Type())) {
            field_size = org.jsengine.v8.Internal.kInt32Size;
            size_string = "kInt32Size";
        } else if (field_type.isSubtypeOf(TypeOracle.getFloat64Type())) {
            field_size = org.jsengine.v8.Internal.kDoubleSize;
            size_string = "kDoubleSize";
        } else if (field_type.isSubtypeOf(TypeOracle.getIntPtrType())) {
            field_size = TargetArchitecture.rawPtrSize();
            size_string = "kIntptrSize";
        } else if (field_type.isSubtypeOf(TypeOracle.getUIntPtrType())) {
            field_size = TargetArchitecture.rawPtrSize();
            size_string = "kIntptrSize";
        } else {
            Torque.reportError("fields of type ", field_type, " are not (yet) supported");
        }
        return new Tuple<Integer, String>(field_size, size_string);
    }
}
