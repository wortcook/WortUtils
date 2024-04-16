package com.wortcook.util;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.SortedSet;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class CircularListIteratorTest {

    @Test
    void constructors(){
        List<String> elements = Arrays.asList("A", "B", "C", "D", "E");
        CircularListIterator<String> iterator = CircularListIterator.<String>of(elements);
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

        iterator = CircularListIterator.<String>of(elements,2);
        assertTrue(iterator.hasNext());
        assertEquals("C", iterator.next());
        assertTrue(iterator.hasNext());
        assertEquals("D", iterator.next());
        assertTrue(iterator.hasNext());
        assertEquals("E", iterator.next());
        //while we won't test to max, quick check to 100
        for(int i = 0; i < 100; i++){
            assertTrue(iterator.hasNext());
            iterator.next();
        }

        iterator = CircularListIterator.<String>of(elements,5);
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
        iterator = CircularListIterator.<String>of(elements,0,10);

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
    void constructorsFail(){
        assertThrows(java.lang.AssertionError.class, () -> CircularListIterator.<String>of((List<String>)null));
        assertThrows(java.lang.AssertionError.class, () -> CircularListIterator.<String>of((List<String>)null, 0));
        assertThrows(java.lang.AssertionError.class, () -> CircularListIterator.<String>of(null, 0, 10));
        assertThrows(java.lang.AssertionError.class, () -> CircularListIterator.<String>of(Collections.<String>emptyList(), -1));
        assertThrows(java.lang.AssertionError.class, () -> CircularListIterator.<String>of(Collections.<String>emptyList(), -1, 10));
        assertThrows(java.lang.AssertionError.class, () -> CircularListIterator.<String>of(Collections.<String>emptyList(), 0, 0));
        assertThrows(java.lang.AssertionError.class, () -> CircularListIterator.<String>of(Collections.<String>emptyList(), 0, -1));
    }

    @Test
    void hasNext(){
        List<String> elements = Arrays.asList("A", "B", "C", "D", "E");
        CircularListIterator<String> iterator = CircularListIterator.<String>builder().over(elements).iterator();
        for(int i = 0; i < 20; i++){
            assertTrue(iterator.hasNext());
            iterator.next();
        }

        assertTrue(iterator.hasNext());
        assertEquals("A",iterator.next());

        iterator = CircularListIterator.<String>builder().over(Collections.<String>emptyList()).iterator();
        assertFalse(iterator.hasNext());
    }

    @Test
    void hasNextNoWrap(){
        List<String> elements = Arrays.asList("A", "B", "C", "D", "E");
        CircularListIterator<String> iterator = CircularListIterator.<String>of(elements);
        assertTrue(iterator.hasNextNoWrap());
        iterator.next();
        assertTrue(iterator.hasNextNoWrap());
        iterator.next();
        assertTrue(iterator.hasNextNoWrap());
        iterator.next();
        assertTrue(iterator.hasNextNoWrap());
        iterator.next();
        assertTrue(iterator.hasNextNoWrap());
        iterator.next();
        assertFalse(iterator.hasNextNoWrap());
    }


    @Test
    void next(){
        List<String> elements = Arrays.asList("A", "B", "C", "D", "E");
        CircularListIterator<String> iterator = CircularListIterator.<String>of(elements);
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

        iterator = CircularListIterator.<String>of(elements,2);;
        assertEquals("C", iterator.next());
        assertEquals("D", iterator.next());
        assertEquals("E", iterator.next());
        assertEquals("A", iterator.next());
        assertEquals("B", iterator.next());
        assertEquals("C", iterator.next());
        assertEquals("D", iterator.next());
        assertEquals("E", iterator.next());

        final CircularListIterator<String> iteratorFail = CircularListIterator.<String>of(Collections.<String>emptyList());
        assertThrows(java.util.NoSuchElementException.class, () -> iteratorFail.next());
    }

    @Test
    void nextElement(){
        List<String> elements = Arrays.asList("A", "B", "C", "D", "E");

        //should iterate through the list twice
        CircularListIterator<String> iterator = CircularListIterator.<String>of(elements, 0, 10);

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
        CircularListIterator<String> iterator = CircularListIterator.<String>of(elements);
        for(int i = 0; i < 20; i++){
            assertTrue(iterator.hasPrevious());
            iterator.previous();
        }

        assertTrue(iterator.hasPrevious());
        assertEquals("E",iterator.previous());

        iterator = CircularListIterator.<String>of(Collections.<String>emptyList());
        assertFalse(iterator.hasPrevious());
    }

    @Test
    void hasPreviousNoWrap(){
        List<String> elements = Arrays.asList("A", "B", "C", "D", "E");
        CircularListIterator<String> iterator = CircularListIterator.<String>of(elements);
        assertFalse(iterator.hasPreviousNoWrap());
        iterator.next();
        iterator.next();
        iterator.next();
        iterator.next();
        iterator.next();
        assertFalse(iterator.hasNextNoWrap());
        assertTrue(iterator.hasPreviousNoWrap());
        iterator.previous();
        assertTrue(iterator.hasPreviousNoWrap());
        iterator.previous();
        assertTrue(iterator.hasPreviousNoWrap());
        iterator.previous();
        assertTrue(iterator.hasPreviousNoWrap());
        iterator.previous();
        assertFalse(iterator.hasPreviousNoWrap());
    }


    @Test
    void previous(){
        List<String> elements = Arrays.asList("A", "B", "C", "D", "E");
        CircularListIterator<String> iterator = CircularListIterator.<String>of(elements);
        assertEquals("E", iterator.previous());
        assertEquals("D", iterator.previous());
        assertEquals("C", iterator.previous());
        assertEquals("B", iterator.previous());
        assertEquals("A", iterator.previous());
        assertEquals("E", iterator.previous());
        assertEquals("D", iterator.previous());
        assertEquals("C", iterator.previous());
        assertEquals("B", iterator.previous());

        iterator = CircularListIterator.<String>of(elements, 0,10);
        assertEquals("E", iterator.previous());
        assertEquals("D", iterator.previous());
        assertEquals("C", iterator.previous());
        assertEquals("B", iterator.previous());
        assertEquals("A", iterator.previous());

        final CircularListIterator<String> iteratorFail = CircularListIterator.<String>of(Collections.<String>emptyList());
        assertThrows(java.util.NoSuchElementException.class, () -> iteratorFail.previous());
    }

    @Test
    void previousElement(){
        List<String> elements = Arrays.asList("A", "B", "C", "D", "E");

        //should iterate through the list twice
        CircularListIterator<String> iterator = CircularListIterator.<String>of(elements, 0, 10);

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
        CircularListIterator<String> iterator = CircularListIterator.<String>of(elements);
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

        iterator = CircularListIterator.<String>of(elements, 5, 5);
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
        assertEquals(5, iterator.nextIndex());
        assertFalse(iterator.hasNext());
    }

    @Test
    void previousIndex(){
        List<String> elements = Arrays.asList("A", "B", "C", "D", "E");
        CircularListIterator<String> iterator = CircularListIterator.<String>of(elements);
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

        iterator = CircularListIterator.<String>of(elements, 4);
        assertEquals(3, iterator.previousIndex());

        iterator = CircularListIterator.<String>of(elements, 0, 5);
        assertEquals(4, iterator.previousIndex());
        iterator.previous();
        assertEquals(3, iterator.previousIndex());
        iterator.previous();
        assertEquals(2, iterator.previousIndex());
        iterator.previous();
        assertEquals(1, iterator.previousIndex());
        iterator.previous();
        assertEquals(0, iterator.previousIndex());
        iterator.previous();
        assertEquals(-1, iterator.previousIndex());



    }

    @Test
    void remove(){
        List<String> elements = new ArrayList<>(Arrays.asList("A", "B", "C", "D", "E"));
        CircularListIterator<String> iterator = CircularListIterator.<String>of(elements);

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


        final CircularListIterator<String> iteratorFail = CircularListIterator.<String>of(Collections.<String>emptyList());
        assertThrows(java.lang.IllegalStateException.class, () -> iteratorFail.remove());

        List<String> elements2 = new ArrayList<>(Arrays.asList("A", "B"));        
        final CircularListIterator<String> iteratorFail2 = CircularListIterator.<String>of(elements2);
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
        CircularListIterator<String> iterator = CircularListIterator.<String>of(elements);

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
        CircularListIterator<String> iterator = CircularListIterator.<String>of(elements);

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
    void addAll(){
        List<String> elements = new ArrayList<>(Arrays.asList("A", "B", "C", "D", "E"));
        CircularListIterator<String> iterator = CircularListIterator.<String>of(elements);

        assertEquals("A", iterator.next());
        iterator.addAll(Arrays.asList("Z", "Y"));
        assertEquals("B", iterator.next());
        assertEquals("C", iterator.next());
        assertEquals("D", iterator.next());
        assertEquals("E", iterator.next());
        assertEquals("Z", iterator.next());
        assertEquals("Y", iterator.next());
        assertEquals("A", iterator.next());
        assertEquals("B", iterator.next());
        iterator.addAll(Arrays.asList("W", "X"));
        assertEquals("C", iterator.next());
        assertEquals("D", iterator.next());
        assertEquals("E", iterator.next());
        assertEquals("Z", iterator.next());
        assertEquals("Y", iterator.next());
        assertEquals("A", iterator.next());
        assertEquals("W", iterator.next());
        assertEquals("X", iterator.next());
        assertEquals("B", iterator.next());
    }

    @Test
    void at(){
        List<String> elements = new ArrayList<>(Arrays.asList("A", "B", "C", "D", "E"));
        CircularListIterator<String> iterator = CircularListIterator.<String>of(elements);

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
        CircularListIterator<String> iterator = CircularListIterator.<String>of(elements,0,10);

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
        CircularListIterator<String> iterator = CircularListIterator.<String>of(elements);

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
        CircularListIterator<String> iterator = CircularListIterator.<String>of(elements);

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
    void writeAhead(){
        List<String> elements = new ArrayList<>(Arrays.asList("A", "B", "C", "D", "E"));
        CircularListIterator<String> iterator = CircularListIterator.<String>of(elements);

        assertThrows(java.lang.IllegalStateException.class, () -> iterator.addNext("Z"));
        assertEquals("A", iterator.next());
        iterator.addNext("Z");
        assertEquals("Z", iterator.next());
        assertEquals("B", iterator.next());
        assertEquals("C", iterator.next());
        assertEquals("D", iterator.next());
        assertEquals("E", iterator.next());
        iterator.addNext("F");
        assertEquals("F", iterator.next());
        assertEquals("A", iterator.next());
        assertEquals("Z", iterator.next());
    }

    @Test
    void writeAllAhead(){
        List<String> elements = new ArrayList<>(Arrays.asList("A", "B", "C", "D", "E"));
        CircularListIterator<String> iterator = CircularListIterator.<String>of(elements);

        assertThrows(java.lang.IllegalStateException.class, () -> iterator.addAllNext(Arrays.asList("Z", "Y")));
        assertEquals("A", iterator.next());
        iterator.addAllNext(Arrays.asList("Z", "Y"));
        assertEquals("Z", iterator.next());
        assertEquals("Y", iterator.next());
        assertEquals("B", iterator.next());
        assertEquals("C", iterator.next());
        assertEquals("D", iterator.next());
        assertEquals("E", iterator.next());
        iterator.addAllNext(Arrays.asList("F", "G"));
        assertEquals("F", iterator.next());
        assertEquals("G", iterator.next());
        assertEquals("A", iterator.next());
        assertEquals("Z", iterator.next());
        assertEquals("Y", iterator.next());
    }

    @Test
    void emptyList(){
        List<String> elements = new ArrayList<>();
        CircularListIterator<String> iterator = CircularListIterator.<String>of(elements);

        assertFalse(iterator.hasNext());
        assertFalse(iterator.hasPrevious());
        assertFalse(iterator.hasNextNoWrap());
        assertFalse(iterator.hasPreviousNoWrap());
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
        CircularListIterator<String> iterator = CircularListIterator.<String>of(Collections.unmodifiableList(Arrays.asList("A", "B", "C", "D", "E")));

        assertEquals("A", iterator.next());//initialize the iterator

        assertThrows(java.lang.UnsupportedOperationException.class, () -> iterator.add("Z"));
        assertThrows(java.lang.UnsupportedOperationException.class, () -> iterator.remove());
        assertThrows(java.lang.UnsupportedOperationException.class, () -> iterator.set("Z"));

        assertEquals("B", iterator.next());
        assertEquals("C", iterator.next());
        assertEquals("D", iterator.next());
        assertEquals("E", iterator.next());
    }

    @Test
    void elements(){
        List<String> elements = Arrays.asList("A", "B", "C", "D", "E");
        CircularListIterator<String> iterator = CircularListIterator.<String>of(elements);

        assertEquals(elements, iterator.elements());
    }

    @Test
    void reset(){
        List<String> elements = new ArrayList<>(Arrays.asList("A", "B", "C", "D", "E"));
        CircularListIterator<String> iterator = CircularListIterator.<String>of(elements,0,10);

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
        assertFalse(iterator.hasNext());

        iterator.reset();

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
        assertFalse(iterator.hasNext());
        iterator.add("BeforeE");
        assertEquals("BeforeE", iterator.previous());
        assertEquals("E", iterator.next());
        assertFalse(iterator.hasNext());

        iterator.reset();

        assertThrows(java.lang.IllegalStateException.class, () -> iterator.add("Y"));
    }

    @Test
    void builder(){
        List<String> elements = Arrays.asList("A", "B", "C", "D", "E");
        CircularListIterator<String> iterator = CircularListIterator.<String>builder().over(elements).iterator();
        assertEquals("A", iterator.next());//initialize the iterator
        assertEquals("B", iterator.next());
        assertEquals("C", iterator.next());
        assertEquals("D", iterator.next());
        assertEquals("E", iterator.next());
        assertEquals("A", iterator.next());
        assertEquals(elements, iterator.elements());

        iterator = CircularListIterator.<String>builder().over(elements).startAt(2).iterator();
        assertEquals("C", iterator.next());//initialize the iterator
        assertEquals("D", iterator.next());
        assertEquals("E", iterator.next());
        assertEquals("A", iterator.next());

        iterator = CircularListIterator.<String>builder().over(elements).startAt(5).iterator();
        assertEquals("A", iterator.next());//initialize the iterator
        assertEquals("B", iterator.next());
        assertEquals("C", iterator.next());
        assertEquals("D", iterator.next());
        assertEquals("E", iterator.next());
        assertEquals("A", iterator.next());

        iterator = CircularListIterator.<String>builder().over(elements).startAt(0).maxSteps(10).iterator();
        assertEquals("A", iterator.next());//initialize the iterator
        assertEquals("B", iterator.next());
        assertEquals("C", iterator.next());
        assertEquals("D", iterator.next());
        assertEquals("E", iterator.next());
        assertEquals("A", iterator.next());
        assertEquals("B", iterator.next());
        assertEquals("C", iterator.next());
        assertEquals("D", iterator.next());
        assertEquals("E", iterator.next());
        assertFalse(iterator.hasNext());

        iterator = CircularListIterator.<String>builder().over(elements).maxEpochs(2).iterator();
        assertEquals("A", iterator.next());//initialize the iterator
        assertEquals("B", iterator.next());
        assertEquals("C", iterator.next());
        assertEquals("D", iterator.next());
        assertEquals("E", iterator.next());
        assertEquals("A", iterator.next());
        assertEquals("B", iterator.next());
        assertEquals("C", iterator.next());
        assertEquals("D", iterator.next());
        assertEquals("E", iterator.next());
        assertFalse(iterator.hasNext());

        iterator = CircularListIterator.<String>builder().over(elements).startAt(0).maxEpochs(2).iterator();
        assertEquals("A", iterator.next());//initialize the iterator
        assertEquals("B", iterator.next());
        assertEquals("C", iterator.next());
        assertEquals("D", iterator.next());
        assertEquals("E", iterator.next());
        assertEquals("A", iterator.next());
        assertEquals("B", iterator.next());
        assertEquals("C", iterator.next());
        assertEquals("D", iterator.next());
        assertEquals("E", iterator.next());
        assertFalse(iterator.hasNext());

        iterator = CircularListIterator.<String>builder().using(elements).iterator();
        assertEquals("A", iterator.next());//initialize the iterator
        assertEquals("B", iterator.next());
        assertEquals("C", iterator.next());
        assertEquals("D", iterator.next());
        assertEquals("E", iterator.next());
        assertEquals("A", iterator.next());
        assertFalse(elements == iterator.elements());

        iterator = CircularListIterator.<String>builder().using(new String[]{"A", "B", "C", "D", "E"}).iterator();
        assertEquals("A", iterator.next());//initialize the iterator
        assertEquals("B", iterator.next());
        assertEquals("C", iterator.next());
        assertEquals("D", iterator.next());
        assertEquals("E", iterator.next());
        assertEquals("A", iterator.next());

        SortedSet<String> orderedSet = new java.util.TreeSet<>(Arrays.asList("A", "B", "C", "D", "E"));
        iterator = CircularListIterator.<String>builder().using(orderedSet).iterator();
        assertEquals("A",iterator.next());//initialize the iterator
        assertEquals("B", iterator.next());
        assertEquals("C", iterator.next());
        assertEquals("D", iterator.next());
        assertEquals("E", iterator.next());

        assertThrows(java.lang.AssertionError.class, () -> CircularListIterator.<String>builder().using((List<String>)null).iterator());
        assertThrows(java.lang.AssertionError.class, () -> CircularListIterator.<String>builder().using((String[])null).iterator());
        assertThrows(java.lang.AssertionError.class, () -> CircularListIterator.<String>builder().using((Collection<String>)null).iterator());
        assertThrows(java.lang.AssertionError.class, () -> CircularListIterator.<String>builder().over((List<String>)null).iterator());
        assertThrows(java.lang.AssertionError.class, () -> CircularListIterator.<String>builder().startAt(-1).iterator());
        assertThrows(java.lang.AssertionError.class, () -> CircularListIterator.<String>builder().maxSteps(0).iterator());
        assertThrows(java.lang.AssertionError.class, () -> CircularListIterator.<String>builder().maxSteps(-1).iterator());
        assertThrows(java.lang.AssertionError.class, () -> CircularListIterator.<String>builder().maxEpochs(0).iterator());
        assertThrows(java.lang.AssertionError.class, () -> CircularListIterator.<String>builder().maxEpochs(-1).iterator());

        assertThrows(java.lang.IllegalStateException.class, () -> CircularListIterator.<String>builder().iterator().next());

        for(String s: CircularListIterator.<String>builder().using(elements).maxEpochs(1).iterable()){
            assertEquals(elements.get(elements.indexOf(s)), s);
        }
    }

    @Test
    void iterableOf(){
        List<String> elements = Arrays.asList("A", "B", "C", "D", "E");
        Iterator<String> iterator = CircularListIterator.iterableOf(elements).iterator();
        assertEquals("A", iterator.next());//initialize the iterator
        assertEquals("B", iterator.next());
        assertEquals("C", iterator.next());
        assertEquals("D", iterator.next());
        assertEquals("E", iterator.next());
        assertEquals("A", iterator.next());

        

        int idx = 0;
        for(String s: CircularListIterator.iterableOf(elements)){
            assertEquals(elements.get(idx), s);
            idx++;
            if(idx == elements.size()){
                break;
            }
        }

        idx = 0;
        for(String s:CircularListIterator.iterableOf(elements,2)){
            assertEquals(elements.get(idx+2), s);
            idx++;
            if(idx == elements.size()-2){
                break;
            }
        }

        idx = 0;
        for(String s:CircularListIterator.iterableOf(elements,0,5)){
            assertEquals(elements.get(idx), s);
            idx++;
        }
        assertEquals(5, idx);

        idx = 0;
        for(String s:CircularListIterator.<String>iterableOf(CircularListIterator.<String>builder().using(elements).maxEpochs(2))){
            assertEquals(elements.get(idx%5), s);
            idx++;
        }
        assertEquals(10, idx);
    }

    @Test
    void iterableOfEmpty(){
        Iterator<String> iterator = CircularListIterator.iterableOf(Collections.<String>emptyList()).iterator();
        assertFalse(iterator.hasNext());
    }

    // @Test
    // void listFromDelegates(){
    //     List<String> elements = Collections.unmodifiableList(Arrays.asList("A", "B", "C", "D", "E"));
    //     List<String> list = CircularListIterator.listFrom(elements);
        
    //     assertEquals(elements.size(), list.size());
    //     for(String s: elements){
    //         assertEquals(elements.get(list.indexOf(s)),list.get(list.indexOf(s)));
    //     }

    //     String[] listArray = list.toArray(new String[0]);
    //     String[] elementsArray = elements.toArray(new String[0]);

    //     for(int i = 0; i < elementsArray.length; i++){
    //         assertEquals(elementsArray[i], listArray[i]);
    //     }

    //     Object[] listObjArray = list.toArray();
    //     Object[] elementsObjArray = elements.toArray();

    //     for(int i = 0; i < elementsObjArray.length; i++){
    //         assertEquals(elementsObjArray[i], listObjArray[i]);
    //     }


    //     assertEquals(elements.isEmpty(), list.isEmpty());
    //     assertEquals(elements.contains("A"), list.contains("A"));
    //     assertEquals(elements.containsAll(Arrays.asList("A", "B")), list.containsAll(Arrays.asList("A", "B")));
    //     assertEquals(elements.indexOf("A"), list.indexOf("A"));
    //     assertEquals(elements.lastIndexOf("A"), list.lastIndexOf("A"));
    //     assertEquals(elements.subList(1, 3), list.subList(1, 3));
    //     assertEquals(elements.iterator().next(), list.iterator().next());
    //     assertEquals(elements.listIterator().next(), list.listIterator().next());
    //     assertEquals(elements.listIterator(2).next(), list.listIterator(2).next());
    //     assertEquals(elements.stream().count(), list.stream().count());
    //     assertEquals(elements.parallelStream().count(), list.parallelStream().count());
    //     assertEquals(elements.spliterator().getExactSizeIfKnown(), list.spliterator().getExactSizeIfKnown());
    //     assertThrows(java.lang.UnsupportedOperationException.class, () -> list.add("Z"));
    //     assertThrows(java.lang.UnsupportedOperationException.class, () -> list.remove("Z"));
    //     assertThrows(java.lang.UnsupportedOperationException.class, () -> list.addAll(Arrays.asList("Z", "Y")));
    //     assertThrows(java.lang.UnsupportedOperationException.class, () -> list.addAll(2, Arrays.asList("Z", "Y")));
    //     assertThrows(java.lang.UnsupportedOperationException.class, () -> list.addAll(2, new HashSet<>(Arrays.asList("Z", "Y"))));
    //     assertThrows(java.lang.UnsupportedOperationException.class, () -> list.clear());
    //     assertThrows(java.lang.UnsupportedOperationException.class, () -> list.removeAll(Arrays.asList("Z", "Y")));
    //     assertThrows(java.lang.UnsupportedOperationException.class, () -> list.retainAll(Arrays.asList("Z", "Y")));
    //     assertThrows(java.lang.UnsupportedOperationException.class, () -> list.set(2, "Z"));
    //     assertThrows(java.lang.UnsupportedOperationException.class, () -> list.add(2, "Z"));
    //     assertThrows(java.lang.UnsupportedOperationException.class, () -> list.remove(2));
    // }

    // @Test
    // void listFromLoop(){
    //     List<String> elements = Arrays.asList("A", "B", "C", "D", "E");
    //     List<String> list = CircularListIterator.listFrom(elements);
        
    //     int maxLoop = 20;

    //     for(String s: list){
    //         assertEquals(elements.get(list.indexOf(s)), s);
    //         if(maxLoop-- == 0){
    //             break;
    //         }
    //     }
    // }
}
