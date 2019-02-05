package com.github.kmizu.jcombinator.datatype;

/**
 * @param <T1> the first argument type of this function
 * @param <T2> the second argument type of this function
 * @param <R> the return type of this function
 */
public interface Function2<T1, T2, R> {
    R invoke(T1 arg1, T2 arg2);
}
