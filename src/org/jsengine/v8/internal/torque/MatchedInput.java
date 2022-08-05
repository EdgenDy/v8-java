package org.jsengine.v8.internal.torque;

public class MatchedInput {
	public InputPosition begin;
	public InputPosition end;
	public SourcePosition pos;
	
	public MatchedInput(InputPosition begin, InputPosition end, SourcePosition pos) {
		this.begin = begin;
		this.end = end;
		this.pos = pos;
	}
	
	public String toString() {
		return new String(begin.toString() + end.toString());
	}
};