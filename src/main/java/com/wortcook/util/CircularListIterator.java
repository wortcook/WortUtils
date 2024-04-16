package com.wortcook.util;

import java.util.List;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

import com.wortcook.util.impl.CircularListIteratorImpl;

/**
 * CircularListIterator is a ListIterator that wraps around a List and allows for circular iteration.
 * Depending on the constructor, the iterator can start at a specific index and iterate through the list
 * or start at the beginning of the list. Once the iterator reaches the end of the list it will wrap around
 * to the beginning of the list and vice versa. So unlike a regular ListIterator, the CircularListIterator
 * will never throw a NoSuchElementException unless the list is empty or the maximum number of steps is reached.
 * <br><br>
 * A maximum number of steps can be set to prevent long infinite loops. The default maximum number of steps is twice
 * the initial list passed in.<br>
 * Once the maximum number of steps is reached the iterator starts behaving like a regular ListIterator.
 * So hasNext() and hasPrevious() will return false and next() and previous() will throw a NoSuchElementException if
 * the iterator is at the end or beginning of the list respectively. The iterator can still be used to get the current
 * element, remove the current element, set the current element, and add an element to the list. The iterator will need
 * to be moved to a valid index before any further changes can be made.<br>
 * expected. The step count can be reset using resetStepCount() once the iterator reaches the max steps.
 */
public interface CircularListIterator<T> extends PositionalListIterator<T>, CountingIterator<T>{

    /**
     * Returns the previous element in the iteration, or throws a NoSuchElementException if the iteration has no more elements.
     * This method does not consider the maximum number of steps set on the iterator.
     * @return true if the iteration has more elements
     */
    public boolean hasPreviousNoWrap();

    /**
     * Returns the next element in the iteration, or throws a NoSuchElementException if the iteration has no more elements.
     * This method does not consider the maximum number of steps set on the iterator.
     * @return true if the iteration has more elements
     */
    public boolean hasNextNoWrap();


    /**
     * Builder class for CircularListIterator. This allows for a more fluent way to create a CircularListIterator.
     * An instance of CircularListIteratorImple is returned.
     * @param <T> - The type of elements in the list. 
     */
    public static class Builder<T>{
        private List<T> elementsList   = null;
        private Integer startIdx           = null;
        private Integer maxSteps           = null;
        private Integer maxEpochs          = null;

        /*
         * Sets the elements to be iterated over. The iterator returned will be over a List copy of the passed elements
         * so the order elements in the list will be the same order as the passed elements but the passed
         * collection will not be altered.
         * @param elements - The elements to iterate over.
         * @return The builder.
         */
        public Builder<T> using(final Collection<T> elements) {
            assert null != elements : "Elements cannot be null.";
            this.elementsList = new ArrayList<T>(elements);
            return this;
        }

        /*
         * Sets the elements to be iterated over. The iterator returned will be over a List copy of the passed elements
         * so the order elements in the list will be the same order as the passed elements but the passed
         * array will not be altered.
         * @param elements - The array of elements to iterate over.
         */
        public Builder<T> using(final T[] elements) {
            assert null != elements : "Elements cannot be null.";
            this.elementsList = new ArrayList<T>(List.of(elements));
            return this;
        }

        /*
         * Sets the elements to be iterated over. The iterator returned will be over a List copy of the passed elements
         * so the order elements in the list will be the same order as the passed elements but the passed
         * list will not be altered.
         * @param elements - The list of elements to iterate over.
         */
        public Builder<T> using(final List<T> elements) {
            assert null != elements : "Elements cannot be null.";
            this.elementsList = new ArrayList<>(elements);
            return this;
        }

        /*
         * Creates an iterator over the passed list. Any changes to the list will be reflected in the iterator and
         * and calls to iterator methods such as add or remove will alter the list.
         * @param elements - The list of elements to iterate over.
         */
        public Builder<T> over(final List<T> elements) {
            assert null != elements : "Elements cannot be null.";
            this.elementsList = elements;
            return this;
        }

        /*
         * Sets the starting index of the iterator. The iterator will start at the element at the passed index.
         * If the index is greater than the size of the list, the index will be the index modulo the size of the list.
         * @param index - The index to start at.
         */
        public Builder<T> startAt(final int index) {
            assert index >= 0 : "Index must be greater than or equal to 0.";
            this.startIdx = index;
            return this;
        }

        /*
         * Sets the maximum number of steps the iterator can take before hasNext() and hasPrevious() return false.
         * The default maximum number of steps is twice the size of the list.
         * @param maxSteps - The maximum number of steps the iterator can take.
         */
        public Builder<T> maxSteps(final int maxSteps) {
            assert maxSteps > 0 : "Max steps must be greater than 0.";
            this.maxSteps = maxSteps;
            return this;
        }

        /*
         * Sets the maximum number of epochs the iterator can take before hasNext() and hasPrevious() return false.
         * This is a convenience method to set the maximum number of steps to the size of the list times the passed number of epochs.
         * The computed maximum number of steps will be the size of the list times the number of epochs at the time
         * the iterator is created. If the list is changed after the iterator is created, the maximum number of steps will not
         * change.
         * @param maxEpochs - The maximum number of epochs the iterator can take.
         */
        public Builder<T> maxEpochs(final int maxEpochs) {
            assert maxEpochs > 0 : "Max epochs must be greater than 0.";
            this.maxEpochs = maxEpochs;
            return this;
        }

        /*
         * Builds the CircularListIterator. If no elements are provided, this will throw an IllegalStateException.
         * If no starting index is provided, the iterator will start at the beginning of the list.
         * If no maximum number of steps is provided, the iterator will not have a maximum number set to Integer.MAX_VALUE.
         * @return The CircularListIterator.
         */
        public CircularListIterator<T> iterator() {
            if(null == elementsList) {
                throw new IllegalStateException("No elements provided.");
            }

            if( maxEpochs != null ) {
                maxSteps = elementsList.size() * maxEpochs;
            }

            if(null == startIdx) {
                return (null == maxSteps) ? CircularListIterator.<T>of(elementsList) : CircularListIterator.<T>of(elementsList, 0, maxSteps);
            } else {
                return (null == maxSteps) ? CircularListIterator.<T>of(elementsList, startIdx) : CircularListIterator.<T>of(elementsList, startIdx, maxSteps);
            }
        }

        public Iterable<T> iterable() {
            final CircularListIterator<T> iterator = iterator();
            return new Iterable<T>(){
                @Override
                public Iterator<T> iterator() {
                    return iterator;
                }
            };
        }
    }

    /**
     * Utility method to create a Builder for a CircularListIterator.
     * @param <T>
     * @return A Builder for a CircularListIterator.
     */
    public static <T> Builder<T> builder() {
        return new Builder<>();
    }

    public static <T> CircularListIterator<T> of(final List<T> elements) {
        return new CircularListIteratorImpl<>(elements);
    }

    public static <T> CircularListIterator<T> of(final List<T> elements, final int index) {
        return new CircularListIteratorImpl<>(elements, index);
    }

    public static <T> CircularListIterator<T> of(final List<T> elements, final int index, final int maxSteps) {
        return new CircularListIteratorImpl<>(elements, index, maxSteps);
    }

    public static <T> CircularListIterator<T> of(@SuppressWarnings("unchecked") T... elements) {
        return new CircularListIteratorImpl<>(new ArrayList<>(List.of(elements)));
    }
    
    /**
     * Utility method to create an Iterable that returns a CircularListIterator when iterator is called.
     * @param <T> - The type of elements in the list.
     * @param elements - The list of elements to iterate over.
     * @return An Iterable that returns a CircularListIterator when iterator is called.
     */
    public static <T> Iterable<T> iterableOf(final List<T> elements){
        return CircularListIterator.<T>builder().over(elements).iterable();
    }

    /**
     * Utility method to create an Iterable that returns a CircularListIterator when iterator is called.
     * @param <T>
     * @param elements - The list of elements to iterate over.
     * @param index - The index to start at.
     * @return An Iterable that returns a CircularListIterator when iterator is called.
     */
    public static <T> Iterable<T> iterableOf(final List<T> elements, final int index){
        return CircularListIterator.<T>builder().over(elements).startAt(index).iterable();
    }

    /**
     * Utility method to create an Iterable that returns a CircularListIterator when iterator is called.
     * @param <T>
     * @param elements - The list of elements to iterate over.
     * @param index - The index to start at.
     * @param maxSteps - The maximum number of steps the iterator can take.
     * @return An Iterable that returns a CircularListIterator when iterator is called.
     */
    public static <T> Iterable<T> iterableOf(final List<T> elements, final int index, final int maxSteps){
        return CircularListIterator.<T>builder().over(elements).startAt(index).maxSteps(maxSteps).iterable();
    }
}
 