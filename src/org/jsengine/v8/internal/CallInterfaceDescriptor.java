package org.jsengine.v8.internal;

import org.jsengine.v8.Internal;

public abstract class CallInterfaceDescriptor {
	private CallInterfaceDescriptorData data_;
	
	public CallInterfaceDescriptor(CallDescriptors.Key key) {
		data_ = CallDescriptors.call_descriptor_data(key);
	}
	
	protected void initializePlatformSpecific(CallInterfaceDescriptorData data) {
		throw new RuntimeException("UNREACHABLE");
	}
	
	protected void initializePlatformIndependent(CallInterfaceDescriptorData data) {
		data.initializePlatformIndependent(new CallInterfaceDescriptorData.Flags(CallInterfaceDescriptorData.Flag.kNoFlags.ordinal()), 1, data.register_param_count(), null, 0);
	}
  
	public void initialize(CallInterfaceDescriptorData data) {
		initializePlatformSpecific(data);
		initializePlatformIndependent(data);
	}
	
	protected static void jSDefaultInitializePlatformSpecific(CallInterfaceDescriptorData data, int non_js_register_parameter_count) {
		int register_parameter_count = 3 + non_js_register_parameter_count;
		Register default_js_stub_registers[] = { Internal.kJavaScriptCallTargetRegister, Internal.kJavaScriptCallNewTargetRegister,
			Internal.kJavaScriptCallArgCountRegister, Internal.kJavaScriptCallExtraArg1Register };
		
		data.initializePlatformSpecific(register_parameter_count,
                                   default_js_stub_registers);
	}
	
	protected static void defaultInitializePlatformSpecific(CallInterfaceDescriptorData data, int register_parameter_count) {
		Register default_stub_registers[] = { Internal.rax, Internal.rbx, Internal.rcx, Internal.rdx, Internal.rdi};
		data.initializePlatformSpecific(register_parameter_count, default_stub_registers);
	}
}