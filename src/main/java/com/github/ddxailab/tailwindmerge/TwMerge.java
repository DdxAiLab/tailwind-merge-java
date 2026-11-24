package com.github.ddxailab.tailwindmerge;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.regex.Pattern;

/**
 * A utility for merging conflicting Tailwind CSS classes.
 *
 * <p>The configuration is copied when the instance is created, so changing it afterwards has no
 * effect. Instances are safe to share between threads.</p>
 */
public final class TwMerge {

    private static final Pattern CLASSES_SEPARATOR = Pattern.compile("\\s+", Pattern.UNICODE_CHARACTER_CLASS);

    private final TwMergeContext context;
    private final LruCache<String, String> cache;

    /**
     * Initializes a new instance with the default configuration.
     */
    public TwMerge() {
        this(new TwMergeConfig());
    }

    /**
     * Initializes a new instance with the given configuration.
     *
     * @param config the configuration; it is copied, so later changes to it are ignored
     */
    public TwMerge(TwMergeConfig config) {
        if (config == null) {
            throw new IllegalArgumentException("config must not be null");
        }
        this.context = new TwMergeContext(config);
        this.cache = new LruCache<>(config.getCacheSize());
    }

    /**
     * Merges the given class names, resolving any conflicts if present.
     *
     * @param classNames the CSS classes to be merged; {@code null} and blank entries are ignored
     * @return the merged CSS classes, or an empty string if there is nothing to merge
     */
    public String merge(String... classNames) {
        if (classNames == null) {
            return "";
        }

        StringBuilder joined = new StringBuilder();
        for (String className : classNames) {
            if (className == null || isBlank(className)) {
                continue;
            }
            if (joined.length() > 0) {
                joined.append(' ');
            }
            joined.append(className);
        }

        if (joined.length() == 0) {
            return "";
        }

        String joinedClassNames = joined.toString();
        String cachedResult = cache.get(joinedClassNames);
        if (cachedResult != null) {
            return cachedResult;
        }

        String result = merge(joinedClassNames);
        cache.put(joinedClassNames, result);

        return result;
    }

    private String merge(String classList) {
        Set<String> classGroupsInConflict = new HashSet<>();
        String[] classNames = CLASSES_SEPARATOR.split(classList);

        // The classes are visited from the last to the first, so the result is built in reverse
        List<String> kept = new ArrayList<>();

        for (int i = classNames.length - 1; i >= 0; i--) {
            String originalClassName = classNames[i];
            if (originalClassName.isEmpty()) {
                // Leading whitespace produces an empty first part
                continue;
            }

            ClassModifiersInfo info = context.splitModifiers(originalClassName);

            if (info.isExternal()) {
                kept.add(originalClassName);
                continue;
            }

            String baseClassName = info.getBaseClassName();
            Integer postfixModifierPosition = info.getPostfixModifierPosition();
            boolean hasPostfixModifier = postfixModifierPosition != null;
            String classGroupId;

            if (hasPostfixModifier) {
                String baseClassNameWithoutPostfix = baseClassName.substring(0, postfixModifierPosition);
                classGroupId = context.getClassGroupId(baseClassNameWithoutPostfix);

                String classGroupIdWithPostfix =
                        !isNullOrEmpty(classGroupId) && context.isPostfixLookupClassGroup(classGroupId)
                                ? context.getClassGroupId(baseClassName)
                                : null;
                if (!isNullOrEmpty(classGroupIdWithPostfix) && !classGroupIdWithPostfix.equals(classGroupId)) {
                    classGroupId = classGroupIdWithPostfix;
                    hasPostfixModifier = false;
                }
            } else {
                classGroupId = context.getClassGroupId(baseClassName);
            }

            if (isNullOrEmpty(classGroupId)) {
                if (!hasPostfixModifier) {
                    // Not a Tailwind class
                    kept.add(originalClassName);
                    continue;
                }

                classGroupId = context.getClassGroupId(baseClassName);

                if (isNullOrEmpty(classGroupId)) {
                    // Not a Tailwind class
                    kept.add(originalClassName);
                    continue;
                }

                hasPostfixModifier = false;
            }

            String variantModifier = String.join(String.valueOf(Constants.MODIFIER_SEPARATOR),
                    context.sortModifiers(info.getModifiers()));

            String modifierId = info.hasImportantModifier()
                    ? variantModifier + Constants.IMPORTANT_MODIFIER
                    : variantModifier;

            String classId = modifierId + classGroupId;

            if (!classGroupsInConflict.add(classId)) {
                // Tailwind class omitted due to conflict
                continue;
            }

            for (String classGroup : context.getConflictingClassGroupIds(classGroupId, hasPostfixModifier)) {
                classGroupsInConflict.add(modifierId + classGroup);
            }

            // Tailwind class not in conflict
            kept.add(originalClassName);
        }

        StringBuilder result = new StringBuilder();
        for (int i = kept.size() - 1; i >= 0; i--) {
            if (result.length() > 0) {
                result.append(' ');
            }
            result.append(kept.get(i));
        }
        return result.toString();
    }

    private static boolean isNullOrEmpty(String value) {
        return value == null || value.isEmpty();
    }

    /** Like {@code string.IsNullOrWhiteSpace}: whitespace is the Unicode one, not only ASCII. */
    private static boolean isBlank(String value) {
        for (int i = 0; i < value.length(); ) {
            int codePoint = value.codePointAt(i);
            if (!Character.isWhitespace(codePoint) && !Character.isSpaceChar(codePoint)
                    && codePoint != '\u0085') {
                return false;
            }
            i += Character.charCount(codePoint);
        }
        return true;
    }
}
