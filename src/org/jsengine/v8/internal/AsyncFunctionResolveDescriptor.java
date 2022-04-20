package org.jsengine.v8.internal;

import org.jsengine.v8.Internal;

public class AsyncFunctionResolveDescriptor extends CallInterfaceDescriptor {
	public static int kDescriptorFlags = CallInterfaceDescriptorData.Flag.kNoFlags.ordinal();
	public static int kReturnCount = 1;
	
	public static enum ParameterIndices {
		__dummy(-1), 
		kAsyncFunctionObject(0),
		kValue(1),
		kCanSuspend(2),

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
	
	public AsyncFunctionResolveDescriptor() {
		super(key()); 
	}
	
	public static CallDescriptors.Key key() {
		return CallDescriptors.Key.AsyncFunctionResolve;
	}
    
	protected static int kRegisterParams = 
      ParameterIndices.kParameterCount.value() > Internal.kMaxTFSBuiltinRegisterParams
          ? Internal.kMaxTFSBuiltinRegisterParams 
          : ParameterIndices.kParameterCount.value(); 
	protected static int kStackParams = ParameterIndices.kParameterCount.value() - kRegisterParams;
	
	@Override
	protected void initializePlatformSpecific(CallInterfaceDescriptorData data) {
		defaultInitializePlatformSpecific(data, kRegisterParams);
	}
	
	@Override
	protected void initializePlatformIndependent(CallInterfaceDescriptorData data) {
		data.initializePlatformIndependent(new CallInterfaceDescriptorData.Flags(kDescriptorFlags), kReturnCount, ParameterIndices.kParameterCount.value(), null, 0);
	}

	protected AsyncFunctionResolveDescriptor(CallDescriptors.Key key) {
		super(key);
	}
};