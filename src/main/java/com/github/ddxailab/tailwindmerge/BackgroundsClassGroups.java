package com.github.ddxailab.tailwindmerge;

import static com.github.ddxailab.tailwindmerge.DefaultConfigScales.BG_POSITION_SCALE;
import static com.github.ddxailab.tailwindmerge.DefaultConfigScales.BG_REPEAT_SCALE;
import static com.github.ddxailab.tailwindmerge.DefaultConfigScales.BG_SIZE_SCALE;
import static com.github.ddxailab.tailwindmerge.DefaultConfigScales.COLOR_SCALE;
import static com.github.ddxailab.tailwindmerge.DefaultConfigScales.GRADIENT_STOP_POSITION_SCALE;
import static com.github.ddxailab.tailwindmerge.common.Validators.IS_ARBITRARY_IMAGE;
import static com.github.ddxailab.tailwindmerge.common.Validators.IS_ARBITRARY_VALUE;
import static com.github.ddxailab.tailwindmerge.common.Validators.IS_ARBITRARY_VARIABLE;
import static com.github.ddxailab.tailwindmerge.common.Validators.IS_ARBITRARY_VARIABLE_IMAGE;
import static com.github.ddxailab.tailwindmerge.common.Validators.IS_INTEGER;

import com.github.ddxailab.tailwindmerge.model.ClassGroup;
import java.util.Map;

/**
 * The "Backgrounds" section of the default class groups.
 */
final class BackgroundsClassGroups {

    private BackgroundsClassGroups() {
    }

    static void addTo(Map<String, ClassGroup> groups) {
        // Background Attachment: https://tailwindcss.com/docs/background-attachment
        groups.put("bg-attachment", ClassGroup.withBase("bg", "fixed", "local", "scroll"));
        // Background Clip: https://tailwindcss.com/docs/background-clip
        groups.put("bg-clip", ClassGroup.withBase("bg-clip", "border", "padding", "content", "text"));
        // Background Origin: https://tailwindcss.com/docs/background-origin
        groups.put("bg-origin", ClassGroup.withBase("bg-origin", "border", "padding", "content"));
        // Background Position: https://tailwindcss.com/docs/background-position
        groups.put("bg-position", ClassGroup.withBase("bg", BG_POSITION_SCALE));
        // Background Repeat: https://tailwindcss.com/docs/background-repeat
        groups.put("bg-repeat", ClassGroup.withBase("bg", BG_REPEAT_SCALE));
        // Background Size: https://tailwindcss.com/docs/background-size
        groups.put("bg-size", ClassGroup.withBase("bg", BG_SIZE_SCALE));
        // Background Image: https://tailwindcss.com/docs/background-image
        groups.put("bg-image", ClassGroup.withBase("bg",
                "none",
                ClassGroup.withBase("linear",
                        ClassGroup.withBase("to", "t", "tr", "r", "br", "b", "bl", "l", "tl"),
                        IS_INTEGER,
                        IS_ARBITRARY_VALUE,
                        IS_ARBITRARY_VARIABLE),
                ClassGroup.withBase("radial", "", IS_ARBITRARY_VALUE, IS_ARBITRARY_VARIABLE),
                ClassGroup.withBase("conic", "", IS_INTEGER, IS_ARBITRARY_VALUE, IS_ARBITRARY_VARIABLE),
                IS_ARBITRARY_IMAGE,
                IS_ARBITRARY_VARIABLE_IMAGE));
        // Background Color: https://tailwindcss.com/docs/background-color
        groups.put("bg-color", ClassGroup.withBase("bg", COLOR_SCALE));
        // Gradient Color Stops From Position: https://tailwindcss.com/docs/gradient-color-stops
        groups.put("gradient-from-pos", ClassGroup.withBase("from", GRADIENT_STOP_POSITION_SCALE));
        // Gradient Color Stops Via Position: https://tailwindcss.com/docs/gradient-color-stops
        groups.put("gradient-via-pos", ClassGroup.withBase("via", GRADIENT_STOP_POSITION_SCALE));
        // Gradient Color Stops To Position: https://tailwindcss.com/docs/gradient-color-stops
        groups.put("gradient-to-pos", ClassGroup.withBase("to", GRADIENT_STOP_POSITION_SCALE));
        // Gradient Color Stops From: https://tailwindcss.com/docs/gradient-color-stops
        groups.put("gradient-from", ClassGroup.withBase("from", COLOR_SCALE));
        // Gradient Color Stops Via: https://tailwindcss.com/docs/gradient-color-stops
        groups.put("gradient-via", ClassGroup.withBase("via", COLOR_SCALE));
        // Gradient Color Stops To: https://tailwindcss.com/docs/gradient-color-stops
        groups.put("gradient-to", ClassGroup.withBase("to", COLOR_SCALE));
    }
}
