package org.jsengine.v8.internal;

import org.jsengine.v8.Internal;

class GrowArrayElementsDescriptor extends CallInterfaceDescriptor {
	public static int kDescriptorFlags = CallInterfaceDescriptorData.Flag.kNoFlags.ordinal();
	public static int kReturnCount = 1;
	
	public static enum ParameterIndices {
		__dummy(-1), 
		kObject(0),
		kKey(1),

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
		MachineType machine_types[] = { MachineType.anyTagged(), MachineType.anyTagged(), MachineType.anyTagged() };
    	data.initializePlatformIndependent(new CallInterfaceDescriptorData.Flags(kDescriptorFlags), kReturnCount, ParameterIndices.kParameterCount.value(), machine_types, machine_types.length);
	}
	
	public GrowArrayElementsDescriptor() {
		super(key());
	}
	
	public static CallDescriptors.Key key() {
		return CallDescriptors.Key.GrowArrayElements;
	}
	
	protected void initializePlatformSpecific(CallInterfaceDescriptorData data) {
		Register registers[] = { objectRegister(), keyRegister() };
		data.initializePlatformSpecific(registers.length, registers);
	}
	
	protected GrowArrayElementsDescriptor(CallDescriptors.Key key) {
		super(key);
	}
	
	public static Register objectRegister() {
		return Internal.rax;
	}
	
	public static Register keyRegister() {
		return Internal.rbx;
	}
};