package com.github.kmizu.jcombinator;

import com.github.kmizu.jcombinator.datatype.Tuple2;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import java.util.Optional;

import static org.junit.Assert.*;

import static com.github.kmizu.jcombinator.Parser.*;
import static com.github.kmizu.jcombinator.TestHelper.*;
import static com.github.kmizu.jcombinator.Functions.*;

@RunWith(JUnit4.class)
public class PrimitiveTest {

    @Test
    public void testStringParser()
    {
        Parser<String> helloWorld = string("Hello, World");
        helloWorld.invoke("Hello, World").fold(
            (success) -> {
                assertEquals("Hello, World", success.value());
                assertEquals("", success.next());
            },
            (failure) -> {
               assertTrue(false);
            }
        );
    }

    @Test
    public void testNot() {
        Parser<String> notA = string("a").not();
        notA.invoke("a").fold(
            (success) -> {
                assertTrue(false);
            },
            (failure) -> {
                assertEquals("a", failure.next());
            }
        );
        notA.invoke("b").fold(
            (success) -> {
                assertEquals(null, success.value());
                assertEquals("b", success.next());
            },
            (failure) -> {
                assertTrue(false);
            }
        );
    }

    @Test
    public void testOr() {
        Parser<String> aOrB = string("a").or(string("b"));
        aOrB.invoke("a").fold(
            (success) -> {
                assertEquals("a", success.value());
                assertEquals("", success.next());
            },
            (failure) -> {
                assertTrue(false);
            }
        );
        aOrB.invoke("b").fold(
            (success) -> {
                assertEquals("b", success.value());
                assertEquals("", success.next());
            },
            (failure) -> {
                assertTrue(false);
            }
        );
    }

    @Test
    public void testOptionParser() {
        let(string("a").option(), ax -> {
            ax.invoke("a").fold(
                (succ) -> {
                    assertEquals(Optional.of("a"), succ.value());
                    assertEquals("", succ.next());
                },
                (fail) -> {
                    assertTrue(false);
                }
            );
            ax.invoke("b").fold(
                (succ) -> {
                    assertEquals(Optional.empty(), succ.value());
                    assertEquals("b", succ.next());
                },
                (fail) -> {
                    assertTrue(false);
                }
            );
        });
    }

    @Test
    public void testSetParser() {
        let(set('a', 'b'), ax -> {
            ax.invoke("a").fold(
                (success) -> {
                    assertEquals("a", success.value());
                    assertEquals("",success.next());
                },
                (failure) -> {
                    assertTrue(false);
                }
            );
            ax.invoke("b").fold(
                (success) -> {
                    assertEquals("b", success.value());
                    assertEquals("", success.next());
                },
                (failure) -> {
                    assertTrue(false);
                }
            );
            ax.invoke("c").fold(
                (success) -> {
                    assertTrue(false);
                },
                (failure) -> {
                    assertEquals("c", failure.next());
                }
            );
        });
    }

    @Test
    public void testManyParser() {
        let(string("a").many(), ax -> {
            ax.invoke("aaaaa").fold(
                    (success) -> {
                        assertEquals(listOf("a", "a", "a", "a", "a"), success.value());
                        assertEquals("", success.next());
                    },
                    (failure) -> {
                        assertTrue(false);
                    }
            );
        });
    }

    @Test
    public void testCat() {
        let(string("a").cat(string("b")), ab -> {
            ab.invoke("ab").fold(
                    (success) -> {
                        assertEquals(new Tuple2<>("a", "b"), success.value());
                        assertEquals("", success.next());
                    },
                    (failure) -> {
                        assertTrue(false);
                    }
            );

        });
    }
}
