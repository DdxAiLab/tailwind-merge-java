package com.github.ddxailab.tailwindmerge;

import static com.github.ddxailab.tailwindmerge.DefaultConfigScales.BLUR_SCALE;
import static com.github.ddxailab.tailwindmerge.DefaultConfigScales.COLOR_SCALE;
import static com.github.ddxailab.tailwindmerge.DefaultConfigScales.DROP_SHADOW_THEME;
import static com.github.ddxailab.tailwindmerge.common.Validators.IS_ARBITRARY_SHADOW;
import static com.github.ddxailab.tailwindmerge.common.Validators.IS_ARBITRARY_VALUE;
import static com.github.ddxailab.tailwindmerge.common.Validators.IS_ARBITRARY_VARIABLE;
import static com.github.ddxailab.tailwindmerge.common.Validators.IS_ARBITRARY_VARIABLE_SHADOW;
import static com.github.ddxailab.tailwindmerge.common.Validators.IS_NUMBER;

import com.github.ddxailab.tailwindmerge.model.ClassGroup;
import java.util.Map;

/**
 * The "Filters" section of the default class groups.
 */
final class FiltersClassGroups {

    private FiltersClassGroups() {
    }

    static void addTo(Map<String, ClassGroup> groups) {
        // Filter: https://tailwindcss.com/docs/filter
        groups.put("filter", ClassGroup.withBase("filter", "none", IS_ARBITRARY_VALUE, IS_ARBITRARY_VARIABLE));
        // Blur: https://tailwindcss.com/docs/blur
        groups.put("blur", ClassGroup.withBase("blur", BLUR_SCALE));
        // Brightness: https://tailwindcss.com/docs/brightness
        groups.put("brightness", ClassGroup.withBase("brightness", IS_NUMBER, IS_ARBITRARY_VALUE, IS_ARBITRARY_VARIABLE));
        // Contrast: https://tailwindcss.com/docs/contrast
        groups.put("contrast", ClassGroup.withBase("contrast", IS_NUMBER, IS_ARBITRARY_VALUE, IS_ARBITRARY_VARIABLE));
        // Drop Shadow: https://tailwindcss.com/docs/drop-shadow
        groups.put("drop-shadow", ClassGroup.withBase("drop-shadow",
                // Deprecated since Tailwind CSS v4.0.0
                "",
                "none",
                DROP_SHADOW_THEME,
                IS_ARBITRARY_SHADOW,
                IS_ARBITRARY_VARIABLE_SHADOW));
        // Drop Shadow Color: https://tailwindcss.com/docs/filter-drop-shadow#setting-the-shadow-color
        groups.put("drop-shadow-color", ClassGroup.withBase("drop-shadow", COLOR_SCALE));
        // Grayscale: https://tailwindcss.com/docs/grayscale
        groups.put("grayscale", ClassGroup.withBase("grayscale",
                "", IS_NUMBER, IS_ARBITRARY_VALUE, IS_ARBITRARY_VARIABLE));
        // Hue Rotate: https://tailwindcss.com/docs/hue-rotate
        groups.put("hue-rotate", ClassGroup.withBase("hue-rotate",
                IS_NUMBER, IS_ARBITRARY_VALUE, IS_ARBITRARY_VARIABLE));
        // Invert: https://tailwindcss.com/docs/invert
        groups.put("invert", ClassGroup.withBase("invert", "", IS_NUMBER, IS_ARBITRARY_VALUE, IS_ARBITRARY_VARIABLE));
        // Saturate: https://tailwindcss.com/docs/saturate
        groups.put("saturate", ClassGroup.withBase("saturate", IS_NUMBER, IS_ARBITRARY_VALUE, IS_ARBITRARY_VARIABLE));
        // Sepia: https://tailwindcss.com/docs/sepia
        groups.put("sepia", ClassGroup.withBase("sepia", "", IS_NUMBER, IS_ARBITRARY_VALUE, IS_ARBITRARY_VARIABLE));
        // Backdrop Filter: https://tailwindcss.com/docs/backdrop-blur
        groups.put("backdrop-filter", ClassGroup.withBase("backdrop-filter",
                "none", IS_ARBITRARY_VALUE, IS_ARBITRARY_VARIABLE));
        // Backdrop Blur: https://tailwindcss.com/docs/backdrop-blur
        groups.put("backdrop-blur", ClassGroup.withBase("backdrop-blur", BLUR_SCALE));
        // Backdrop Brightness: https://tailwindcss.com/docs/backdrop-brightness
        groups.put("backdrop-brightness", ClassGroup.withBase("backdrop-brightness",
                IS_NUMBER, IS_ARBITRARY_VALUE, IS_ARBITRARY_VARIABLE));
        // Backdrop Contrast: https://tailwindcss.com/docs/backdrop-contrast
        groups.put("backdrop-contrast", ClassGroup.withBase("backdrop-contrast",
                IS_NUMBER, IS_ARBITRARY_VALUE, IS_ARBITRARY_VARIABLE));
        // Backdrop Grayscale: https://tailwindcss.com/docs/backdrop-grayscale
        groups.put("backdrop-grayscale", ClassGroup.withBase("backdrop-grayscale",
                "", IS_NUMBER, IS_ARBITRARY_VALUE, IS_ARBITRARY_VARIABLE));
        // Backdrop Hue Rotate: https://tailwindcss.com/docs/backdrop-hue-rotate
        groups.put("backdrop-hue-rotate", ClassGroup.withBase("backdrop-hue-rotate",
                IS_NUMBER, IS_ARBITRARY_VALUE, IS_ARBITRARY_VARIABLE));
        // Backdrop Invert: https://tailwindcss.com/docs/backdrop-invert
        groups.put("backdrop-invert", ClassGroup.withBase("backdrop-invert",
                "", IS_NUMBER, IS_ARBITRARY_VALUE, IS_ARBITRARY_VARIABLE));
        // Backdrop Opacity: https://tailwindcss.com/docs/backdrop-opacity
        groups.put("backdrop-opacity", ClassGroup.withBase("backdrop-opacity",
                IS_NUMBER, IS_ARBITRARY_VALUE, IS_ARBITRARY_VARIABLE));
        // Backdrop Saturate: https://tailwindcss.com/docs/backdrop-saturate
        groups.put("backdrop-saturate", ClassGroup.withBase("backdrop-saturate",
                IS_NUMBER, IS_ARBITRARY_VALUE, IS_ARBITRARY_VARIABLE));
        // Backdrop Sepia: https://tailwindcss.com/docs/backdrop-sepia
        groups.put("backdrop-sepia", ClassGroup.withBase("backdrop-sepia",
                "", IS_NUMBER, IS_ARBITRARY_VALUE, IS_ARBITRARY_VARIABLE));
    }
}
