package org.jsengine.v8.internal;

import org.jsengine.v8.Internal;

class InterpreterPushArgsThenConstructDescriptor extends CallInterfaceDescriptor {
	public static int kDescriptorFlags = CallInterfaceDescriptorData.Flag.kNoFlags.ordinal();
	public static int kReturnCount = 1;
	
	public static enum ParameterIndices {
		__dummy(-1), 
		kNumberOfArguments(0),
		kFirstArgument(1),
		kConstructor(2),
		kNewTarget(3),
		kFeedbackElement(4),

		kParameterCount(5),
		kContext(5); // = kParameterCount

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
		MachineType machine_types[] = { MachineType.anyTagged(), MachineType.int32(), MachineType.pointer(), MachineType.anyTagged(), MachineType.anyTagged(), MachineType.anyTagged() };
    	data.initializePlatformIndependent(new CallInterfaceDescriptorData.Flags(kDescriptorFlags), kReturnCount, ParameterIndices.kParameterCount.value(), machine_types, machine_types.length);
	}
	
	public InterpreterPushArgsThenConstructDescriptor() {
		super(key());
	}
	
	public static CallDescriptors.Key key() {
		return CallDescriptors.Key.InterpreterPushArgsThenConstruct;
	}
	
	protected void initializePlatformSpecific(CallInterfaceDescriptorData data) {
		Register registers[] = { Internal.rax, Internal.rcx, Internal.rdi, Internal.rdx, Internal.rbx };
		data.initializePlatformSpecific(registers.length, registers);
	}
	
	protected InterpreterPushArgsThenConstructDescriptor(CallDescriptors.Key key) {
		super(key);
	}

	public static boolean kPassLastArgsOnStack = false;
	public static int kStackArgumentsCount = kPassLastArgsOnStack ? 3 : 0;
};