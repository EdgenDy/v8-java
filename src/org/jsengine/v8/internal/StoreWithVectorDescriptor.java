package org.jsengine.v8.internal;

import org.jsengine.v8.Internal;

public class StoreWithVectorDescriptor extends StoreDescriptor {
	public static int kDescriptorFlags = CallInterfaceDescriptorData.Flag.kNoFlags.ordinal();
	public static int kReturnCount = 1;
	
	public static enum ParameterIndices {
		__dummy(-1), 
		kReceiver(0),
		kName(1),
		kValue(2),
		kSlot(3),
		kVector(4),

		kParameterCount(5),
		kContext(5); // = kParameterCount

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
		MachineType machine_types[] = { MachineType.anyTagged(), MachineType.anyTagged(), MachineType.anyTagged(), MachineType.anyTagged(), MachineType.taggedSigned(), MachineType.anyTagged() };
    	data.initializePlatformIndependent(new CallInterfaceDescriptorData.Flags(kDescriptorFlags), kReturnCount, ParameterIndices.kParameterCount.value(), machine_types, machine_types.length);
	}
	
	public StoreWithVectorDescriptor() {
		super(key());
	}
	
	public static CallDescriptors.Key key() {
		return CallDescriptors.Key.StoreWithVector;
	}
	
	protected void initializePlatformSpecific(CallInterfaceDescriptorData data) {
		Register registers[] = { receiverRegister(), nameRegister(), valueRegister(), slotRegister(), vectorRegister() };
 
		int len = registers.length - kStackArgumentsCount;
		data.initializePlatformSpecific(len, registers);
	}
	
	protected StoreWithVectorDescriptor(CallDescriptors.Key key) {
		super(key);
	}

	public static Register vectorRegister() {
		return Internal.rbx;
	}

	public static int kStackArgumentsCount = kPassLastArgsOnStack ? 3 : 0;
};