package com.wortcook.util;

import static com.wortcook.Wort.*;

import java.util.ListIterator;
import java.util.Optional;

public interface OptionalListIterator<T> extends ListIterator<T> {
    default Optional<T> nextElement() {
        return tryOptional(this::next);
    }

    default Optional<T> previousElement() {
        return tryOptional(this::previous);
    }
}
