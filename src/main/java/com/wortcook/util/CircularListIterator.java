package com.wortcook.util;

import java.util.List;
import java.util.ArrayList;
import java.util.Collection;
import java.util.ListIterator;
import java.util.NoSuchElementException;
import java.util.Optional;

/**
 * CircularListIterator is a ListIterator that wraps around a List and allows for circular iteration.
 * Depending on the constructor, the iterator can start at a specific index and iterate through the list
 * or start at the beginning of the list. Once the iterator reaches the end of the list it will wrap around
 * to the beginning of the list and vice versa. So unlike a regular ListIterator, the CircularListIterator
 * will never throw a NoSuchElementException unless the list is empty or the maximum number of steps is reached.
 * 
 * A maximum number of steps can be set to prevent long infinite loops. The default maximum number of steps is twice
 * the initial list passed in.
 * Once the maximum number of steps is reached the iterator starts behaving like a regular ListIterator.
 * So hasNext() and hasPrevious() will return false and next() and previous() will throw a NoSuchElementException if
 * the iterator is at the end or beginning of the list respectively. The iterator can still be used to get the current
 * element, remove the current element, set the current element, and add an element to the list. The iterator will need
 * to be moved to a valid index before any further changes can be made.
 * expected. The step count can be reset using resetStepCount() once the iterator reaches the max steps.
 */
public class CircularListIterator<T> implements ListIterator<T> {
    private final List<T> elements;
    private Integer currentIndex = null;
    private int starterIdx;
    private int stepCount = 0;
    private final int maxAllowedSteps;

    /**
     * Creates a CircularListIterator that starts at the beginning of elements
     * @param elements
     */
    public CircularListIterator(final T[] elements) {
        this(new ArrayList<T>(List.of(elements)));
    }

    public CircularListIterator(final T[] elements, final int index) {
        this(new ArrayList<T>(List.of(elements)), index);
    }

    public CircularListIterator(final T[] elements, final int index, final int maxSteps) {
        this(new ArrayList<T>(List.of(elements)), index, maxSteps);
    }

    public CircularListIterator(final Collection<T> elements) {
        this(new ArrayList<T>(elements));
    }

    public CircularListIterator(final Collection<T> elements, final int index) {
        this(new ArrayList<T>(elements), index);
    }

    public CircularListIterator(final Collection<T> elements, final int index, final int maxSteps) {
        this(new ArrayList<T>(elements), index, maxSteps);
    }

    /**
     * Creates a CircularListIterator that starts at the beginning of the passed list.
     * @param elements
     */
    public CircularListIterator(final List<T> elements) {
        this(new ArrayList<>(), 0);
    }

    /**
     * Creates a CircularListIterator that starts at the specified index of the passed list.
     * @param elements - The list of elements to iterate through.
     * @param index - The index to start at. When next or previous is first called this will be the index of the element returned.
     *                A value of 0 will start at the beginning of the list.
     */
    public CircularListIterator(final List<T> elements, final int index) {
        this(elements, index, 2*elements.size());
    }

    /**
     * Creates a CircularListIterator that starts at the specified index of the passed list.
     * @param elements - The list of elements to iterate through.
     * @param index - The index to start at. When next or previous is first called this will be the index of the element returned.
     *                use 0 to start at the beginning of the list so that max steps can be used. 
     * @param maxSteps - The maximum number of steps the iterator can take before hasNext() and hasPrevious() return false defined
     *                   as calls to next() or previous(). The default maximum number of steps is 100000.
     */
    public CircularListIterator(final List<T> elements, final int index, final int maxSteps) {
        this.elements = elements;
        this.starterIdx = index % elements.size();
        this.maxAllowedSteps = maxSteps;
    }


    /**
     * Returns if there is a next element in the list. If the list is empty this will return false.
     * Once the maximum number of steps is reached, this will return false if the iterator is at the end of the list.
     * @return true if there is a next element, false otherwise, false if the list is empty.
     */
    @Override
    public boolean hasNext() {
        return !elements.isEmpty() && 
        (stepCount < maxAllowedSteps)? true : hasLinearNext();
    }

    /**
     * The 'traditional' version of hasNext for an iterator. If the iterator is at the end of the list, this will return false.
     * If the list is empty this will return false.
     * @return true if there is a next element, false otherwise, false if the list is empty.
     */
    public boolean hasLinearNext(){
        return (null == currentIndex) ? !elements.isEmpty() : currentIndex < elements.size() - 1;
    }

    /**
     * Returns the next element in the list. If hasNext() returns false, this will throw a NoSuchElementException.
     * @return The next element in the list.
     */
    @Override
    public T next() {
        if (!hasNext()) {
            throw new NoSuchElementException();
        }

        final int nextIndex = this.nextIndex();
        final T element = elements.get(nextIndex);
        currentIndex = nextIndex;
        stepCount++;

        return element;
    }

    /**
     * Returns the next element in the list as an Optional. If hasNext() returns false, this will return an empty Optional.
     * @return The next element in the list as an Optional.
     */
    public Optional<T> nextElement() {
        if (!hasNext()) {
            return Optional.empty();
        }

        final int nextIndex = this.nextIndex();
        final T element = elements.get(nextIndex);
        currentIndex = nextIndex;
        stepCount++;

        return Optional.of(element);
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
        return !elements.isEmpty() && 
               (stepCount < maxAllowedSteps)? true : hasLinearPrevious();
    }

    /**
     * The 'traditional' version of hasPrevious for an iterator. If the iterator is at the beginning of the list, this will return false.
     * @return true if there is a previous element, false otherwise, false if the list is empty.
     */
    public boolean hasLinearPrevious() {
        return (null == currentIndex) ? false : currentIndex > 0;
    }

    /**
     * Returns the previous element in the list. If hasPrevious() returns false, this will throw a NoSuchElementException.
     * If the iterator is at the beginning of the list, this will return the last element in the list. If this is the first
     * call to previous() then the last element in the list will be returned.
     * @return The previous element in the list.
     */
    @Override
    public T previous() {
        if (!hasPrevious()) {
            throw new NoSuchElementException();
        }

        final int prevIndex = this.previousIndex();
        final T element = elements.get(prevIndex);
        currentIndex = prevIndex;
        stepCount++;

        return element;
    }

    /**
     * Returns the previous element in the list as an Optional. If hasPrevious() returns false, this will return an empty Optional.
     * If the iterator is at the beginning of the list, this will return the last element in the list.
     * If this is the first call to previousElement() then the last element in the list will be returned.
     * @return The previous element in the list as an Optional.
     */
    public Optional<T> previousElement() {
        if (!hasPrevious()) {
            return Optional.empty();
        }

        final int prevIndex = this.previousIndex();
        final T element = elements.get(prevIndex);
        currentIndex = prevIndex;
        stepCount++;

        return Optional.of(element);
    }

    /**
     * Returns the index of the next element in the list. If the iterator is at the end of the list, this will return 0.
     * If the list is empty, this will return 0.
     * @return The index of the next element in the list.
     */
    @Override
    public int nextIndex() {
        return (null == currentIndex) ? starterIdx % elements.size() : (currentIndex + 1)%elements.size();
    }

    /**
     * Returns the index of the previous element in the list. If the iterator is at the beginning of the list, this will return the last index.
     * If the list is empty, this will return 0.
     * @return The index of the previous element in the list.
     */
    @Override
    public int previousIndex() {
        return (null == currentIndex) ? (0 == starterIdx)? elements.size() - 1 : (starterIdx - 1)%elements.size() : (0 == currentIndex) ? elements.size() - 1 : currentIndex - 1;
    }

    /**
     * Removes the current element from the list. If the iterator is not initialized by either calling next(), previous(),
     * nextElement(), or previousElement() first, this will throw an IllegalStateException.
     * After remove is called the iterator is no longer initialized and a call to next(), previous(), nextElement(),
     * or previousElement() must be made before any further changes can be made; remove(), set(), or add().
     * <code>
     *    CircularListIterator<String> iterator = new CircularListIterator<>(new ArrayList<String>(Arrays.asList("A", "B", "C")));
     *    iterator.next(); //iterator now "points" to "A"
     *    iterator.next(); //iterator now "points" to "B"
     *    iterator.remove(); //removes "B" from the list
     *    iterator.next(); //iterator now "points" to "C"
     * 
     * //note that iteratoror.prev() would have returned A.
     * </code>
     * Another way to think about it is that the remove leaves a hole where iterator is currently pointing. So
     * in order to make any further changes the iterator must be moved to a valid index.
     */
    @Override
    public void remove() {
        checkIndex();
        elements.remove((int)currentIndex);
        starterIdx = currentIndex;
        currentIndex = null;
    }

    /**
     * Sets the current element to the passed element. If the iterator is not initialized by either calling next(), previous(),
     * nextElement(), or previousElement() first, this will throw an IllegalStateException.
     * The iterator still "points" to the current element after set is called.
     * @param t - The element to set the current element to.
     */
    @Override
    public void set(final T t) {
        checkIndex();
        elements.set(currentIndex, t);
    }

    /**
     * Adds the passed element to the list. If the iterator is not initialized by either calling next(), previous(),
     * nextElement(), or previousElement() first, this will throw an IllegalStateException. I.e. you cannot add an element
     * to an iterator created over an empty list.
     * The element is added before the current element/index. If the iterator is at the beginning of the list, the element
     * is added at the end of the list. This is slightly different than the behavior of the ListIterator add method in that
     * for ListIterator the element would be added to the beginning of the list though the iterator would not change position.
     * This was a choice to keep the behavior of the iterator consistent with the rest of the methods. It also simplified the math.
     * @param t - The element to add to the list.
     */
    @Override
    public void add(final T t) {
        checkIndex();

        //For the iterator, if the current index is 0, then the element is added at the end of the list.
        //Otherwise, the element is added before the current index/element.
        if(0 == currentIndex) {
            elements.add(t);
            starterIdx = 0;
        } else {
            elements.add(currentIndex, t);
            currentIndex = this.nextIndex();
        }
    }

    /**
     * Returns the current element in the list. If the iterator is not initialized by either calling next(), previous(),
     * nextElement(), or previousElement() first, this will throw an IllegalStateException.
     * @return The current element in the list.
     */
    public T at() {
        checkIndex();
        return elements.get(currentIndex);
    }

    /**
     * Returns the current element in the list as an Optional. If the iterator is not initialized by either calling next(), previous(),
     * nextElement(), or previousElement() first, this will return an empty Optional.
     * @return The current element in the list as an Optional.
     */
    public Optional<T> elementAt() {
        return (null == currentIndex) ? Optional.empty() : Optional.of(elements.get(currentIndex));
    }

    /**
     * Returns the current step count, i.e. how many times next(), nextElement(), previous(), or previousElement() has been called.
     * @return The current step count.
     */
    public int getStepCount() {
        return stepCount;
    }

    /**
     * Resets the step count to 0. This can be useful if the maximum number of steps is reached and the iterator needs to be used again.
     */
    public void resetStepCount() {
        stepCount = 0;
    }

    /**
     * Checks if the iterator is initialized. If the iterator is not initialized by either calling next(), previous(),
     * nextElement(), or previousElement() first, this will throw an IllegalStateException.
     */
    protected void checkIndex() {
        if(null == currentIndex) {
            throw new IllegalStateException("Iterator not initialized, call next or previous first.");
        }
    }
}
