package com.wortcook.experiment.util;

import java.util.ArrayList;
import java.util.List;

import com.wortcook.util.CircularListIterator;

public class CircularListIteratorExp {
    public static void main(String[] args) {
        List<String> list = new ArrayList<>();
        list.add("A");
        list.add("B");
        list.add("C");
        list.add("D");
        list.add("E");

        CircularListIterator<String> iterator = CircularListIterator.of(list);
        while (iterator.hasNext()) {
            System.out.println(iterator.next());
        }

        for(String s:CircularListIterator.iterableOf(list, 0, 10)) {
            System.out.println(s);
        }
    }
}
