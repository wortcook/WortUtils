package com.wortcook.util.impl;

import java.util.Collection;
import java.util.List;
import java.util.NoSuchElementException;

import com.wortcook.util.CircularListIterator;

public class CircularListIteratorImpl<T> implements CircularListIterator<T>{
    private final List<T> elements;
    private Integer currentIndex = null;
    private int starterIdx;
    private int stepCount = 0;
    private final int maxAllowedSteps;

    /////////////////////////////////////////////////////////////////
    // Constructors
    /////////////////////////////////////////////////////////////////
    /**
     * Creates a CircularListIterator that starts at the beginning of the passed list
     * and has a maximum number of steps equal Integer.MAX_VALUE.
     * @param elements - The list of elements to iterate through. Note any changes made to
     *                   the list will be reflected in the iterator and vice versa.
     */
    public CircularListIteratorImpl(final List<T> elements) {
        this(elements, 0);
    }

    /**
     * Creates a CircularListIterator that starts at the specified index of the passed list
     * and has a maximum number of steps equal Integer.MAX_VALUE.
     * @param elements - The list of elements to iterate through. Note any changes made to
     *                   the list will be reflected in the iterator and vice versa.
     * @param index - The index to start at. When next or previous is first called this will be the index of the element returned.
     *                use 0 to start at the beginning of the list so that max steps can be used. 
     */
    public CircularListIteratorImpl(final List<T> elements, final int index) {
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
    public CircularListIteratorImpl(final List<T> elements, final int index, final int maxSteps) {
        assert null != elements : "Elements cannot be null.";
        assert index >= 0 : "Index must be greater than or equal to 0.";
        assert maxSteps > 0 : "Max steps must be greater than 0.";

        this.elements = elements;
        this.starterIdx = elements.isEmpty()?0:(index % elements.size());
        this.maxAllowedSteps = maxSteps;
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
        return (null == currentIndex) ? !elements.isEmpty() : currentIndex < elements.size() - 1;
    }

    /**
     * The 'traditional' version of hasPrevious for an iterator. If the iterator is at the beginning of the list, this will return false.
     * @return true if there is a previous element, false otherwise, false if the list is empty.
     */
    @Override
    public boolean hasPreviousNoWrap() {
        return (null == currentIndex) ? false : currentIndex > 0;
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
        if (!hasNext()) {
            throw new NoSuchElementException();
        }

        final int nextIndex = this.nextIndex();
        final T element = elements.get(nextIndex);
        currentIndex = nextIndex;
        stepCount++;

        return element;
    }

    @Override
    public boolean hasNext() {
        return !elements.isEmpty() && //empty lists always return false
                (isUnder() || hasNextNoWrap());
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
        checkIndex();
        elements.remove((int)currentIndex);
        starterIdx = currentIndex; //reset the starter index to the current index that way next/prev will work as expected.
        currentIndex = null; //iterator is no longer initialized, we have a "hole" in the list where the iterator was pointing.
    }


    
    
    /////////////////////////////////////////////////////////////////
    // ListIterator methods
    /////////////////////////////////////////////////////////////////
    @Override
    public void add(final T t) {
        checkIndex();

        //For the iterator, if the current index is 0, then the element is added at the end of the list.
        //Otherwise, the element is added before the current index/element.
        if(0 == currentIndex) {
            elements.add(t);
        } else {
            elements.add(currentIndex, t);
            //ensure that the current index still points to the same element.
            //since we added an element before the current index, the current index needs to be incremented.
            currentIndex = this.nextIndex();
        }
    }

    /**
     * Returns the index of the next element in the list. If the iterator is at the end of the list, this will return 0.
     * If the list is empty, this will return 0.
     * @return The index of the next element in the list.
     */
    @Override
    public int nextIndex() {
        return (null == currentIndex) ? //if the iterator is not initialized
            starterIdx % elements.size() // then start at starterIdx modulo the size of the list
            :
            (stepCount < maxAllowedSteps) ? //else if the max steps has not been reached
                (currentIndex + 1)%elements.size() : //then return the next index modulo the size of the list to wrap around
                (currentIndex < elements.size() - 1) ? //else if the current index is not at the end of the list
                    currentIndex + 1 : //then return the next index
                    elements.size(); //else we are at the end of the list so return elements.size() per ListIterator spec
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
        return !elements.isEmpty() && (isUnder() || hasPreviousNoWrap());
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
     * Returns the index of the previous element in the list. If the iterator is at the beginning of the list, this will return the last index.
     * If the list is empty, this will return 0.
     * @return The index of the previous element in the list.
     */
    @Override
    public int previousIndex() {
        return (null == currentIndex) ? //if the iterator is not initialized
            (0 == starterIdx)? //and the starter index is 0
                elements.size() - 1 //then the previous index is the last index, wrap around
                : 
                (starterIdx - 1)%elements.size() //else the starterIdx is not 0 so return the previous index modulo the size of the list
            : 
            (0 == currentIndex) ? //else the iterator is initialized and is at the beginning of the list
                (stepCount < maxAllowedSteps) ? //if the max steps has not been reached
                    elements.size() - 1 : //then return the last index
                    -1  //else we are at the beginning of the list so return -1 per ListIterator spec
                :
                currentIndex - 1; //iterator initialized and not at the beginning of the list so return the previous index
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
        checkIndex();
        return elements.get(currentIndex);
    }

    /**
     * Writes the passed element ahead of the current element. If the iterator is not initialized by either calling next(), previous(),
     * nextElement(), or previousElement() first, this will throw an IllegalStateException. After calling writeAhead the iterator
     * the next call to next() or nextElement() will return the element that was written ahead.
     * @param t - The element to write ahead of the current element.
     */
    @Override
    public void addNext(final T t) {
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
    @Override
    public void addAllNext(final Collection<T> c) {
        checkIndex();
        if(currentIndex == elements.size() - 1) {
            elements.addAll(c);
        } else {
            elements.addAll(currentIndex + 1, c);
        }
    }

    /**
     * Writes the passed element before the current element. If the iterator is not initialized by either calling next(), previous(),
     * nextElement(), or previousElement() first, this will throw an IllegalStateException. After calling addPrevious the iterator
     * the next call to previous() or previousElement() will return the element.
     * @param t - The element to write before the current element.
     */
    @Override
    public void addPrevious(final T element) {
        checkIndex();

        //For the iterator, if the current index is 0, then the element is added at the end of the list.
        //Otherwise, the element is added before the current index/element.
        if(0 == currentIndex) {
            elements.add(element);
        } else {
            elements.add(currentIndex, element);
            //stupid math trick to get the current index to continue pointing to the same element.
            currentIndex += 1;
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
    @Override
    public void addAllPrevious(final Collection<T> c) {
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
     * Returns the list of elements that the iterator is iterating over. This is a reference to the list passed in the constructor
     * so changes to the list will be reflected in the iterator.
     * @return
     */
    @Override
    public List<T> elements() {
        return elements;
    }


    ///////////////////////////////////////////////////////////////////////////
    // OptionalListIterator methods
    ///////////////////////////////////////////////////////////////////////////
    //none
    //default methods in the interface are sufficient

    ///////////////////////////////////////////////////////////////////////////
    // CountingIterator methods
    ///////////////////////////////////////////////////////////////////////////
    /**
     * Returns the current step count, i.e. how many times next(), nextElement(), previous(), or previousElement() has been called.
     * @return The current step count.
     */
    @Override
    public int getCount() {
        return stepCount;
    }

    @Override
    public int getLimit() {
        return maxAllowedSteps;
    }

    /**
     * Resets the step count to 0. This can be useful if the maximum number of steps is reached and the iterator needs to be used again.
     */
    @Override
    public void resetCount() {
        stepCount = 0;
    }

    /**
     * Resets the iterator to the same state as if it were newly created. The iterator will start at the "beginning" of the list
     * and the step count will be reset to 0.
     */
    @Override
    public void reset() {
        currentIndex = null;
        stepCount = 0;
    }


    ///////////////////////////////////////////////////////////////////////////
    // Protected utility methods
    ///////////////////////////////////////////////////////////////////////////
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