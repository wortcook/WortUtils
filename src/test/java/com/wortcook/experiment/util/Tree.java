package com.wortcook.experiment.util;

import java.util.SortedSet;

public interface Tree<T extends Comparable<T>> extends Comparable<Tree<T>>{
    public Tree<T> getParent();
    public void setParent(Tree<T> parent);
    public T getValue();
    public void setValue(T value);
    public SortedSet<Tree<T>> getChildren();

    @Override
    public default int compareTo(Tree<T> o) {
        if(o == null) return 1;

        T value = this.getValue();
        T oValue = o.getValue();

        if(value == null && oValue == null) return 0;
        if(value == null) return -1;
        if(oValue == null) return 1;

        return value.compareTo(oValue);
    }
}
