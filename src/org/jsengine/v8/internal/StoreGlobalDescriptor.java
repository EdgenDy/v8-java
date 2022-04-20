package org.jsengine.v8.internal;

import org.jsengine.v8.Internal;

public class StoreGlobalDescriptor extends CallInterfaceDescriptor {
	public static int kDescriptorFlags = CallInterfaceDescriptorData.Flag.kNoFlags.ordinal();
	public static int kReturnCount = 1;
	
	public static enum ParameterIndices {
		__dummy(-1), 
		kName(0),
		kValue(1),
		kSlot(2),

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
	
	@Override
	public void initializePlatformIndependent(CallInterfaceDescriptorData data) {
		MachineType machine_types[] = { MachineType.anyTagged(), MachineType.anyTagged(), MachineType.anyTagged(), MachineType.taggedSigned() };
    	data.initializePlatformIndependent(new CallInterfaceDescriptorData.Flags(kDescriptorFlags), kReturnCount, ParameterIndices.kParameterCount.value(), machine_types, machine_types.length);
	}
	
	public StoreGlobalDescriptor() {
		super(key());
	}
	
	public static CallDescriptors.Key key() {
		return CallDescriptors.Key.StoreGlobal;
	}
	
	protected void initializePlatformSpecific(CallInterfaceDescriptorData data) {
		Register registers[] = { nameRegister(), valueRegister(), slotRegister() };
		int len = registers.length - kStackArgumentsCount;
		data.initializePlatformSpecific(len, registers);
	}
	
	protected StoreGlobalDescriptor(CallDescriptors.Key key) {
		super(key);
	}

	public static boolean kPassLastArgsOnStack = StoreDescriptor.kPassLastArgsOnStack;
	
	public static int kStackArgumentsCount = kPassLastArgsOnStack ? 2 : 0;

	public static Register nameRegister() {
		return StoreDescriptor.nameRegister();
	}

	public static Register valueRegister() {
    	return StoreDescriptor.valueRegister();
	}

	public static Register slotRegister() {
		return StoreDescriptor.slotRegister();
	}
};