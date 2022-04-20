package org.jsengine.v8.internal;

import org.jsengine.v8.Internal;

public class AllocateDescriptor extends CallInterfaceDescriptor {
	public static int kDescriptorFlags = CallInterfaceDescriptorData.Flag.kNoContext.ordinal();
	public static int kReturnCount = 1;
	
	public AllocateDescriptor() {
		super(key());
	}
	
	public static enum ParameterIndices {
		__dummy(-1),
		kRequestedSize(0),
		kParameterCount(1);

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
    	MachineType machine_types[] = {MachineType.anyTagged(), MachineType.taggedPointer(), MachineType.intPtr()};
    	data.initializePlatformIndependent(new CallInterfaceDescriptorData.Flags(kDescriptorFlags),
			kReturnCount, ParameterIndices.kParameterCount.value(), machine_types, machine_types.length);
	}
	
	@Override
	public void initializePlatformSpecific(CallInterfaceDescriptorData data) {
		Register registers[] = {Internal.kAllocateSizeRegister};
		data.initializePlatformSpecific(registers.length, registers);
	}
	
	public static CallDescriptors.Key key() {
		return CallDescriptors.Key.Allocate;
	}
}