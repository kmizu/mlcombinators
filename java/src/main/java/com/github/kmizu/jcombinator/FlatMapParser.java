package com.github.kmizu.jcombinator;

import com.github.kmizu.jcombinator.datatype.Function1;
import static com.github.kmizu.jcombinator.ParseResult.*;

public class FlatMapParser<T, R> implements Parser<R> {
    private final Parser<T>               parser;
    private final Function1<T, Parser<R>> fn;

    public FlatMapParser(Parser<T> parser, Function1<T, Parser<R>> fn) {
        this.parser = parser;
        this.fn = fn;
    }
    @Override
    public ParseResult<R> invoke(String input) {
        ParseResult<T> result = parser.invoke(input);
        return result.fold(
            (Success<T> success) -> fn.invoke(success.value()).invoke(success.next()),
            (Failure<T> failure) -> (Failure<R>)failure
        );
    }
}