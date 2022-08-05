package org.jsengine.v8.internal.torque; 

public class ParseResultHolder<T> extends ParseResultHolderBase {
	private TypeId id = TypeId.kStdString;
	public T value_;
	
	public ParseResultHolder(T value) {
		super(TypeId.kStdString);
		value_ = value;
	}
}