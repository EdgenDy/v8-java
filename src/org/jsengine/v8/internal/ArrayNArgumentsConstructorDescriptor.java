package org.jsengine.v8.internal;

import org.jsengine.v8.Internal;

public class ArrayNArgumentsConstructorDescriptor extends CallInterfaceDescriptor {
	public static int kDescriptorFlags = CallInterfaceDescriptorData.Flag.kNoFlags.ordinal();
	public static int kReturnCount = 1;
	
	public static enum ParameterIndices {
		__dummy(-1), 
		kFunction(0), 
		kAllocationSite(1), 
		kActualArgumentsCount(2),

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
		MachineType machine_types[] = { MachineType.anyTagged(), MachineType.anyTagged(), MachineType.anyTagged(), MachineType.int32() };
    	data.initializePlatformIndependent(new CallInterfaceDescriptorData.Flags(kDescriptorFlags), kReturnCount, ParameterIndices.kParameterCount.value(), machine_types, machine_types.length);
	}
	
	public ArrayNArgumentsConstructorDescriptor() {
		super(key());
	}
	
	public static CallDescriptors.Key key() {
		return CallDescriptors.Key.ArrayNArgumentsConstructor;
	}
	
	protected void initializePlatformSpecific(CallInterfaceDescriptorData data) {
		Register registers[] = { Internal.kJavaScriptCallTargetRegister, Internal.kJavaScriptCallExtraArg1Register, Internal.kJavaScriptCallArgCountRegister };
		data.initializePlatformSpecific(registers.length, registers);
	}
	
	protected ArrayNArgumentsConstructorDescriptor(CallDescriptors.Key key) {
		super(key);
	}
};