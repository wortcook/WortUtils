package com.wortcook.util;

import java.util.List;
import java.lang.module.ModuleDescriptor.Builder;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
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

    public CircularListIterator(final List<T> elements) {
        this(elements, 0);
    }

    public CircularListIterator(final List<T> elements, final int index) {
        this(elements, index, Integer.MAX_VALUE);
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
        assert null != elements : "Elements cannot be null.";
        assert index >= 0 : "Index must be greater than or equal to 0.";
        assert maxSteps > 0 : "Max steps must be greater than 0.";

        this.elements = elements;
        this.starterIdx = elements.isEmpty()?0:(index % elements.size());
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
        return Optional.of(this.next());
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

        return Optional.of(this.previous());
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
            //ensure that the current index still points to the same element.
            //since we added an element before the current index, the current index needs to be incremented.
            currentIndex = this.nextIndex();
        }
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
    public void addAll(final Collection<T> c) {
        checkIndex();

        //For the iterator, if the current index is 0, then the elements are added at the end of the list.
        //Otherwise, the elements are added before the current index/element.
        if( 0 == currentIndex) {
            elements.addAll(c);
        } else {
            elements.addAll(currentIndex, c);
            //stupid math trick to get the current index to continue pointing to the same element.
            currentIndex += (c.size() - 1);
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
     * Returns the list of elements that the iterator is iterating over. This is a reference to the list passed in the constructor
     * so changes to the list will be reflected in the iterator.
     * @return
     */
    public List<T> elements() {
        return elements;
    }

    /**
     * Writes the passed element ahead of the current element. If the iterator is not initialized by either calling next(), previous(),
     * nextElement(), or previousElement() first, this will throw an IllegalStateException. After calling writeAhead the iterator
     * the next call to next() or nextElement() will return the element that was written ahead.
     * @param t - The element to write ahead of the current element.
     */
    public void writeAhead(final T t) {
        checkIndex();
        if(currentIndex == elements.size() - 1) {
            elements.add(t);
        } else {
            elements.add(currentIndex + 1, t);
        }
    }

    /**
     * Writes the passed elements ahead of the current element. If the iterator is not initialized by either calling next(), previous(),
     * nextElement(), or previousElement() first, this will throw an IllegalStateException. After calling writeAllAhead the iterator
     * the next call to next() or nextElement() will return the first element that was written ahead. Note that the order that the 
     * elements are written is the order that they are in the collection.
     * @param c - The elements to write ahead of the current element.
     */
    public void writeAllAhead(final Collection<T> c) {
        checkIndex();
        if(currentIndex == elements.size() - 1) {
            elements.addAll(c);
        } else {
            elements.addAll(currentIndex + 1, c);
        }
    }

    /**
     * Resets the iterator to the same state as if it were newly created. The iterator will start at the "beginning" of the list
     * and the step count will be reset to 0.
     */
    public void reset() {
        currentIndex = null;
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

    /**
     * Builder class for CircularListIterator. This allows for a more fluent way to create a CircularListIterator.
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
        public CircularListIterator<T> build() {
            if(null == elementsList) {
                throw new IllegalStateException("No elements provided.");
            }

            if( maxEpochs != null ) {
                maxSteps = elementsList.size() * maxEpochs;
            }

            if(null == startIdx) {
                return (null == maxSteps) ? new CircularListIterator<>(elementsList) : new CircularListIterator<>(elementsList, 0, maxSteps);
            } else {
                return (null == maxSteps) ? new CircularListIterator<>(elementsList, startIdx) : new CircularListIterator<>(elementsList, startIdx, maxSteps);
            }
        }

        public Iterable<T> iterable() {
            final CircularListIterator<T> iterator = build();
            return new Iterable<T>(){
                @Override
                public Iterator<T> iterator() {
                    return iterator;
                }
            };
        }
    }

    public static <T> Builder<T> builder() {
        return new Builder<>();
    }

    public static <T> Iterable<T> iterableOf(Builder<T> builder){
        return new Iterable<T>(){
            @Override
            public Iterator<T> iterator() {
                return builder.build();
            }
        };
    }
    /**
     * Utility method to create an Iterable that returns a CircularListIterator when iterator is called.
     * @param <T> - The type of elements in the list.
     * @param elements - The list of elements to iterate over.
     * @return An Iterable that returns a CircularListIterator when iterator is called.
     */
    public static <T> Iterable<T> iterableOf(final List<T> elements){
        return new Iterable<T>(){
            @Override
            public Iterator<T> iterator() {
                return new CircularListIterator<>(elements);
            }
        };
    }

    public static <T> Iterable<T> iterableOf(final List<T> elements, final int index){
        return new Iterable<T>(){
            @Override
            public Iterator<T> iterator() {
                return new CircularListIterator<>(new ArrayList<>(elements), index);
            }
        };
    }

    public static <T> Iterable<T> iterableOf(final List<T> elements, final int index, final int maxSteps){
        return new Iterable<T>(){
            @Override
            public Iterator<T> iterator() {
                return new CircularListIterator<>(new ArrayList<>(elements), index, maxSteps);
            }
        };
    }

    /**
     * Utility method to create a List that returns a CircularListIterator when iterator or listIterator is called.
     * The returned List is a thin wrapper around the passed list so changes to the list will be reflected in the iterator.
     * The returned List cannot be cast to any other type so only the methods of List can be used.
     * @param <T> - The type of elements in the list.
     * @param elements - The list of elements to wrap.
     * @return A List that returns a CircularListIterator when iterator or listIterator is called that wraps the underlying list.
     */
    // public static <T> List<T> listFrom(final List<T> elements) {
    //     return new List<T>(){
    //         @Override
    //         public int size() {
    //             return elements.size();
    //         }

    //         @Override
    //         public boolean isEmpty() {
    //             return elements.isEmpty();
    //         }

    //         @Override
    //         public boolean contains(Object o) {
    //             return elements.contains(o);
    //         }

    //         @Override
    //         public Iterator<T> iterator() {
    //             return new CircularListIterator<>(elements);
    //         }

    //         @Override
    //         public Object[] toArray() {
    //             return elements.toArray();
    //         }

    //         @Override
    //         public <E> E[] toArray(E[] a) {
    //             return elements.toArray(a);
    //         }

    //         @Override
    //         public boolean add(T e) {
    //             return elements.add(e);
    //         }

    //         @Override
    //         public boolean remove(Object o) {
    //             return elements.remove(o);
    //         }

    //         @Override
    //         public boolean containsAll(Collection<?> c) {
    //             return elements.containsAll(c);
    //         }


    //         @Override
    //         public boolean addAll(int index, Collection<? extends T> c) {
    //             return elements.addAll(index, c);
    //         }

    //         @Override
    //         public boolean removeAll(Collection<?> c) {
    //             return elements.removeAll(c);
    //         }

    //         @Override
    //         public boolean retainAll(Collection<?> c) {
    //             return elements.retainAll(c);
    //         }

    //         @Override
    //         public void clear() {
    //             elements.clear();
    //         }

    //         @Override
    //         public T get(int index) {
    //             return elements.get(index);
    //         }

    //         @Override
    //         public T set(int index, T element) {
    //             return elements.set(index, element);
    //         }

    //         @Override
    //         public void add(int index, T element) {
    //             elements.add(index, element);
    //         }

    //         @Override
    //         public T remove(int index) {
    //             return elements.remove(index);
    //         }

    //         @Override
    //         public int indexOf(Object o) {
    //             return elements.indexOf(o);
    //         }

    //         @Override
    //         public int lastIndexOf(Object o) {
    //             return elements.lastIndexOf(o);
    //         }

    //         @Override
    //         public ListIterator<T> listIterator() {
    //             return new CircularListIterator<>(elements);
    //         }

    //         @Override
    //         public ListIterator<T> listIterator(int index) {
    //             return new CircularListIterator<>(elements, index);
    //         }

    //         @Override
    //         public List<T> subList(int fromIndex, int toIndex) {
    //             return elements.subList(fromIndex, toIndex);
    //         }

    //         @Override
    //         public boolean addAll(Collection<? extends T> c) {
    //             return elements.addAll(c);
    //         }
    //     };
    // }
}
 