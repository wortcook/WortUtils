package com.wortcook.util.impl;

import static org.junit.jupiter.api.Assertions.*;

import java.util.Collections;
import java.util.List;

import org.junit.jupiter.api.Test;

import com.wortcook.util.CircularListIterator;

public class CircularListIteratorImplTest {
    @Test
    void constructors(){
        assertThrows(java.lang.AssertionError.class, () -> new CircularListIteratorImpl<>(null,0,1));
        assertThrows(java.lang.AssertionError.class, () -> new CircularListIteratorImpl<>(List.of("A"), -1,1));
        assertThrows(java.lang.AssertionError.class, () -> new CircularListIteratorImpl<>(List.of("A"), 0, 0));
        assertThrows(java.lang.AssertionError.class, () -> new CircularListIteratorImpl<>(List.of("A"), 0, -1));
        CircularListIterator<String> c = new CircularListIteratorImpl<>(List.of("A"), 0, 1);
        assertEquals("A", c.next());
        c = new CircularListIteratorImpl<>(Collections.<String>emptyList(), 0, 1);
        assertFalse(c.hasNext());
    }    
}
