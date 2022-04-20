package org.jsengine.v8.internal;

import org.jsengine.v8.Internal;

public class AbortDescriptor extends CallInterfaceDescriptor {
	public static int kDescriptorFlags = CallInterfaceDescriptorData.Flag.kNoContext.ordinal();
	public static int kReturnCount = 1;
	
	public AbortDescriptor() {
		super(key());
	}
	
	public static enum ParameterIndices {
		__dummy(-1),
		kMessageOrMessageId(0),
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
	protected void initializePlatformSpecific(CallInterfaceDescriptorData data) {
		Register registers[] = {Internal.rdx};
		data.initializePlatformSpecific(registers.length, registers);
	}
	
	@Override
	public void initializePlatformIndependent(CallInterfaceDescriptorData data) {
    	MachineType machine_types[] = {MachineType.anyTagged(), MachineType.anyTagged()};
    	data.initializePlatformIndependent(new CallInterfaceDescriptorData.Flags(kDescriptorFlags),
			kReturnCount, ParameterIndices.kParameterCount.value(), machine_types, machine_types.length);
	}
	
	public static CallDescriptors.Key key() {
		return CallDescriptors.Key.Abort;
	}
}