package org.jsengine.v8.internal;

import org.jsengine.v8.Internal;

public class StoreGlobalWithVectorDescriptor extends StoreGlobalDescriptor {
	public static int kDescriptorFlags = CallInterfaceDescriptorData.Flag.kNoFlags.ordinal();
	public static int kReturnCount = 1;
	
	public static enum ParameterIndices {
		__dummy(-1), 
		kName(0),
		kValue(1),
		kSlot(2),
		kVector(3),

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
		MachineType machine_types[] = { MachineType.anyTagged(), MachineType.anyTagged(), MachineType.anyTagged(), MachineType.taggedSigned(), MachineType.anyTagged() };
    	data.initializePlatformIndependent(new CallInterfaceDescriptorData.Flags(kDescriptorFlags), kReturnCount, ParameterIndices.kParameterCount.value(), machine_types, machine_types.length);
	}
	
	public StoreGlobalWithVectorDescriptor() {
		super(key());
	}
	
	public static CallDescriptors.Key key() {
		return CallDescriptors.Key.StoreGlobalWithVector;
	}
	
	protected void initializePlatformSpecific(CallInterfaceDescriptorData data) {
		Register registers[] = { nameRegister(), valueRegister(), slotRegister(), vectorRegister() };
		int len = registers.length - kStackArgumentsCount;
		data.initializePlatformSpecific(len, registers);
	}
	
	protected StoreGlobalWithVectorDescriptor(CallDescriptors.Key key) {
		super(key);
	}

	public static Register vectorRegister() {
		return StoreWithVectorDescriptor.vectorRegister();
	}
	
	public static int kStackArgumentsCount = kPassLastArgsOnStack ? 3 : 0;
};