package com.wortcook.util;

public interface CountingIterator<T> {
    public int getCount();
    public int getLimit();
    public void resetCount();
    public void reset();

    default boolean isUnder() {
        return getCount() < getLimit();
    }

    default boolean isOver() {
        return getCount() >= getLimit();
    }
}
