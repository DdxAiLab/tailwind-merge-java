package com.github.ddxailab.tailwindmerge;

import com.github.ddxailab.tailwindmerge.common.ThemeGetter;
import com.github.ddxailab.tailwindmerge.model.ClassGroup;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Predicate;

/**
 * Builds the {@link ClassNameNode} trie that maps class names to class groups.
 */
final class TwMergeMapFactory {

    /** Initial capacity of the root node, sized for the default configuration. */
    private static final int ROOT_CAPACITY = 168;

    private TwMergeMapFactory() {
    }

    /**
     * Creates the class map of a configuration.
     *
     * @param config the configuration holding the class groups and the theme
     * @return the root node of the class map
     */
    static ClassNameNode create(TwMergeConfig config) {
        // Initialize a root node of the map
        ClassNameNode classMap = new ClassNameNode();
        classMap.setNext(new HashMap<String, ClassNameNode>(ROOT_CAPACITY));

        for (Map.Entry<String, ClassGroup> entry : config.getClassGroups().entrySet()) {
            ClassGroup classGroup = entry.getValue();
            processClassGroupsRecursively(
                    classMap,
                    entry.getKey(),
                    classGroup.getBaseClassName(),
                    classGroup.getDefinitions(),
                    config.getTheme());
        }

        return classMap;
    }

    @SuppressWarnings("unchecked")
    private static void processClassGroupsRecursively(
            ClassNameNode node,
            String classGroupId,
            String classGroupBaseClassName,
            List<Object> definitions,
            Map<String, List<Object>> theme) {
        ClassNameNode current = node;

        // In order to process all class groups but standalone
        if (classGroupBaseClassName != null && !classGroupBaseClassName.isEmpty()) {
            current = node.addNextNode(classGroupBaseClassName);
        }

        for (Object definition : definitions) {
            if (definition instanceof String) {
                String stringDefinition = (String) definition;
                ClassNameNode next = !stringDefinition.isEmpty() ? current.addNextNode(stringDefinition) : current;
                next.setClassGroupId(classGroupId);
            } else if (definition instanceof Predicate) {
                current.addValidator((Predicate<String>) definition, classGroupId);
            } else if (definition instanceof ThemeGetter) {
                processClassGroupsRecursively(
                        current, classGroupId, null, ((ThemeGetter) definition).apply(theme), theme);
            } else if (definition instanceof ClassGroup) {
                ClassGroup nestedClassGroup = (ClassGroup) definition;
                processClassGroupsRecursively(
                        current,
                        classGroupId,
                        nestedClassGroup.getBaseClassName(),
                        nestedClassGroup.getDefinitions(),
                        theme);
            }
        }
    }
}
