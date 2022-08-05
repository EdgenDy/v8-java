package org.jsengine.v8.internal.torque;

public class MessageBuilder {
	private TorqueMessage message_;
	
	public MessageBuilder(String message, TorqueMessage.Kind kind) {
		SourcePosition position = null;
		if (CurrentSourcePosition.hasScope()) {
			position = CurrentSourcePosition.get();
		}
		message_ = new TorqueMessage(message, position, kind);
	}

	public MessageBuilder position(SourcePosition position) {
		message_.position = position;
		return this;
	}
	
	public void Throw() {
		throw new TorqueAbortCompilation();
	}
}