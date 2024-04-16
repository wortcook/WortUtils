package com.wortcook.util;

import static com.wortcook.Wort.*;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

/**
 * An extension of the OptionalListIterator interface that provides positional methods for accessing elements.
 */
public interface PositionalListIterator<T> extends OptionalListIterator<T>{
    /**
     * Returns the element at the current position in the iteration. E.g. calling
     * this method after calling next() will return the element that was returned by next().
     * @return the element at the current position in the iteration
     */
    T at();

    /**
     * Adds the specified element to the list after the current position in the iteration.
     * A call to next() will return the element that was added.
     * @param element
     */
    void addNext(T element);

    /**
     * Adds the specified elements to the list after the current position in the iteration.
     * A call to next() will return the first element from the collection that was added.
     * @param elements
     */
    void addAllNext(Collection<T> elements);

    /**
     * Adds the specified element to the list before the current position in the iteration.
     * A call to previous() will return the element that was added.
     * @param element
     */
    void addPrevious(T element);

    /**
     * Adds the specified elements to the list before the current position in the iteration.
     * A call to previous() will return the last element from the collection that was added.
     * @param elements
     */
    void addAllPrevious(Collection<T> elements);

    /**
     * Returns the list of elements being iterated over.
     * @return
     */
    List<T> elements();

    /**
     * Resets the iteration.
     */
    void reset();

    /**
     * Returns the element at the current position in the iteration as an Optional.
     * If there is no element at the current position, an empty Optional is returned.
     * @return
     */
    default Optional<T> elementAt() {
        return tryOptional(this::at);
    }
}
