package org.jsengine.v8.internal;

import org.jsengine.v8.Internal;

public class LoadDescriptor extends CallInterfaceDescriptor {
	public static int kDescriptorFlags = CallInterfaceDescriptorData.Flag.kNoFlags.ordinal();
	public static int kReturnCount = 1;
	
	public static enum ParameterIndices {
		__dummy(-1),
		kReceiver(0),
		kName(1),
		kSlot(2),
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
    	MachineType machine_types[] = {MachineType.anyTagged(), MachineType.anyTagged(), MachineType.anyTagged(), MachineType.taggedSigned()};
    	data.initializePlatformIndependent(new CallInterfaceDescriptorData.Flags(kDescriptorFlags),
			kReturnCount, ParameterIndices.kParameterCount.value(), machine_types, machine_types.length);
	}
	
	public LoadDescriptor() {
		super(key());
	}
	
	public static CallDescriptors.Key key() {
		return CallDescriptors.Key.Load;
	}
	
	@Override
	protected void initializePlatformSpecific(CallInterfaceDescriptorData data) {
		Register registers[] = {receiverRegister(), nameRegister(), slotRegister()};
		data.initializePlatformSpecific(registers.length, registers);
	}
	
	protected LoadDescriptor(CallDescriptors.Key key) {
		super(key);
	}
	
	public static Register receiverRegister() {
		return Internal.rdx;
	}
	
	public static Register nameRegister() {
		return Internal.rcx;
	}
	
	public static Register slotRegister() {
		return Internal.rax;
	}
}
