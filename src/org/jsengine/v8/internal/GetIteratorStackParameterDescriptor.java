package org.jsengine.v8.internal;

import org.jsengine.v8.Internal;

public class GetIteratorStackParameterDescriptor extends CallInterfaceDescriptor {
	public static int kDescriptorFlags = CallInterfaceDescriptorData.Flag.kNoFlags.ordinal();
	public static int kReturnCount = 1;
	
	public static enum ParameterIndices {
		__dummy(-1), 
		kReceiver(0),
		kCallSlot(1),
		kFeedback(2), 
		kResult(3),

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
		MachineType machine_types[] = { MachineType.anyTagged(), MachineType.anyTagged(), MachineType.anyTagged(), MachineType.anyTagged(), MachineType.anyTagged() };
    	data.initializePlatformIndependent(new CallInterfaceDescriptorData.Flags(kDescriptorFlags), kReturnCount, ParameterIndices.kParameterCount.value(), machine_types, machine_types.length);
	}
	
	public GetIteratorStackParameterDescriptor() {
		super(key());
	}
	
	public static CallDescriptors.Key key() {
		return CallDescriptors.Key.GetIteratorStackParameter;
	}
	
	protected void initializePlatformSpecific(CallInterfaceDescriptorData data) {
		data.initializePlatformSpecific(0, null);
	}
	
	protected GetIteratorStackParameterDescriptor(CallDescriptors.Key key) {
		super(key);
	}
};