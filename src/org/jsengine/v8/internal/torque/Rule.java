package org.jsengine.v8.internal.torque; 

import java.util.Vector;

public class Rule {
	private Symbol left_hand_side_ = null;
	private Vector<Symbol> right_hand_side_;
	private Action action_;
	
	public Rule(Vector<Symbol> right_hand_side, Action action) {
		right_hand_side_ = right_hand_side;
		action_ = action;
	}
	
	public Rule(Vector<Symbol> right_hand_side) {
		this(right_hand_side, org.jsengine.v8.internal.Torque.defaultAction);
	}
	
	public Rule(Symbol[] right_hand_side, Action action) {
		this(new Vector<Symbol>(), action);
		for (Symbol symbol : right_hand_side)
			right_hand_side_.add(symbol);
	}
	
	public Rule(Symbol[] right_hand_side) {
		this(right_hand_side, org.jsengine.v8.internal.Torque.defaultAction);
	}
	
	public Vector<Symbol> right() { 
		return right_hand_side_;
	}
	
	public Symbol left() {
		return left_hand_side_;
	}
	
	public ParseResult runAction(Item completed_item, LexerResult tokens) {
		Vector<ParseResult> results = new Vector<ParseResult>();
		for (Item child : completed_item.children()) {
			if (child == null) continue;
			ParseResult child_result = child.left().runAction(child, tokens);
			if (child_result != null) results.add(child_result);
		}
		MatchedInput matched_input = completed_item.getMatchedInput(tokens);
		CurrentSourcePosition.Scope pos_scope = new CurrentSourcePosition.Scope(matched_input.pos);
		ParseResultIterator iterator = new ParseResultIterator(results, matched_input);
		return action_.apply(iterator);
	}
}