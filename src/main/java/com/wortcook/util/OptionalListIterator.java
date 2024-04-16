package com.wortcook.util;

import static com.wortcook.Wort.*;

import java.util.ListIterator;
import java.util.Optional;

/**
 * An extension of the ListIterator interface that provides optional methods for accessing elements.
 */
public interface OptionalListIterator<T> extends ListIterator<T>, OptionalIterator<T> {
    /**
     * Returns the previous element in the iteration, or an empty Optional if the iteration has no more elements.
     * @return the previous element in the iteration, or an empty Optional if the iteration has no more elements
     */
    default Optional<T> previousElement() {
        return tryOptional(this::previous);
    }
}
