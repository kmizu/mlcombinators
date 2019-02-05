package com.github.kmizu.jcombinator;

import junit.framework.TestCase;

import java.util.ArrayList;
import java.util.List;

public class TestHelper {
    public static <T> List<T> listOf(T... elements) {
        List<T> values = new ArrayList<T>();
        for(T element:elements) {
            values.add(element);
        }
        return values;
    }
}
