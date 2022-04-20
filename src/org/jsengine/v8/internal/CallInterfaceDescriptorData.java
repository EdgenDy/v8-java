package org.jsengine.v8.internal;

import org.jsengine.v8.Internal;

public class CallInterfaceDescriptorData {
	private int register_param_count_ = -1;
	private int return_count_ = -1;
	private int param_count_ = -1;
	private RegList allocatable_registers_ = new RegList(0);
	private Flags flags_ = new Flags(Flag.kNoFlags.ordinal());
	
	private Register register_params_[] = null;
	private MachineType machine_types_[] = null;
	
	public static enum Flag {
		kNoFlags(0),
		kNoContext(1 << 0),
		kNoStackScan(1 << 1);
		
		int value_;
		Flag(int value) {
			value_ = value;
		}
		
		public int value() {
			return value_;
		}
	};
	
	public static class Flags extends org.jsengine.v8.base.Flags {
		public Flags(int flag) {
			super(flag);
		}
	}
	
	public void initializePlatformSpecific(int register_parameter_count, Register[] registers) {
		register_param_count_ = register_parameter_count;
		
		if (register_parameter_count == 0) return;
		
		try {
			register_params_ = new Register[register_parameter_count];
		} catch(Exception e) { }
		
		if(register_params_ == null) {
			V8.getCurrentPlatform().onCriticalMemoryPressure();
			try {
				register_params_ = new Register[register_parameter_count];
			} catch(Exception e) { }
			
			if(register_params_ == null)
				Internal.fatalProcessOutOfMemory(null, "NewArray");
		}
		
		Internal.newArray(register_params_, Internal.no_reg);
		
		for(int i = 0; i < register_parameter_count; i++) {
			register_params_[i] = registers[i];
		}
	}
	
	public void initializePlatformIndependent(Flags flags, int return_count, int parameter_count, MachineType machine_types[], int machine_types_length) {
    	flags_ = flags;
		return_count_ = return_count;
		param_count_ = parameter_count;
		int types_length = return_count_ + param_count_;
		
		if (machine_types == null) {
			machine_types_ = new MachineType[types_length];
			Internal.newArray(machine_types_, MachineType.anyTagged());
		}
		else {
			machine_types_ = new MachineType[types_length];
			Internal.newArray(machine_types_);
			for (int i = 0; i < types_length; i++) machine_types_[i] = machine_types[i];
		}
    }
    
	public void restrictAllocatableRegisters(Register[] registers, int num) {
		for (int i = 0; i < num; ++i) {
		  allocatable_registers_.value |= registers[i].bit().value;
		}
	}
    
    public int register_param_count() {
		return register_param_count_;
	}
	
	public Register register_param(int index) { 
		return register_params_[index];
	}
}