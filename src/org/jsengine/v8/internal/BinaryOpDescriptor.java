package org.jsengine.v8.internal;

import org.jsengine.v8.Internal;

public class BinaryOpDescriptor extends CallInterfaceDescriptor {
	public static int kDescriptorFlags = CallInterfaceDescriptorData.Flag.kNoFlags.ordinal();
	public static int kReturnCount = 1;
	
	public static enum ParameterIndices {
		__dummy(-1), 
		kLeft(0),
		kRight(1),

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
	
	public BinaryOpDescriptor() {
		super(key());
	}
	
	public static CallDescriptors.Key key() {
		return CallDescriptors.Key.BinaryOp;
	}
	
	protected void initializePlatformSpecific(CallInterfaceDescriptorData data) {
		Register registers[] = { Internal.rdx, Internal.rax };
		data.initializePlatformSpecific(registers.length, registers);
	}
	
	protected BinaryOpDescriptor(CallDescriptors.Key key) {
		super(key);
	}
};