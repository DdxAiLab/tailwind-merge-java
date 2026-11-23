package com.github.ddxailab.tailwindmerge;

import static com.github.ddxailab.tailwindmerge.DefaultConfigScales.ANIMATE_THEME;
import static com.github.ddxailab.tailwindmerge.DefaultConfigScales.EASE_THEME;
import static com.github.ddxailab.tailwindmerge.DefaultConfigScales.PERSPECTIVE_THEME;
import static com.github.ddxailab.tailwindmerge.DefaultConfigScales.POSITION_WITH_ARBITRARY_SCALE;
import static com.github.ddxailab.tailwindmerge.DefaultConfigScales.ROTATE_SCALE;
import static com.github.ddxailab.tailwindmerge.DefaultConfigScales.SCALE_SCALE;
import static com.github.ddxailab.tailwindmerge.DefaultConfigScales.SKEW_SCALE;
import static com.github.ddxailab.tailwindmerge.DefaultConfigScales.TRANSLATE_SCALE;
import static com.github.ddxailab.tailwindmerge.DefaultConfigScales.UNAMBIGUOUS_SPACING_SCALE;
import static com.github.ddxailab.tailwindmerge.common.Validators.IS_ARBITRARY_VALUE;
import static com.github.ddxailab.tailwindmerge.common.Validators.IS_ARBITRARY_VARIABLE;
import static com.github.ddxailab.tailwindmerge.common.Validators.IS_INTEGER;
import static com.github.ddxailab.tailwindmerge.common.Validators.IS_NUMBER;

import com.github.ddxailab.tailwindmerge.model.ClassGroup;
import java.util.Map;

/**
 * The "Tables", "Transitions and Animation" and "Transforms" sections of the default class groups.
 */
final class TablesTransitionsTransformsClassGroups {

    private TablesTransitionsTransformsClassGroups() {
    }

    static void addTo(Map<String, ClassGroup> groups) {
        addTables(groups);
        addTransitionsAndAnimation(groups);
        addTransforms(groups);
    }

    private static void addTables(Map<String, ClassGroup> groups) {
        // Border Collapse: https://tailwindcss.com/docs/border-collapse
        groups.put("border-collapse", ClassGroup.withBase("border", "collapse", "separate"));
        // Border Spacing: https://tailwindcss.com/docs/border-spacing
        groups.put("border-spacing", ClassGroup.withBase("border-spacing", UNAMBIGUOUS_SPACING_SCALE));
        groups.put("border-spacing-x", ClassGroup.withBase("border-spacing-x", UNAMBIGUOUS_SPACING_SCALE));
        groups.put("border-spacing-y", ClassGroup.withBase("border-spacing-y", UNAMBIGUOUS_SPACING_SCALE));
        // Table Layout: https://tailwindcss.com/docs/table-layout
        groups.put("table-layout", ClassGroup.withBase("table", "auto", "fixed"));
        // Caption Side: https://tailwindcss.com/docs/caption-side
        groups.put("caption", ClassGroup.withBase("caption", "top", "bottom"));
    }

    private static void addTransitionsAndAnimation(Map<String, ClassGroup> groups) {
        // Transition Property: https://tailwindcss.com/docs/transition-property
        groups.put("transition", ClassGroup.withBase("transition",
                "",
                "all",
                "colors",
                "opacity",
                "shadow",
                "transform",
                "none",
                IS_ARBITRARY_VALUE,
                IS_ARBITRARY_VARIABLE));
        // Transition Behavior: https://tailwindcss.com/docs/transition-behavior
        groups.put("transition-behavior", ClassGroup.withBase("transition", "normal", "discrete"));
        // Transition Duration: https://tailwindcss.com/docs/transition-duration
        groups.put("duration", ClassGroup.withBase("duration",
                "initial", IS_NUMBER, IS_ARBITRARY_VALUE, IS_ARBITRARY_VARIABLE));
        // Transition Timing Function: https://tailwindcss.com/docs/transition-timing-function
        groups.put("ease", ClassGroup.withBase("ease",
                "linear", "initial", EASE_THEME, IS_ARBITRARY_VALUE, IS_ARBITRARY_VARIABLE));
        // Transition Delay: https://tailwindcss.com/docs/transition-delay
        groups.put("delay", ClassGroup.withBase("delay", IS_NUMBER, IS_ARBITRARY_VALUE, IS_ARBITRARY_VARIABLE));
        // Animation: https://tailwindcss.com/docs/animation
        groups.put("animate", ClassGroup.withBase("animate",
                "none", ANIMATE_THEME, IS_ARBITRARY_VALUE, IS_ARBITRARY_VARIABLE));
    }

    private static void addTransforms(Map<String, ClassGroup> groups) {
        // Backface Visibility: https://tailwindcss.com/docs/backface-visibility
        groups.put("backface", ClassGroup.withBase("backface", "hidden", "visible"));
        // Perspective: https://tailwindcss.com/docs/perspective
        groups.put("perspective", ClassGroup.withBase("perspective",
                PERSPECTIVE_THEME, IS_ARBITRARY_VALUE, IS_ARBITRARY_VARIABLE));
        // Perspective Origin: https://tailwindcss.com/docs/perspective-origin
        groups.put("perspective-origin", ClassGroup.withBase("perspective-origin", POSITION_WITH_ARBITRARY_SCALE));
        // Rotate: https://tailwindcss.com/docs/rotate
        groups.put("rotate", ClassGroup.withBase("rotate", ROTATE_SCALE));
        groups.put("rotate-x", ClassGroup.withBase("rotate-x", ROTATE_SCALE));
        groups.put("rotate-y", ClassGroup.withBase("rotate-y", ROTATE_SCALE));
        groups.put("rotate-z", ClassGroup.withBase("rotate-z", ROTATE_SCALE));
        // Scale: https://tailwindcss.com/docs/scale
        groups.put("scale", ClassGroup.withBase("scale", SCALE_SCALE));
        groups.put("scale-x", ClassGroup.withBase("scale-x", SCALE_SCALE));
        groups.put("scale-y", ClassGroup.withBase("scale-y", SCALE_SCALE));
        groups.put("scale-z", ClassGroup.withBase("scale-z", SCALE_SCALE));
        groups.put("scale-3d", ClassGroup.of("scale-3d"));
        // Skew: https://tailwindcss.com/docs/skew
        groups.put("skew", ClassGroup.withBase("skew", SKEW_SCALE));
        groups.put("skew-x", ClassGroup.withBase("skew-x", SKEW_SCALE));
        groups.put("skew-y", ClassGroup.withBase("skew-y", SKEW_SCALE));
        // Transform: https://tailwindcss.com/docs/transform
        groups.put("transform", ClassGroup.withBase("transform",
                "", "none", "gpu", "cpu", IS_ARBITRARY_VALUE, IS_ARBITRARY_VARIABLE));
        // Transform Origin: https://tailwindcss.com/docs/transform-origin
        groups.put("transform-origin", ClassGroup.withBase("origin", POSITION_WITH_ARBITRARY_SCALE));
        // Transform Style: https://tailwindcss.com/docs/transform-style
        groups.put("transform-style", ClassGroup.withBase("transform", "3d", "flat"));
        // Translate: https://tailwindcss.com/docs/translate
        groups.put("translate", ClassGroup.withBase("translate", TRANSLATE_SCALE));
        groups.put("translate-x", ClassGroup.withBase("translate-x", TRANSLATE_SCALE));
        groups.put("translate-y", ClassGroup.withBase("translate-y", TRANSLATE_SCALE));
        groups.put("translate-z", ClassGroup.withBase("translate-z", TRANSLATE_SCALE));
        groups.put("translate-none", ClassGroup.of("translate-none"));
        // Zoom: https://tailwindcss.com/docs/zoom
        groups.put("zoom", ClassGroup.withBase("zoom", IS_INTEGER, IS_ARBITRARY_VARIABLE, IS_ARBITRARY_VALUE));
    }
}
