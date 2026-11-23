package com.github.ddxailab.tailwindmerge;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Predicate;

/**
 * A node of the trie that maps class name parts to class groups.
 */
final class ClassNameNode {

    private String classGroupId;
    private Map<String, ClassNameNode> next;
    private List<ClassValidator> validators;

    String getClassGroupId() {
        return classGroupId;
    }

    void setClassGroupId(String classGroupId) {
        this.classGroupId = classGroupId;
    }

    /**
     * Gets the child nodes by class name part.
     *
     * @return the child nodes, or {@code null} if there are none
     */
    Map<String, ClassNameNode> getNext() {
        return next;
    }

    void setNext(Map<String, ClassNameNode> next) {
        this.next = next;
    }

    /**
     * Gets the validators to run against the remaining class name.
     *
     * @return the validators, or {@code null} if there are none
     */
    List<ClassValidator> getValidators() {
        return validators;
    }

    /**
     * Walks (creating the missing nodes) the path described by a class name split on
     * {@link Constants#CLASS_NAME_SEPARATOR}, ignoring empty parts.
     *
     * @param className the class name part to add
     * @return the node at the end of the path; this node if the class name has no parts
     */
    ClassNameNode addNextNode(String className) {
        ClassNameNode current = this;
        int length = className.length();
        int start = 0;

        while (start <= length) {
            int end = className.indexOf(Constants.CLASS_NAME_SEPARATOR, start);
            if (end < 0) {
                end = length;
            }

            if (end > start) {
                String part = className.substring(start, end);
                if (current.next == null) {
                    current.next = new HashMap<>();
                }
                ClassNameNode child = current.next.get(part);
                if (child == null) {
                    child = new ClassNameNode();
                    current.next.put(part, child);
                }
                current = child;
            }

            start = end + 1;
        }

        return current;
    }

    void addValidator(Predicate<String> validator, String classGroupId) {
        if (validators == null) {
            validators = new ArrayList<>();
        }
        validators.add(new ClassValidator(classGroupId, validator));
    }
}
