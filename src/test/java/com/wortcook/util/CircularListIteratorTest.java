package com.wortcook.util;

import java.util.ArrayList;
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

        //should iterate through the list twice
        iterator = new CircularListIterator<>(elements, 0, 7);

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
        assertFalse(iterator.hasNext());
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
    void hasNextLinear(){
        List<String> elements = Arrays.asList("A", "B", "C", "D", "E");
        CircularListIterator<String> iterator = new CircularListIterator<>(elements);
        assertTrue(iterator.hasLinearNext());
        iterator.next();
        assertTrue(iterator.hasLinearNext());
        iterator.next();
        assertTrue(iterator.hasLinearNext());
        iterator.next();
        assertTrue(iterator.hasLinearNext());
        iterator.next();
        assertTrue(iterator.hasLinearNext());
        iterator.next();
        assertFalse(iterator.hasLinearNext());
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
    void nextElement(){
        List<String> elements = Arrays.asList("A", "B", "C", "D", "E");

        //should iterate through the list twice
        CircularListIterator<String> iterator = new CircularListIterator<>(elements, 0, 7);

        assertEquals("A", iterator.nextElement().get());
        assertEquals("B", iterator.nextElement().get());
        assertEquals("C", iterator.nextElement().get());
        assertEquals("D", iterator.nextElement().get());
        assertEquals("E", iterator.nextElement().get());
        assertEquals("A", iterator.nextElement().get());
        assertEquals("B", iterator.nextElement().get());
        assertEquals("C", iterator.nextElement().get());
        assertEquals("D", iterator.nextElement().get());
        assertEquals("E", iterator.nextElement().get());
        assertFalse(iterator.nextElement().isPresent());
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
        assertEquals("E",iterator.previous());

        iterator = new CircularListIterator<>(Collections.EMPTY_LIST);
        assertFalse(iterator.hasPrevious());
    }

    @Test
    void hasPreviousLinear(){
        List<String> elements = Arrays.asList("A", "B", "C", "D", "E");
        CircularListIterator<String> iterator = new CircularListIterator<>(elements);
        assertFalse(iterator.hasLinearPrevious());
        iterator.next();
        iterator.next();
        iterator.next();
        iterator.next();
        iterator.next();
        assertFalse(iterator.hasLinearNext());
        assertTrue(iterator.hasLinearPrevious());
        iterator.previous();
        assertTrue(iterator.hasLinearPrevious());
        iterator.previous();
        assertTrue(iterator.hasLinearPrevious());
        iterator.previous();
        assertTrue(iterator.hasLinearPrevious());
        iterator.previous();
        assertFalse(iterator.hasLinearPrevious());
    }


    @Test
    void previous(){
        List<String> elements = Arrays.asList("A", "B", "C", "D", "E");
        CircularListIterator<String> iterator = new CircularListIterator<>(elements);
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
        assertEquals("E", iterator.previous());
        assertEquals("D", iterator.previous());
        assertEquals("C", iterator.previous());
        assertEquals("B", iterator.previous());
        assertEquals("A", iterator.previous());

        final CircularListIterator<String> iteratorFail = new CircularListIterator<>(Collections.EMPTY_LIST);
        assertThrows(java.util.NoSuchElementException.class, () -> iteratorFail.previous());
    }

    @Test
    void previousElement(){
        List<String> elements = Arrays.asList("A", "B", "C", "D", "E");

        //should iterate through the list twice
        CircularListIterator<String> iterator = new CircularListIterator<>(elements, 0, 7);

        assertEquals("E", iterator.previousElement().get());
        assertEquals("D", iterator.previousElement().get());
        assertEquals("C", iterator.previousElement().get());
        assertEquals("B", iterator.previousElement().get());
        assertEquals("A", iterator.previousElement().get());
        assertEquals("E", iterator.previousElement().get());
        assertEquals("D", iterator.previousElement().get());
        assertEquals("C", iterator.previousElement().get());
        assertEquals("B", iterator.previousElement().get());
        assertEquals("A", iterator.previousElement().get());
        assertFalse(iterator.previousElement().isPresent());
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
        assertEquals(4, iterator.previousIndex());
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

        iterator = new CircularListIterator<>(elements, 4);
        assertEquals(3, iterator.previousIndex());
    }

    @Test
    void remove(){
        List<String> elements = new ArrayList<>(Arrays.asList("A", "B", "C", "D", "E"));
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

        assertEquals(elements.get(0), iterator.next());


        final CircularListIterator<String> iteratorFail = new CircularListIterator<>(Collections.EMPTY_LIST);
        assertThrows(java.lang.IllegalStateException.class, () -> iteratorFail.remove());

        List<String> elements2 = new ArrayList<>(Arrays.asList("A", "B"));        
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
        List<String> elements = new ArrayList<>(Arrays.asList("A", "B", "C", "D", "E"));
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
        List<String> elements = new ArrayList<>(Arrays.asList("A", "B", "C", "D", "E"));
        CircularListIterator<String> iterator = new CircularListIterator<>(elements);

        assertEquals("A", iterator.next());
        iterator.add("Z");
        assertEquals("Z", elements.get(elements.size()-1));
        assertEquals("B", iterator.next());
        iterator.add("Y");
        assertEquals("Y", elements.get(1));
        assertEquals("C", iterator.next());
        assertEquals("D", iterator.next());
        assertEquals("E", iterator.next());
        assertEquals("Z", iterator.next());
        assertEquals("A", iterator.next());
        assertEquals("Y", iterator.next());
        assertEquals("B", iterator.next());
        assertEquals("C", iterator.next());
        assertEquals("D", iterator.next());
        assertEquals("E", iterator.next());
    }

    @Test
    void at(){
        List<String> elements = new ArrayList<>(Arrays.asList("A", "B", "C", "D", "E"));
        CircularListIterator<String> iterator = new CircularListIterator<>(elements);

        assertEquals("A", iterator.next());
        assertEquals("A", iterator.at());
        assertEquals("B", iterator.next());
        assertEquals("B", iterator.at());
        assertEquals("C", iterator.next());
        assertEquals("C", iterator.at());
        assertEquals("D", iterator.next());
        assertEquals("D", iterator.at());
        assertEquals("E", iterator.next());
        assertEquals("E", iterator.at());
        assertEquals("A", iterator.next());
        assertEquals("A", iterator.at());
    }

    @Test
    void elementAt(){
        List<String> elements = new ArrayList<>(Arrays.asList("A", "B", "C", "D", "E"));
        CircularListIterator<String> iterator = new CircularListIterator<>(elements, 0,7);

        assertTrue(iterator.elementAt().isEmpty());
        assertEquals("A", iterator.next());
        assertEquals("A", iterator.elementAt().get());
        assertEquals("B", iterator.next());
        assertEquals("B", iterator.elementAt().get());
        assertEquals("C", iterator.next());
        assertEquals("C", iterator.elementAt().get());
        assertEquals("D", iterator.next());
        assertEquals("D", iterator.elementAt().get());
        assertEquals("E", iterator.next());
        assertEquals("E", iterator.elementAt().get());
        assertEquals("A", iterator.next());
        assertEquals("A", iterator.elementAt().get());
        assertEquals("B", iterator.next());
        assertEquals("B", iterator.elementAt().get());
        assertEquals("C", iterator.next());
        assertEquals("C", iterator.elementAt().get());
        assertEquals("D", iterator.next());
        assertEquals("D", iterator.elementAt().get());
        assertEquals("E", iterator.next());
        assertEquals("E", iterator.elementAt().get());
        assertTrue(iterator.nextElement().isEmpty());

        //We're still pointing at E
        assertEquals("E", iterator.elementAt().get());
    }

    @Test
    void getStepCount(){
        List<String> elements = new ArrayList<>(Arrays.asList("A", "B", "C", "D", "E"));
        CircularListIterator<String> iterator = new CircularListIterator<>(elements);

        assertEquals(0, iterator.getStepCount());
        assertEquals("A", iterator.next());
        assertEquals(1, iterator.getStepCount());
        assertEquals("B", iterator.next());
        assertEquals(2, iterator.getStepCount());
        assertEquals("C", iterator.next());
        assertEquals(3, iterator.getStepCount());
        assertEquals("D", iterator.next());
        assertEquals(4, iterator.getStepCount());
        assertEquals("E", iterator.next());
        assertEquals(5, iterator.getStepCount());
        assertEquals("A", iterator.next());
        assertEquals(6, iterator.getStepCount());
        assertEquals("E", iterator.previous());
        assertEquals(7, iterator.getStepCount());
        assertEquals("D", iterator.previous());
        assertEquals(8, iterator.getStepCount());
        assertEquals("C", iterator.previous());
        assertEquals(9, iterator.getStepCount());
        assertEquals("B", iterator.previous());
        assertEquals(10, iterator.getStepCount());
        assertEquals("A", iterator.previous());
    }

    @Test
    void resetStepCount(){
        List<String> elements = new ArrayList<>(Arrays.asList("A", "B", "C", "D", "E"));
        CircularListIterator<String> iterator = new CircularListIterator<>(elements);

        assertEquals(0, iterator.getStepCount());
        assertEquals("A", iterator.next());
        assertEquals(1, iterator.getStepCount());
        assertEquals("B", iterator.next());
        assertEquals(2, iterator.getStepCount());
        assertEquals("C", iterator.next());
        assertEquals(3, iterator.getStepCount());
        assertEquals("D", iterator.next());
        assertEquals(4, iterator.getStepCount());
        assertEquals("E", iterator.next());
        assertEquals(5, iterator.getStepCount());
        assertEquals("A", iterator.next());
        assertEquals(6, iterator.getStepCount());
        iterator.resetStepCount();
        assertEquals(0, iterator.getStepCount());
        assertEquals("B", iterator.next());
        assertEquals(1, iterator.getStepCount());
        assertEquals("C", iterator.next());
        assertEquals(2, iterator.getStepCount());
        assertEquals("D", iterator.next());
        assertEquals(3, iterator.getStepCount());
        assertEquals("E", iterator.next());
        assertEquals(4, iterator.getStepCount());
    }

    @Test
    void emptyList(){
        List<String> elements = new ArrayList<>();
        CircularListIterator<String> iterator = new CircularListIterator<>(elements);

        assertFalse(iterator.hasNext());
        assertFalse(iterator.hasPrevious());
        assertFalse(iterator.hasLinearNext());
        assertFalse(iterator.hasLinearPrevious());
        assertFalse(iterator.nextElement().isPresent());
        assertFalse(iterator.previousElement().isPresent());
        assertFalse(iterator.elementAt().isPresent());
        assertEquals(0, iterator.getStepCount());
        iterator.resetStepCount();
        assertEquals(0, iterator.getStepCount());
        assertThrows(java.util.NoSuchElementException.class, () -> iterator.next());
        assertThrows(java.util.NoSuchElementException.class, () -> iterator.previous());
        assertThrows(java.lang.IllegalStateException.class, () -> iterator.remove());
        assertThrows(java.lang.IllegalStateException.class, () -> iterator.set("A"));
        assertThrows(java.lang.IllegalStateException.class, () -> iterator.add("A"));
    }

    @Test
    void readOnlyTest(){
        CircularListIterator<String> iterator = new CircularListIterator<>(Collections.unmodifiableList(Arrays.asList("A", "B", "C", "D", "E")));

        assertEquals("A", iterator.next());//initialize the iterator

        assertThrows(java.lang.UnsupportedOperationException.class, () -> iterator.add("Z"));
        assertThrows(java.lang.UnsupportedOperationException.class, () -> iterator.remove());
        assertThrows(java.lang.UnsupportedOperationException.class, () -> iterator.set("Z"));

        assertEquals("B", iterator.next());
        assertEquals("C", iterator.next());
        assertEquals("D", iterator.next());
        assertEquals("E", iterator.next());
    }
}
