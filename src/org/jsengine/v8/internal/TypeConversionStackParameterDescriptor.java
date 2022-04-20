package org.jsengine.v8.internal;

import org.jsengine.v8.Internal;

public class TypeConversionStackParameterDescriptor extends CallInterfaceDescriptor {
	public static int kDescriptorFlags = CallInterfaceDescriptorData.Flag.kNoFlags.ordinal();
	public static int kReturnCount = 1;
	
	public static enum ParameterIndices {
		__dummy(-1), 
		kArgument(0),

		kParameterCount(1),
		kContext(1); // = kParameterCount

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
		MachineType machine_types[] = { MachineType.anyTagged(), MachineType.anyTagged() };
    	data.initializePlatformIndependent(new CallInterfaceDescriptorData.Flags(kDescriptorFlags), kReturnCount, ParameterIndices.kParameterCount.value(), machine_types, machine_types.length);
	}
	
	public TypeConversionStackParameterDescriptor() {
		super(key());
	}
	
	public static CallDescriptors.Key key() {
		return CallDescriptors.Key.TypeConversionStackParameter;
	}
	
	protected void initializePlatformSpecific(CallInterfaceDescriptorData data) {
		data.initializePlatformSpecific(0, null);
	}
	
	protected TypeConversionStackParameterDescriptor(CallDescriptors.Key key) {
		super(key);
	}
};