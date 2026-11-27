package com.github.ddxailab.tailwindmerge;

import com.github.ddxailab.tailwindmerge.common.Validators;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Everything the merge needs from a configuration: the class map, the conflicts and the rules to
 * split and sort the modifiers of a class name.
 *
 * <p>The state is copied from the configuration when the context is created, so changes made to the
 * configuration afterwards have no effect. The context is immutable and safe to share between threads
 * (as long as the validators of the configuration are).
 */
final class TwMergeContext {

    /** Start of the class group ID of an arbitrary property, followed by the property. */
    static final String ARBITRARY_PROPERTY_GROUP_PREFIX = "arbitrary..";

    private static final Pattern ARBITRARY_PROPERTY =
            Pattern.compile("^\\[(.+)\\]$", Pattern.UNIX_LINES | Pattern.UNICODE_CHARACTER_CLASS);

    private final ClassNameNode classMap;
    private final String prefix;
    private final Map<String, List<String>> conflictingClassGroups;
    private final Map<String, List<String>> conflictingClassGroupModifiers;
    private final Set<String> postfixLookupClassGroups;
    private final Set<String> orderSensitiveModifiers;

    TwMergeContext(TwMergeConfig config) {
        this.classMap = TwMergeMapFactory.create(config);
        this.prefix = config.getPrefix();
        this.conflictingClassGroups = copy(config.getConflictingClassGroups());
        this.conflictingClassGroupModifiers = copy(config.getConflictingClassGroupModifiers());
        this.postfixLookupClassGroups = new HashSet<>(config.getPostfixLookupClassGroups());
        this.orderSensitiveModifiers = new HashSet<>(config.getOrderSensitiveModifiers());
    }

    boolean isPostfixLookupClassGroup(String classGroupId) {
        return postfixLookupClassGroups.contains(classGroupId);
    }

    /**
     * Finds the class group of a class name (without modifiers).
     *
     * @param className the base class name
     * @return the class group ID, or {@code null} if the class does not belong to any class group
     */
    String getClassGroupId(String className) {
        String[] classNameParts = splitClassName(className);

        // Classes like "-inset-1" produce an empty string as the first classNamePart.
        // We assume that classes for negative values are used correctly and remove it from classNameParts.
        int start = classNameParts[0].isEmpty() && classNameParts.length != 1 ? 1 : 0;

        String classGroupId = getClassGroupIdRecursive(classNameParts, start, classMap);
        return classGroupId != null ? classGroupId : getGroupIdForArbitraryProperty(className);
    }

    /**
     * Tells whether the slash of a class name belongs to its value, as in {@code w-1/2}, instead of
     * introducing a postfix modifier: the class group accepts the text after the last {@code -} as a
     * literal definition or as a fraction.
     *
     * @param className    the base class name, slash included
     * @param classGroupId the class group of the class
     * @return {@code true} if the slash is part of the value
     */
    boolean isSlashPartOfValue(String className, String classGroupId) {
        String[] parts = splitClassName(className);

        // Walk to the node of the utility, i.e. every part but the value
        ClassNameNode node = classMap;
        for (int i = 0; i < parts.length - 1; i++) {
            if (!parts[i].isEmpty()) {
                node = child(node, parts[i]);
                if (node == null) {
                    return false;
                }
            }
        }

        String value = parts[parts.length - 1];
        if (value.indexOf('/') < 0) {
            return false;
        }

        ClassNameNode literal = child(node, value);
        if (literal != null && classGroupId.equals(literal.getClassGroupId())) {
            return true;
        }

        if (node.getValidators() == null || !Validators.IS_FRACTION.test(value)) {
            return false;
        }

        for (ClassValidator validator : node.getValidators()) {
            if (validator.getValidator() == Validators.IS_FRACTION
                    && validator.getClassGroupId().equals(classGroupId)) {
                return true;
            }
        }

        return false;
    }

    private static ClassNameNode child(ClassNameNode node, String part) {
        return node.getNext() == null ? null : node.getNext().get(part);
    }

    /**
     * Gets the class groups that conflict with a class group.
     *
     * @param classGroupId       the class group ID
     * @param hasPostfixModifier whether the class has a postfix modifier ({@code /})
     * @return the IDs of the conflicting class groups; empty if there are none
     */
    List<String> getConflictingClassGroupIds(String classGroupId, boolean hasPostfixModifier) {
        List<String> conflicts = conflictingClassGroups.get(classGroupId);
        List<String> conflictingModifiers =
                hasPostfixModifier ? conflictingClassGroupModifiers.get(classGroupId) : null;

        if (conflictingModifiers == null) {
            return conflicts != null ? conflicts : Collections.<String>emptyList();
        }

        List<String> all = new ArrayList<>();
        if (conflicts != null) {
            all.addAll(conflicts);
        }
        all.addAll(conflictingModifiers);
        return all;
    }

    /**
     * Splits a class name into its modifiers, its important modifier, its base class name and the
     * position of its postfix modifier.
     *
     * @param className the full class name, e.g. {@code hover:md:bg-red-500/50!}
     * @return the parts of the class name
     */
    ClassModifiersInfo splitModifiers(String className) {
        // Skipping 'experimentalParseClassName' for the time being.
        // https://github.com/dcastil/tailwind-merge/blob/main/src/lib/parse-class-name.ts#L83
        if (prefix == null || prefix.isEmpty()) {
            return parseClassName(className);
        }

        String fullPrefix = prefix + Constants.MODIFIER_SEPARATOR;
        if (className.startsWith(fullPrefix)) {
            return parseClassName(className.substring(fullPrefix.length()));
        }

        return new ClassModifiersInfo(Collections.<String>emptyList(), false, className, null, true);
    }

    /**
     * Sorts the modifiers so that equivalent class names produce the same list. Modifiers that are
     * order sensitive (arbitrary variants and the configured ones) keep their position and split the
     * list in segments that are sorted independently.
     *
     * @param modifiers the modifiers in the order they appear in the class name
     * @return the modifiers in canonical order
     */
    List<String> sortModifiers(Collection<String> modifiers) {
        if (modifiers.size() <= 1) {
            return new ArrayList<>(modifiers);
        }

        List<String> sortedModifiers = new ArrayList<>(modifiers.size());
        List<String> unsortedModifiers = new ArrayList<>();

        for (String modifier : modifiers) {
            boolean isPositionSensitive = modifier.startsWith("[") || orderSensitiveModifiers.contains(modifier);

            if (isPositionSensitive) {
                if (!unsortedModifiers.isEmpty()) {
                    // Only a canonical order is needed (the result is used as a key, never shown),
                    // so the natural String order replaces the culture-sensitive sort of C#
                    Collections.sort(unsortedModifiers);
                    sortedModifiers.addAll(unsortedModifiers);
                    unsortedModifiers.clear();
                }

                sortedModifiers.add(modifier);
            } else {
                unsortedModifiers.add(modifier);
            }
        }

        if (!unsortedModifiers.isEmpty()) {
            Collections.sort(unsortedModifiers);
            sortedModifiers.addAll(unsortedModifiers);
        }

        return sortedModifiers;
    }

    private static ClassModifiersInfo parseClassName(String className) {
        List<String> modifiers = new ArrayList<>();
        int bracketDepth = 0;
        int parenDepth = 0;
        int modifierStart = 0;
        int postfixModifierPosition = -1;

        for (int i = 0; i < className.length(); i++) {
            char currChar = className.charAt(i);

            if (bracketDepth == 0 && parenDepth == 0) {
                if (currChar == Constants.MODIFIER_SEPARATOR) {
                    modifiers.add(className.substring(modifierStart, i));
                    modifierStart = i + Constants.MODIFIER_SEPARATOR_LENGTH;
                    continue;
                }

                if (currChar == '/') {
                    postfixModifierPosition = i;
                    continue;
                }
            }

            switch (currChar) {
                case '[':
                    bracketDepth++;
                    break;
                case ']':
                    bracketDepth--;
                    break;
                case '(':
                    parenDepth++;
                    break;
                case ')':
                    parenDepth--;
                    break;
                default:
                    break;
            }
        }

        String baseClassNameWithImportantModifier = modifiers.isEmpty() ? className : className.substring(modifierStart);
        String baseClassName = stripImportantModifier(baseClassNameWithImportantModifier);
        boolean hasImportantModifier = !baseClassName.equals(baseClassNameWithImportantModifier);

        Integer postfixPosition =
                postfixModifierPosition > modifierStart ? Integer.valueOf(postfixModifierPosition - modifierStart) : null;

        return new ClassModifiersInfo(modifiers, hasImportantModifier, baseClassName, postfixPosition);
    }

    private static String stripImportantModifier(String className) {
        if (!className.isEmpty() && className.charAt(className.length() - 1) == Constants.IMPORTANT_MODIFIER) {
            return className.substring(0, className.length() - 1);
        }

        /*
         * In Tailwind CSS v3 the important modifier was at the start of the base class name.
         * This is still supported for legacy reasons.
         * See https://github.com/dcastil/tailwind-merge/issues/513#issuecomment-2614029864
         */
        if (!className.isEmpty() && className.charAt(0) == Constants.IMPORTANT_MODIFIER) {
            return className.substring(1);
        }

        return className;
    }

    private static String getClassGroupIdRecursive(String[] classNameParts, int index, ClassNameNode node) {
        if (index >= classNameParts.length) {
            return node.getClassGroupId();
        }

        Map<String, ClassNameNode> next = node.getNext();
        if (next != null) {
            ClassNameNode nextNode = next.get(classNameParts[index]);
            if (nextNode != null) {
                String classGroupId = getClassGroupIdRecursive(classNameParts, index + 1, nextNode);
                if (classGroupId != null && !classGroupId.isEmpty()) {
                    return classGroupId;
                }
            }
        }

        List<ClassValidator> validators = node.getValidators();
        if (validators == null || validators.isEmpty()) {
            return null;
        }

        String classNameRest = joinFrom(classNameParts, index);
        for (ClassValidator validator : validators) {
            if (validator.getValidator().test(classNameRest)) {
                return validator.getClassGroupId();
            }
        }

        return null;
    }

    private static String getGroupIdForArbitraryProperty(String className) {
        Matcher match = ARBITRARY_PROPERTY.matcher(className);
        if (match.find()) {
            String arbitraryPropertyClassName = match.group(1);
            if (!arbitraryPropertyClassName.isEmpty()) {
                int colon = arbitraryPropertyClassName.indexOf(':');
                // The C# code throws when there is no colon ("[foo]"); no group is the sensible answer
                if (colon >= 0) {
                    return ARBITRARY_PROPERTY_GROUP_PREFIX + arbitraryPropertyClassName.substring(0, colon);
                }
            }
        }

        return null;
    }

    /** Splits on {@link Constants#CLASS_NAME_SEPARATOR} keeping the empty parts, like {@code string.Split}. */
    private static String[] splitClassName(String className) {
        List<String> parts = new ArrayList<>();
        int start = 0;
        int end;
        while ((end = className.indexOf(Constants.CLASS_NAME_SEPARATOR, start)) >= 0) {
            parts.add(className.substring(start, end));
            start = end + 1;
        }
        parts.add(className.substring(start));
        return parts.toArray(new String[0]);
    }

    private static String joinFrom(String[] parts, int from) {
        StringBuilder sb = new StringBuilder();
        for (int i = from; i < parts.length; i++) {
            if (i > from) {
                sb.append(Constants.CLASS_NAME_SEPARATOR);
            }
            sb.append(parts[i]);
        }
        return sb.toString();
    }

    private static Map<String, List<String>> copy(Map<String, List<String>> source) {
        Map<String, List<String>> result = new LinkedHashMap<>();
        for (Map.Entry<String, List<String>> entry : source.entrySet()) {
            result.put(entry.getKey(), Collections.unmodifiableList(new ArrayList<>(entry.getValue())));
        }
        return result;
    }
}
