package org.jsengine.v8.internal.torque; 

import org.jsengine.v8.base.Optional;

public class TorqueMessage {
	public static enum Kind { kError, kLint };
	public String message;
	public SourcePosition position;
	public Kind kind;
	
	public TorqueMessage(String message, SourcePosition position, Kind kind) {
		this.message = message;
		this.position = position;
		this.kind = kind;
	}
};