package com.github.ddxailab.tailwindmerge;

import static com.github.ddxailab.tailwindmerge.DefaultConfigScales.BG_POSITION_SCALE;
import static com.github.ddxailab.tailwindmerge.DefaultConfigScales.BG_REPEAT_SCALE;
import static com.github.ddxailab.tailwindmerge.DefaultConfigScales.BG_SIZE_SCALE;
import static com.github.ddxailab.tailwindmerge.DefaultConfigScales.BLEND_MODE_SCALE;
import static com.github.ddxailab.tailwindmerge.DefaultConfigScales.BORDER_WIDTH_SCALE;
import static com.github.ddxailab.tailwindmerge.DefaultConfigScales.COLOR_SCALE;
import static com.github.ddxailab.tailwindmerge.DefaultConfigScales.INSET_SHADOW_THEME;
import static com.github.ddxailab.tailwindmerge.DefaultConfigScales.MASK_IMAGE_POSITION_SCALE;
import static com.github.ddxailab.tailwindmerge.DefaultConfigScales.POSITION_SCALE;
import static com.github.ddxailab.tailwindmerge.DefaultConfigScales.SHADOW_THEME;
import static com.github.ddxailab.tailwindmerge.DefaultConfigScales.TEXT_SHADOW_THEME;
import static com.github.ddxailab.tailwindmerge.DefaultConfigScales.concat;
import static com.github.ddxailab.tailwindmerge.common.Validators.IS_ARBITRARY_LENGTH;
import static com.github.ddxailab.tailwindmerge.common.Validators.IS_ARBITRARY_SHADOW;
import static com.github.ddxailab.tailwindmerge.common.Validators.IS_ARBITRARY_VALUE;
import static com.github.ddxailab.tailwindmerge.common.Validators.IS_ARBITRARY_VARIABLE;
import static com.github.ddxailab.tailwindmerge.common.Validators.IS_ARBITRARY_VARIABLE_SHADOW;
import static com.github.ddxailab.tailwindmerge.common.Validators.IS_NUMBER;

import com.github.ddxailab.tailwindmerge.model.ClassGroup;
import java.util.Map;

/**
 * The "Effects" section of the default class groups (box shadow, rings, opacity, blend modes and masks).
 */
final class EffectsClassGroups {

    private EffectsClassGroups() {
    }

    static void addTo(Map<String, ClassGroup> groups) {
        // Box Shadow: https://tailwindcss.com/docs/box-shadow
        groups.put("shadow", ClassGroup.withBase("shadow",
                // Deprecated since Tailwind CSS v4.0.0
                "",
                // Deprecated since Tailwind CSS v4.0.0
                "inner",
                "none",
                SHADOW_THEME,
                IS_ARBITRARY_SHADOW,
                IS_ARBITRARY_VARIABLE_SHADOW));
        // Box Shadow Color: https://tailwindcss.com/docs/box-shadow#setting-the-shadow-color
        groups.put("shadow-color", ClassGroup.withBase("shadow", COLOR_SCALE));
        // Inset Box Shadow: https://tailwindcss.com/docs/box-shadow#adding-an-inset-shadow
        groups.put("inset-shadow", ClassGroup.withBase("inset-shadow",
                "none",
                INSET_SHADOW_THEME,
                IS_ARBITRARY_SHADOW,
                IS_ARBITRARY_VARIABLE_SHADOW));
        // Inset Box Shadow Color: https://tailwindcss.com/docs/box-shadow#setting-the-inset-shadow-color
        groups.put("inset-shadow-color", ClassGroup.withBase("inset-shadow", COLOR_SCALE));
        // Ring Width: https://tailwindcss.com/docs/box-shadow#adding-a-ring
        groups.put("ring-w", ClassGroup.withBase("ring", BORDER_WIDTH_SCALE));
        // Ring Width Inset: https://v3.tailwindcss.com/docs/ring-width#inset-rings
        // Deprecated since Tailwind CSS v4.0.0.
        // See https://github.com/tailwindlabs/tailwindcss/blob/v4.0.0/packages/tailwindcss/src/utilities.ts#L4158
        groups.put("ring-w-inset", ClassGroup.of("ring-inset"));
        // Ring Color: https://tailwindcss.com/docs/box-shadow#setting-the-ring-color
        groups.put("ring-color", ClassGroup.withBase("ring", COLOR_SCALE));
        // Ring Offset Width: https://v3.tailwindcss.com/docs/ring-offset-width
        // Deprecated since Tailwind CSS v4.0.0.
        // See https://github.com/tailwindlabs/tailwindcss/blob/v4.0.0/packages/tailwindcss/src/utilities.ts#L4158
        groups.put("ring-offset-w", ClassGroup.withBase("ring-offset", IS_NUMBER, IS_ARBITRARY_LENGTH));
        // Ring Offset Color: https://v3.tailwindcss.com/docs/ring-offset-color
        // Deprecated since Tailwind CSS v4.0.0.
        // See https://github.com/tailwindlabs/tailwindcss/blob/v4.0.0/packages/tailwindcss/src/utilities.ts#L4158
        groups.put("ring-offset-color", ClassGroup.withBase("ring-offset", COLOR_SCALE));
        // Inset Ring Width: https://tailwindcss.com/docs/box-shadow#adding-an-inset-ring
        groups.put("inset-ring-w", ClassGroup.withBase("inset-ring", BORDER_WIDTH_SCALE));
        // Inset Ring Color: https://tailwindcss.com/docs/box-shadow#setting-the-inset-ring-color
        groups.put("inset-ring-color", ClassGroup.withBase("inset-ring", COLOR_SCALE));
        // Text Shadow: https://tailwindcss.com/docs/text-shadow
        groups.put("text-shadow", ClassGroup.withBase("text-shadow",
                "none",
                TEXT_SHADOW_THEME,
                IS_ARBITRARY_SHADOW,
                IS_ARBITRARY_VARIABLE_SHADOW));
        // Text Shadow Color: https://tailwindcss.com/docs/text-shadow#setting-the-shadow-color
        groups.put("text-shadow-color", ClassGroup.withBase("text-shadow", COLOR_SCALE));
        // Opacity: https://tailwindcss.com/docs/opacity
        groups.put("opacity", ClassGroup.withBase("opacity", IS_NUMBER, IS_ARBITRARY_VALUE, IS_ARBITRARY_VARIABLE));
        // Mix Blend Mode: https://tailwindcss.com/docs/mix-blend-mode
        groups.put("mix-blend", ClassGroup.withBase("mix-blend", concat("plus-darker", "plus-lighter", BLEND_MODE_SCALE)));
        // Background Blend Mode: https://tailwindcss.com/docs/mix-blend-mode
        groups.put("bg-blend", ClassGroup.withBase("bg-blend", BLEND_MODE_SCALE));

        // Mask Clip: https://tailwindcss.com/docs/mask-clip
        groups.put("mask-clip", ClassGroup.withBase("mask",
                "no-clip",
                ClassGroup.withBase("clip", "border", "padding", "content", "fill", "stroke", "view")));
        // Mask Composite: https://tailwindcss.com/docs/mask-composite
        groups.put("mask-composite", ClassGroup.withBase("mask", "add", "subtract", "intersect", "exclude"));

        // Mask Image: https://tailwindcss.com/docs/mask-image
        groups.put("mask-image-linear-pos", ClassGroup.withBase("mask-linear", IS_NUMBER));
        groups.put("mask-image-linear-from-pos", ClassGroup.withBase("mask-linear-from", MASK_IMAGE_POSITION_SCALE));
        groups.put("mask-image-linear-to-pos", ClassGroup.withBase("mask-linear-to", MASK_IMAGE_POSITION_SCALE));
        groups.put("mask-image-linear-from-color", ClassGroup.withBase("mask-linear-from", COLOR_SCALE));
        groups.put("mask-image-linear-to-color", ClassGroup.withBase("mask-linear-to", COLOR_SCALE));
        groups.put("mask-image-t-from-pos", ClassGroup.withBase("mask-t-from", MASK_IMAGE_POSITION_SCALE));
        groups.put("mask-image-t-to-pos", ClassGroup.withBase("mask-t-to", MASK_IMAGE_POSITION_SCALE));
        groups.put("mask-image-t-from-color", ClassGroup.withBase("mask-t-from", COLOR_SCALE));
        groups.put("mask-image-t-to-color", ClassGroup.withBase("mask-t-to", COLOR_SCALE));
        groups.put("mask-image-r-from-pos", ClassGroup.withBase("mask-r-from", MASK_IMAGE_POSITION_SCALE));
        groups.put("mask-image-r-to-pos", ClassGroup.withBase("mask-r-to", MASK_IMAGE_POSITION_SCALE));
        groups.put("mask-image-r-from-color", ClassGroup.withBase("mask-r-from", COLOR_SCALE));
        groups.put("mask-image-r-to-color", ClassGroup.withBase("mask-r-to", COLOR_SCALE));
        groups.put("mask-image-b-from-pos", ClassGroup.withBase("mask-b-from", MASK_IMAGE_POSITION_SCALE));
        groups.put("mask-image-b-to-pos", ClassGroup.withBase("mask-b-to", MASK_IMAGE_POSITION_SCALE));
        groups.put("mask-image-b-from-color", ClassGroup.withBase("mask-b-from", COLOR_SCALE));
        groups.put("mask-image-b-to-color", ClassGroup.withBase("mask-b-to", COLOR_SCALE));
        groups.put("mask-image-l-from-pos", ClassGroup.withBase("mask-l-from", MASK_IMAGE_POSITION_SCALE));
        groups.put("mask-image-l-to-pos", ClassGroup.withBase("mask-l-to", MASK_IMAGE_POSITION_SCALE));
        groups.put("mask-image-l-from-color", ClassGroup.withBase("mask-l-from", COLOR_SCALE));
        groups.put("mask-image-l-to-color", ClassGroup.withBase("mask-l-to", COLOR_SCALE));
        groups.put("mask-image-x-from-pos", ClassGroup.withBase("mask-x-from", MASK_IMAGE_POSITION_SCALE));
        groups.put("mask-image-x-to-pos", ClassGroup.withBase("mask-x-to", MASK_IMAGE_POSITION_SCALE));
        groups.put("mask-image-x-from-color", ClassGroup.withBase("mask-x-from", COLOR_SCALE));
        groups.put("mask-image-x-to-color", ClassGroup.withBase("mask-x-to", COLOR_SCALE));
        groups.put("mask-image-y-from-pos", ClassGroup.withBase("mask-y-from", MASK_IMAGE_POSITION_SCALE));
        groups.put("mask-image-y-to-pos", ClassGroup.withBase("mask-y-to", MASK_IMAGE_POSITION_SCALE));
        groups.put("mask-image-y-from-color", ClassGroup.withBase("mask-y-from", COLOR_SCALE));
        groups.put("mask-image-y-to-color", ClassGroup.withBase("mask-y-to", COLOR_SCALE));
        groups.put("mask-image-radial", ClassGroup.withBase("mask-radial", IS_ARBITRARY_VALUE, IS_ARBITRARY_VARIABLE));
        groups.put("mask-image-radial-from-pos", ClassGroup.withBase("mask-radial-from", MASK_IMAGE_POSITION_SCALE));
        groups.put("mask-image-radial-to-pos", ClassGroup.withBase("mask-radial-to", MASK_IMAGE_POSITION_SCALE));
        groups.put("mask-image-radial-from-color", ClassGroup.withBase("mask-radial-from", COLOR_SCALE));
        groups.put("mask-image-radial-to-color", ClassGroup.withBase("mask-radial-to", COLOR_SCALE));
        groups.put("mask-image-radial-shape", ClassGroup.withBase("mask-radial", "circle", "ellipse"));
        groups.put("mask-image-radial-size", ClassGroup.withBase("mask-radial",
                ClassGroup.withBase("closest", "side", "corner"),
                ClassGroup.withBase("farthest", "side", "corner")));
        groups.put("mask-image-radial-pos", ClassGroup.withBase("mask-radial-at", POSITION_SCALE));
        groups.put("mask-image-conic-pos", ClassGroup.withBase("mask-conic", IS_NUMBER));
        groups.put("mask-image-conic-from-pos", ClassGroup.withBase("mask-conic-from", MASK_IMAGE_POSITION_SCALE));
        groups.put("mask-image-conic-to-pos", ClassGroup.withBase("mask-conic-to", MASK_IMAGE_POSITION_SCALE));
        groups.put("mask-image-conic-from-color", ClassGroup.withBase("mask-conic-from", COLOR_SCALE));
        groups.put("mask-image-conic-to-color", ClassGroup.withBase("mask-conic-to", COLOR_SCALE));

        // Mask Mode: https://tailwindcss.com/docs/mask-mode
        groups.put("mask-mode", ClassGroup.withBase("mask", "alpha", "luminance", "match"));
        // Mask Origin: https://tailwindcss.com/docs/mask-origin
        groups.put("mask-origin", ClassGroup.withBase("mask-origin",
                "border", "padding", "content", "fill", "stroke", "view"));
        // Mask Position: https://tailwindcss.com/docs/mask-position
        groups.put("mask-position", ClassGroup.withBase("mask", BG_POSITION_SCALE));
        // Mask Repeat: https://tailwindcss.com/docs/mask-repeat
        groups.put("mask-repeat", ClassGroup.withBase("mask", BG_REPEAT_SCALE));
        // Mask Size: https://tailwindcss.com/docs/mask-size
        groups.put("mask-size", ClassGroup.withBase("mask", BG_SIZE_SCALE));
        // Mask Type: https://tailwindcss.com/docs/mask-type
        groups.put("mask-type", ClassGroup.withBase("mask-type", "alpha", "luminance"));
        // Mask Image: https://tailwindcss.com/docs/mask-image
        groups.put("mask-image", ClassGroup.withBase("mask", "none", IS_ARBITRARY_VALUE, IS_ARBITRARY_VARIABLE));
    }
}
