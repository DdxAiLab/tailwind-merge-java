package com.github.ddxailab.tailwindmerge.common;

import java.util.function.Predicate;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Provides a set of validation functions for different class name parts.
 *
 * <p>Every validator is a {@link Predicate} exposed as a constant so it can be used directly as a
 * definition of a class group.
 */
public final class Validators {

    // Regular expressions. They are compiled with UNIX_LINES, UNICODE_CHARACTER_CLASS (and, for the
    // case-insensitive ones, UNICODE_CASE) so that they behave like their .NET counterparts: only
    // '\n' is a line terminator and \d, \w and \b are Unicode-aware.

    private static final Pattern ARBITRARY_VALUE =
            compile("^\\[(?:(\\w[\\w-]*):)?(.+)\\]$", true);

    private static final Pattern ARBITRARY_VARIABLE =
            compile("^\\((?:(\\w[\\w-]*):)?(.+)\\)$", true);

    private static final Pattern FRACTION =
            compile("^\\d+(?:\\.\\d+)?\\/\\d+(?:\\.\\d+)?$", false);

    private static final Pattern TSHIRT_UNIT =
            compile("^(\\d+(\\.\\d+)?)?(xs|sm|md|lg|xl)$", false);

    private static final Pattern LENGTH_UNIT = compile(
            "\\d+(%|px|r?em|[sdl]?v([hwib]|min|max)|pt|pc|in|cm|mm|cap|ch|ex|r?lh|cq(w|h|i|b|min|max))"
                    + "|\\b(calc|min|max|clamp)\\(.+\\)|^0$",
            false);

    private static final Pattern COLOR_FUNCTION = compile(
            "^(rgba?|hsla?|hwb|(ok)?(lab|lch)|color-mix|color|light-dark)\\(.+\\)$", false);

    private static final Pattern SHADOW = compile(
            "^(inset_)?-?((\\d+)?\\.?(\\d+)[a-z]+|0)_-?((\\d+)?\\.?(\\d+)[a-z]+|0)", false);

    private static final Pattern IMAGE = compile(
            "^(url|image|image-set|cross-fade|element|(repeating-)?(linear|radial|conic)-gradient)\\(.+\\)$",
            false);

    // Equivalent of double.TryParse(value, NumberStyles.Float, CultureInfo.InvariantCulture, ...):
    // optional surrounding white space, optional sign, decimal point and exponent. The digits are
    // ASCII only on purpose (the .NET parser does not accept other Unicode digits either).
    // NaN is not recognized: neither IS_NUMBER nor IS_INTEGER accept it anyway.
    private static final Pattern NUMBER = Pattern.compile(
            "^[\\t\\n\\u000B\\f\\r ]*([+-]?)(?:([0-9]+\\.?[0-9]*|\\.[0-9]+)(?:[eE][+-]?[0-9]+)?|(?i:infinity))"
                    + "[\\t\\n\\u000B\\f\\r ]*$",
            Pattern.UNIX_LINES);

    /** Always returns {@code true}, indicating that any value is valid. */
    public static final Predicate<String> IS_ANY = value -> true;

    /** Validates whether a string represents a fraction. */
    public static final Predicate<String> IS_FRACTION = value -> FRACTION.matcher(value).find();

    /** Validates whether a string represents a number. */
    public static final Predicate<String> IS_NUMBER = value -> parseNumber(value) != null;

    /** Validates whether a string represents an integer. */
    public static final Predicate<String> IS_INTEGER = value -> {
        Double number = parseNumber(value);
        return number != null && !Double.isInfinite(number) && number == Math.rint(number);
    };

    /** Validates whether a string represents a percentage. */
    public static final Predicate<String> IS_PERCENT = value ->
            value.endsWith("%") && IS_NUMBER.test(value.substring(0, value.length() - 1));

    /** Validates whether a string represents a t-shirt size value. */
    public static final Predicate<String> IS_TSHIRT_SIZE = value -> TSHIRT_UNIT.matcher(value).find();

    /** Validates whether a string represents an arbitrary value. */
    public static final Predicate<String> IS_ARBITRARY_VALUE = value -> ARBITRARY_VALUE.matcher(value).find();

    /** Validates whether a string represents an arbitrary variable. */
    public static final Predicate<String> IS_ARBITRARY_VARIABLE =
            value -> ARBITRARY_VARIABLE.matcher(value).find();

    /** Validates whether a string represents an arbitrary variable length value. */
    public static final Predicate<String> IS_ARBITRARY_VARIABLE_LENGTH =
            value -> getIsArbitraryVariable(value, Validators::isLabelLength, false);

    /** Validates whether a string represents an arbitrary variable family name value. */
    public static final Predicate<String> IS_ARBITRARY_VARIABLE_FAMILY_NAME =
            value -> getIsArbitraryVariable(value, Validators::isLabelFamilyName, false);

    /** Validates whether a string represents an arbitrary variable position value. */
    public static final Predicate<String> IS_ARBITRARY_VARIABLE_POSITION =
            value -> getIsArbitraryVariable(value, Validators::isLabelPosition, false);

    /** Validates whether a string represents an arbitrary variable size value. */
    public static final Predicate<String> IS_ARBITRARY_VARIABLE_SIZE =
            value -> getIsArbitraryVariable(value, Validators::isLabelSize, false);

    /** Validates whether a string represents an arbitrary variable image value. */
    public static final Predicate<String> IS_ARBITRARY_VARIABLE_IMAGE =
            value -> getIsArbitraryVariable(value, Validators::isLabelImage, false);

    /** Validates whether a string represents an arbitrary variable shadow value. */
    public static final Predicate<String> IS_ARBITRARY_VARIABLE_SHADOW =
            value -> getIsArbitraryVariable(value, Validators::isLabelShadow, true);

    /** Validates whether a string represents an arbitrary variable font weight value. */
    public static final Predicate<String> IS_ARBITRARY_VARIABLE_WEIGHT =
            value -> getIsArbitraryVariable(value, Validators::isLabelWeight, true);

    /** Validates whether a string represents an arbitrary size. */
    public static final Predicate<String> IS_ARBITRARY_SIZE =
            value -> getIsArbitraryValue(value, Validators::isLabelSize, Validators::isNever);

    /** Validates whether a string represents an arbitrary length value. */
    public static final Predicate<String> IS_ARBITRARY_LENGTH =
            value -> getIsArbitraryValue(value, Validators::isLabelLength, Validators::isLengthOnly);

    /** Validates whether a string represents an arbitrary number. */
    public static final Predicate<String> IS_ARBITRARY_NUMBER =
            value -> getIsArbitraryValue(value, Validators::isLabelNumber, IS_NUMBER);

    /** Validates whether a string represents an arbitrary font weight value. */
    public static final Predicate<String> IS_ARBITRARY_WEIGHT =
            value -> getIsArbitraryValue(value, Validators::isLabelWeight, IS_ANY);

    /** Validates whether a string represents an arbitrary font family name value. */
    public static final Predicate<String> IS_ARBITRARY_FAMILY_NAME =
            value -> getIsArbitraryValue(value, Validators::isLabelFamilyName, Validators::isNever);

    /** Validates whether a string represents an arbitrary position. */
    public static final Predicate<String> IS_ARBITRARY_POSITION =
            value -> getIsArbitraryValue(value, Validators::isLabelPosition, Validators::isNever);

    /** Validates whether a string represents an arbitrary image value. */
    public static final Predicate<String> IS_ARBITRARY_IMAGE =
            value -> getIsArbitraryValue(value, Validators::isLabelImage, Validators::isImage);

    /** Validates whether a string represents an arbitrary shadow value. */
    public static final Predicate<String> IS_ARBITRARY_SHADOW =
            value -> getIsArbitraryValue(value, Validators::isLabelShadow, Validators::isShadow);

    /** Validates whether a string is not an arbitrary value or variable. */
    public static final Predicate<String> IS_ANY_NON_ARBITRARY =
            value -> !IS_ARBITRARY_VALUE.test(value) && !IS_ARBITRARY_VARIABLE.test(value);

    /** Validates whether a string represents a named container query class. */
    public static final Predicate<String> IS_NAMED_CONTAINER_QUERY = value ->
            value.startsWith("@container")
                    && ((value.length() > 10 && value.charAt(10) == '/' && value.length() > 11)
                    || (value.length() > 11 && value.charAt(11) == 's'
                            && value.length() > 16 && value.startsWith("-size/", 10))
                    || (value.length() > 11 && value.charAt(11) == 'n'
                            && value.length() > 18 && value.startsWith("-normal/", 10)));

    private Validators() {
    }

    private static Pattern compile(String regex, boolean ignoreCase) {
        int flags = Pattern.UNIX_LINES | Pattern.UNICODE_CHARACTER_CLASS;
        if (ignoreCase) {
            flags |= Pattern.CASE_INSENSITIVE | Pattern.UNICODE_CASE;
        }
        return Pattern.compile(regex, flags);
    }

    private static boolean getIsArbitraryValue(
            String value, Predicate<String> testLabel, Predicate<String> testValue) {
        Matcher match = ARBITRARY_VALUE.matcher(value);
        if (match.find()) {
            String label = match.group(1);
            if (label != null && !label.isEmpty()) {
                return testLabel.test(label);
            }
            return testValue.test(match.group(2));
        }
        return false;
    }

    private static boolean getIsArbitraryVariable(
            String value, Predicate<String> testLabel, boolean shouldMatchNoLabel) {
        Matcher match = ARBITRARY_VARIABLE.matcher(value);
        if (match.find()) {
            String label = match.group(1);
            if (label != null && !label.isEmpty()) {
                return testLabel.test(label);
            }
            return shouldMatchNoLabel;
        }
        return false;
    }

    /**
     * Parses {@code value} like {@code double.TryParse} with {@code NumberStyles.Float} and the
     * invariant culture. Returns {@code null} when it is not a number (or is {@code NaN}).
     */
    private static Double parseNumber(String value) {
        if (value == null || value.isEmpty()) {
            return null;
        }
        Matcher match = NUMBER.matcher(value);
        if (!match.find()) {
            return null;
        }
        boolean negative = "-".equals(match.group(1));
        if (match.group(2) == null) {
            return negative ? Double.NEGATIVE_INFINITY : Double.POSITIVE_INFINITY;
        }
        // The matched text (without white space) is a plain decimal literal that Java can parse.
        return Double.valueOf(value.trim());
    }

    private static boolean isLengthOnly(String value) {
        return LENGTH_UNIT.matcher(value).find() && !COLOR_FUNCTION.matcher(value).find();
    }

    private static boolean isNever(String value) {
        return false;
    }

    private static boolean isShadow(String value) {
        return SHADOW.matcher(value).find();
    }

    private static boolean isImage(String value) {
        return IMAGE.matcher(value).find();
    }

    private static boolean isLabelLength(String label) {
        return "length".equals(label);
    }

    private static boolean isLabelNumber(String label) {
        return "number".equals(label);
    }

    private static boolean isLabelShadow(String label) {
        return "shadow".equals(label);
    }

    private static boolean isLabelPosition(String label) {
        return "position".equals(label) || "percentage".equals(label);
    }

    private static boolean isLabelFamilyName(String label) {
        return "family-name".equals(label);
    }

    private static boolean isLabelWeight(String label) {
        return "number".equals(label) || "weight".equals(label);
    }

    private static boolean isLabelSize(String label) {
        return "length".equals(label) || "size".equals(label) || "bg-size".equals(label);
    }

    private static boolean isLabelImage(String label) {
        return "image".equals(label) || "url".equals(label);
    }
}
