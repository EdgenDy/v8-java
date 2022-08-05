package org.jsengine.v8.internal.torque; 

import java.util.Vector;
import java.util.Iterator;

public class ParseResultIterator {
	private Vector<ParseResult> results_;
	private int i_ = 0;
	private MatchedInput matched_input_;
	private Iterator<ParseResult> iterator;
	
	public ParseResultIterator(Vector<ParseResult> results, MatchedInput matched_input) {
		results_ = results;
		matched_input_ = matched_input;
		iterator = results.iterator();
	}
	
	public ParseResult next() {
		return iterator.next();
	} 
	
	public <T> T nextAs(Class<T> type) {
		return (T) next().cast(type);
	}
	
	public boolean hasNext() {
		return iterator.hasNext();
	}
	
	public MatchedInput matched_input() {
		return matched_input_;
	}
}