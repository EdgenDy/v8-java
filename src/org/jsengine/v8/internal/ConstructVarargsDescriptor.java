package org.jsengine.v8.internal;

import org.jsengine.v8.Internal;

class ConstructVarargsDescriptor extends CallInterfaceDescriptor {
	public static int kDescriptorFlags = CallInterfaceDescriptorData.Flag.kNoFlags.ordinal();
	public static int kReturnCount = 1;

	public static enum ParameterIndices {
		kTarget(0),
		kNewTarget(1),
		kActualArgumentsCount(2),
		
		kArgumentsLength(3), 
		kArgumentsList(4),
		
		kParameterCount(5),
		kContext(5); //,= kParameterCount ;
		
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
		MachineType machine_types[] = { MachineType.anyTagged(), MachineType.anyTagged(), MachineType.anyTagged(), MachineType.int32(), MachineType.int32(), MachineType.anyTagged() };
    	data.initializePlatformIndependent(new CallInterfaceDescriptorData.Flags(kDescriptorFlags), kReturnCount, ParameterIndices.kParameterCount.value(), machine_types, machine_types.length);
	}
	
	public ConstructVarargsDescriptor() {
		super(key());
	}
	
	public static CallDescriptors.Key key() {
		return CallDescriptors.Key.ConstructVarargs;
	}
	
	protected void initializePlatformSpecific(CallInterfaceDescriptorData data) {
		Register registers[] = { Internal.rdi, Internal.rdx, Internal.rax, Internal.rcx, Internal.rbx};
		data.initializePlatformSpecific(registers.length, registers);
	}
	
	protected ConstructVarargsDescriptor(CallDescriptors.Key key) {
		super(key);
	}
};