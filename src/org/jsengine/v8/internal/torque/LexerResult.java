package org.jsengine.v8.internal.torque; 

import java.util.Vector;

public class LexerResult {
	public Vector<Symbol> token_symbols = new Vector<Symbol>();
	public Vector<MatchedInput> token_contents = new Vector<MatchedInput>();
};