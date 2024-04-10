package com.wortcook.util;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class CircularListIteratorTest {

    @Test
    void constructors(){
        List<String> elements = Arrays.asList("A", "B", "C", "D", "E");
        CircularListIterator<String> iterator = new CircularListIterator<>(elements);
        assertTrue(iterator.hasNext());
        assertEquals("A", iterator.next());
        assertTrue(iterator.hasNext());
        assertEquals("B", iterator.next());
        assertTrue(iterator.hasNext());
        assertEquals("C", iterator.next());
        assertTrue(iterator.hasNext());
        assertEquals("D", iterator.next());
        assertTrue(iterator.hasNext());
        assertEquals("E", iterator.next());

        iterator = new CircularListIterator<>(elements, 2);
        assertTrue(iterator.hasNext());
        assertEquals("C", iterator.next());
        assertTrue(iterator.hasNext());
        assertEquals("D", iterator.next());
        assertTrue(iterator.hasNext());
        assertEquals("E", iterator.next());

        iterator = new CircularListIterator<>(elements, 5);
        assertTrue(iterator.hasNext());
        assertEquals("A", iterator.next());
        assertTrue(iterator.hasNext());
        assertEquals("B", iterator.next());
        assertTrue(iterator.hasNext());
        assertEquals("C", iterator.next());
        assertTrue(iterator.hasNext());
        assertEquals("D", iterator.next());
        assertTrue(iterator.hasNext());
        assertEquals("E", iterator.next());
    }

    @Test
    void hasNext(){
        List<String> elements = Arrays.asList("A", "B", "C", "D", "E");
        CircularListIterator<String> iterator = new CircularListIterator<>(elements);
        for(int i = 0; i < 20; i++){
            assertTrue(iterator.hasNext());
            iterator.next();
        }

        assertTrue(iterator.hasNext());
        assertEquals("A",iterator.next());

        iterator = new CircularListIterator<>(Collections.EMPTY_LIST);
        assertFalse(iterator.hasNext());
    }


    @Test
    void next(){
        List<String> elements = Arrays.asList("A", "B", "C", "D", "E");
        CircularListIterator<String> iterator = new CircularListIterator<>(elements);
        assertEquals("A", iterator.next());
        assertEquals("B", iterator.next());
        assertEquals("C", iterator.next());
        assertEquals("D", iterator.next());
        assertEquals("E", iterator.next());
        assertEquals("A", iterator.next());
        assertEquals("B", iterator.next());
        assertEquals("C", iterator.next());
        assertEquals("D", iterator.next());
        assertEquals("E", iterator.next());

        iterator = new CircularListIterator<>(elements, 2);
        assertEquals("C", iterator.next());
        assertEquals("D", iterator.next());
        assertEquals("E", iterator.next());
        assertEquals("A", iterator.next());
        assertEquals("B", iterator.next());
        assertEquals("C", iterator.next());
        assertEquals("D", iterator.next());
        assertEquals("E", iterator.next());

        final CircularListIterator<String> iteratorFail = new CircularListIterator<>(Collections.EMPTY_LIST);
        assertThrows(java.util.NoSuchElementException.class, () -> iteratorFail.next());
    }


    @Test
    void hasPrevious(){
        List<String> elements = Arrays.asList("A", "B", "C", "D", "E");
        CircularListIterator<String> iterator = new CircularListIterator<>(elements);
        for(int i = 0; i < 20; i++){
            assertTrue(iterator.hasPrevious());
            iterator.previous();
        }

        assertTrue(iterator.hasPrevious());
        assertEquals("A",iterator.previous());

        iterator = new CircularListIterator<>(Collections.EMPTY_LIST);
        assertFalse(iterator.hasPrevious());
    }

    @Test
    void previous(){
        List<String> elements = Arrays.asList("A", "B", "C", "D", "E");
        CircularListIterator<String> iterator = new CircularListIterator<>(elements);
        assertEquals("A", iterator.previous());
        assertEquals("E", iterator.previous());
        assertEquals("D", iterator.previous());
        assertEquals("C", iterator.previous());
        assertEquals("B", iterator.previous());
        assertEquals("A", iterator.previous());
        assertEquals("E", iterator.previous());
        assertEquals("D", iterator.previous());
        assertEquals("C", iterator.previous());
        assertEquals("B", iterator.previous());

        iterator = new CircularListIterator<>(elements, 10);
        assertEquals("A", iterator.previous());
        assertEquals("E", iterator.previous());
        assertEquals("D", iterator.previous());
        assertEquals("C", iterator.previous());
        assertEquals("B", iterator.previous());
        assertEquals("A", iterator.previous());

        final CircularListIterator<String> iteratorFail = new CircularListIterator<>(Collections.EMPTY_LIST);
        assertThrows(java.util.NoSuchElementException.class, () -> iteratorFail.previous());
    }

    @Test
    void nextIndex(){
        List<String> elements = Arrays.asList("A", "B", "C", "D", "E");
        CircularListIterator<String> iterator = new CircularListIterator<>(elements);
        assertEquals(0, iterator.nextIndex());
        iterator.next();
        assertEquals(1, iterator.nextIndex());
        iterator.next();
        assertEquals(2, iterator.nextIndex());
        iterator.next();
        assertEquals(3, iterator.nextIndex());
        iterator.next();
        assertEquals(4, iterator.nextIndex());
        iterator.next();
        assertEquals(0, iterator.nextIndex());
        iterator.next();
        assertEquals(1, iterator.nextIndex());
        iterator.next();
        assertEquals(2, iterator.nextIndex());
        iterator.next();
        assertEquals(3, iterator.nextIndex());
        iterator.next();
        assertEquals(4, iterator.nextIndex());
        iterator.next();
        assertEquals(0, iterator.nextIndex());
    }

    @Test
    void previousIndex(){
        List<String> elements = Arrays.asList("A", "B", "C", "D", "E");
        CircularListIterator<String> iterator = new CircularListIterator<>(elements);
        assertEquals(0, iterator.previousIndex());
        iterator.next();
        assertEquals(4, iterator.previousIndex());
        iterator.next();
        assertEquals(0, iterator.previousIndex());
        iterator.next();
        assertEquals(1, iterator.previousIndex());
        iterator.next();
        assertEquals(2, iterator.previousIndex());
        iterator.next();
        assertEquals(3, iterator.previousIndex());
        iterator.next();
        assertEquals(4, iterator.previousIndex());
        iterator.next();
        assertEquals(0, iterator.previousIndex());
        iterator.next();
        assertEquals(1, iterator.previousIndex());
        iterator.next();
        assertEquals(2, iterator.previousIndex());
        iterator.next();
        assertEquals(3, iterator.previousIndex());
        iterator.next();
        assertEquals(4, iterator.previousIndex());
    }

    @Test
    void remove(){
        List<String> elements = new CircularQueueList<>(Arrays.asList("A", "B", "C", "D", "E"));
        CircularListIterator<String> iterator = new CircularListIterator<>(elements);

        assertEquals("A",iterator.next());
        assertEquals("B",iterator.next());
        iterator.remove(); // remove B
        assertEquals("C", iterator.next());
        iterator.remove(); // remove C
        assertEquals("D", iterator.next());

        // C is now the head of the list and we're pointing there
        assertEquals("E", iterator.next());

        assertEquals("A", iterator.next());
        assertEquals("D", iterator.next());
        assertEquals("E", iterator.next());

        iterator.remove();
        assertEquals("A", iterator.next());
        assertEquals("D", iterator.next());
        assertEquals("A", iterator.next());
        assertEquals("D", iterator.next());

        assertEquals(elements.get(4), iterator.next());


        final CircularListIterator<String> iteratorFail = new CircularListIterator<>(Collections.EMPTY_LIST);
        assertThrows(java.lang.IllegalStateException.class, () -> iteratorFail.remove());

        List<String> elements2 = new CircularQueueList<>(Arrays.asList("A", "B"));        
        final CircularListIterator<String> iteratorFail2 = new CircularListIterator<>(elements2);
        assertEquals("A", iteratorFail2.next());
        assertEquals("B", iteratorFail2.next());
        iteratorFail2.remove();
        assertEquals("A", iteratorFail2.next());
        iteratorFail2.remove();

        assertThrows(java.lang.IllegalStateException.class, () -> iteratorFail2.remove());
    }

    @Test
    void set(){
        List<String> elements = new CircularQueueList<>(Arrays.asList("A", "B", "C", "D", "E"));
        CircularListIterator<String> iterator = new CircularListIterator<>(elements);

        assertEquals("A", iterator.next());
        assertEquals("A", elements.get(0)); 
        iterator.set("Z");
        assertEquals("Z", elements.get(0));

        assertEquals("B", iterator.next());
        assertEquals("B", elements.get(1));
        iterator.set("Y");
        assertEquals("Y", elements.get(1));

        assertEquals("C", iterator.next());
        assertEquals("C", elements.get(2));
        iterator.set("X");
        assertEquals("X", elements.get(2));

        assertEquals("D",iterator.next());
        assertEquals("D", elements.get(3));
        iterator.set("W");
        assertEquals("W", elements.get(3));

        assertEquals("E", iterator.next());
        iterator.set("V");
        assertEquals("V", elements.get(4));
        
        assertEquals("Z", iterator.next());
        assertEquals("Y", iterator.next());
        assertEquals("X", iterator.next());
        assertEquals("W", iterator.next());
        assertEquals("V", iterator.next());        
    }

    @Test
    void add(){
        List<String> elements = new CircularQueueList<>(Arrays.asList("A", "B", "C", "D", "E"));
        CircularListIterator<String> iterator = new CircularListIterator<>(elements);

        assertEquals("A", iterator.next());
        assertEquals("B", iterator.next());
        assertEquals("C", iterator.next());
        assertEquals("D", iterator.next());
        assertEquals("E", iterator.next());
        assertEquals("A", iterator.next());
        assertEquals("B", iterator.next());
        assertEquals("C", iterator.next());
        assertEquals("D", iterator.next());
        assertEquals("E", iterator.next());

        // iterator.add("Z");
        // assertEquals("Z", iterator.next());
        // assertEquals(10, iterator.getCurrentIndex());
        // assertEquals("A", iterator.next());
    //     iterator.add("Y");
    //     assertEquals("Y", iterator.next());
    //     assertEquals("B", iterator.next());
    //     assertEquals("C", iterator.next());
    //     assertEquals("D", iterator.next());
    //     assertEquals("E", iterator.next());
    //     assertEquals("Z", iterator.next());
    //     assertEquals("A", iterator.next());
    //     assertEquals("Y", iterator.next());
    //     assertEquals("B", iterator.next());
    }

    

    // @Override
    // public void add(T t) {
    //     elements.add(currentIndex, t);
    // }
}
