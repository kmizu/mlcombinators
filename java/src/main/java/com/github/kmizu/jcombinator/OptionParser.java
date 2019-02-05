package com.github.kmizu.jcombinator;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * This parser is like `parser+` in EBNF
 * The result value is `List&lt;T&gt;`.
 *
 * @param <T> the type of List element
 */
public class OptionParser<T> implements Parser<Optional<T>> {
	private Parser<T> parser;
	public OptionParser(Parser<T> parser) {
	    this.parser = parser;
	}

	@Override
	public ParseResult<Optional<T>> invoke(String input) {
		return parser.invoke(input).fold(
			(succ) -> {
				return new ParseResult.Success<>(Optional.of(succ.value()), succ.next());
			},
			(fail) -> new ParseResult.Success<>(Optional.ofNullable(null), input)
		);
	}
}