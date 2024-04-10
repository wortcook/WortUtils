package com.wortcook.util;

import java.util.Arrays;
import java.util.List;
import java.util.ListIterator;
import java.util.Iterator;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CircularQueueListTest {

    @Test
    void constructorTests() {
        CircularQueueList<Integer> queue1 = new CircularQueueList<>();
        assertEquals(0, queue1.size());


        CircularQueueList<Integer> queue2 = new CircularQueueList<>(Arrays.asList(1, 2, 3));
        assertEquals(3, queue2.size());

        CircularQueueList<Integer> queue3 = new CircularQueueList<>(Arrays.asList(1, 2, 3, 4, 5));
        assertEquals(5, queue3.size());

        CircularQueueList<Integer> queue4 = new CircularQueueList<>(Arrays.asList(1, 2, 3, 4, 5, 6, 7, 8, 9, 10));
        assertEquals(10, queue4.size());
    }

    @Test
    void add() {
        CircularQueueList<Integer> queue = new CircularQueueList<>();
        assertTrue(queue.add(1));
        assertTrue(queue.add(2));
        assertTrue(queue.add(3));
        assertEquals(3, queue.size());

        queue.add(0, 4);
        assertEquals(4, queue.size());
        assertEquals(4, queue.get(0));
        assertEquals(1, queue.get(1));
        assertEquals(2, queue.get(2));
        assertEquals(3, queue.get(3));

        queue.add(4, 5);
        assertEquals(5, queue.size());
        assertEquals(4, queue.get(0));
        assertEquals(1, queue.get(1));
        assertEquals(2, queue.get(2));
        assertEquals(3, queue.get(3));
        assertEquals(5, queue.get(4));

        //wrap around
        queue.add(7, 6);
        assertEquals(6, queue.size());
        assertEquals(4, queue.get(0));
        assertEquals(6, queue.get(1));
        assertEquals(1, queue.get(2));
        assertEquals(2, queue.get(3));
        assertEquals(3, queue.get(4));
        assertEquals(5, queue.get(5));

        queue.add(-2, 7);
        assertEquals(7, queue.size());
        assertEquals(4, queue.get(0));
        assertEquals(6, queue.get(1));
        assertEquals(7, queue.get(2));
        assertEquals(1, queue.get(3));
        assertEquals(2, queue.get(4));
        assertEquals(3, queue.get(5));
        assertEquals(5, queue.get(6));
    }

    @Test
    void offer() {
        CircularQueueList<Integer> queue = new CircularQueueList<>();
        assertTrue(queue.offer(1));
        assertTrue(queue.offer(2));
        assertTrue(queue.offer(3));
        assertEquals(3, queue.size());

        assertEquals(3, queue.get(0));
        assertEquals(2, queue.get(1));
        assertEquals(1, queue.get(2));
    }

    @Test
    void remove() {
        CircularQueueList<String> queue = new CircularQueueList<>(Arrays.asList("1","2","3","4","5"));
        assertEquals(5, queue.size());

        assertEquals("1", queue.remove());
        assertEquals(4, queue.size());
        assertEquals("2", queue.get(0));
        assertEquals("3", queue.get(1));
        assertEquals("4", queue.get(2));
        assertEquals("5", queue.get(3));

        assertEquals("2", queue.remove());
        assertEquals(3, queue.size());
        assertEquals("3", queue.get(0));
        assertEquals("4", queue.get(1));
        assertEquals("5", queue.get(2));

        assertEquals("3", queue.remove());
        assertEquals(2, queue.size());
        assertEquals("4", queue.get(0));
        assertEquals("5", queue.get(1));

        assertEquals("4", queue.remove());
        assertEquals(1, queue.size());
        assertEquals("5", queue.get(0));

        assertEquals("5", queue.remove());
        assertEquals(0, queue.size());
        assertTrue(queue.isEmpty());

        //remove by object
        queue.addAll(Arrays.asList("1", "2", "3", "4", "5"));
        assertEquals(5, queue.size());
        assertTrue(queue.remove("3"));
        assertEquals("1", queue.get(0));
        assertEquals("2", queue.get(1));
        assertEquals("4", queue.get(2));
        assertEquals("5", queue.get(3));

        //remove by index
        assertEquals("1", queue.remove(0));
        assertEquals(3, queue.size());
        assertEquals("2", queue.get(0));
        assertEquals("4", queue.get(1));
        assertEquals("5", queue.get(2));

        assertEquals("4", queue.remove(1));
        assertEquals(2, queue.size());
        assertEquals("2", queue.get(0));
        assertEquals("5", queue.get(1));

        assertEquals("5", queue.remove(1));
        assertEquals(1, queue.size());
        assertEquals("2", queue.get(0));

        assertEquals("2", queue.remove(0));
        assertEquals(0, queue.size());
        assertTrue(queue.isEmpty());

        //remove by index with wrap around
        queue.addAll(Arrays.asList("1", "2", "3", "4", "5"));
        assertEquals(5, queue.size());
        assertEquals("1", queue.remove(5));
        assertEquals(4, queue.size());
        assertEquals("2", queue.get(0));
        assertEquals("3", queue.get(1));
        assertEquals("4", queue.get(2));
        assertEquals("5", queue.get(3));

        assertEquals("2", queue.remove(256));
        assertEquals(3, queue.size());
        assertEquals("3", queue.get(0));
        assertEquals("4", queue.get(1));
        assertEquals("5", queue.get(2));

        assertEquals("5", queue.remove(-2));
        assertEquals(2, queue.size());
        assertEquals("3", queue.get(0));
        assertEquals("4", queue.get(1));

        assertEquals("4", queue.remove(100001));
        assertEquals(1, queue.size());
        assertEquals("3", queue.get(0));

        assertEquals("3", queue.remove(-1));
        assertEquals(0, queue.size());
        assertTrue(queue.isEmpty());
    }

    @Test
    void poll(){
        CircularQueueList<String> queue = new CircularQueueList<>(Arrays.asList("1","2","3","4","5"));
        assertEquals(5, queue.size());

        assertEquals("1", queue.poll());
        assertEquals(4, queue.size());
        assertEquals("2", queue.get(0));
        assertEquals("3", queue.get(1));
        assertEquals("4", queue.get(2));
        assertEquals("5", queue.get(3));

        assertEquals("2", queue.poll());
        assertEquals(3, queue.size());
        assertEquals("3", queue.get(0));
        assertEquals("4", queue.get(1));
        assertEquals("5", queue.get(2));

        assertEquals("3", queue.poll());
        assertEquals(2, queue.size());
        assertEquals("4", queue.get(0));
        assertEquals("5", queue.get(1));

        assertEquals("4", queue.poll());
        assertEquals(1, queue.size());
        assertEquals("5", queue.get(0));

        assertEquals("5", queue.poll());
        assertEquals(0, queue.size());
        assertTrue(queue.isEmpty());
    }

    @Test
    void element() {
        CircularQueueList<String> queue = new CircularQueueList<>(Arrays.asList("1","2","3","4","5"));
        assertEquals("1", queue.element());
        assertEquals(5, queue.size());
    }

    @Test
    void peek() {
        CircularQueueList<String> queue = new CircularQueueList<>(Arrays.asList("1","2","3","4","5"));
        assertEquals("1", queue.peek());
        assertEquals(5, queue.size());
    }

    @Test
    void size() {
        CircularQueueList<Integer> queue = new CircularQueueList<>();
        assertEquals(0, queue.size());

        queue.add(1);
        assertEquals(1, queue.size());

        queue.add(2);
        assertEquals(2, queue.size());

        queue.add(3);
        assertEquals(3, queue.size());

        queue.remove();
        assertEquals(2, queue.size());

        queue.remove();
        assertEquals(1, queue.size());

        queue.remove();
        assertEquals(0, queue.size());
    }

    @Test
    void isEmpty() {
        CircularQueueList<Integer> queue = new CircularQueueList<>();
        assertTrue(queue.isEmpty());

        queue.add(1);
        assertFalse(queue.isEmpty());

        queue.remove();
        assertTrue(queue.isEmpty());
    }

    @Test
    void contains() {
        CircularQueueList<Integer> queue = new CircularQueueList<>(Arrays.asList(1, 2, 3));
        assertTrue(queue.contains(1));
        assertTrue(queue.contains(2));
        assertTrue(queue.contains(3));
        assertFalse(queue.contains(4));
    }

    @Test
    void iterator() {
        CircularQueueList<Integer> queue = new CircularQueueList<>(Arrays.asList(1, 2, 3));
        int i = 0;
        Iterator<Integer> iterator = queue.iterator();
        while(iterator.hasNext()){
            assertEquals((i % 3)+1, iterator.next());
            i++;
            if(i > 100){
                break;
            }
        }
    }

    @Test
    void ciruclarListIterator(){
        CircularQueueList<Integer> queue = new CircularQueueList<>(Arrays.asList(1, 2, 3));
        int i = 0;
        CircularListIterator<Integer> iterator = queue.ciruclarListIterator();
        while(iterator.hasNext()){
            assertEquals((i % 3)+1, iterator.next());
            i++;
            if(i > 100){
                break;
            }
        }
    }

    @Test
    void toArray() {
        CircularQueueList<Integer> queue = new CircularQueueList<>(Arrays.asList(1, 2, 3));
        Object[] array = queue.toArray();
        assertEquals(3, array.length);
        assertEquals(1, array[0]);
        assertEquals(2, array[1]);
        assertEquals(3, array[2]);
    }

    @Test
    void toArrayWithArgument() {
        CircularQueueList<Integer> queue = new CircularQueueList<>(Arrays.asList(1, 2, 3));
        Integer[] array = new Integer[3];
        queue.toArray(array);
        assertEquals(3, array.length);
        assertEquals(1, array[0]);
        assertEquals(2, array[1]);
        assertEquals(3, array[2]);
    }

    @Test
    void addAll() {
        CircularQueueList<Integer> queue = new CircularQueueList<>();
        assertTrue(queue.addAll(Arrays.asList(1, 2, 3)));
        assertEquals(3, queue.size());
        assertEquals(1, queue.get(0));
        assertEquals(2, queue.get(1));
        assertEquals(3, queue.get(2));
    }

    @Test
    void containsAll() {
        CircularQueueList<Integer> queue = new CircularQueueList<>(Arrays.asList(1, 2, 3));
        assertTrue(queue.containsAll(Arrays.asList(1, 2)));
        assertTrue(queue.containsAll(Arrays.asList(2, 3)));
        assertTrue(queue.containsAll(Arrays.asList(1, 2, 3)));
        assertFalse(queue.containsAll(Arrays.asList(1, 2, 3, 4)));
    }

    @Test
    void removeAll() {
        CircularQueueList<Integer> queue = new CircularQueueList<>(Arrays.asList(1, 2, 3, 4, 5));
        assertTrue(queue.removeAll(Arrays.asList(1, 2)));
        assertEquals(3, queue.size());
        assertEquals(3, queue.get(0));
        assertEquals(4, queue.get(1));
        assertEquals(5, queue.get(2));
    }

    @Test
    void retainAll() {
        CircularQueueList<Integer> queue = new CircularQueueList<>(Arrays.asList(1, 2, 3, 4, 5));
        assertTrue(queue.retainAll(Arrays.asList(1, 2)));
        assertEquals(2, queue.size());
        assertEquals(1, queue.get(0));
        assertEquals(2, queue.get(1));
    }

    @Test
    void clear() {
        CircularQueueList<Integer> queue = new CircularQueueList<>(Arrays.asList(1, 2, 3, 4, 5));
        queue.clear();
        assertTrue(queue.isEmpty());
    }

    @Test
    void addAllAtIndex() {
        CircularQueueList<Integer> queue = new CircularQueueList<>(Arrays.asList(1, 2, 3));
        queue.addAll(1, Arrays.asList(4, 5));
        assertEquals(5, queue.size());
        assertEquals(1, queue.get(0));
        assertEquals(4, queue.get(1));
        assertEquals(5, queue.get(2));
        assertEquals(2, queue.get(3));
        assertEquals(3, queue.get(4));

        queue.addAll(5, Arrays.asList(6, 7));
        assertEquals(7, queue.size());
        assertEquals(1, queue.get(0));
        assertEquals(4, queue.get(1));
        assertEquals(5, queue.get(2));
        assertEquals(2, queue.get(3));
        assertEquals(3, queue.get(4));
        assertEquals(6, queue.get(5));
        assertEquals(7, queue.get(6));

        queue.addAll(24, Arrays.asList(8, 9));
        assertEquals(9, queue.size());
        assertEquals(8, queue.get(0));
        assertEquals(9, queue.get(1));
        assertEquals(1, queue.get(2));
        assertEquals(4, queue.get(3));
        assertEquals(5, queue.get(4));
        assertEquals(2, queue.get(5));
        assertEquals(3, queue.get(6));
        assertEquals(6, queue.get(7));
        assertEquals(7, queue.get(8));


    }

    @Test
    void get() {
        CircularQueueList<Integer> queue = new CircularQueueList<>(Arrays.asList(1, 2, 3, 4, 5));
        assertEquals(1, queue.get(0));
        assertEquals(2, queue.get(1));
        assertEquals(3, queue.get(2));
        assertEquals(4, queue.get(3));
        assertEquals(5, queue.get(4));

        assertEquals(1, queue.get(5));
        assertEquals(2, queue.get(6));
        assertEquals(3, queue.get(7));
        assertEquals(4, queue.get(8));
        assertEquals(5, queue.get(9));

        assertEquals(1, queue.get(-5));
        assertEquals(5, queue.get(-4));
        assertEquals(4, queue.get(-3));
        assertEquals(3, queue.get(-2));
        assertEquals(2, queue.get(-1));
    }

    @Test
    void set(){
        CircularQueueList<Integer> queue = new CircularQueueList<>(Arrays.asList(1, 2, 3, 4, 5));
        assertEquals(1, queue.set(0, 6));
        assertEquals(6, queue.get(0));
        
        assertEquals(2, queue.set(1, 7));
        assertEquals(7, queue.get(1));

        assertEquals(3, queue.set(2, 8));
        assertEquals(8, queue.get(2));

        assertEquals(4, queue.set(3, 9));
        assertEquals(9, queue.get(3));

        assertEquals(5, queue.set(4, 10));
        assertEquals(10, queue.get(4));

        assertEquals(6, queue.set(5, 11));
        assertEquals(11, queue.get(0));

        assertEquals(7, queue.set(6, 12));
        assertEquals(12, queue.get(1));

        assertEquals(8, queue.set(7, 13));
        assertEquals(13, queue.get(2));

        assertEquals(9, queue.set(8, 14));
        assertEquals(14, queue.get(3));

        assertEquals(10, queue.set(9, 15));
        assertEquals(15, queue.get(4));

        assertEquals(11, queue.set(-5, 16));
        assertEquals(16, queue.get(0));

        assertEquals(15, queue.set(-4, 17));
        assertEquals(17, queue.get(4));

        assertEquals(14, queue.set(-3, 18));
        assertEquals(18, queue.get(3));

        assertEquals(13, queue.set(-2, 19));
        assertEquals(19, queue.get(2));

        assertEquals(12, queue.set(-1, 20));
        assertEquals(20, queue.get(1));
   
    }

    @Test
    void indexOf(){
        CircularQueueList<Integer> queue = new CircularQueueList<>(Arrays.asList(1, 2, 3, 4, 5));
        assertEquals(0, queue.indexOf(1));
        assertEquals(1, queue.indexOf(2));
        assertEquals(2, queue.indexOf(3));
        assertEquals(3, queue.indexOf(4));
        assertEquals(4, queue.indexOf(5));
        assertEquals(-1, queue.indexOf(6));
    }

    @Test
    void lastIndexOf(){
        CircularQueueList<Integer> queue = new CircularQueueList<>(Arrays.asList(1, 2, 3, 4, 5));
        assertEquals(0, queue.lastIndexOf(1));
        assertEquals(1, queue.lastIndexOf(2));
        assertEquals(2, queue.lastIndexOf(3));
        assertEquals(3, queue.lastIndexOf(4));
        assertEquals(4, queue.lastIndexOf(5));
        assertEquals(-1, queue.lastIndexOf(6));
    }

    @Test
    void listIterator(){
        CircularQueueList<Integer> queue = new CircularQueueList<>(Arrays.asList(1, 2, 3));
        int i = 0;
        ListIterator<Integer> iterator = queue.listIterator();
        while(iterator.hasNext()){
            assertEquals((i % 3)+1, iterator.next());
            i++;
            if(i > 100){
                break;
            }
        }

        i = 0;
        iterator = queue.listIterator(0);
        while(iterator.hasNext()){
            assertEquals((i % 3)+1, iterator.next());
            i++;
            if(i > 100){
                break;
            }
        }

        i = 0;
        iterator = queue.listIterator(1);
        while(iterator.hasNext()){
            assertEquals(((i+1) % 3)+1, iterator.next());
            i++;
            if(i > 100){
                break;
            }
        }

        i = 0;
        iterator = queue.listIterator(2);
        while(iterator.hasNext()){
            assertEquals(((i+2) % 3)+1, iterator.next());
            i++;
            if(i > 100){
                break;
            }
        }

        i = 0;
        iterator = queue.listIterator(15);
        while(iterator.hasNext()){
            assertEquals((i % 3)+1, iterator.next());
            i++;
            if(i > 100){
                break;
            }
        }


        i = 0;
        iterator = queue.listIterator(-2);
        while(iterator.hasNext()){
            assertEquals(((i+2) % 3)+1, iterator.next());
            i++;
            if(i > 100){
                break;
            }
        }

    }
    
    @Test
    void subList(){
        CircularQueueList<Integer> queue = new CircularQueueList<>(Arrays.asList(1, 2, 3, 4, 5));
        List<Integer> subList = queue.subList(1, 4);
        assertEquals(3, subList.size());
        assertEquals(2, subList.get(0));
        assertEquals(3, subList.get(1));
        assertEquals(4, subList.get(2));

        subList = queue.subList(3, 1);
        assertEquals(3, subList.size());
        assertEquals(4, subList.get(0));
        assertEquals(5, subList.get(1));
        assertEquals(1, subList.get(2));

        subList = queue.subList(4, 1);
        assertEquals(2, subList.size());
        assertEquals(5, subList.get(0));
        assertEquals(1, subList.get(1));

        subList = queue.subList(-4, 5);
        assertEquals(1, subList.size());
        assertEquals(5, subList.get(0));

        subList = queue.subList(-1, -5);
        assertEquals(4, subList.size());
        assertEquals(2, subList.get(0));
        assertEquals(3, subList.get(1));
        assertEquals(4, subList.get(2));
        assertEquals(5, subList.get(3));
    }
}

