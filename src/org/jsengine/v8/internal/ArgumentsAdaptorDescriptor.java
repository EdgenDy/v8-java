package org.jsengine.v8.internal;

import org.jsengine.v8.Internal;

public class ArgumentsAdaptorDescriptor extends CallInterfaceDescriptor {
	public static int kDescriptorFlags = CallInterfaceDescriptorData.Flag.kNoFlags.ordinal();
	public static int kReturnCount = 1;

	public static enum ParameterIndices {
		kTarget(0),
		kNewTarget(1),
		kActualArgumentsCount(2),
		kExpectedArgumentsCount(3),
		kParameterCount(4),
		kContext(4); //,= kParameterCount ;
		
		int value_;
		ParameterIndices(int value) {
			value_ = value;
		}
		
		public int value() {
			return value_;
		}
	};
  
	public ArgumentsAdaptorDescriptor() {
  	super(key());
	}
  
	@Override
	public void initializePlatformIndependent(CallInterfaceDescriptorData data) {
    	MachineType machine_types[] = {MachineType.anyTagged(), MachineType.anyTagged(), MachineType.anyTagged(), MachineType.int32(), MachineType.int32()};
    	data.initializePlatformIndependent(new CallInterfaceDescriptorData.Flags(kDescriptorFlags),
			kReturnCount, ParameterIndices.kParameterCount.value(), machine_types, machine_types.length);
	}
	
	@Override
	public void initializePlatformSpecific(CallInterfaceDescriptorData data) {
		Register registers[] = { Internal.rdi, Internal.rdx, Internal.rax, Internal.rbx };
		data.initializePlatformSpecific(registers.length, registers);
	}
	
	public static CallDescriptors.Key key() {
		return CallDescriptors.Key.ArgumentsAdaptor;
	}
}