package com.github.kmizu.jcombinator;

/**
 * This parser consume any one character.
 * It fails only if `input` is empty.
 */
public class AnyParser implements Parser<String> {
	public AnyParser() {
	}
	
	@Override
	public ParseResult<String> invoke(String input) {
		if(input.length() < 1) {
			return new ParseResult.Failure<>("expect: one character", input);
		}else {
			String value = input.substring(0, 1);
			return new ParseResult.Success<String>(value, input.substring(1));
		}
	}
}