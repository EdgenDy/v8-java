package org.jsengine.v8.internal.torque; 

public class LineAndColumnTracker {
	public LineAndColumn previous = new LineAndColumn(0, 0);
	public LineAndColumn current = new LineAndColumn(0, 0);

	public void advance(InputPosition from, InputPosition to) {
		previous = current;
		while (from.notEquals(to)) {
			if (from.deref() == '\n') {
				current.line += 1;
				current.column = 0;
			}
			else {
				current.column += 1;
			}
			from.incLeft();
		}
	}

	public SourcePosition toSourcePosition() {
		return new SourcePosition(CurrentSourceFile.get(), previous, current);
	}
};