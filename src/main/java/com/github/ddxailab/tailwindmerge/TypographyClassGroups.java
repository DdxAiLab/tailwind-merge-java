package com.github.ddxailab.tailwindmerge;

import static com.github.ddxailab.tailwindmerge.DefaultConfigScales.COLOR_SCALE;
import static com.github.ddxailab.tailwindmerge.DefaultConfigScales.FONT_THEME;
import static com.github.ddxailab.tailwindmerge.DefaultConfigScales.FONT_WEIGHT_THEME;
import static com.github.ddxailab.tailwindmerge.DefaultConfigScales.LEADING_THEME;
import static com.github.ddxailab.tailwindmerge.DefaultConfigScales.LINE_STYLE_SCALE;
import static com.github.ddxailab.tailwindmerge.DefaultConfigScales.TEXT_THEME;
import static com.github.ddxailab.tailwindmerge.DefaultConfigScales.TRACKING_THEME;
import static com.github.ddxailab.tailwindmerge.DefaultConfigScales.UNAMBIGUOUS_SPACING_SCALE;
import static com.github.ddxailab.tailwindmerge.DefaultConfigScales.concat;
import static com.github.ddxailab.tailwindmerge.common.Validators.IS_ARBITRARY_FAMILY_NAME;
import static com.github.ddxailab.tailwindmerge.common.Validators.IS_ARBITRARY_LENGTH;
import static com.github.ddxailab.tailwindmerge.common.Validators.IS_ARBITRARY_NUMBER;
import static com.github.ddxailab.tailwindmerge.common.Validators.IS_ARBITRARY_VALUE;
import static com.github.ddxailab.tailwindmerge.common.Validators.IS_ARBITRARY_VARIABLE;
import static com.github.ddxailab.tailwindmerge.common.Validators.IS_ARBITRARY_VARIABLE_FAMILY_NAME;
import static com.github.ddxailab.tailwindmerge.common.Validators.IS_ARBITRARY_VARIABLE_LENGTH;
import static com.github.ddxailab.tailwindmerge.common.Validators.IS_ARBITRARY_VARIABLE_WEIGHT;
import static com.github.ddxailab.tailwindmerge.common.Validators.IS_ARBITRARY_WEIGHT;
import static com.github.ddxailab.tailwindmerge.common.Validators.IS_INTEGER;
import static com.github.ddxailab.tailwindmerge.common.Validators.IS_NUMBER;
import static com.github.ddxailab.tailwindmerge.common.Validators.IS_PERCENT;

import com.github.ddxailab.tailwindmerge.model.ClassGroup;
import java.util.Map;

/**
 * The "Typography" section of the default class groups.
 */
final class TypographyClassGroups {

    private TypographyClassGroups() {
    }

    static void addTo(Map<String, ClassGroup> groups) {
        // Font Size: https://tailwindcss.com/docs/font-size
        groups.put("font-size", ClassGroup.withBase("text",
                "base",
                TEXT_THEME,
                IS_ARBITRARY_LENGTH,
                IS_ARBITRARY_VARIABLE_LENGTH));
        // Font Smoothing: https://tailwindcss.com/docs/font-smoothing
        groups.put("font-smoothing", ClassGroup.of("antialiased", "subpixel-antialiased"));
        // Font Style: https://tailwindcss.com/docs/font-style
        groups.put("font-style", ClassGroup.of("italic", "not-italic"));
        // Font Weight: https://tailwindcss.com/docs/font-weight
        groups.put("font-weight", ClassGroup.withBase("font",
                FONT_WEIGHT_THEME, IS_ARBITRARY_WEIGHT, IS_ARBITRARY_VARIABLE_WEIGHT));
        // Font Stretch: https://tailwindcss.com/docs/font-stretch
        groups.put("font-stretch", ClassGroup.withBase("font-stretch",
                "ultra-condensed",
                "extra-condensed",
                "condensed",
                "semi-condensed",
                "normal",
                "semi-expanded",
                "expanded",
                "extra-expanded",
                "ultra-expanded",
                IS_PERCENT,
                IS_ARBITRARY_VALUE));
        // Font Family: https://tailwindcss.com/docs/font-family
        groups.put("font-family", ClassGroup.withBase("font",
                FONT_THEME,
                IS_ARBITRARY_FAMILY_NAME,
                IS_ARBITRARY_VARIABLE_FAMILY_NAME));
        // Font Feature Settings: https://tailwindcss.com/docs/font-feature-settings
        groups.put("font-features", ClassGroup.withBase("font-features", IS_ARBITRARY_VALUE));
        // Font Variant Numeric: https://tailwindcss.com/docs/font-variant-numeric
        groups.put("fvn-normal", ClassGroup.of("normal-nums"));
        groups.put("fvn-ordinal", ClassGroup.of("ordinal"));
        groups.put("fvn-slashed-zero", ClassGroup.of("slashed-zero"));
        groups.put("fvn-figure", ClassGroup.of("lining-nums", "oldstyle-nums"));
        groups.put("fvn-spacing", ClassGroup.of("proportional-nums", "tabular-nums"));
        groups.put("fvn-fraction", ClassGroup.of("diagonal-fractions", "stacked-fractions"));
        // Letter Spacing: https://tailwindcss.com/docs/letter-spacing
        groups.put("tracking", ClassGroup.withBase("tracking",
                TRACKING_THEME, IS_ARBITRARY_VALUE, IS_ARBITRARY_VARIABLE));
        // Line Clamp: https://tailwindcss.com/docs/line-clamp
        groups.put("line-clamp", ClassGroup.withBase("line-clamp",
                "none",
                IS_NUMBER,
                IS_ARBITRARY_NUMBER,
                IS_ARBITRARY_VARIABLE));
        // Line Height: https://tailwindcss.com/docs/line-height
        groups.put("leading", ClassGroup.withBase("leading", concat(
                "none",
                // Deprecated since Tailwind CSS v4.0.0.
                // See https://github.com/tailwindlabs/tailwindcss.com/issues/2027#issuecomment-2620152757
                LEADING_THEME,
                UNAMBIGUOUS_SPACING_SCALE)));
        // List Style Image: https://tailwindcss.com/docs/list-style-image
        groups.put("list-image", ClassGroup.withBase("list-image", "none", IS_ARBITRARY_VALUE, IS_ARBITRARY_VARIABLE));
        // List Style Position: https://tailwindcss.com/docs/list-style-position
        groups.put("list-style-position", ClassGroup.withBase("list", "inside", "outside"));
        // List Style Type: https://tailwindcss.com/docs/list-style-type
        groups.put("list-style-type", ClassGroup.withBase("list",
                "disc",
                "decimal",
                "none",
                IS_ARBITRARY_VALUE,
                IS_ARBITRARY_VARIABLE));
        // Text Alignment: https://tailwindcss.com/docs/text-align
        groups.put("text-alignment", ClassGroup.withBase("text", "left", "center", "right", "justify", "start", "end"));
        // Text Color: https://tailwindcss.com/docs/text-color
        groups.put("text-color", ClassGroup.withBase("text", COLOR_SCALE));
        // Text Decoration: https://tailwindcss.com/docs/text-decoration
        groups.put("text-decoration", ClassGroup.of("underline", "overline", "line-through", "no-underline"));
        // Text Decoration Style: https://tailwindcss.com/docs/text-decoration-style
        groups.put("text-decoration-style", ClassGroup.withBase("decoration", concat("wavy", LINE_STYLE_SCALE)));
        // Text Decoration Thickness: https://tailwindcss.com/docs/text-decoration-thickness
        groups.put("text-decoration-thickness", ClassGroup.withBase("decoration",
                "from-font",
                "auto",
                IS_NUMBER,
                IS_ARBITRARY_LENGTH,
                IS_ARBITRARY_VARIABLE));
        // Text Decoration Color: https://tailwindcss.com/docs/text-decoration-color
        groups.put("text-decoration-color", ClassGroup.withBase("decoration", COLOR_SCALE));
        // Text Underline Offset: https://tailwindcss.com/docs/text-underline-offset
        groups.put("underline-offset", ClassGroup.withBase("underline-offset",
                "auto",
                IS_NUMBER,
                IS_ARBITRARY_VALUE,
                IS_ARBITRARY_VARIABLE));
        // Text Transform: https://tailwindcss.com/docs/text-transform
        groups.put("text-transform", ClassGroup.of("uppercase", "lowercase", "capitalize", "normal-case"));
        // Text Overflow: https://tailwindcss.com/docs/text-overflow
        groups.put("text-overflow", ClassGroup.of("truncate", "text-ellipsis", "text-clip"));
        // Text Wrap: https://tailwindcss.com/docs/text-wrap
        groups.put("text-wrap", ClassGroup.withBase("text", "wrap", "nowrap", "balance", "pretty"));
        // Text Indent: https://tailwindcss.com/docs/text-indent
        groups.put("indent", ClassGroup.withBase("indent", UNAMBIGUOUS_SPACING_SCALE));
        // Tab Size: https://tailwindcss.com/docs/tab-size
        groups.put("tab-size", ClassGroup.withBase("tab", IS_INTEGER, IS_ARBITRARY_VARIABLE, IS_ARBITRARY_VALUE));
        // Vertical Alignment: https://tailwindcss.com/docs/vertical-align
        groups.put("vertical-align", ClassGroup.withBase("align",
                "baseline",
                "top",
                "middle",
                "bottom",
                "text-top",
                "text-bottom",
                "sub",
                "super",
                IS_ARBITRARY_VALUE,
                IS_ARBITRARY_VARIABLE));
        // Whitespace: https://tailwindcss.com/docs/whitespace
        groups.put("whitespace", ClassGroup.withBase("whitespace",
                "normal",
                "nowrap",
                "pre",
                "pre-line",
                "pre-wrap",
                "break-spaces"));
        // Word Break: https://tailwindcss.com/docs/word-break
        groups.put("break", ClassGroup.withBase("break", "normal", "words", "all", "keep"));
        // Overflow Wrap: https://tailwindcss.com/docs/overflow-wrap
        groups.put("wrap", ClassGroup.withBase("wrap", "break-word", "anywhere", "normal"));
        // Hyphens: https://tailwindcss.com/docs/hyphens
        groups.put("hyphens", ClassGroup.withBase("hyphens", "none", "manual", "auto"));
        // Content: https://tailwindcss.com/docs/content
        groups.put("content", ClassGroup.withBase("content", "none", IS_ARBITRARY_VALUE, IS_ARBITRARY_VARIABLE));
    }
}
