package com.github.kmizu.jcombinator;

import com.github.kmizu.jcombinator.showcase.regex.RegexInterpreter;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import static org.junit.Assert.*;

@RunWith(JUnit4.class)
public class RegexTest {
    private RegexInterpreter i;
    @Before
    public void initializeInterpreter() {
        i = new RegexInterpreter();
    }
    @Test
    public void testPrimitives() {
        assertTrue(i.matches("a", "a"));
        assertFalse(i.matches("b", "a"));
        assertFalse(i.matches("a", "b"));
        assertTrue(i.matches(".", "a"));
        assertTrue(i.matches(".", "b"));
    }

    @Test
    public void testSequence() {
        assertTrue(i.matches("ab", "ab"));
        assertTrue(i.matches("bc", "bc"));
        assertTrue(i.matches(".a", "xa"));
        assertTrue(i.matches(".a", "ya"));
        assertTrue(i.matches(".a", "za"));
        assertFalse(i.matches(".a", "xb"));
    }
}
