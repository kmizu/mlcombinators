package com.github.kmizu.jcombinator;

import com.github.kmizu.jcombinator.datatype.Tuple2;

/**
 * This parser matches concatenation like `lhs rhs`
 * @param <X> The first element type of `Tuple2`
 * @param <Y> The second element type of `Tuple2`
 */
public class Cat<X, Y> implements Parser<Tuple2<X, Y>> {
	private Parser<X> lhs;
	private Parser<Y> rhs;
	public Cat(Parser<X> lhs, Parser<Y> rhs) {
		this.lhs = lhs;
		this.rhs = rhs;
	}
	public Parser<X> getLhs() {
		return lhs;
	}
	
	public Parser<Y> getRhs() {
		return rhs;
	}
	
	@Override
	public ParseResult<Tuple2<X, Y>> invoke(String input) {
		ParseResult<X> lresult = lhs.invoke(input);
		return lresult.fold(
		  (succ1) -> {			  
			  ParseResult<Y> rresult = rhs.invoke(succ1.next());
			  return rresult.fold(
			    (succ2) -> new ParseResult.Success<>(new Tuple2<>(succ1.value(), succ2.value()), succ2.next()),
			    (failure) -> {
			    	ParseResult<Tuple2<X, Y>> newFailure = new ParseResult.Failure<>(failure.message(), failure.next());
			    	return newFailure;
			    }
			  );
		  },
		  (failure) -> {
			  ParseResult<Tuple2<X, Y>> newFailure = new ParseResult.Failure<>(failure.message(), failure.next());
			  return newFailure;
		  }
		);
	}
}