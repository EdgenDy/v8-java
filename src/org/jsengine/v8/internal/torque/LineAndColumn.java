package org.jsengine.v8.internal.torque; 

public class LineAndColumn {
	public int line;
	public int column;
	
	public LineAndColumn(int line, int column) {
		this.line = line;
		this.column = column;
	}

	public static LineAndColumn invalid() {
		return new LineAndColumn(-1, -1);
	}

	public boolean equals(LineAndColumn other) {
		return line == other.line && column == other.column;
	}
};