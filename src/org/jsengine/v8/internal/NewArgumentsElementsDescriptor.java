package org.jsengine.v8.internal;

import org.jsengine.v8.Internal;

public class NewArgumentsElementsDescriptor extends CallInterfaceDescriptor {
	public static int kDescriptorFlags = CallInterfaceDescriptorData.Flag.kNoFlags.ordinal();
	public static int kReturnCount = 1;
	
	public static enum ParameterIndices {
		__dummy(-1), 
		kFrame(0),
		kLength(1),
		kMappedCount(2),

		kParameterCount(3),
		kContext(3); // = kParameterCount

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
		MachineType machine_types[] = { MachineType.anyTagged(), MachineType.pointer(), MachineType.taggedSigned(), MachineType.taggedSigned() };
    	data.initializePlatformIndependent(new CallInterfaceDescriptorData.Flags(kDescriptorFlags), kReturnCount, ParameterIndices.kParameterCount.value(), machine_types, machine_types.length);
	}
	
	public NewArgumentsElementsDescriptor() {
		super(key());
	}
	
	public static CallDescriptors.Key key() {
		return CallDescriptors.Key.NewArgumentsElements;
	}
	
	protected void initializePlatformSpecific(CallInterfaceDescriptorData data) {
		defaultInitializePlatformSpecific(data, ParameterIndices.kParameterCount.value());
	}
	
	protected NewArgumentsElementsDescriptor(CallDescriptors.Key key) {
		super(key);
	}
};