package com.wortcook.util.impl;

import java.util.ArrayList;
import java.util.Arrays;

import com.wortcook.util.CircularListIterator;
import com.wortcook.util.CircularListIteratorTest;

public class ConcurrentCircularListIteratorImplTest extends CircularListIteratorTest{
    @Override
    protected CircularListIterator.Builder<String> getBuilderWithElements(){
        return CircularListIterator.<String>builder().over(new ArrayList<>(Arrays.asList("A", "B", "C", "D", "E"))).withConcurrency();
    }
}
