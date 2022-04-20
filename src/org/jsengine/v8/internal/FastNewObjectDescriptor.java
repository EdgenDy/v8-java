package org.jsengine.v8.internal;

import org.jsengine.v8.Internal;

public class FastNewObjectDescriptor extends CallInterfaceDescriptor {
	public static int kDescriptorFlags = CallInterfaceDescriptorData.Flag.kNoFlags.ordinal();
	public static int kReturnCount = 1;
	
	public static enum ParameterIndices {
		__dummy(-1), 
		kTarget(0),
		kNewTarget(1),

		kParameterCount(2),
		kContext(2); // = kParameterCount

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
		MachineType machine_types[] = { MachineType.anyTagged(), MachineType.anyTagged(), MachineType.anyTagged() };
    	data.initializePlatformIndependent(new CallInterfaceDescriptorData.Flags(kDescriptorFlags), kReturnCount, ParameterIndices.kParameterCount.value(), machine_types, machine_types.length);
	}
	
	public FastNewObjectDescriptor() {
		super(key());
	}
	
	public static CallDescriptors.Key key() {
		return CallDescriptors.Key.FastNewObject;
	}
	
	protected void initializePlatformSpecific(CallInterfaceDescriptorData data) {
		Register registers[] = { targetRegister(), newTargetRegister() };
		data.initializePlatformSpecific(registers.length, registers);
	}
	
	protected FastNewObjectDescriptor(CallDescriptors.Key key) {
		super(key);
	}
	
	public static Register targetRegister() {
		return Internal.kJSFunctionRegister;
	}
	
	public static Register newTargetRegister() {
		return Internal.kJavaScriptCallNewTargetRegister;
	}
};