package com.github.ddxailab.tailwindmerge;

import static com.github.ddxailab.tailwindmerge.DefaultConfigScales.BREAKPOINT_THEME;
import static com.github.ddxailab.tailwindmerge.DefaultConfigScales.CONTAINER_THEME;
import static com.github.ddxailab.tailwindmerge.DefaultConfigScales.SIZING_BLOCK_SCALE;
import static com.github.ddxailab.tailwindmerge.DefaultConfigScales.SIZING_INLINE_SCALE;
import static com.github.ddxailab.tailwindmerge.DefaultConfigScales.SIZING_SCALE;
import static com.github.ddxailab.tailwindmerge.DefaultConfigScales.concat;

import com.github.ddxailab.tailwindmerge.model.ClassGroup;
import java.util.Map;

/**
 * The "Sizing" section of the default class groups.
 */
final class SizingClassGroups {

    private SizingClassGroups() {
    }

    static void addTo(Map<String, ClassGroup> groups) {
        // Size: https://tailwindcss.com/docs/width#setting-both-width-and-height
        groups.put("size", ClassGroup.withBase("size", SIZING_SCALE));
        // Inline Size: https://tailwindcss.com/docs/inline-size
        groups.put("inline-size", ClassGroup.withBase("inline", concat("auto", SIZING_INLINE_SCALE)));
        // Min-Inline Size: https://tailwindcss.com/docs/min-inline-size
        groups.put("min-inline-size", ClassGroup.withBase("min-inline", concat("auto", SIZING_INLINE_SCALE)));
        // Max-Inline Size: https://tailwindcss.com/docs/max-inline-size
        groups.put("max-inline-size", ClassGroup.withBase("max-inline", concat("none", SIZING_INLINE_SCALE)));
        // Block Size: https://tailwindcss.com/docs/block-size
        groups.put("block-size", ClassGroup.withBase("block", concat("auto", SIZING_BLOCK_SCALE)));
        // Min-Block Size: https://tailwindcss.com/docs/min-block-size
        groups.put("min-block-size", ClassGroup.withBase("min-block", concat("auto", SIZING_BLOCK_SCALE)));
        // Max-Block Size: https://tailwindcss.com/docs/max-block-size
        groups.put("max-block-size", ClassGroup.withBase("max-block", concat("none", SIZING_BLOCK_SCALE)));
        // Width: https://tailwindcss.com/docs/width
        groups.put("w", ClassGroup.withBase("w", concat("screen", CONTAINER_THEME, SIZING_SCALE)));
        // Min-Width: https://tailwindcss.com/docs/min-width
        groups.put("min-w", ClassGroup.withBase("min-w", concat(
                "screen",
                CONTAINER_THEME,
                // Deprecated since Tailwind CSS v4.0.0.
                // See https://github.com/tailwindlabs/tailwindcss.com/issues/2027#issuecomment-2620152757
                "none",
                SIZING_SCALE)));
        // Max-Width: https://tailwindcss.com/docs/max-width
        groups.put("max-w", ClassGroup.withBase("max-w", concat(
                "screen",
                "none",
                CONTAINER_THEME,
                // Deprecated since Tailwind CSS v4.0.0.
                // See https://github.com/tailwindlabs/tailwindcss.com/issues/2027#issuecomment-2620152757
                "prose",
                // Deprecated since Tailwind CSS v4.0.0.
                // See https://github.com/tailwindlabs/tailwindcss.com/issues/2027#issuecomment-2620152757
                ClassGroup.withBase("screen", BREAKPOINT_THEME),
                SIZING_SCALE)));
        // Height: https://tailwindcss.com/docs/height
        groups.put("h", ClassGroup.withBase("h", concat("screen", "lh", SIZING_SCALE)));
        // Min-Height: https://tailwindcss.com/docs/min-height
        groups.put("min-h", ClassGroup.withBase("min-h", concat("screen", "lh", "none", SIZING_SCALE)));
        // Max-Height: https://tailwindcss.com/docs/max-height
        groups.put("max-h", ClassGroup.withBase("max-h", concat("screen", "lh", "none", SIZING_SCALE)));
    }
}
