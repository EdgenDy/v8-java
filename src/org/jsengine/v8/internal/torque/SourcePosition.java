package org.jsengine.v8.internal.torque; 

public class SourcePosition {
	public SourceId source = null;
	public LineAndColumn start = null;
	public LineAndColumn end = null;
	
	public SourcePosition(SourceId source, LineAndColumn start, LineAndColumn end) {
		this.source = source;
		this.start = start;
		this.end = end;
	}
	
	public static SourcePosition invalid() {
		return new SourcePosition(SourceId.invalid(), LineAndColumn.invalid(),
			LineAndColumn.invalid());
	}

	public boolean compareStartIgnoreColumn(SourcePosition pos) {
		return start.line == pos.start.line && source == pos.source;
	}
}