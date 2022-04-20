package org.jsengine.v8.internal;

import org.jsengine.v8.Internal;

public class I64ToBigIntDescriptor extends CallInterfaceDescriptor {
	public static int kDescriptorFlags = CallInterfaceDescriptorData.Flag.kNoContext.value();
	public static int kReturnCount = 1;
	
	public static enum ParameterIndices {
		__dummy(-1), 
		kArgument(0),

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
		MachineType machine_types[] = { MachineType.anyTagged(), MachineType.int64() };
    	data.initializePlatformIndependent(new CallInterfaceDescriptorData.Flags(kDescriptorFlags), kReturnCount, ParameterIndices.kParameterCount.value(), machine_types, machine_types.length);
	}
	
	public I64ToBigIntDescriptor() {
		super(key());
	}
	
	public static CallDescriptors.Key key() {
		return CallDescriptors.Key.I64ToBigInt;
	}
	
	protected void initializePlatformSpecific(CallInterfaceDescriptorData data) {
		defaultInitializePlatformSpecific(data, ParameterIndices.kParameterCount.value());
	}
	
	protected I64ToBigIntDescriptor(CallDescriptors.Key key) {
		super(key);
	}
};