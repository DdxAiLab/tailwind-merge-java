package com.github.ddxailab.tailwindmerge;

import static com.github.ddxailab.tailwindmerge.common.Validators.IS_ARBITRARY_LENGTH;
import static com.github.ddxailab.tailwindmerge.common.Validators.IS_ARBITRARY_POSITION;
import static com.github.ddxailab.tailwindmerge.common.Validators.IS_ARBITRARY_SIZE;
import static com.github.ddxailab.tailwindmerge.common.Validators.IS_ARBITRARY_VALUE;
import static com.github.ddxailab.tailwindmerge.common.Validators.IS_ARBITRARY_VARIABLE;
import static com.github.ddxailab.tailwindmerge.common.Validators.IS_ARBITRARY_VARIABLE_LENGTH;
import static com.github.ddxailab.tailwindmerge.common.Validators.IS_ARBITRARY_VARIABLE_POSITION;
import static com.github.ddxailab.tailwindmerge.common.Validators.IS_ARBITRARY_VARIABLE_SIZE;
import static com.github.ddxailab.tailwindmerge.common.Validators.IS_FRACTION;
import static com.github.ddxailab.tailwindmerge.common.Validators.IS_INTEGER;
import static com.github.ddxailab.tailwindmerge.common.Validators.IS_NUMBER;
import static com.github.ddxailab.tailwindmerge.common.Validators.IS_PERCENT;

import com.github.ddxailab.tailwindmerge.common.ThemeGetter;
import com.github.ddxailab.tailwindmerge.common.ThemeKeys;
import com.github.ddxailab.tailwindmerge.common.ThemeUtility;
import com.github.ddxailab.tailwindmerge.model.ClassGroup;
import java.util.ArrayList;
import java.util.List;

/**
 * Theme getters and scales shared by the sections of the default configuration.
 *
 * <p>They are the "helpers to avoid repeating the same scales" of the reference
 * {@code TwMergeConfig} constructor. Every value is immutable (a {@link ThemeGetter}, a
 * {@link ClassGroup} or a validator is stateless), so a single static copy is shared by all the
 * configurations.
 *
 * <p>The scales are {@code Object[]} so they can be passed straight to {@link ClassGroup#of} /
 * {@link ClassGroup#withBase} as the whole definition list. To embed a scale in a longer list use
 * {@link #concat(Object...)}; never pass it next to other arguments, as it would be nested instead
 * of spread.
 */
final class DefaultConfigScales {

    // Theme getters for theme variable namespaces
    // See https://tailwindcss.com/docs/theme#theme-variable-namespaces

    static final ThemeGetter COLOR_THEME = ThemeUtility.fromTheme(ThemeKeys.COLOR);
    static final ThemeGetter FONT_THEME = ThemeUtility.fromTheme(ThemeKeys.FONT);
    static final ThemeGetter TEXT_THEME = ThemeUtility.fromTheme(ThemeKeys.TEXT);
    static final ThemeGetter FONT_WEIGHT_THEME = ThemeUtility.fromTheme(ThemeKeys.FONT_WEIGHT);
    static final ThemeGetter TRACKING_THEME = ThemeUtility.fromTheme(ThemeKeys.TRACKING);
    static final ThemeGetter LEADING_THEME = ThemeUtility.fromTheme(ThemeKeys.LEADING);
    static final ThemeGetter BREAKPOINT_THEME = ThemeUtility.fromTheme(ThemeKeys.BREAKPOINT);
    static final ThemeGetter CONTAINER_THEME = ThemeUtility.fromTheme(ThemeKeys.CONTAINER);
    static final ThemeGetter SPACING_THEME = ThemeUtility.fromTheme(ThemeKeys.SPACING);
    static final ThemeGetter RADIUS_THEME = ThemeUtility.fromTheme(ThemeKeys.RADIUS);
    static final ThemeGetter SHADOW_THEME = ThemeUtility.fromTheme(ThemeKeys.SHADOW);
    static final ThemeGetter INSET_SHADOW_THEME = ThemeUtility.fromTheme(ThemeKeys.INSET_SHADOW);
    static final ThemeGetter TEXT_SHADOW_THEME = ThemeUtility.fromTheme(ThemeKeys.TEXT_SHADOW);
    static final ThemeGetter DROP_SHADOW_THEME = ThemeUtility.fromTheme(ThemeKeys.DROP_SHADOW);
    static final ThemeGetter BLUR_THEME = ThemeUtility.fromTheme(ThemeKeys.BLUR);
    static final ThemeGetter PERSPECTIVE_THEME = ThemeUtility.fromTheme(ThemeKeys.PERSPECTIVE);
    static final ThemeGetter ASPECT_THEME = ThemeUtility.fromTheme(ThemeKeys.ASPECT);
    static final ThemeGetter EASE_THEME = ThemeUtility.fromTheme(ThemeKeys.EASE);
    static final ThemeGetter ANIMATE_THEME = ThemeUtility.fromTheme(ThemeKeys.ANIMATE);

    // Helpers to avoid repeating the same scales

    static final Object[] BREAK_SCALE = {
        "auto", "avoid", "all", "avoid-page", "page", "left", "right", "column"
    };

    static final Object[] POSITION_SCALE = {
        "center",
        "top",
        "bottom",
        "left",
        "right",
        "top-left",
        // Deprecated since Tailwind CSS v4.1.0, see https://github.com/tailwindlabs/tailwindcss/pull/17378
        "left-top",
        "top-right",
        // Deprecated since Tailwind CSS v4.1.0, see https://github.com/tailwindlabs/tailwindcss/pull/17378
        "right-top",
        "bottom-right",
        // Deprecated since Tailwind CSS v4.1.0, see https://github.com/tailwindlabs/tailwindcss/pull/17378
        "right-bottom",
        "bottom-left",
        // Deprecated since Tailwind CSS v4.1.0, see https://github.com/tailwindlabs/tailwindcss/pull/17378
        "left-bottom"
    };

    static final Object[] OVERFLOW_SCALE = {"auto", "hidden", "clip", "visible", "scroll"};

    static final Object[] OVERSCROLL_SCALE = {"auto", "contain", "none"};

    static final Object[] ALIGN_PRIMARY_AXIS_SCALE = {
        "start",
        "end",
        "center",
        "between",
        "around",
        "evenly",
        "stretch",
        "baseline",
        "center-safe",
        "end-safe"
    };

    static final Object[] ALIGN_SECONDARY_AXIS_SCALE = {
        "start", "end", "center", "stretch", "center-safe", "end-safe"
    };

    static final Object[] LINE_STYLE_SCALE = {"solid", "dashed", "dotted", "double"};

    static final Object[] BLEND_MODE_SCALE = {
        "normal",
        "multiply",
        "screen",
        "overlay",
        "darken",
        "lighten",
        "color-dodge",
        "color-burn",
        "hard-light",
        "soft-light",
        "difference",
        "exclusion",
        "hue",
        "saturation",
        "color",
        "luminosity"
    };

    static final Object[] POSITION_WITH_ARBITRARY_SCALE =
            concat(IS_ARBITRARY_VARIABLE, IS_ARBITRARY_VALUE, POSITION_SCALE);

    static final Object[] UNAMBIGUOUS_SPACING_SCALE = {IS_ARBITRARY_VARIABLE, IS_ARBITRARY_VALUE, SPACING_THEME};

    static final Object[] INSET_SCALE = concat("full", "auto", IS_FRACTION, UNAMBIGUOUS_SPACING_SCALE);

    static final Object[] GRID_TEMPLATE_COLS_ROWS_SCALE = {
        "none", "subgrid", IS_INTEGER, IS_ARBITRARY_VALUE, IS_ARBITRARY_VARIABLE
    };

    static final Object[] GRID_COL_ROW_START_AND_END_SCALE = {
        "auto",
        ClassGroup.withBase("span", "full", IS_INTEGER, IS_ARBITRARY_VALUE, IS_ARBITRARY_VARIABLE),
        IS_INTEGER,
        IS_ARBITRARY_VALUE,
        IS_ARBITRARY_VARIABLE
    };

    static final Object[] GRID_COL_ROW_START_OR_END_SCALE = {
        "auto", IS_INTEGER, IS_ARBITRARY_VALUE, IS_ARBITRARY_VARIABLE
    };

    static final Object[] GRID_AUTO_COLS_ROWS_SCALE = {
        "auto", "min", "max", "fr", IS_ARBITRARY_VALUE, IS_ARBITRARY_VARIABLE
    };

    static final Object[] MARGIN_SCALE = concat("auto", UNAMBIGUOUS_SPACING_SCALE);

    static final Object[] SIZING_SCALE = concat(
            "auto",
            "full",
            "dvw",
            "dvh",
            "lvw",
            "lvh",
            "svw",
            "svh",
            "min",
            "max",
            "fit",
            IS_FRACTION,
            UNAMBIGUOUS_SPACING_SCALE);

    static final Object[] SIZING_INLINE_SCALE = concat(
            "screen",
            "full",
            "dvw",
            "lvw",
            "svw",
            "min",
            "max",
            "fit",
            CONTAINER_THEME,
            IS_FRACTION,
            UNAMBIGUOUS_SPACING_SCALE);

    static final Object[] SIZING_BLOCK_SCALE = concat(
            "screen",
            "full",
            "lh",
            "dvh",
            "lvh",
            "svh",
            "min",
            "max",
            "fit",
            IS_FRACTION,
            UNAMBIGUOUS_SPACING_SCALE);

    static final Object[] COLOR_SCALE = {COLOR_THEME, IS_ARBITRARY_VALUE, IS_ARBITRARY_VARIABLE};

    static final Object[] BG_POSITION_SCALE = concat(
            POSITION_SCALE,
            IS_ARBITRARY_POSITION,
            IS_ARBITRARY_VARIABLE_POSITION,
            ClassGroup.withBase("position", IS_ARBITRARY_VALUE, IS_ARBITRARY_VARIABLE));

    static final Object[] BG_REPEAT_SCALE = {
        "no-repeat", ClassGroup.withBase("repeat", "", "x", "y", "round", "space")
    };

    static final Object[] BG_SIZE_SCALE = {
        "auto",
        "cover",
        "contain",
        IS_ARBITRARY_SIZE,
        IS_ARBITRARY_VARIABLE_SIZE,
        ClassGroup.withBase("size", IS_ARBITRARY_VALUE, IS_ARBITRARY_VARIABLE)
    };

    static final Object[] GRADIENT_STOP_POSITION_SCALE = {
        IS_PERCENT, IS_ARBITRARY_LENGTH, IS_ARBITRARY_VARIABLE_LENGTH
    };

    static final Object[] RADIUS_SCALE = {
        // Deprecated since Tailwind CSS v4.0.0
        "",
        "none",
        "full",
        RADIUS_THEME,
        IS_ARBITRARY_VALUE,
        IS_ARBITRARY_VARIABLE
    };

    static final Object[] BORDER_WIDTH_SCALE = {"", IS_NUMBER, IS_ARBITRARY_LENGTH, IS_ARBITRARY_VARIABLE_LENGTH};

    static final Object[] MASK_IMAGE_POSITION_SCALE = {
        IS_NUMBER, IS_PERCENT, IS_ARBITRARY_POSITION, IS_ARBITRARY_VARIABLE_POSITION
    };

    static final Object[] BLUR_SCALE = {
        // Deprecated since Tailwind CSS v4.0.0
        "",
        "none",
        BLUR_THEME,
        IS_ARBITRARY_VALUE,
        IS_ARBITRARY_VARIABLE
    };

    static final Object[] ROTATE_SCALE = {"none", IS_NUMBER, IS_ARBITRARY_VALUE, IS_ARBITRARY_VARIABLE};

    static final Object[] SCALE_SCALE = {"none", IS_NUMBER, IS_ARBITRARY_VALUE, IS_ARBITRARY_VARIABLE};

    static final Object[] SKEW_SCALE = {IS_NUMBER, IS_ARBITRARY_VALUE, IS_ARBITRARY_VARIABLE};

    // The reference names this one "traslateScale" (sic)
    static final Object[] TRANSLATE_SCALE = concat("full", IS_FRACTION, UNAMBIGUOUS_SPACING_SCALE);

    private DefaultConfigScales() {
    }

    /**
     * Builds a definition list, spreading every {@code Object[]} argument into it (the equivalent
     * of the C# collection-expression spread {@code [.. scale]}).
     *
     * @param parts the definitions, or arrays of definitions to spread
     * @return a new array with the flattened definitions
     */
    static Object[] concat(Object... parts) {
        List<Object> result = new ArrayList<>();
        for (Object part : parts) {
            if (part instanceof Object[]) {
                for (Object item : (Object[]) part) {
                    result.add(item);
                }
            } else {
                result.add(part);
            }
        }
        return result.toArray();
    }
}
