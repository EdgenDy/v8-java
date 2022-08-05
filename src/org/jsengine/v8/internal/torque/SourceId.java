package org.jsengine.v8.internal.torque;

public class SourceId {
	public int id_ = 0;
	
	public SourceId(int id) {
		id_ = id;
	}
	
	public boolean equals(SourceId s) {
		return id_ == s.id_;
	}
	
	public static SourceId invalid() {
		return new SourceId(-1);
	}
	
	public boolean isValid() {
		return id_ != -1;
	}
}