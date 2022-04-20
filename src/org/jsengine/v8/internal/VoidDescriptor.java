package org.jsengine.v8.internal;

import org.jsengine.v8.Internal;

public class VoidDescriptor extends CallInterfaceDescriptor {
	public static int kDescriptorFlags = CallInterfaceDescriptorData.Flag.kNoFlags.ordinal();
	public static int kReturnCount = 1;
	
	public static enum ParameterIndices {
		__dummy(-1),

		kParameterCount(0),
		kContext(0); // = kParameterCount

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
		MachineType machine_types[] = { MachineType.anyTagged() };
    	data.initializePlatformIndependent(new CallInterfaceDescriptorData.Flags(kDescriptorFlags), kReturnCount, ParameterIndices.kParameterCount.value(), machine_types, machine_types.length);
	}
	
	public VoidDescriptor() {
		super(key());
	}
	
	public static CallDescriptors.Key key() {
		return CallDescriptors.Key.Void;
	}
	
	protected void initializePlatformSpecific(CallInterfaceDescriptorData data) {
		data.initializePlatformSpecific(0, null);
	}
	
	protected VoidDescriptor(CallDescriptors.Key key) {
		super(key);
	}
};