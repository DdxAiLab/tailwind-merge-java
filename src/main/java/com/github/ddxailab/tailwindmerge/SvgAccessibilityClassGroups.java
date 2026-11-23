package com.github.ddxailab.tailwindmerge;

import static com.github.ddxailab.tailwindmerge.DefaultConfigScales.COLOR_SCALE;
import static com.github.ddxailab.tailwindmerge.DefaultConfigScales.concat;
import static com.github.ddxailab.tailwindmerge.common.Validators.IS_ARBITRARY_LENGTH;
import static com.github.ddxailab.tailwindmerge.common.Validators.IS_ARBITRARY_NUMBER;
import static com.github.ddxailab.tailwindmerge.common.Validators.IS_ARBITRARY_VARIABLE_LENGTH;
import static com.github.ddxailab.tailwindmerge.common.Validators.IS_NUMBER;

import com.github.ddxailab.tailwindmerge.model.ClassGroup;
import java.util.Map;

/**
 * The "SVG" and "Accessibility" sections of the default class groups.
 */
final class SvgAccessibilityClassGroups {

    private SvgAccessibilityClassGroups() {
    }

    static void addTo(Map<String, ClassGroup> groups) {
        // Fill: https://tailwindcss.com/docs/fill
        groups.put("fill", ClassGroup.withBase("fill", concat("none", COLOR_SCALE)));
        // Stroke Width: https://tailwindcss.com/docs/stroke-width
        groups.put("stroke-w", ClassGroup.withBase("stroke",
                IS_NUMBER,
                IS_ARBITRARY_NUMBER,
                IS_ARBITRARY_LENGTH,
                IS_ARBITRARY_VARIABLE_LENGTH));
        // Stroke: https://tailwindcss.com/docs/stroke
        groups.put("stroke", ClassGroup.withBase("stroke", concat("none", COLOR_SCALE)));

        // Forced Color Adjust: https://tailwindcss.com/docs/forced-color-adjust
        groups.put("forced-color-adjust", ClassGroup.withBase("forced-color-adjust", "auto", "none"));
    }
}
