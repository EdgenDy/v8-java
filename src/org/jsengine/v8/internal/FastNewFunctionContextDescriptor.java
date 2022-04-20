package org.jsengine.v8.internal;

import org.jsengine.v8.Internal;

public class FastNewFunctionContextDescriptor extends CallInterfaceDescriptor {
	public static int kDescriptorFlags = CallInterfaceDescriptorData.Flag.kNoFlags.ordinal();
	public static int kReturnCount = 1;
	
	public static enum ParameterIndices {
		__dummy(-1), 
		kScopeInfo(0),
		kSlots(1),

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
		MachineType machine_types[] = { MachineType.anyTagged(), MachineType.anyTagged(), MachineType.int32() };
    	data.initializePlatformIndependent(new CallInterfaceDescriptorData.Flags(kDescriptorFlags), kReturnCount, ParameterIndices.kParameterCount.value(), machine_types, machine_types.length);
	}
	
	public FastNewFunctionContextDescriptor() {
		super(key());
	}
	
	public static CallDescriptors.Key key() {
		return CallDescriptors.Key.FastNewFunctionContext;
	}
	
	protected void initializePlatformSpecific(CallInterfaceDescriptorData data) {
		Register registers[] = { scopeInfoRegister(), slotsRegister() };
		data.initializePlatformSpecific(registers.length, registers);
	}
	
	protected FastNewFunctionContextDescriptor(CallDescriptors.Key key) {
		super(key);
	}
	
	public static Register scopeInfoRegister() {
		return Internal.rdi;
	}
	
	public static Register slotsRegister() {
		return Internal.rax;
	}
};

  