package com.wortcook.experiment.util;

import java.util.Collections;
import java.util.List;

public interface Tree<T extends Comparable<T>> extends Comparable<Tree<T>>{
    public Tree<T> getParent();
    public void setParent(Tree<T> parent);
    public T getValue();
    public void setValue(T value);
    public List<Tree<T>> getChildren();
    public boolean isRoot();
    public boolean isLeaf();

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

    public static class DefaultTree<T extends Comparable<T>> implements Tree<T> {
        private Tree<T> parent;
        private T value;
        private List<Tree<T>> children;

        public DefaultTree() {
        }

        public DefaultTree(T value) {
            this.value = value;
        }

        @Override
        public Tree<T> getParent() {
            return parent;
        }

        @Override
        public void setParent(Tree<T> parent) {
            this.parent = parent;
        }

        @Override
        public T getValue() {
            return value;
        }

        @Override
        public void setValue(T value) {
            this.value = value;
        }

        @Override
        public List<Tree<T>> getChildren() {
            if(null == children) {
                children = new java.util.ArrayList<>();
            }
            Collections.sort(children);
            return children;
        }

        @Override
        public boolean isRoot() {
            return parent == null;
        }

        @Override
        public boolean isLeaf() {
            return children == null || children.isEmpty();
        }
    }

    public static <T extends Comparable<T>> Tree<T> of(T value) {
        return new DefaultTree<>(value);
    }
}
