package com.github.kmizu.jcombinator;

import com.github.kmizu.jcombinator.datatype.Function0;

public class Rule<T> implements Parser<T> {
	private final Function0<Parser<T>> body;
	public Rule(Function0<Parser<T>> body) {
		this.body = body;
	}
	
	@Override
	public ParseResult<T> invoke(String input) {
		return body.invoke().invoke(input);
	}
	
}
