package com.github.ddxailab.tailwindmerge;

import static com.github.ddxailab.tailwindmerge.DefaultConfigScales.BORDER_WIDTH_SCALE;
import static com.github.ddxailab.tailwindmerge.DefaultConfigScales.COLOR_SCALE;
import static com.github.ddxailab.tailwindmerge.DefaultConfigScales.LINE_STYLE_SCALE;
import static com.github.ddxailab.tailwindmerge.DefaultConfigScales.RADIUS_SCALE;
import static com.github.ddxailab.tailwindmerge.DefaultConfigScales.concat;
import static com.github.ddxailab.tailwindmerge.common.Validators.IS_ARBITRARY_LENGTH;
import static com.github.ddxailab.tailwindmerge.common.Validators.IS_ARBITRARY_VALUE;
import static com.github.ddxailab.tailwindmerge.common.Validators.IS_ARBITRARY_VARIABLE;
import static com.github.ddxailab.tailwindmerge.common.Validators.IS_ARBITRARY_VARIABLE_LENGTH;
import static com.github.ddxailab.tailwindmerge.common.Validators.IS_NUMBER;

import com.github.ddxailab.tailwindmerge.model.ClassGroup;
import java.util.Map;

/**
 * The "Borders" section of the default class groups.
 */
final class BordersClassGroups {

    private BordersClassGroups() {
    }

    static void addTo(Map<String, ClassGroup> groups) {
        // Border Radius: https://tailwindcss.com/docs/border-radius
        groups.put("rounded", ClassGroup.withBase("rounded", RADIUS_SCALE));
        groups.put("rounded-s", ClassGroup.withBase("rounded-s", RADIUS_SCALE));
        groups.put("rounded-e", ClassGroup.withBase("rounded-e", RADIUS_SCALE));
        groups.put("rounded-t", ClassGroup.withBase("rounded-t", RADIUS_SCALE));
        groups.put("rounded-r", ClassGroup.withBase("rounded-r", RADIUS_SCALE));
        groups.put("rounded-b", ClassGroup.withBase("rounded-b", RADIUS_SCALE));
        groups.put("rounded-l", ClassGroup.withBase("rounded-l", RADIUS_SCALE));
        groups.put("rounded-ss", ClassGroup.withBase("rounded-ss", RADIUS_SCALE));
        groups.put("rounded-se", ClassGroup.withBase("rounded-se", RADIUS_SCALE));
        groups.put("rounded-ee", ClassGroup.withBase("rounded-ee", RADIUS_SCALE));
        groups.put("rounded-es", ClassGroup.withBase("rounded-es", RADIUS_SCALE));
        groups.put("rounded-tl", ClassGroup.withBase("rounded-tl", RADIUS_SCALE));
        groups.put("rounded-tr", ClassGroup.withBase("rounded-tr", RADIUS_SCALE));
        groups.put("rounded-br", ClassGroup.withBase("rounded-br", RADIUS_SCALE));
        groups.put("rounded-bl", ClassGroup.withBase("rounded-bl", RADIUS_SCALE));

        // Border Width: https://tailwindcss.com/docs/border-width
        groups.put("border-w", ClassGroup.withBase("border", BORDER_WIDTH_SCALE));
        groups.put("border-w-x", ClassGroup.withBase("border-x", BORDER_WIDTH_SCALE));
        groups.put("border-w-y", ClassGroup.withBase("border-y", BORDER_WIDTH_SCALE));
        groups.put("border-w-s", ClassGroup.withBase("border-s", BORDER_WIDTH_SCALE));
        groups.put("border-w-e", ClassGroup.withBase("border-e", BORDER_WIDTH_SCALE));
        groups.put("border-w-bs", ClassGroup.withBase("border-bs", BORDER_WIDTH_SCALE));
        groups.put("border-w-be", ClassGroup.withBase("border-be", BORDER_WIDTH_SCALE));
        groups.put("border-w-t", ClassGroup.withBase("border-t", BORDER_WIDTH_SCALE));
        groups.put("border-w-r", ClassGroup.withBase("border-r", BORDER_WIDTH_SCALE));
        groups.put("border-w-b", ClassGroup.withBase("border-b", BORDER_WIDTH_SCALE));
        groups.put("border-w-l", ClassGroup.withBase("border-l", BORDER_WIDTH_SCALE));

        // Divide Width: https://tailwindcss.com/docs/divide-width
        groups.put("divide-x", ClassGroup.withBase("divide-x", BORDER_WIDTH_SCALE));
        groups.put("divide-x-reverse", ClassGroup.of("divide-x-reverse"));
        groups.put("divide-y", ClassGroup.withBase("divide-y", BORDER_WIDTH_SCALE));
        groups.put("divide-y-reverse", ClassGroup.of("divide-y-reverse"));

        // Border Style: https://tailwindcss.com/docs/border-style
        groups.put("border-style", ClassGroup.withBase("border", concat("hidden", "none", LINE_STYLE_SCALE)));
        // Divide Style: https://tailwindcss.com/docs/divide-style
        groups.put("divide-style", ClassGroup.withBase("divide", concat("hidden", "none", LINE_STYLE_SCALE)));

        // Border Color: https://tailwindcss.com/docs/border-color
        groups.put("border-color", ClassGroup.withBase("border", COLOR_SCALE));
        groups.put("border-color-x", ClassGroup.withBase("border-x", COLOR_SCALE));
        groups.put("border-color-y", ClassGroup.withBase("border-y", COLOR_SCALE));
        groups.put("border-color-s", ClassGroup.withBase("border-s", COLOR_SCALE));
        groups.put("border-color-e", ClassGroup.withBase("border-e", COLOR_SCALE));
        groups.put("border-color-bs", ClassGroup.withBase("border-bs", COLOR_SCALE));
        groups.put("border-color-be", ClassGroup.withBase("border-be", COLOR_SCALE));
        groups.put("border-color-t", ClassGroup.withBase("border-t", COLOR_SCALE));
        groups.put("border-color-r", ClassGroup.withBase("border-r", COLOR_SCALE));
        groups.put("border-color-b", ClassGroup.withBase("border-b", COLOR_SCALE));
        groups.put("border-color-l", ClassGroup.withBase("border-l", COLOR_SCALE));

        // Divide Color: https://tailwindcss.com/docs/divide-color
        groups.put("divide-color", ClassGroup.withBase("divide", COLOR_SCALE));

        // Outline Style: https://tailwindcss.com/docs/outline-style
        groups.put("outline-style", ClassGroup.withBase("outline", concat("none", "hidden", LINE_STYLE_SCALE)));
        // Outline Offset: https://tailwindcss.com/docs/outline-offset
        groups.put("outline-offset", ClassGroup.withBase("outline-offset",
                IS_NUMBER,
                IS_ARBITRARY_VALUE,
                IS_ARBITRARY_VARIABLE));
        // Outline Width: https://tailwindcss.com/docs/outline-width
        groups.put("outline-w", ClassGroup.withBase("outline",
                "",
                IS_NUMBER,
                IS_ARBITRARY_LENGTH,
                IS_ARBITRARY_VARIABLE_LENGTH));
        // Outline Color: https://tailwindcss.com/docs/outline-color
        groups.put("outline-color", ClassGroup.withBase("outline", COLOR_SCALE));
    }
}
