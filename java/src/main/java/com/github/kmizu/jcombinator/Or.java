package com.github.kmizu.jcombinator;

/**
 * This parser represent ordered choice like
 * `lhs / rhs`.  Note that `lhs / rhs` is not
 * equal to `rhs / lhs`
 * @param <X> the type of result value
 */
public class Or<X> implements Parser<X> {
	private Parser<X> lhs;
	private Parser<X> rhs;
	public Or(Parser<X> lhs, Parser<X> rhs) {
		this.lhs = lhs;
		this.rhs = rhs;
	}
	public Parser<X> lhs() {
		return lhs;
	}
	
	public Parser<X> rhs() {
		return rhs;
	}
	
	@Override
	public ParseResult<X> invoke(String input) {
		ParseResult<X> lresult = lhs.invoke(input);
		return lresult.fold(
		  succ -> succ,
		  fail -> rhs.invoke(input)
		);
	}
}