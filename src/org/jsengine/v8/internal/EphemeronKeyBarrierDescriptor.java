package org.jsengine.v8.internal;

import org.jsengine.v8.Internal;

public class EphemeronKeyBarrierDescriptor extends CallInterfaceDescriptor {
	public static int kDescriptorFlags = CallInterfaceDescriptorData.Flag.kNoContext.value();
	public static int kReturnCount = 1;
	
	public static enum ParameterIndices {
		__dummy(-1), 
		kObject(0),
		kSlotAddress(1),
		kFPMode(2),

		kParameterCount(3);
		
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
		MachineType machine_types[] = { MachineType.anyTagged(), MachineType.taggedPointer(), MachineType.pointer(), MachineType.taggedSigned() };
    	data.initializePlatformIndependent(new CallInterfaceDescriptorData.Flags(kDescriptorFlags), kReturnCount, ParameterIndices.kParameterCount.value(), machine_types, machine_types.length);
	}
	
	public EphemeronKeyBarrierDescriptor() {
		super(key());
	}
	
	public static CallDescriptors.Key key() {
		return CallDescriptors.Key.EphemeronKeyBarrier;
	}
	
	protected void initializePlatformSpecific(CallInterfaceDescriptorData data) {
		Register default_stub_registers[] = { Internal.arg_reg_1, Internal.arg_reg_2, Internal.arg_reg_3, Internal.arg_reg_4, Internal.kReturnRegister0};
		data.restrictAllocatableRegisters(default_stub_registers, default_stub_registers.length);
		data.initializePlatformSpecific(ParameterIndices.kParameterCount.value(), default_stub_registers);
	}
	
	protected EphemeronKeyBarrierDescriptor(CallDescriptors.Key key) {
		super(key);
	}
};