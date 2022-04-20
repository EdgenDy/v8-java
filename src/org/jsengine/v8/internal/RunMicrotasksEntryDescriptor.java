package org.jsengine.v8.internal;

import org.jsengine.v8.Internal;

public class RunMicrotasksEntryDescriptor extends CallInterfaceDescriptor {
	public static int kDescriptorFlags = CallInterfaceDescriptorData.Flag.kNoContext.value() | CallInterfaceDescriptorData.Flag.kNoStackScan.value();
	public static int kReturnCount = 1;
	public static enum ParameterIndices {
		__dummy(-1), 
		kRootRegisterValue(0),
		kMicrotaskQueue(1),

		kParameterCount(2);
		
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
		MachineType machine_types[] = { MachineType.anyTagged(), MachineType.pointer(), MachineType.pointer() };
    	data.initializePlatformIndependent(new CallInterfaceDescriptorData.Flags(kDescriptorFlags), kReturnCount, ParameterIndices.kParameterCount.value(), machine_types, machine_types.length);
	}
	
	public RunMicrotasksEntryDescriptor() {
		super(key());
	}
	
	public static CallDescriptors.Key key() {
		return CallDescriptors.Key.RunMicrotasksEntry;
	}
	
	protected void initializePlatformSpecific(CallInterfaceDescriptorData data) {
		Register registers[] = { Internal.arg_reg_1, Internal.arg_reg_2};
		data.initializePlatformSpecific(registers.length, registers);
	}
	
	protected RunMicrotasksEntryDescriptor(CallDescriptors.Key key) {
		super(key);
	}
};