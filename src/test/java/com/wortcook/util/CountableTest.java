package com.wortcook.util;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

public class CountableTest {
    static class StringCounter implements Counter<String> {
        private String count;
        private String limit;

        public StringCounter(String limit) {
            this.count = "";
            this.limit = limit;
        }

        @Override
        public String getCount() {
            return count;
        }

        @Override
        public String getLimit() {
            return limit;
        }

        @Override
        public void resetCount() {
            count = "";
        }

        @Override
        public void count() {
            count += "A";
        }
    }

    @Test
    void testCounter(){
        Counter<String> c = new StringCounter("AAAAA");
        assertTrue(c.isUnder());
        assertFalse(c.isOver());
        assertFalse(c.isAt());
        assertTrue(c.isAtOrUnder());
        assertFalse(c.isAtOrOver());

        c.count(); //"A"
        assertTrue(c.isUnder());
        assertFalse(c.isOver());
        assertFalse(c.isAt());
        assertTrue(c.isAtOrUnder());
        assertFalse(c.isAtOrOver());

        c.count(); //AA
        c.count(); //AAA
        c.count(); //AAAA
        c.count(); //AAAAA
        assertFalse(c.isUnder());
        assertFalse(c.isOver());
        assertTrue(c.isAt());
        assertTrue(c.isAtOrUnder());
        assertTrue(c.isAtOrOver());

        c.count(); //AAAAAA
        assertFalse(c.isUnder());
        assertTrue(c.isOver());
        assertFalse(c.isAt());
        assertFalse(c.isAtOrUnder());
        assertTrue(c.isAtOrOver());
    }
}
