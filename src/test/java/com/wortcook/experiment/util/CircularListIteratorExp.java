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

        CircularListIterator.Builder<String> iterBuilder = CircularListIterator.<String>builder().using(list).withEpochs(2);

        CircularListIterator<String> iterator = iterBuilder.build();
        while (iterator.hasNext()) {
            System.out.println(iterator.next());
        }

        for(String s: iterBuilder.iterable()) {
            System.out.println(s);
        }

        for(String s: CircularListIterator.builder(list).withEpochs(2).iterable()) {
            System.out.println(s);
        }

        // for(String s:CircularListIterator.iterableOf(list, 0, 10)) {
        //     System.out.println(s);
        // }
    }
}
