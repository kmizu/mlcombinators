package com.github.kmizu.jcombinator;

import java.util.Set;
/**
 * This parser is like a character class like `[from-to]`
 * `from` and `to` are both `char` type.
 */
public class SetParser implements Parser<String> {
	private final Set<Character> characters;
	public SetParser(Set<Character> characters) {
		this.characters = characters;
	}
	
	@Override
	public ParseResult<String> invoke(String input) {
		if(input.length() < 1) {
			return new ParseResult.Failure<>("expect: " + characters, input);
		}else {
			char ch = input.charAt(0);
			return characters.contains(ch) ?
				new ParseResult.Success<String>(input.substring(0, 1), input.substring(1)) :
  			    new ParseResult.Failure<>("expect: " + characters, input);
		}
	}
}