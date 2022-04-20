package org.jsengine.v8.internal;

import org.jsengine.v8.Internal;

public class StoreTransitionDescriptor extends StoreDescriptor {
	public static int kDescriptorFlags = CallInterfaceDescriptorData.Flag.kNoFlags.ordinal();
	public static int kReturnCount = 1;
	
	public static enum ParameterIndices {
		__dummy(-1), 
		kReceiver(0),
		kName(1),
		kMap(2),
		kValue(3),
		kSlot(4),
		kVector(5),

		kParameterCount(6),
		kContext(6); // = kParameterCount

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
		MachineType machine_types[] = { MachineType.anyTagged(), MachineType.anyTagged(), MachineType.anyTagged(), MachineType.anyTagged(), MachineType.anyTagged(), MachineType.taggedSigned(), MachineType.anyTagged() };
    	data.initializePlatformIndependent(new CallInterfaceDescriptorData.Flags(kDescriptorFlags), kReturnCount, ParameterIndices.kParameterCount.value(), machine_types, machine_types.length);
	}
	
	public StoreTransitionDescriptor() {
		super(key());
	}
	
	public static CallDescriptors.Key key() {
		return CallDescriptors.Key.StoreTransition;
	}
	
	protected void initializePlatformSpecific(CallInterfaceDescriptorData data) {
		Register registers[] = { receiverRegister(), nameRegister(), mapRegister(), valueRegister(), slotRegister(), vectorRegister() };
		int len = registers.length - kStackArgumentsCount;
		data.initializePlatformSpecific(len, registers);
	}
	
	protected StoreTransitionDescriptor(CallDescriptors.Key key) {
		super(key);
	}

	public static Register mapRegister() {
		return Internal.r11;
	}
	
	public static Register slotRegister() {
		return Internal.rdi;
	}
	
	public static Register vectorRegister() {
		return Internal.rbx;
	}
	
	public static int kStackArgumentsCount = kPassLastArgsOnStack ? 3 : 0;
};