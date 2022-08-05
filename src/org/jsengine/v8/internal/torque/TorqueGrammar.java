package org.jsengine.v8.internal.torque; 

import org.jsengine.v8.internal.Torque;

import java.util.function.Function;

public class TorqueGrammar extends Grammar {
	private Symbol declaration = new Symbol();
	private Symbol namespaceDeclaration = new Symbol();
	private Symbol stringLiteral = new Symbol();
	private Symbol externalString = new Symbol();
	private Symbol file = new Symbol();
	
	public TorqueGrammar() {
		super(null);
		
		start_ = file;
		stringLiteral.addRule(new Rule[] {
			new Rule(new Symbol[] { pattern(matchStringLiteral) }, yieldMatchedInput)
		});
		
		externalString.addRule(new Rule[] {
			new Rule(new Symbol[] { stringLiteral }, Torque.stringLiteralUnquoteAction)
		});
		
		file.addRule(new Rule[] {
			new Rule(new Symbol[] { file, token("import"), externalString }, Torque.processTorqueImportDeclaration),
			new Rule(new Symbol[] { file, namespaceDeclaration }, Torque.addGlobalDeclarations),
			new Rule(new Symbol[] { file, declaration }, Torque.addGlobalDeclarations), 
			new Rule(new Symbol[] {})
		});
	}
	
	public static PatternFunction matchStringLiteral = new PatternFunction() {
		@Override
		public Boolean apply(InputPosition pos) {
			InputPosition current = pos.clone();
			if (matchString("\"", current)) {
				while ((matchString("\\", current) && matchAnyChar(current)) ||
					matchChar(new Function<Character, Boolean>() {
									public Boolean apply(Character c) { return c != '"' && c != '\n'; }
								}, current)) {
				}
				
				if (matchString("\"", current)) {
					pos.set(current);
					return true;
				}
			}
			current = pos.clone();
			if (matchString("'", current)) {
				while ((matchString("\\", current) && matchAnyChar(current)) ||
					matchChar(new Function<Character, Boolean>() {
									public Boolean apply(Character c) { return c != '\'' && c != '\n'; }
								}, current)) {
				}
				
				if (matchString("'", current)) {
					pos.set(current);
					return true;
				}
			}
			return false;
		}
	};
}