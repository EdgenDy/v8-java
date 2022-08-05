package org.jsengine.v8.internal.torque;

import java.util.Vector;
import java.util.Collections;

public class Item {
	private Rule rule_;
	private int mark_;
	private int start_;
	private int pos_;
	
	private Item prev_ = null;
	private Item child_ = null;
	
	public Item(Rule rule, int mark, int start, int pos) {
		rule_ = rule;
		mark_ = mark;
		start_ = start;
		pos_ = pos;
	}
	
	public Item advance(int new_pos) {
		return advance(new_pos, null);
	}
	
	public Item advance(int new_pos, Item child) {
		Item result = new Item(rule_, mark_ + 1, start_, new_pos);
		result.prev_ = this;
		result.child_ = child;
    	return result;
	}
	
	public boolean isComplete() {
		return mark_ == right().size();
	}
	
	public Symbol nextSymbol() {
		return right().get(mark_);
	}
	
	public MatchedInput getMatchedInput(LexerResult tokens) {
		MatchedInput start = tokens.token_contents.get(start_);
		MatchedInput end = start_ == pos_ ? tokens.token_contents.get(start_) : tokens.token_contents.get(pos_ - 1);
		
		SourcePosition combined = new SourcePosition(start.pos.source, start.pos.start, end.pos.end);
		return new MatchedInput(start.begin, end.end, combined);
	}
	
	public Vector<Item> children() {
		Vector<Item> children = new Vector<Item>();
		
		for (Item current = this; current.prev_ != null; current = current.prev_) {
			children.add(current.child_);
		}
		Collections.reverse(children);
		return children;
	}
	
	public String splitByChildren(LexerResult tokens) {
		if (right().size() == 1) {
			Item child = null;
			if ((child = children().get(0)) != null)
				return child.splitByChildren(tokens);
		}
		
		StringBuffer s = new StringBuffer();
		boolean first = true;
		
		for (Item item : children()) {
			if (item == null) continue;
			if (!first) s.append("  ");
			s.append(item.getMatchedInput(tokens).toString());
			first = false;
		}
		return s.toString();
	}
	
	public void checkAmbiguity(Item other, LexerResult tokens) {
		if (child_ != other.child_) {
			StringBuffer s = new StringBuffer();
			s.append("Ambiguous grammer rules for \"");
			s.append(child_.getMatchedInput(tokens).toString() + "\":\n   ");
			s.append(child_.splitByChildren(tokens) + "\nvs\n   ");
			s.append(other.child_.splitByChildren(tokens));
			
			System.err.print(s.toString());
		}
		
		if (prev_ != other.prev_) {
			StringBuffer s = new StringBuffer();
			s.append("Ambiguous grammer rules for \"" + getMatchedInput(tokens).toString());
			s.append("\":\n   " + splitByChildren(tokens) + "  ...\nvs\n   ");
			s.append(other.splitByChildren(tokens) + "  ...");
		}
	}
	
	public Vector<Symbol> right() { 
		return rule_.right();
	}
	
	public int pos() {
		return pos_;
	}
	
	public Symbol left() {
		return rule_.left();
	}
	
	public int start() {
		return start_;
	}
	
	public Rule rule() {
		return rule_;
	}
	
	@Override
	public boolean equals(Object object) {
		Item other = (Item) object;
		return rule_ == other.rule_ && mark_ == other.mark_ && 
			start_ == other.start_ && pos_ == other.pos_;
	}
}