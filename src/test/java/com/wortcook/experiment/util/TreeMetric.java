package com.wortcook.experiment.util;

import java.util.List;

public class TreeMetric extends Tree.DefaultTree<Tree<? extends Comparable<?>>>{

    private Integer height = null;
    private Integer depth  = null;
    private Integer width  = null;
    


    public TreeMetric(Tree<?> value) {
        super(value);
    }

    @SuppressWarnings("unchecked")
    private void checkChildren() {
        List<TreeMetric> children = getChildren();
        
    }

    public Integer getHeight() {
        if(height == null) {
            checkChildren();
            height = 0;
            for(Tree<Tree<? extends Comparable<T>>> child : getChildren()) {
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
                parent = (TreeMetric<T>)parent.getParent();
            }
        }
        return depth;
    }

    public Integer getWidth() {
        if(width == null) {
            checkChildren();
            width = getChildren().size();
        }
        return width;
    }
}
