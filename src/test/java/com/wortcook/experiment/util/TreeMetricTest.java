package com.wortcook.experiment.util;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

public class TreeMetricTest {
    
    public Tree<String> createTree() {
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
    void height() {
        Tree<String> tree = createTree();
        TreeMetric<String> treeMetric = new TreeMetric<>(tree);
        assertEquals(2, treeMetric.getHeight());
    }

}
