package org.jsengine.v8.internal;

import org.jsengine.v8.Internal;

public class StoreDescriptor extends CallInterfaceDescriptor {
	public static int kDescriptorFlags = CallInterfaceDescriptorData.Flag.kNoFlags.ordinal();
	public static int kReturnCount = 1;
	
	public static enum ParameterIndices {
		__dummy(-1), 
		kReceiver(0),
		kName(1),
		kValue(2),
		kSlot(3),

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
		MachineType machine_types[] = { MachineType.anyTagged(), MachineType.anyTagged(), MachineType.anyTagged(), MachineType.anyTagged(), MachineType.taggedSigned() };
    	data.initializePlatformIndependent(new CallInterfaceDescriptorData.Flags(kDescriptorFlags), kReturnCount, ParameterIndices.kParameterCount.value(), machine_types, machine_types.length);
	}
	
	public StoreDescriptor() {
		super(key());
	}
	
	public static CallDescriptors.Key key() {
		return CallDescriptors.Key.Store;
	}
	
	protected void initializePlatformSpecific(CallInterfaceDescriptorData data) {
		Register registers[] = { receiverRegister(), nameRegister(), valueRegister(), slotRegister() };
		int len = registers.length - kStackArgumentsCount;
		data.initializePlatformSpecific(len, registers);
	}
	
	protected StoreDescriptor(CallDescriptors.Key key) {
		super(key);
	}

	public static Register receiverRegister() {
		return Internal.rdx;
	}
	
	public static Register nameRegister() {
		return Internal.rcx;
	}
	
	public static Register valueRegister() {
		return Internal.rax;
	}
	
	public static Register slotRegister() {
		return Internal.rdi;
	}

	public static boolean kPassLastArgsOnStack = false;
	public static int kStackArgumentsCount = kPassLastArgsOnStack ? 2 : 0;
};