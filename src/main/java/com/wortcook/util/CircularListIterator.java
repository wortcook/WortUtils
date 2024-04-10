package com.wortcook.util;

import java.util.List;
import java.util.ListIterator;
import java.util.NoSuchElementException;

public class CircularListIterator<T> implements ListIterator<T> {
    private final List<T> elements;
    private Integer currentIndex = null;
    private int starterIdx;

    public CircularListIterator(List<T> elements) {
        this.elements = elements;
        this.starterIdx = 0;
    }

    public CircularListIterator(List<T> elements, final int index) {
        this.elements = elements;
        this.starterIdx = index % elements.size();
    }

    @Override
    public boolean hasNext() {
        return !elements.isEmpty();
    }

    @Override
    public T next() {
        if (!hasNext()) {
            throw new NoSuchElementException();
        }

        final int nextIndex = this.nextIndex();
        final T element = elements.get(nextIndex);
        currentIndex = nextIndex;

        return element;
    }

    @Override
    public boolean hasPrevious() {
        return !elements.isEmpty();
    }

    @Override
    public T previous() {
        if (!hasPrevious()) {
            throw new NoSuchElementException();
        }

        final int prevIndex = this.previousIndex();
        final T element = elements.get(prevIndex);
        currentIndex = prevIndex;

        return element;
    }

    @Override
    public int nextIndex() {
        return (null == currentIndex) ? starterIdx : (currentIndex + 1)%elements.size();
    }

    @Override
    public int previousIndex() {
        return (null == currentIndex) ? starterIdx : (0 == currentIndex) ? elements.size() - 1 : currentIndex - 1;
    }

    @Override
    public void remove() {
        checkIndex();
        elements.remove((int)currentIndex);
        starterIdx = currentIndex;
        currentIndex = null;
    }

    @Override
    public void set(T t) {
        checkIndex();
        elements.set(currentIndex, t);
    }

    @Override
    public void add(T t) {
        checkIndex();
        elements.add(currentIndex, t);
        currentIndex++;
    }

    protected void checkIndex() {
        if(null == currentIndex) {
            throw new IllegalStateException("Iterator not initialized, call next or previous first.");
        }
    }
}
