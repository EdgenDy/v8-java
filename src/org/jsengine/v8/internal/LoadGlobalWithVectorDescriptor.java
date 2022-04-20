package org.jsengine.v8.internal;

import org.jsengine.v8.Internal;

public class LoadGlobalWithVectorDescriptor extends LoadGlobalDescriptor {
	public static int kDescriptorFlags = CallInterfaceDescriptorData.Flag.kNoFlags.ordinal();
	public static int kReturnCount = 1;
	
	public static enum ParameterIndices {
		__dummy(-1), 
		kName(0),
		kSlot(1),
		kVector(2),

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

	public static Register vectorRegister() {
		return LoadWithVectorDescriptor.vectorRegister();
	}
	
	@Override
	public void initializePlatformIndependent(CallInterfaceDescriptorData data) {
		MachineType machine_types[] = { MachineType.anyTagged(), MachineType.anyTagged(), MachineType.taggedSigned(), MachineType.anyTagged() };
    	data.initializePlatformIndependent(new CallInterfaceDescriptorData.Flags(kDescriptorFlags), kReturnCount, ParameterIndices.kParameterCount.value(), machine_types, machine_types.length);
	}
	
	public LoadGlobalWithVectorDescriptor() {
		super(key());
	}
	
	public static CallDescriptors.Key key() {
		return CallDescriptors.Key.LoadGlobalWithVector;
	}
	
	protected void initializePlatformSpecific(CallInterfaceDescriptorData data) {
		Register registers[] = { nameRegister(), slotRegister(), vectorRegister() };
		data.initializePlatformSpecific(registers.length, registers);
	}
	
	protected LoadGlobalWithVectorDescriptor(CallDescriptors.Key key) {
		super(key);
	}
};