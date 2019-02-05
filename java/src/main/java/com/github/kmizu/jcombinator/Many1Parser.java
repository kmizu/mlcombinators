package com.github.kmizu.jcombinator;

import java.util.ArrayList;
import java.util.List;

/**
 * This parser is like `parser+` in EBNF
 * The result value is `List&lt;T&gt;`.
 *
 * @param <T> the type of List element
 */
public class Many1Parser<T> implements Parser<List<T>> {
	private Parser<T> parser;
	public Many1Parser(Parser<T> parser) {
	    this.parser = parser;
	}

	@Override
	public ParseResult<List<T>> invoke(String input) {
		ParseResult<T> result;
		String next = input;
		List<T> values = new ArrayList<T>();
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
			return new ParseResult.Failure<>("", previous);
		}
		while(true) {
		    previous = next;
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