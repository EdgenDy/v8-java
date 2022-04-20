package org.jsengine.v8.internal;

import org.jsengine.v8.Internal;

public class ConstructWithArrayLikeDescriptor extends CallInterfaceDescriptor {
	public static int kDescriptorFlags = CallInterfaceDescriptorData.Flag.kNoFlags.ordinal();
	public static int kReturnCount = 1;
	
	public static enum ParameterIndices {
		__dummy(-1), 
		kTarget(0), 
		kNewTarget(1),
		kArgumentsList(2),

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
		MachineType machine_types[] = { MachineType.anyTagged(), MachineType.anyTagged(), MachineType.anyTagged(), MachineType.anyTagged() };
    	data.initializePlatformIndependent(new CallInterfaceDescriptorData.Flags(kDescriptorFlags), kReturnCount, ParameterIndices.kParameterCount.value(), machine_types, machine_types.length);
	}
	
	public ConstructWithArrayLikeDescriptor() {
		super(key());
	}
	
	public static CallDescriptors.Key key() {
		return CallDescriptors.Key.ConstructWithArrayLike;
	}
	
	protected void initializePlatformSpecific(CallInterfaceDescriptorData data) {
		Register registers[] = { Internal.rdi, Internal.rdx,Internal.rbx };
		data.initializePlatformSpecific(registers.length, registers);
	}
	
	protected ConstructWithArrayLikeDescriptor(CallDescriptors.Key key) {
		super(key);
	}
};