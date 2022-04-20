package org.jsengine.v8.internal;

import org.jsengine.v8.Internal;

class CEntry1ArgvOnStackDescriptor extends CallInterfaceDescriptor {
	public static int kDescriptorFlags = CallInterfaceDescriptorData.Flag.kNoFlags.ordinal();
	public static int kReturnCount = 1;
	
	public static enum ParameterIndices {
		__dummy(-1), 
		kArity(0),
		kCFunction(1),
		kPadding(2),
		kArgcSmi(3),
		kTargetCopy(4),
		kNewTargetCopy(5),

		kParameterCount(6),
		kContext(6); // = kParameterCount

		int value_;
		ParameterIndices(int value) {
			value_ = value;
		}
		
		public int value() {
			return value_;
		}
	};
	
	@Override
	public void initializePlatformIndependent(CallInterfaceDescriptorData data) {
		MachineType machine_types[] = { MachineType.anyTagged(), MachineType.int32(), MachineType.pointer(), MachineType.anyTagged(), MachineType.anyTagged(), MachineType.anyTagged(), MachineType.anyTagged() };
    	data.initializePlatformIndependent(new CallInterfaceDescriptorData.Flags(kDescriptorFlags), kReturnCount, ParameterIndices.kParameterCount.value(), machine_types, machine_types.length);
	}
	
	public CEntry1ArgvOnStackDescriptor() {
		super(key());
	}
	
	public static CallDescriptors.Key key() {
		return CallDescriptors.Key.CEntry1ArgvOnStack;
	}
	
	protected void initializePlatformSpecific(CallInterfaceDescriptorData data) {
		
	}
	
	protected CEntry1ArgvOnStackDescriptor(CallDescriptors.Key key) {
		super(key);
	}
};