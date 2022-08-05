package org.jsengine.v8.internal.torque; 

public class ParseResult<T> {
	public ParseResultHolderBase value_;
	
	public ParseResult(T x) {
		value_ = new ParseResultHolder<T>(x);
	}
	
	public <T> T cast(Class<T> type) {
		return value_.cast(type);
	}
}