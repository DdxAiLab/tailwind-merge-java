package com.github.ddxailab.tailwindmerge;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.function.Predicate;
import org.junit.jupiter.api.Test;

class ClassNameNodeTest {

    @Test
    void newNodeIsEmpty() {
        ClassNameNode node = new ClassNameNode();

        assertNull(node.getClassGroupId());
        assertNull(node.getNext());
        assertNull(node.getValidators());
    }

    @Test
    void addNextNodeCreatesOneNodePerPart() {
        ClassNameNode root = new ClassNameNode();

        ClassNameNode leaf = root.addNextNode("border-t-2");

        ClassNameNode border = root.getNext().get("border");
        ClassNameNode t = border.getNext().get("t");
        assertSame(leaf, t.getNext().get("2"));
        assertEquals(1, root.getNext().size());
    }

    @Test
    void addNextNodeReusesExistingNodes() {
        ClassNameNode root = new ClassNameNode();

        ClassNameNode first = root.addNextNode("border-t");
        ClassNameNode second = root.addNextNode("border-t");
        ClassNameNode sibling = root.addNextNode("border-b");

        assertSame(first, second);
        assertEquals(2, root.getNext().get("border").getNext().size());
        assertSame(sibling, root.getNext().get("border").getNext().get("b"));
    }

    @Test
    void addNextNodeIgnoresEmptyParts() {
        ClassNameNode root = new ClassNameNode();

        ClassNameNode leaf = root.addNextNode("-a--b-");

        assertSame(leaf, root.getNext().get("a").getNext().get("b"));
        assertTrue(leaf.getNext() == null);
    }

    @Test
    void addNextNodeWithoutPartsReturnsTheSameNode() {
        ClassNameNode root = new ClassNameNode();

        assertSame(root, root.addNextNode(""));
        assertSame(root, root.addNextNode("-"));
        assertNull(root.getNext());
    }

    @Test
    void addValidatorKeepsInsertionOrder() {
        ClassNameNode node = new ClassNameNode();
        Predicate<String> first = value -> true;
        Predicate<String> second = value -> false;

        node.addValidator(first, "group-a");
        node.addValidator(second, "group-b");

        assertEquals(2, node.getValidators().size());
        assertEquals("group-a", node.getValidators().get(0).getClassGroupId());
        assertSame(first, node.getValidators().get(0).getValidator());
        assertEquals("group-b", node.getValidators().get(1).getClassGroupId());
        assertSame(second, node.getValidators().get(1).getValidator());
    }
}
