package com.github.kmizu.jcombinator.datatype;

/**
 * Created by kota_mizushima on 2016/12/08.
 */
public interface Procedure2<T1, T2> {
    void invoke(T1 arg1, T2 arg2);
}
