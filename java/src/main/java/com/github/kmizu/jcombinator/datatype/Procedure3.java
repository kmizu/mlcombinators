package com.github.kmizu.jcombinator.datatype;

/**
 * Created by kota_mizushima on 2016/12/08.
 */
public interface Procedure3<T1, T2, T3> {
    void invoke(T1 arg1, T2 arg2, T3 arg3);
}
