package com.wortcook.util.impl;

import static com.wortcook.Wort.withLock;

import java.util.Collection;
import java.util.List;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public class ConcurrentCircularListIteratorImpl<T> extends CircularListIteratorImpl<T> {
    private final ReentrantReadWriteLock lock = new ReentrantReadWriteLock();

    public ConcurrentCircularListIteratorImpl(final List<T> elements) {
        super(elements);
    }

    public ConcurrentCircularListIteratorImpl(final List<T> elements, final int index) {
        super(elements, index);
    }

    public ConcurrentCircularListIteratorImpl(final List<T> elements, final int index, final int step) {
        super(elements, index, step);
    }

    ///////////////////////////////////////////////////////////////////
    // CircularListIterator methods
    ///////////////////////////////////////////////////////////////////
    /**
     * The 'traditional' version of hasNext for an iterator. If the iterator is at the end of the list, this will return false.
     * If the list is empty this will return false.
     * @return true if there is a next element, false otherwise, false if the list is empty.
     */
    @Override
    public boolean hasNextNoWrap(){
        return withLock(lock.readLock(), super::hasNextNoWrap);
    }

    /**
     * The 'traditional' version of hasPrevious for an iterator. If the iterator is at the beginning of the list, this will return false.
     * @return true if there is a previous element, false otherwise, false if the list is empty.
     */
    @Override
    public boolean hasPreviousNoWrap() {
        return withLock(lock.readLock(), super::hasPreviousNoWrap);
    }


    ///////////////////////////////////////////////////////////////////////////
    // Iterator methods
    ///////////////////////////////////////////////////////////////////////////
    /**
     * Returns the next element in the list. If hasNext() returns false, this will throw a NoSuchElementException.
     * @return The next element in the list.
     */
    @Override
    public T next() {
        return withLock(lock.writeLock(), super::next);
    }

    @Override
    public boolean hasNext() {
        return withLock(lock.readLock(), super::hasNext);
    }

    /**
     * Removes the current element from the list. If the iterator is not initialized by either calling next(), previous(),
     * nextElement(), or previousElement() first, this will throw an IllegalStateException.
     * After remove is called the iterator is no longer initialized and a call to next(), previous(), nextElement(),
     * or previousElement() must be made before any further changes can be made; remove(), set(), or add().
     * <br><br><br>
     * <code>
     *    CircularListIterator&lt;String&gt; iterator = new CircularListIterator&lt;&gt;(new ArrayList&lt;String&gt;(Arrays.asList("A", "B", "C")));<br>
     *    iterator.next(); //iterator now "points" to "A"<br>
     *    iterator.next(); //iterator now "points" to "B"<br>
     *    iterator.remove(); //removes "B" from the list<br>
     *    iterator.next(); //iterator now "points" to "C"<br>
     * <br>
     * //note that iteratoror.prev() would have returned A.<br>
     * </code><br>
     * Another way to think about it is that the remove leaves a hole where iterator is currently pointing. So
     * in order to make any further changes the iterator must be moved to a valid index.
     */
    @Override
    public void remove() {
        withLock(lock.writeLock(), super::remove);
    }


    /////////////////////////////////////////////////////////////////
    // ListIterator methods
    /////////////////////////////////////////////////////////////////
    @Override
    public void add(final T t) {
        withLock(lock.writeLock(), () -> super.add(t));
    }

    /**
     * Returns the index of the next element in the list. If the iterator is at the end of the list, this will return 0.
     * If the list is empty, this will return 0.
     * @return The index of the next element in the list.
     */
    @Override
    public int nextIndex() {
        return withLock(lock.readLock(), super::nextIndex);
    }


    /**
     * Returns if there is a previous element in the list. If the maximum number of steps is reached, this will return false.
     * If the list is at the beginning of the list, this will return true as the next element will be the last element in the list.
     * If the list is empty this will return false. Note, once max steps is reached, this will return false if the
     * iterator is at the beginning of the list.
     * @return true if there is a previous element, false otherwise, false if the list is empty.
     */
    @Override
    public boolean hasPrevious() {
        return withLock(lock.readLock(), super::hasPrevious);
    }


    /**
     * Returns the previous element in the list. If hasPrevious() returns false, this will throw a NoSuchElementException.
     * If the iterator is at the beginning of the list, this will return the last element in the list. If this is the first
     * call to previous() then the last element in the list will be returned.
     * @return The previous element in the list.
     */
    @Override
    public T previous() {
        return withLock(lock.writeLock(), super::previous);
    }


    /**
     * Returns the index of the previous element in the list. If the iterator is at the beginning of the list, this will return the last index.
     * If the list is empty, this will return 0.
     * @return The index of the previous element in the list.
     */
    @Override
    public int previousIndex() {
        return withLock(lock.readLock(), super::previousIndex);
    }


    /**
     * Sets the current element to the passed element. If the iterator is not initialized by either calling next(), previous(),
     * nextElement(), or previousElement() first, this will throw an IllegalStateException.
     * The iterator still "points" to the current element after set is called.
     * @param t - The element to set the current element to.
     */
    @Override
    public void set(final T t) {
        withLock(lock.writeLock(), () -> super.set(t));
    }



    ///////////////////////////////////////////////////////////////////////////
    // PositionalListIterator methods
    ///////////////////////////////////////////////////////////////////////////
    /**
     * Returns the current element in the list. If the iterator is not initialized by either calling next(), previous(),
     * nextElement(), or previousElement() first, this will throw an IllegalStateException.
     * @return The current element in the list.
     */
    @Override
    public T at() {
        return withLock(lock.readLock(), super::at);
    }

    /**
     * Writes the passed element ahead of the current element. If the iterator is not initialized by either calling next(), previous(),
     * nextElement(), or previousElement() first, this will throw an IllegalStateException. After calling writeAhead the iterator
     * the next call to next() or nextElement() will return the element that was written ahead.
     * @param t - The element to write ahead of the current element.
     */
    @Override
    public void addNext(final T t) {
        withLock(lock.writeLock(), () -> super.addNext(t));
    }

    /**
     * Writes the passed elements ahead of the current element. If the iterator is not initialized by either calling next(), previous(),
     * nextElement(), or previousElement() first, this will throw an IllegalStateException. After calling writeAllAhead the iterator
     * the next call to next() or nextElement() will return the first element that was written ahead. Note that the order that the 
     * elements are written is the order that they are in the collection.
     * @param c - The elements to write ahead of the current element.
     */
    @Override
    public void addAllNext(final Collection<T> c) {
        withLock(lock.writeLock(), () -> super.addAllNext(c));
    }

    /**
     * Writes the passed element before the current element. If the iterator is not initialized by either calling next(), previous(),
     * nextElement(), or previousElement() first, this will throw an IllegalStateException. After calling addPrevious the iterator
     * the next call to previous() or previousElement() will return the element.
     * @param t - The element to write before the current element.
     */
    @Override
    public void addPrevious(final T element) {
        withLock(lock.writeLock(), () -> super.addPrevious(element));
    }

    /**
     * Adds all the elements in the passed collection to the list. If the iterator is not initialized by either calling next(), previous(),
     * nextElement(), or previousElement() first, this will throw an IllegalStateException. I.e. you cannot add elements
     * to an iterator created over an empty list. The elements are added before the current element/index. If the iterator is at the
     * beginning of the list, the elements are added at the end of the list. This is slightly different than the behavior of the ListIterator
     * addAll method in that for ListIterator the elements would be added to the beginning of the list though the iterator would not change position.
     * This was a choice to keep the behavior of the iterator consistent with the rest of the methods. It also simplified the math.
     * @param c - The collection of elements to add to the list.
     */
    @Override
    public void addAllPrevious(final Collection<T> c) {
        withLock(lock.writeLock(), () -> super.addAllPrevious(c));
    }

    /**
     * Returns the list of elements that the iterator is iterating over. This is a reference to the list passed in the constructor
     * so changes to the list will be reflected in the iterator.
     * @return
     */
    @Override
    public List<T> elements() {
        return withLock(lock.readLock(), super::elements);
    }

    /**
     * Resets the iterator to the same state as if it were newly created. The iterator will start at the "beginning" of the list
     * and the step count will be reset to 0.
     */
    @Override
    public void reset() {
        withLock(lock.writeLock(), super::reset);
    }

    ///////////////////////////////////////////////////////////////////////////
    // OptionalListIterator methods
    ///////////////////////////////////////////////////////////////////////////
    //none
    //default methods in the interface are sufficient

    ///////////////////////////////////////////////////////////////////////////
    // Counter methods
    ///////////////////////////////////////////////////////////////////////////
    /**
     * Returns the current step count, i.e. how many times next(), nextElement(), previous(), or previousElement() has been called.
     * @return The current step count.
     */
    @Override
    public Integer getCount() {
        return withLock(lock.readLock(), super::getCount);
    }

    @Override
    public Integer getLimit() {
        return withLock(lock.readLock(), super::getLimit);
    }

    /**
     * Resets the step count to 0. This can be useful if the maximum number of steps is reached and the iterator needs to be used again.
     */
    @Override
    public void resetCount() {
        withLock(lock.writeLock(), super::resetCount);
    }

    /**
     * Increments the step count by 1. This is called automatically by next(), nextElement(), previous(), and previousElement().
     */
    @Override
    public void count() {
        withLock(lock.writeLock(), super::count);
    }
}
