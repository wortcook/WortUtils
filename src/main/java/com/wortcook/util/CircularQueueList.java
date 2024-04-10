package com.wortcook.util;

import java.util.Queue;
import java.util.Set;
import java.util.AbstractQueue;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ArrayList;
import java.util.ListIterator;

/**
 * A circular queue list implementation that implements both the Queue and List interfaces.
 * This class is not thread-safe.
 */
public class CircularQueueList<T> implements Queue<T>, List<T> {
    private final List<T> elements;

    public CircularQueueList() {
        elements = new ArrayList<>();
    }

    public CircularQueueList(Collection<T> elements) {
        this.elements = new ArrayList<>(elements);
    }

    // Implement methods for Queue interface

    @Override
    public boolean add(T t) {
        return elements.add(t);
    }

    @Override
    public boolean offer(T t) {
        elements.add(0, t);
        return true;
    }

    @Override
    public T remove() {
        return elements.remove(0);
    }

    @Override
    public T poll() {
        return elements.remove(0);
    }

    @Override
    public T element() {
        return elements.get(0);
    }

    @Override
    public T peek() {
        return elements.get(0);
    }

    // Implement methods for Collection interface

    @Override
    public int size() {
        return elements.size();
    }

    @Override
    public boolean isEmpty() {
        return elements.isEmpty();
    }

    @Override
    public boolean contains(Object o) {
        return elements.contains(o);
    }

    @Override
    public Iterator<T> iterator() {
        return new CircularListIterator<T>(elements);
    }

    public CircularListIterator<T> ciruclarListIterator() {
        return new CircularListIterator<T>(elements);
    }

    @Override
    public Object[] toArray() {
        return elements.toArray();
    }

    @Override
    public <T1> T1[] toArray(T1[] a) {
        return elements.toArray(a);
    }

    @Override
    public boolean addAll(Collection<? extends T> c) {
        return elements.addAll(c);
    }

    @Override
    public boolean remove(Object o) {
        return elements.remove(o);
    }

    @Override
    public boolean containsAll(Collection<?> c) {
        return elements.containsAll(c);
    }

    @Override
    public boolean removeAll(Collection<?> c) {
        return elements.removeAll(c);
    }

    @Override
    public boolean retainAll(Collection<?> c) {
        return elements.retainAll(c);
    }

    @Override
    public void clear() {
        elements.clear();
    }

    @Override
    public boolean addAll(int index, Collection<? extends T> c) {
        return elements.addAll(wrapIndexToLimit(this, index), c);
    }

    @Override
    public void add(int index, T element) {
        elements.add(wrapIndexToLimit(this, index), element);
    }
    
    @Override
    public T get(int index) {
        return elements.get(wrapIndex(this, index));
    }
    
    @Override
    public T set(int index, T element) {
        return elements.set(wrapIndex(this, index), element);
    }
    
    @Override
    public T remove(int index) {
        return elements.remove(wrapIndex(this, index));
    }
    
    @Override
    public int indexOf(Object o) {
        return elements.indexOf(o);
    }
    
    @Override
    public int lastIndexOf(Object o) {
        return elements.lastIndexOf(o);
    }
    
    @Override
    public ListIterator<T> listIterator() {
        return new CircularListIterator<T>(elements);
    }
    
    @Override
    public ListIterator<T> listIterator(final int index) {
        return new CircularListIterator<T>(elements, wrapIndex(this, index));
    }
    
    @Override
    public List<T> subList(final int fromIndex, final int toIndex) {
        final int startIdx = wrapIndex(this, fromIndex);
        final int endIdx = wrapIndexToLimit(this, toIndex);

        if (startIdx < endIdx) {
            return elements.subList(startIdx, endIdx);
        } else {
            final List<T> subList = new ArrayList<>();
            subList.addAll(elements.subList(startIdx, elements.size()));
            subList.addAll(elements.subList(0, endIdx));
            return subList;
        }
    }

    public static final int wrapIndex(final Collection<?> queue, final int index) {
        return Math.abs(index) % queue.size();
    }

    public static final int wrapIndexToLimit(final Collection<?> queue, final int index){
        return Math.abs(index) % (queue.size()+1);
    }
}
