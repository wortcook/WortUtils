package com.wortcook.util;

import java.util.Iterator;
import java.util.Optional;

import static com.wortcook.Wort.*;

public interface OptionalIterator<T> extends Iterator<T>{
    /**
     * Returns the next element in the iteration, or an empty Optional if the iteration has no more elements.
     * @return the next element in the iteration, or an empty Optional if the iteration has no more elements
     */
    default Optional<T> nextElement() {
        return tryOptional(this::next);
    }
}
