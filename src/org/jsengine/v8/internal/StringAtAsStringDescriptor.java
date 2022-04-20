package org.jsengine.v8.internal;

import org.jsengine.v8.Internal;

public class StringAtAsStringDescriptor extends CallInterfaceDescriptor {
	public static int kDescriptorFlags = CallInterfaceDescriptorData.Flag.kNoFlags.ordinal();
	public static int kReturnCount = 1;
	
	public static enum ParameterIndices {
		__dummy(-1), 
		kReceiver(0),
		kPosition(1),

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
		MachineType machine_types[] = { MachineType.taggedPointer(), MachineType.anyTagged(), MachineType.intPtr() };
		data.initializePlatformIndependent(new CallInterfaceDescriptorData.Flags(kDescriptorFlags), kReturnCount, ParameterIndices.kParameterCount.value(), machine_types, machine_types.length);
	}
	
	public StringAtAsStringDescriptor() {
		super(key());
	}
	
	public static CallDescriptors.Key key() {
		return CallDescriptors.Key.StringAtAsString;
	}
	
	protected void initializePlatformSpecific(CallInterfaceDescriptorData data) {
		
	}
	
	protected StringAtAsStringDescriptor(CallDescriptors.Key key) {
		super(key);
	}
};