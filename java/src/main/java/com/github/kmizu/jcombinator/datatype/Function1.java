package com.github.kmizu.jcombinator.datatype;

/**
 * @param <T1> the first argument type of this function
 * @param <R> the return type of this function
 */
public interface Function1<T1, R> {
    R invoke(T1 arg);
}
