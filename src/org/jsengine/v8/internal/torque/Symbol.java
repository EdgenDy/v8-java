package org.jsengine.v8.internal.torque; 

import java.util.List;
import java.util.ArrayList;

public class Symbol {
	public List<Rule> rules_ = new ArrayList<Rule>();
	public Symbol(List<Rule> rules) {
		set(rules);
	}
	
	public Symbol() {
		this(new ArrayList<Rule>());
	}
	
	public Symbol set(List<Rule> rules) {
		rules_.clear();
		 
		for (Rule rule : rules) {
			addRule(rule);
		}
		return this;
	}
	
	public boolean isTerminal() {
		return rules_.size() == 0;
	}
	
	public Rule rule(int index) {
		return rules_.get(index);
	}
	
	public int rule_number() {
		return rules_.size();
	}
	
	public ParseResult runAction(Item item, LexerResult tokens) {
		return item.rule().runAction(item, tokens);
	}
	
	public void addRule(Rule rule) {
		rules_.add(rule);
		//rules_.back()->SetLeftHandSide(this);
	}
	
	public void addRule(Rule[] rules) {
		for (Rule rule : rules)
			addRule(rule);
	}
}