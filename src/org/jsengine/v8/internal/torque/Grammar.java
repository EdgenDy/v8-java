package org.jsengine.v8.internal.torque; 

import org.jsengine.v8.internal.Torque;
import org.jsengine.utils.ConstCharPtr;

import java.util.function.Function;

public class Grammar {
	protected Symbol start_;
	private Lexer lexer_ = new Lexer();
	
	public Grammar(Symbol start) {
		start_ = start;
	}
	
	public ParseResult parse(String input) {
		LexerResult tokens = lexer().runLexer(input);
		return Torque.parseTokens(start_, tokens);
	}
	
	protected Symbol token(String s) {
		return lexer_.token(s);
	}
	
	protected Symbol pattern(PatternFunction pattern) {
		return lexer_.pattern(pattern);
	}
	
	protected Lexer lexer() {
		return lexer_;
	}
	
	public static interface PatternFunction extends Lexer.PatternFunction {
		
	}
	
	public static boolean matchString(ConstCharPtr s, InputPosition pos) {
		InputPosition current = pos;
		for (; s.deref() != 0; s.incLeft(), current.incLeft()) {
			if (s.deref() != current.deref()) return false;
		}
		pos.set(current);
		return true;
	}
	
	public static boolean matchString(String s, InputPosition pos) {
		return matchString(new ConstCharPtr(s), pos);
	}
	
	public static boolean matchChar(Function<Character, Boolean> char_class, InputPosition pos) {
		if (pos.deref() != 0 && char_class.apply(pos.deref())) {
			pos.incLeft();
			return true;
		}
		return false;
	}
	
	public static boolean matchAnyChar(InputPosition pos) {
		return matchChar(new Function<Character, Boolean>() {
			public Boolean apply(Character c) {
				return true;
			}
		}, pos);
	}
	
	public static Action yieldMatchedInput = new Action() {
		@Override
		public ParseResult apply(ParseResultIterator child_results) {
			return new ParseResult(child_results.matched_input().toString());
    	}
	};
}