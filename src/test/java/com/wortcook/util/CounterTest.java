package com.wortcook.util;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

public class CounterTest {
    @Test
    void testCounter(){
        Counter<Integer> c = Counter.of(5);
        assertTrue(c.isUnder());
        assertFalse(c.isOver());
        assertFalse(c.isAt());
        assertTrue(c.isAtOrUnder());
        assertFalse(c.isAtOrOver());

        c.count(); //1
        assertTrue(c.isUnder());
        assertFalse(c.isOver());
        assertFalse(c.isAt());
        assertTrue(c.isAtOrUnder());
        assertFalse(c.isAtOrOver());

        c.count(); //2
        c.count(); //3
        c.count(); //4
        c.count(); //5
        assertFalse(c.isUnder());
        assertFalse(c.isOver());
        assertTrue(c.isAt());
        assertTrue(c.isAtOrUnder());
        assertTrue(c.isAtOrOver());

        c.count(); //6
        assertFalse(c.isUnder());
        assertTrue(c.isOver());
        assertFalse(c.isAt());
        assertFalse(c.isAtOrUnder());
        assertTrue(c.isAtOrOver());
    }

}
