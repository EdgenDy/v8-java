package org.jsengine.v8.internal;

import org.jsengine.v8.Internal;

class InterpreterCEntry2Descriptor extends CallInterfaceDescriptor {
	public static int kDescriptorFlags = CallInterfaceDescriptorData.Flag.kNoFlags.value();
	public static int kReturnCount = 2;

	public static enum ParameterIndices {
		__dummy(-1), 
		kNumberOfArguments(0),
		kFirstArgument(1),
		kFunctionEntry(2),
		
		kParameterCount(3),
		kContext(3); // = kParameterCount
		
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
		MachineType machine_types[] = { MachineType.anyTagged(), MachineType.anyTagged(), MachineType.int32(), MachineType.pointer(), MachineType.pointer() };
		data.initializePlatformIndependent(new CallInterfaceDescriptorData.Flags(kDescriptorFlags), kReturnCount, ParameterIndices.kParameterCount.value(), machine_types, machine_types.length);
	}
	
	public InterpreterCEntry2Descriptor() {
		super(key());
	}
	
	public static CallDescriptors.Key key() {
		return CallDescriptors.Key.InterpreterCEntry2;
	}
	
	protected void initializePlatformSpecific(CallInterfaceDescriptorData data) {
		Internal.interpreterCEntryDescriptor_InitializePlatformSpecific(data);
	}
	
	protected InterpreterCEntry2Descriptor(CallDescriptors.Key key) {
		super(key);
	}
};