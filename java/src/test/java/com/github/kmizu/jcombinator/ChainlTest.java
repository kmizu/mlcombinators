package com.github.kmizu.jcombinator;

import com.github.kmizu.jcombinator.datatype.Function2;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import static org.junit.Assert.*;

import static com.github.kmizu.jcombinator.Parser.*;
import static com.github.kmizu.jcombinator.Functions.*;

@RunWith(JUnit4.class)
public class ChainlTest {
	private Rule<Integer> expression() {
	    return rule(() ->
			additive().cat(eof()).map(t -> t.extract((result, __) -> result))
		);
	}
	private Rule<Integer> additive() {
		return rule(() -> {
			final Parser<Function2<Integer, Integer, Integer>> Q = string("+").map(op -> (Integer lhs, Integer rhs) -> lhs + rhs);
			final Parser<Function2<Integer, Integer, Integer>> R = string("-").map(op -> (Integer lhs, Integer rhs) -> lhs - rhs);
			return multitive().chain(Q.or(R));
		});
	}
	private Rule<Integer> multitive() {
		return rule(() -> {
			final Parser<Function2<Integer, Integer, Integer>> Q = string("*").map(op -> (Integer lhs, Integer rhs) -> lhs * rhs);
			final Parser<Function2<Integer, Integer, Integer>> R = string("/").map(op -> (Integer lhs, Integer rhs) -> lhs / rhs);
			return primary().chain(Q.or(R));
		});
	}

	private final Rule<Integer> primary() {
		return rule(() ->
			number().or((string("(").cat(expression())).cat(string(")")).map(t -> t.item1().item2()))
		);
	}

	private final Rule<Integer> number() {
		return rule(() ->
			digit().many1().map(digits -> Integer.parseInt(join(digits, "")))
		);
	}

	@Test
    public void testExpression() {
	    Parser<Integer> arithmetic = expression();
    	arithmetic.invoke("100").onSuccess(s -> {
    		assertEquals((Integer)100, s.value());
    	});
    	arithmetic.invoke("100+200").onSuccess(s -> {
    		assertEquals((Integer)300, s.value());
    	});
    	arithmetic.invoke("(1+2)*(3+4)").onSuccess(s -> {
    		assertEquals((Integer)21, s.value());
    	});
        arithmetic.invoke("1+2*3+4").onSuccess(s -> {
            assertEquals((Integer)11, s.value());
        });
    }
}
