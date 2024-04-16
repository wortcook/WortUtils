package com.wortcook.util;

import static com.wortcook.Wort.*;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

public interface PositionalListIterator<T> extends OptionalListIterator<T>{
    T at();
    void addNext(T element);
    void addAllNext(Collection<T> elements);
    void addPrevious(T element);
    void addAllPrevious(Collection<T> elements);
    List<T> elements();


    default Optional<T> elementAt() {
        return tryOptional(this::at);
    }
}
