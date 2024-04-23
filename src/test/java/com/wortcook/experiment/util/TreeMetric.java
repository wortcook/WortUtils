package com.wortcook.experiment.util;

import java.util.List;

public class TreeMetric extends Tree.DefaultTree<Tree<?>>{

    private Integer height    = null;
    private Integer depth     = null;
    private Integer avgWidth  = null;


    public TreeMetric(Tree<?> value) {
        super(value);
        calculateChildren();
    }

    protected void calculateChildren(){
        final List<Tree<Tree<?>>> children = getChildren();
        for(Tree<?> valChild: getValue().getChildren()){
            TreeMetric child = new TreeMetric(valChild);
            child.setParent(this);
            children.add(child);
        }
    }

    protected int calculateHeight() {
        return isLeaf() ? 1 : getChildren().stream().mapToInt(child -> ((TreeMetric)child).getHeight()).max().getAsInt() + 1;
    }

    public Integer getHeight() {
        return (null == height) ? (height = calculateHeight()) : height;
    }

    protected int calculateDepth() {
        return isRoot() ? 1 : ((TreeMetric)getParent()).getDepth() + 1;
    }

    public Integer getDepth() {
        return (null == depth) ? (depth = calculateDepth()) : depth;
    }

    protected int calculateWidth() {
        return getChildren().size();
    }

    public Integer getWidth() {
        return getChildren().size();
    }

    protected int calculateAvgWidth() {
        return isLeaf() ? 0 : getChildren().stream().mapToInt(child -> ((TreeMetric)child).getAvgWidth()).sum() / getWidth();
    }

    public Integer getAvgWidth() {
        return (null == avgWidth) ? (avgWidth = calculateAvgWidth()) : avgWidth;
    }
}
