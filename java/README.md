## JCombinator: A parser combinator written in Java 8
[![Gitter](https://badges.gitter.im/kmizu/jcombinator.svg)](https://gitter.im/kmizu/jcombinator?utm_source=badge&utm_medium=badge&utm_campaign=pr-badge)
[![Build Status](https://travis-ci.org/kmizu/jcombinator.png?branch=master)](https://travis-ci.org/kmizu/jcombinator)
[![Maven Central](https://maven-badges.herokuapp.com/maven-central/com.github.kmizu/jcombinator/badge.svg)](https://maven-badges.herokuapp.com/maven-central/com.github.kmizu/jcombinator)
[![Javadoc](http://javadoc-badge.appspot.com/com.github.kmizu/jcombinator.svg?label=javadoc)](http://javadoc-badge.appspot.com/com.github.kmizu/jcombinator/index.html#com.github.kmizu.jcombinator.package)

JCombinator is yet another parser combinator library for Java 8 or later.  JCombinator is written in Java 8
to reduce boilerplate code using lambda expression.

To use jcombinator, Add the following lines to your pom.xml

```xml
<dependencies>
    <dependency>
        <groupId>com.github.kmizu</groupId>
        <artifactId>jcombinator</artifactId>
        <version>0.0.1</version>
        <scope>compile</scope>
    </dependency>
</dependencies>
```

### Usage

See [JavaDoc](http://javadoc-badge.appsp    ot.com/com.github.kmizu/jcombinator/index.html#com.github.kmizu.jcombinator.package) and [Tests](https://github.com/kmizu/jcombinator/tree/releases%2F0.0.1/src/test/java/com/github/kmizu/jcombinator)

### Example

The following code is a parser of arithmetic expression:

```java

import com.github.kmizu.jcombinator.datatype.Function2;

import static com.github.kmizu.jcombinator.Parser.*;
import static com.github.kmizu.jcombinator.Functions.*;

public class ArithmeticExpression {
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

    public void testExpression() {
	    Parser<Integer> arithmetic = expression();
    	arithmetic.invoke("100").onSuccess(s -> {
    		assert ((Integer)100) == s.value();
    	});
    	arithmetic.invoke("100+200").onSuccess(s -> {
    		assert ((Integer)300) == s.value();
    	});
    	arithmetic.invoke("(1+2)*(3+4)").onSuccess(s -> {
    		assert ((Integer)21) == s.value();
    	});
        arithmetic.invoke("1+2*3+4").onSuccess(s -> {
            assert ((Integer)11) == s.value();
        });
    }
}
```