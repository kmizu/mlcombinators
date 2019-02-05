package com.github.kmizu.jcombinator;

import java.util.ArrayList;
import java.util.List;

/**
 * This parser is like `parser*` in EBNF
 * The result value is `List&lt;T&gt;`.
 * Note that this parser always succeed even if `parser` doesn't match any input.
 *
 * @param <T> the type of List element
 */
public class ManyParser<T> implements Parser<List<T>> {
	private Parser<T> parser;
	public ManyParser(Parser<T> parser) {
	    this.parser = parser;
	}

	@Override
	public ParseResult<List<T>> invoke(String input) {
		ParseResult<T> result;
		String next = input;
		List<T> values = new ArrayList<T>();
		while(true) {
			String previous = next;
		    result = parser.invoke(next);
		    next = result.fold(
				(success) -> {
					values.add(success.value());
					return success.next();
				},
				(failure) -> {
				    return null;
				}
			);
		    if(next == null) {
		        return new ParseResult.Success<>(values, previous);
			}
		}
	}
}