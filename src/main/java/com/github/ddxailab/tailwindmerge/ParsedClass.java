package com.github.ddxailab.tailwindmerge;

import com.github.ddxailab.tailwindmerge.common.ArbitraryValue;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

/**
 * The parts of a single class name, as {@link TwMerge#merge(String...)} understands them. Instances
 * are created by {@link TwMerge#parse(String)}; they are immutable and safe to share between threads.
 *
 * <p>For a class that is not recognized ({@link #isRecognized()} is {@code false}) only the class name,
 * the variants, the important modifier and the base class name are filled in: whether it is negative,
 * has a modifier or has an arbitrary value is only told for recognized classes.
 */
public final class ParsedClass {

    private final String className;
    private final List<String> variants;
    private final boolean important;
    private final String baseClassName;
    private final String modifier;
    private final ArbitraryValue arbitraryValue;
    private final ArbitraryProperty arbitraryProperty;
    private final String classGroupId;
    private final boolean external;

    ParsedClass(
            String className,
            List<String> variants,
            boolean important,
            String baseClassName,
            String modifier,
            ArbitraryValue arbitraryValue,
            ArbitraryProperty arbitraryProperty,
            String classGroupId,
            boolean external) {
        this.className = className;
        this.variants = Collections.unmodifiableList(new ArrayList<>(variants));
        this.important = important;
        this.baseClassName = baseClassName;
        this.modifier = modifier;
        this.arbitraryValue = arbitraryValue;
        this.arbitraryProperty = arbitraryProperty;
        this.classGroupId = classGroupId;
        this.external = external;
    }

    /**
     * Gets the class name that was parsed, without surrounding white space.
     *
     * @return the whole class name, e.g. {@code md:hover:bg-red-300/50!}
     */
    public String getClassName() {
        return className;
    }

    /**
     * Gets the variants in the order they are written, without their {@code :} separator. Arbitrary
     * variants keep their brackets ({@code [&>*]}, {@code data-[open]}). The configured prefix is not
     * a variant.
     *
     * @return the variants (unmodifiable); empty if there are none
     */
    public List<String> getVariants() {
        return variants;
    }

    /**
     * Tells whether the class has the important modifier, either at the end ({@code p-4!}) or at the
     * start of the base class name as in Tailwind CSS v3 ({@code !p-4}).
     *
     * @return {@code true} for an important class
     */
    public boolean isImportant() {
        return important;
    }

    /**
     * Gets the class name without its prefix, variants, important modifier and postfix modifier. The
     * leading {@code -} of a negative value is part of it.
     *
     * @return the base class name, e.g. {@code bg-red-300}, {@code w-1/2} or {@code -m-4}
     */
    public String getBaseClassName() {
        return baseClassName;
    }

    /**
     * Tells whether the class has a negative value ({@code -m-4}).
     *
     * @return {@code true} if the class is recognized and its base class name starts with {@code -}
     */
    public boolean isNegative() {
        return isRecognized() && baseClassName.startsWith("-");
    }

    /**
     * Gets the postfix modifier, the text after the {@code /} of {@code bg-red-300/50} or
     * {@code text-lg/7}, as written. A slash that belongs to the value ({@code w-1/2}) is not a
     * modifier.
     *
     * @return the modifier without the slash, or an empty string if there is none
     */
    public String getModifier() {
        return modifier;
    }

    /**
     * Gets the arbitrary value or variable used as the value of the utility ({@code w-[10px]},
     * {@code text-(length:--size)}). The ones of variants, modifiers and arbitrary properties are not
     * reported here.
     *
     * @return the arbitrary value, or an empty optional if the class has none
     */
    public Optional<ArbitraryValue> getArbitraryValue() {
        return Optional.ofNullable(arbitraryValue);
    }

    /**
     * Gets the CSS declaration of an arbitrary property class ({@code [padding:1rem]}).
     *
     * @return the arbitrary property, or an empty optional if the class is not one
     */
    public Optional<ArbitraryProperty> getArbitraryProperty() {
        return Optional.ofNullable(arbitraryProperty);
    }

    /**
     * Gets the ID of the class group of the class, the one used to resolve conflicts. Arbitrary
     * properties have the ID {@code arbitrary..<property>}.
     *
     * @return the class group ID, or an empty string if the class is not recognized
     */
    public String getClassGroupId() {
        return classGroupId;
    }

    /**
     * Tells whether the class belongs to a class group.
     *
     * @return {@code true} for a recognized class
     */
    public boolean isRecognized() {
        return !classGroupId.isEmpty();
    }

    /**
     * Tells whether the class lacks the configured prefix. Such a class is not interpreted at all: its
     * base class name is the whole class name and it is not recognized.
     *
     * @return {@code true} if a prefix is configured and the class does not start with it
     */
    public boolean isExternal() {
        return external;
    }

    /**
     * Gets the class name that was parsed.
     *
     * @return the same as {@link #getClassName()}
     */
    @Override
    public String toString() {
        return className;
    }

    /**
     * The CSS declaration written by an arbitrary property class such as {@code [padding:1rem]}.
     * Instances are immutable.
     */
    public static final class ArbitraryProperty {

        private final String name;
        private final String value;

        ArbitraryProperty(String name, String value) {
            this.name = name;
            this.value = value;
        }

        /**
         * Gets the CSS property.
         *
         * @return the property, e.g. {@code padding}
         */
        public String getName() {
            return name;
        }

        /**
         * Gets the value of the property, exactly as written (underscores are not turned into spaces).
         *
         * @return the value, e.g. {@code 1rem}
         */
        public String getValue() {
            return value;
        }
    }
}
