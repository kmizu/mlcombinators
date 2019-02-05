package com.github.kmizu.jcombinator.datatype;

/**
 * @param <T1> the first argument type of this function
 * @param <T2> the second argument type of this function
 * @param <T3> the third argument type of this function
 * @param <R> the return type of this function
 */
public interface Function3<T1, T2, T3, R> {
    R invoke(T1 arg1, T2 arg2, T3 arg3);
}
