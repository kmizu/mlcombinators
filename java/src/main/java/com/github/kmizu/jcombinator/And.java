package com.github.kmizu.jcombinator;

/**
 * This parser represent and predicate like
 * `&e1 e2`.  Note that `&e1` doesn't consume
 * any input even if evaluation of `e1` succeed.
 * @param <X> the type of result value
 */
public class And<X> implements Parser<X> {
	private Parser<X> target;
	public And(Parser<X> target) {
	    this.target = target;
	}

	@Override
	public ParseResult<X> invoke(String input) {
		ParseResult<X> result = target.invoke(input);
		return result.fold(
		  succ -> new ParseResult.Success<X>(null, input),
          fail -> {
		  	return new ParseResult.Failure<X>(null, input);
		  }
		);
	}
}