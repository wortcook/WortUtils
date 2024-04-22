package com.wortcook.experiment.util;

import java.util.List;
import java.util.SortedSet;
import java.util.TreeSet;

public class TreeMetric<T> extends Tree.DefaultTree<Tree<? extends Comparable<T>>>{

    private Integer height = null;
    private Integer depth  = null;
    private Integer width  = null;
    


    @SuppressWarnings("unchecked")
    private void checkChildren() {
        List<Tree<Tree<? extends Comparable<T>>>> children = getChildren();
        
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
