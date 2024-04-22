package com.wortcook.experiment.util;

import java.util.SortedSet;
import java.util.TreeSet;

public class TreeMetric<T> implements Tree<Tree<? extends Comparable<T>>>{
    private TreeMetric<T> parent;
    private Tree<? extends Comparable<T>> tRootNode;
    private TreeSet<Tree<Tree<? extends Comparable<T>>>> children = new TreeSet<>();

    private Integer height = null;
    private Integer depth  = null;
    private Integer width  = null;
    

    public TreeMetric(Tree<? extends Comparable<T>> value) {
        this.tRootNode = value;
    }

    @Override
    public TreeMetric<T> getParent() {
        return parent;
    }

    @Override
    public void setParent(final Tree<Tree<? extends Comparable<T>>> parent) {
        this.parent = (TreeMetric<T>) parent;
    }

    public void setParent(final TreeMetric<T> parent) {
        this.parent = parent;
    }

    @Override
    public Tree<? extends Comparable<T>> getValue() {
        return tRootNode;
    }

    @Override
    public void setValue(Tree<? extends Comparable<T>> value) {
        this.tRootNode = value;
    }

    @Override
    public SortedSet<Tree<Tree<? extends Comparable<T>>>> getChildren() {
        return children;
    }

    @SuppressWarnings("unchecked")
    private void checkChildren() {
        if(children == null) {
            Tree<? extends Comparable<T>> value = getValue();
            children = new TreeSet<>();

            SortedSet<?> valChildren = value.getChildren();
            
            if(valChildren != null) {
                for(Object child : valChildren) {
                    children.add(new TreeMetric<>((Tree<? extends Comparable<T>>)child));
                }
            }
        }
    }

    public Integer getHeight() {
        if(height == null) {
            checkChildren();
            height = 0;
            for(Tree<Tree<? extends Comparable<T>>> child : children) {
                height = Math.max(height, ((TreeMetric<T>)child.getValue()).getHeight());
            }
            height++;
        }
        return height;
    }

    public Integer getDepth() {
        if(depth == null) {
            depth = 0;
            TreeMetric<T> parent = this;
            while(parent != null) {
                depth++;
                parent = parent.getParent();
            }
        }
        return depth;
    }

    public Integer getWidth() {
        if(width == null) {
            checkChildren();
            width = children.size();
        }
        return width;
    }
}
