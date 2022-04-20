package org.jsengine.v8.internal;

import org.jsengine.v8.Internal;

public class LoadGlobalDescriptor extends CallInterfaceDescriptor {
	public static int kDescriptorFlags = CallInterfaceDescriptorData.Flag.kNoFlags.ordinal();
	public static int kReturnCount = 1;
	
	public static enum ParameterIndices {
		__dummy(-1), 
		kName(0),
		kSlot(1),

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
		MachineType machine_types[] = { MachineType.anyTagged(), MachineType.anyTagged(), MachineType.taggedSigned() };
    	data.initializePlatformIndependent(new CallInterfaceDescriptorData.Flags(kDescriptorFlags), kReturnCount, ParameterIndices.kParameterCount.value(), machine_types, machine_types.length);
	}
	
	public LoadGlobalDescriptor() {
		super(key());
	}
	
	public static CallDescriptors.Key key() {
		return CallDescriptors.Key.LoadGlobal;
	}
	
	protected void initializePlatformSpecific(CallInterfaceDescriptorData data) {
		Register registers[] = { nameRegister(), slotRegister() };
		data.initializePlatformSpecific(registers.length, registers);
	}
	
	protected LoadGlobalDescriptor(CallDescriptors.Key key) {
		super(key);
	}

	public static Register nameRegister() {
		return LoadDescriptor.nameRegister();
	}

	public static Register slotRegister() {
		return LoadDescriptor.slotRegister();
	}
};