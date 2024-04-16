package com.wortcook.util;

public interface CountingIterator<T> {
    public int getCount();
    public int getLimit();
    public boolean hasExceeded();
    public void resetCount();
    public void reset();
}
