package org.jsengine.v8.internal;

import org.jsengine.Globals;

public class MachineType {
	private MachineRepresentation representation_;
	private MachineSemantic semantic_;
	
	public static enum MachineRepresentation {
		kNone(0),
		kBit(1),
		kWord8(2),
		kWord16(3),
		kWord32(4),
		kWord64(5),
		kTaggedSigned(6),
		kTaggedPointer(7),
		kTagged(8),
		kCompressedSigned(9),
		kCompressedPointer(10),
		kCompressed(11),
  
		kFloat32(12),
		kFloat64(13),
		kSimd128(14),
		kFirstFPRepresentation(12),
		kLastRepresentation(14);
		
		private int value_ = 0;
		MachineRepresentation(int value) {
			value_ = value;
		}
		
		public int value() {
			return value_;
		}
	};
	
	public static enum MachineSemantic {
		kNone,
		kBool,
		kInt32,
		kUint32,
		kInt64,
		kUint64,
		kNumber,
		kAny
	};
	
	public MachineType(MachineRepresentation representation, MachineSemantic semantic) {
		representation_ = representation;
		semantic_ = semantic;
	}
	
	public static MachineRepresentation pointerRepresentation() {
		return (Globals.kSystemPointerSize == 4) ? MachineRepresentation.kWord32
                                     : MachineRepresentation.kWord64;
	}
	
	public static MachineType anyTagged() {
		return new MachineType(MachineRepresentation.kTagged, MachineSemantic.kAny);
	}
	
	public static MachineType taggedPointer() {
		return new MachineType(MachineRepresentation.kTaggedPointer, MachineSemantic.kAny);
	}
	
	public static MachineType intPtr() {
		return (Globals.kSystemPointerSize == 4) ? int32() : int64();
	}
	
	public static MachineType int32() {
		return new MachineType(MachineRepresentation.kWord32, MachineSemantic.kInt32);
	}
	
	public static MachineType int64() {
		return new MachineType(MachineRepresentation.kWord64, MachineSemantic.kInt64);
	}
	
	public static MachineType pointer() {
		return new MachineType(pointerRepresentation(), MachineSemantic.kNone);
	}
	
	public static MachineType taggedSigned() {
		return new MachineType(MachineRepresentation.kTaggedSigned, MachineSemantic.kInt32);
	}
	
	public static MachineType uint32() {
		return new MachineType(MachineRepresentation.kWord32, MachineSemantic.kUint32);
	}
	
	public static MachineType float64() {
		return new MachineType(MachineRepresentation.kFloat64, MachineSemantic.kNumber);
	}
}