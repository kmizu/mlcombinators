package com.github.kmizu.jcombinator;

/**
 * This parser represent not predicate like
 * `!e1 e2`.  Note that `!e1` doesn't consume
 * any input.
 * @param <X> the type of result value
 */
public class Not<X> implements Parser<X> {
	private Parser<X> target;
	public Not(Parser<X> target) {
	    this.target = target;
	}

	@Override
	public ParseResult<X> invoke(String input) {
		ParseResult<X> result = target.invoke(input);
		return result.fold(
		  succ -> new ParseResult.Failure<X>("shouuld not match", input),
          fail -> {
		  	return new ParseResult.Success<X>(null, input);
		  }
		);
	}
}