package org.jsengine.v8.internal;

import org.jsengine.v8.Internal;

public class ApiCallbackDescriptor extends CallInterfaceDescriptor {
	public static int kDescriptorFlags = CallInterfaceDescriptorData.Flag.kNoFlags.ordinal();
	public static int kReturnCount = 1;
	
	public ApiCallbackDescriptor() {
		super(key());
	}
	
	public static enum ParameterIndices {
		__dummy(-1),
		kApiFunctionAddress(0), 
		kActualArgumentsCount(1),
		kCallData(2),
		kHolder(3),
		kParameterCount(4),
		
		kContext(4); // = kParameterCount;

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
    	MachineType machine_types[] = {MachineType.anyTagged(), MachineType.pointer(), MachineType.intPtr(), MachineType.anyTagged(), MachineType.anyTagged()};
    	data.initializePlatformIndependent(new CallInterfaceDescriptorData.Flags(kDescriptorFlags),
			kReturnCount, ParameterIndices.kParameterCount.value(), machine_types, machine_types.length);
	}
	
	@Override
	public void initializePlatformSpecific(CallInterfaceDescriptorData data) {
		Register registers[] = {Internal.rdx, Internal.rcx, Internal.rbx, Internal.rdi};
		data.initializePlatformSpecific(registers.length, registers);
	}
	
	public static CallDescriptors.Key key() {
		return CallDescriptors.Key.ApiCallback;
	}
}