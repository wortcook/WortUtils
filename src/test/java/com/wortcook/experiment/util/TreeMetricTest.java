package com.wortcook.experiment.util;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

public class TreeMetricTest {
    
    public Tree<String> createSimpleTree() {
        Tree<String> root = Tree.of("root");

        Tree<String> child1 = Tree.of("child1");
        Tree<String> child2 = Tree.of("child2");
        Tree<String> child3 = Tree.of("child3");
        Tree<String> child4 = Tree.of("child4");

        root.getChildren().add(child1);
        root.getChildren().add(child2);
        root.getChildren().add(child3);
        root.getChildren().add(child4);

        return root;
    }

    @Test
    void basic() {
        Tree<String> tree = createSimpleTree();
        TreeMetric treeMetric = new TreeMetric(tree);
        assertEquals(2, treeMetric.getHeight());
        assertEquals(1, treeMetric.getDepth());
        assertEquals(4, treeMetric.getWidth());
        assertEquals(0, treeMetric.getAvgWidth());

        TreeMetric child1 = (TreeMetric) treeMetric.getChildren().get(0);
        assertEquals(1, child1.getHeight());
        assertEquals(2 , child1.getDepth());
        assertEquals(0 , child1.getWidth());
        assertEquals(0 , child1.getAvgWidth());
    }
}
