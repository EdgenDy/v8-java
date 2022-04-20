package org.jsengine.v8.internal;

import org.jsengine.v8.Internal;

public class ArrayNoArgumentConstructorDescriptor extends ArrayNArgumentsConstructorDescriptor {
	public static int kDescriptorFlags = CallInterfaceDescriptorData.Flag.kNoFlags.ordinal();
	public static int kReturnCount = 1;
	
	public static enum ParameterIndices {
		__dummy(-1), 
		kFunction(0), 
		kAllocationSite(1),
		kActualArgumentsCount(2),
		kFunctionParameter(3),

		kParameterCount(4),
		kContext(4); // = kParameterCount

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
	
	public ArrayNoArgumentConstructorDescriptor() {
		super(key());
	}
	
	public static CallDescriptors.Key key() {
		return CallDescriptors.Key.ArrayNoArgumentConstructor;
	}
	
	protected void initializePlatformSpecific(CallInterfaceDescriptorData data) {
		super.initializePlatformSpecific(data);
	}
	
	protected ArrayNoArgumentConstructorDescriptor(CallDescriptors.Key key) {
		super(key);
	}
};