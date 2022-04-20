package org.jsengine.v8.internal;

import org.jsengine.v8.Internal;

class InterpreterPushArgsThenCallDescriptor extends CallInterfaceDescriptor {
	public static int kDescriptorFlags = CallInterfaceDescriptorData.Flag.kNoFlags.ordinal();
	public static int kReturnCount = 1;
	
	public static enum ParameterIndices {
		__dummy(-1), 
		kNumberOfArguments(0),
		kFirstArgument(1),
		kFunction(2),

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
		MachineType machine_types[] = { MachineType.anyTagged(), MachineType.int32(), MachineType.pointer(), MachineType.anyTagged() };
    	data.initializePlatformIndependent(new CallInterfaceDescriptorData.Flags(kDescriptorFlags), kReturnCount, ParameterIndices.kParameterCount.value(), machine_types, machine_types.length);
	}
	
	public InterpreterPushArgsThenCallDescriptor() {
		super(key());
	}
	
	public static CallDescriptors.Key key() {
		return CallDescriptors.Key.InterpreterPushArgsThenCall;
	}
	
	protected void initializePlatformSpecific(CallInterfaceDescriptorData data) {
		Register registers[] = { Internal.rax, Internal.rbx, Internal.rdi };
		data.initializePlatformSpecific(registers.length, registers);
	}
	
	protected InterpreterPushArgsThenCallDescriptor(CallDescriptors.Key key) {
		super(key);
	}
};