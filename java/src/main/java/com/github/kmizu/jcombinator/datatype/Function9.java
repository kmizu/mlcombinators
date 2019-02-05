package com.github.kmizu.jcombinator.datatype;

/**
 * @param <T1> the first argument type of this function
 * @param <T2> the second argument type of this function
 * @param <T3> the third argument type of this function
 * @param <T4> the fourth argument type of this function
 * @param <T5> the fifth argument type of this function
 * @param <T6> the sixth argument type of this function
 * @param <T7> the seventh argument type of this function
 * @param <T8> the eighth argument type of this function
 * @param <T9> the ninth argument type of this function
 * @param <R> the return type of this function
 */
public interface Function9<T1, T2, T3, T4, T5, T6, T7, T8, T9, R> {
    R invoke(T1 arg1, T2 arg2, T3 arg3, T4 arg4, T5 arg5, T6 arg6, T7 arg7, T8 arg8, T9 arg9);
}
