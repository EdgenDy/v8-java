package org.jsengine.v8.internal;

import org.jsengine.v8.Internal;

public class ArrayConstructorDescriptor extends CallInterfaceDescriptor {
	public static int kDescriptorFlags = CallInterfaceDescriptorData.Flag.kNoFlags.ordinal();
	public static int kReturnCount = 1;

	public static enum ParameterIndices {
		kTarget(0),
		kNewTarget(1),
		kActualArgumentsCount(2),
		
		kAllocationSite(3),
		
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
	
	@Override
	public void initializePlatformIndependent(CallInterfaceDescriptorData data) {
		MachineType machine_types[] = { MachineType.anyTagged(), MachineType.anyTagged(), MachineType.anyTagged(), MachineType.int32(), MachineType.anyTagged() };
    	data.initializePlatformIndependent(new CallInterfaceDescriptorData.Flags(kDescriptorFlags), kReturnCount, ParameterIndices.kParameterCount.value(), machine_types, machine_types.length);
	}
	
	public ArrayConstructorDescriptor() {
		super(key());
	}
	
	public static CallDescriptors.Key key() {
		return CallDescriptors.Key.ArrayConstructor;
	}
	
	@Override
	public void initializePlatformSpecific(CallInterfaceDescriptorData data) {
		jSDefaultInitializePlatformSpecific(data, 1);
	}
	
	protected ArrayConstructorDescriptor(CallDescriptors.Key key) {
		super(key);
	}
}