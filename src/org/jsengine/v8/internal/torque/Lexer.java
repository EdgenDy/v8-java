package org.jsengine.v8.internal.torque; 

import java.util.function.Function;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

public class Lexer {
	private LinkedHashMap<String, Symbol> keywords_ = new LinkedHashMap<String, Symbol>();
	private LinkedHashMap<PatternFunction, Symbol> patterns_ = new LinkedHashMap<PatternFunction, Symbol>();
	
	private PatternFunction match_whitespace_ = new PatternFunction() {
		@Override
		public Boolean apply(InputPosition pos) {
			return false;
		}
	};
	
	public LexerResult runLexer(String input) {
		LexerResult result = new LexerResult();
		InputPosition begin = new InputPosition(input);
		InputPosition end = begin.add(input.length());
		InputPosition pos = begin.clone();
		InputPosition token_start = pos.clone();
		LineAndColumnTracker line_column_tracker = new LineAndColumnTracker();
		
		match_whitespace_.apply(pos);
		line_column_tracker.advance(token_start, pos);
		
		while (pos.notEquals(end)) {
			token_start.set(pos);
			Symbol symbol = matchToken(pos, end);
			InputPosition token_end = pos.clone();
			line_column_tracker.advance(token_start, token_end);
			
			if (symbol == null) {
				CurrentSourcePosition.Scope pos_scope = new CurrentSourcePosition.Scope(line_column_tracker.toSourcePosition());
				System.err.println("Unimplemented report error.");
			}
			
			result.token_symbols.add(symbol);
			result.token_contents.add(new MatchedInput(token_start, pos, line_column_tracker.toSourcePosition()));
			match_whitespace_.apply(pos);
			line_column_tracker.advance(token_end, pos);
		}
		
		line_column_tracker.advance(token_start, pos);
		result.token_contents.add(new MatchedInput(pos, pos, line_column_tracker.toSourcePosition()));
		return result;
	}
	
	public Symbol token(String keyword) {
		Symbol symbol = keywords_.get(keyword);
		if (symbol == null)
			keywords_.put(keyword, new Symbol());
		return symbol;
	}
	
	public Symbol pattern(PatternFunction pattern) {
		Symbol symbol = patterns_.get(pattern);
		if (symbol == null)
			patterns_.put(pattern, new Symbol());
		return symbol;
	}
	
	private Symbol matchToken(InputPosition pos, InputPosition end) {
		InputPosition token_start = pos.clone();
		Symbol symbol = null;
		
		Set<Map.Entry<PatternFunction, Symbol>> set = patterns_.entrySet();
		for (Map.Entry<PatternFunction, Symbol> pair : set) {
			InputPosition token_end = token_start.clone();
			PatternFunction matchPattern = pair.getKey();
			
			if (matchPattern.apply(token_end) && token_end.greaterThan(pos)) {
				pos.set(token_end);
				symbol = pair.getValue();
			}
		}
		
		int pattern_size = pos.toInt() - token_start.toInt();
		
		Set<Map.Entry<String, Symbol>> keywords_set = keywords_.entrySet();
		for (Map.Entry<String, Symbol> it : keywords_set) {
			String keyword = it.getKey();
			
			if ((end.toInt() - token_start.toInt()) < keyword.length()) continue;
			if (keyword.length() >= pattern_size && keyword == token_start.toString(keyword.length())) {
				pos.set(token_start.add(keyword.length()));
				return it.getValue();
			}
		}
		
		if (pattern_size > 0) return symbol;
		return null;
	}
	
	public static interface PatternFunction extends Function<InputPosition, Boolean> {
		
	}
}