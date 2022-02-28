package org.jsengine.v8.internal;

import org.jsengine.utils.Var;

public class Flag {
	private FlagType type_;
	private String name_;
	private Object valptr_;
	private Object defptr_;
	private String cmt_;
	private boolean owns_ptr_;   
	
	public Flag(FlagType type, String name, Object valptr, Object defptr, String cmt, boolean owns_ptr) {
		type_ = type;
		name_ = name;
		valptr_ = valptr;
		defptr_ = defptr;
		cmt_ = cmt;
		owns_ptr_ = owns_ptr;
	}
	
	public static enum FlagType {
	    TYPE_BOOL,
	    TYPE_MAYBE_BOOL,
	    TYPE_INT,
	    TYPE_UINT,
	    TYPE_UINT64,
	    TYPE_FLOAT,
	    TYPE_SIZE_T,
	    TYPE_STRING,
	}

	public FlagType type() {
		return type_;
	}

	public String name() {
		return name_;
	}

	public String comment() { 
		return cmt_;
	}

	public Var<Boolean> bool_variable() {
		return new Var<Boolean>(valptr_);
	}
	
	public Var<MaybeBoolFlag> maybe_bool_variable() {
		return new Var<MaybeBoolFlag>(valptr_);
	}

	public Var<Integer> int_variable() {
		return new Var<Integer>(valptr_);
	}

	public Var<Integer> uint_variable() {
    	return new Var<Integer>(valptr_);
	}

	Var<Integer> uint64_variable() {
		return new Var<Integer>(valptr_);
	}

	public Var<Double> float_variable() {
    	return new Var<Double>(valptr_);
	}

	public Var<Integer> size_t_variable() {
		return new Var<Integer>(valptr_);
	}

	public String string_value() {
		return (String) ((Var<?>) valptr_).getValue();
	}
	
	@SuppressWarnings("unchecked")
	public void set_string_value(String value, boolean owns_ptr) {
		if (owns_ptr_ && ((Var<?>) valptr_).getValue() != null)
			((Var<?>) valptr_).setValue(null);
		((Var<Object>) valptr_).setValue(value);
		owns_ptr_ = owns_ptr;
	}
	
	@SuppressWarnings("unchecked")
	public boolean bool_default() {
		return (boolean) ((Var<Boolean>) defptr_).getValue();
	}

	@SuppressWarnings("unchecked")
	public int int_default() {
    	return (int) ((Var<Integer>) defptr_).getValue();
	}

	@SuppressWarnings("unchecked")
	public int uint_default() {
    	return (int) ((Var<Integer>) defptr_).getValue();
	}

	@SuppressWarnings("unchecked")
	public int uint64_default() {
		return (int) ((Var<Integer>) defptr_).getValue();
	}

	@SuppressWarnings("unchecked")
	public double float_default() {
		return (double) ((Var<Double>) defptr_).getValue();
	}

	@SuppressWarnings("unchecked")
	public int size_t_default() {
		return (int) ((Var<Integer>) defptr_).getValue();
	}

	@SuppressWarnings("unchecked")
	public String string_default() {
		return (String) ((Var<String>) defptr_).getValue();
	}
	
	public boolean isDefault() {
		switch (type_) {
			case TYPE_BOOL:
				return bool_variable().getValue() == bool_default();
			case TYPE_MAYBE_BOOL:
				return maybe_bool_variable().getValue().has_value == false;
			case TYPE_INT:
				return int_variable().getValue() == int_default();
			case TYPE_UINT:
				return uint_variable().getValue() == uint_default();
			case TYPE_UINT64:
				return uint64_variable().getValue() == uint64_default();
			case TYPE_FLOAT:
				return float_variable().getValue() == float_default();
			case TYPE_SIZE_T:
				return size_t_variable().getValue() == size_t_default();
			case TYPE_STRING: {
				String str1 = string_value();
				String str2 = string_default();
				if (str2 == null) return str1 == null;
				if (str1 == null) return str2 == null;
				return str1 == str2;
			}
		}
		throw new RuntimeException("UNREACHABLE");
	}

	public void reset() {
		switch (type_) {
			case TYPE_BOOL:
				bool_variable().setValue(bool_default());
				break;
			case TYPE_MAYBE_BOOL:
				maybe_bool_variable().setValue(MaybeBoolFlag.create(false, false));
				break;
			case TYPE_INT:
				int_variable().setValue(int_default());
				break;
			case TYPE_UINT:
				uint_variable().setValue(uint_default());
				break;
			case TYPE_UINT64:
				uint64_variable().setValue(uint64_default());
				break;
			case TYPE_FLOAT:
				float_variable().setValue(float_default());
				break;
			case TYPE_SIZE_T:
				size_t_variable().setValue(size_t_default());
				break;
			case TYPE_STRING:
				set_string_value(string_default(), false);
				break;
		}
	}
}