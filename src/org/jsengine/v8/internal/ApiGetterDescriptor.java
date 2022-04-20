package org.jsengine.v8.internal;

import org.jsengine.v8.Internal;

public class ApiGetterDescriptor extends CallInterfaceDescriptor {
	public static int kDescriptorFlags = CallInterfaceDescriptorData.Flag.kNoFlags.ordinal();
	public static int kReturnCount = 1;
	
	public ApiGetterDescriptor() {
		super(key());
	}
	
	public static enum ParameterIndices {
		__dummy(-1),
		kReceiver(0),
		kHolder(1),
		kCallback(2),
		kParameterCount(3),
		
		kContext(3); // = kParameterCount;

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
    	MachineType machine_types[] = {MachineType.anyTagged(), MachineType.anyTagged(), MachineType.anyTagged(), MachineType.anyTagged()};
    	data.initializePlatformIndependent(new CallInterfaceDescriptorData.Flags(kDescriptorFlags),
			kReturnCount, ParameterIndices.kParameterCount.value(), machine_types, machine_types.length);
	}
	
	@Override
	public void initializePlatformSpecific(CallInterfaceDescriptorData data) {
		Register registers[] = {receiverRegister(), holderRegister(), callbackRegister()};
		data.initializePlatformSpecific(registers.length, registers);
	}
	
	public static CallDescriptors.Key key() {
		return CallDescriptors.Key.ApiGetter;
	}
	
	public static Register receiverRegister() {
		return LoadDescriptor.receiverRegister();
	}
	
	public static Register holderRegister() {
		return Internal.rcx;
	}
	
	public static Register callbackRegister() {
		return Internal.rbx;
	}
}
