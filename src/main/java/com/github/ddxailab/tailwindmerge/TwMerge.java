package com.github.ddxailab.tailwindmerge;

import static com.github.ddxailab.tailwindmerge.common.Validators.IS_ARBITRARY_VALUE;
import static com.github.ddxailab.tailwindmerge.common.Validators.IS_ARBITRARY_VARIABLE;
import static com.github.ddxailab.tailwindmerge.common.Validators.IS_NUMBER;

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

    /** Group of the legacy {@code bg-opacity-*} classes; it only exists while merging, it is not a config group. */
    private static final String LEGACY_BG_OPACITY_GROUP = "bg-opacity";
    private static final String LEGACY_BG_OPACITY_PREFIX = "bg-opacity-";
    private static final String BG_COLOR_GROUP = "bg-color";

    private final TwMergeContext context;
    private final LruCache<String, String> cache;
    private final boolean legacyBgOpacity;

    /** Holder idiom: the shared instance is created the first time {@link #getDefault()} is called. */
    private static final class DefaultHolder {
        static final TwMerge INSTANCE = new TwMerge();
    }

    /**
     * Gets a shared instance with the default configuration, created lazily on first use.
     *
     * <p>It is the Java counterpart of registering {@code TwMerge} as a singleton in the C# dependency
     * injection container. Use it when the default configuration is enough; to customize the
     * configuration create your own instance with {@link #TwMerge(TwMergeConfig)} and share it.</p>
     *
     * @return the shared instance; it is thread-safe
     */
    public static TwMerge getDefault() {
        return DefaultHolder.INSTANCE;
    }

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
        this.legacyBgOpacity = config.isLegacyBgOpacity();
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
        List<KeptClass> kept = new ArrayList<>();

        for (int i = classNames.length - 1; i >= 0; i--) {
            String originalClassName = classNames[i];
            if (originalClassName.isEmpty()) {
                // Leading whitespace produces an empty first part
                continue;
            }

            ClassModifiersInfo info = context.splitModifiers(originalClassName);

            if (info.isExternal()) {
                kept.add(KeptClass.unrelated(originalClassName));
                continue;
            }

            String baseClassName = info.getBaseClassName();
            Integer postfixModifierPosition = info.getPostfixModifierPosition();
            boolean hasPostfixModifier = postfixModifierPosition != null;
            String classGroupId;

            String legacyOpacity = legacyBgOpacity && !hasPostfixModifier ? legacyBgOpacityValue(baseClassName) : null;

            if (legacyOpacity != null) {
                // A class of its own group: it does not conflict with the background color
                classGroupId = LEGACY_BG_OPACITY_GROUP;
            } else if (hasPostfixModifier) {
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
                    kept.add(KeptClass.unrelated(originalClassName));
                    continue;
                }

                classGroupId = context.getClassGroupId(baseClassName);

                if (isNullOrEmpty(classGroupId)) {
                    // Not a Tailwind class
                    kept.add(KeptClass.unrelated(originalClassName));
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
            kept.add(new KeptClass(originalClassName, modifierId, classGroupId, hasPostfixModifier, legacyOpacity));
        }

        if (legacyBgOpacity) {
            applyLegacyBgOpacity(kept);
        }

        StringBuilder result = new StringBuilder();
        for (int i = kept.size() - 1; i >= 0; i--) {
            if (result.length() > 0) {
                result.append(' ');
            }
            result.append(kept.get(i).text);
        }
        return result.toString();
    }

    /**
     * Moves the value of every surviving legacy {@code bg-opacity-*} class (Tailwind CSS v3) into the
     * surviving {@code bg-<color>} class that has the same modifiers, as {@code bg-<color>/<value>}, and
     * drops the {@code bg-opacity-*} class. A {@code bg-opacity-*} class with no matching color is kept.
     * A color that already has its own {@code /<value>} keeps it, as in Tailwind CSS v3 where an explicit
     * alpha ignores {@code bg-opacity-*}; the {@code bg-opacity-*} class is dropped.
     */
    private static void applyLegacyBgOpacity(List<KeptClass> kept) {
        for (int i = 0; i < kept.size(); i++) {
            KeptClass opacity = kept.get(i);
            if (opacity.legacyOpacity == null) {
                continue;
            }

            // At most one background color per modifiers survives the conflict resolution
            KeptClass color = null;
            for (KeptClass candidate : kept) {
                if (BG_COLOR_GROUP.equals(candidate.classGroupId)
                        && opacity.modifierId.equals(candidate.modifierId)) {
                    color = candidate;
                    break;
                }
            }

            if (color == null) {
                continue;
            }

            if (!color.hasPostfixModifier) {
                color.text = withOpacity(color.text, opacity.legacyOpacity);
            }
            kept.remove(i);
            i--;
        }
    }

    /** Appends {@code /<opacity>} to a class, before its trailing important modifier if it has one. */
    private static String withOpacity(String className, String opacity) {
        boolean trailingImportant =
                !className.isEmpty() && className.charAt(className.length() - 1) == Constants.IMPORTANT_MODIFIER;
        String body = trailingImportant ? className.substring(0, className.length() - 1) : className;
        return body + '/' + opacity + (trailingImportant ? String.valueOf(Constants.IMPORTANT_MODIFIER) : "");
    }

    /**
     * Gets the opacity of a legacy {@code bg-opacity-<value>} base class name.
     *
     * @return the value ({@code 50}, {@code [0.35]}, {@code (--my-opacity)}), or {@code null} if the
     *         class is not a legacy background opacity class
     */
    private static String legacyBgOpacityValue(String baseClassName) {
        if (!baseClassName.startsWith(LEGACY_BG_OPACITY_PREFIX)) {
            return null;
        }

        String value = baseClassName.substring(LEGACY_BG_OPACITY_PREFIX.length());
        return IS_NUMBER.test(value) || IS_ARBITRARY_VALUE.test(value) || IS_ARBITRARY_VARIABLE.test(value)
                ? value
                : null;
    }

    /** A class that survived the conflict resolution, with what is needed to merge legacy opacities. */
    private static final class KeptClass {
        /** The class as written; rewritten when an opacity is merged into it. */
        String text;
        /** Variants and important modifier, or {@code null} for classes that are not Tailwind classes. */
        final String modifierId;
        final String classGroupId;
        final boolean hasPostfixModifier;
        /** The value of a legacy {@code bg-opacity-*} class, otherwise {@code null}. */
        final String legacyOpacity;

        KeptClass(String text, String modifierId, String classGroupId, boolean hasPostfixModifier,
                String legacyOpacity) {
            this.text = text;
            this.modifierId = modifierId;
            this.classGroupId = classGroupId;
            this.hasPostfixModifier = hasPostfixModifier;
            this.legacyOpacity = legacyOpacity;
        }

        static KeptClass unrelated(String text) {
            return new KeptClass(text, null, null, false, null);
        }
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
