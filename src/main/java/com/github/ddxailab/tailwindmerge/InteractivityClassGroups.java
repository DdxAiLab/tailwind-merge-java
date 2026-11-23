package com.github.ddxailab.tailwindmerge;

import static com.github.ddxailab.tailwindmerge.DefaultConfigScales.COLOR_SCALE;
import static com.github.ddxailab.tailwindmerge.DefaultConfigScales.UNAMBIGUOUS_SPACING_SCALE;
import static com.github.ddxailab.tailwindmerge.common.Validators.IS_ARBITRARY_VALUE;
import static com.github.ddxailab.tailwindmerge.common.Validators.IS_ARBITRARY_VARIABLE;

import com.github.ddxailab.tailwindmerge.model.ClassGroup;
import java.util.Map;

/**
 * The "Interactivity" section of the default class groups.
 */
final class InteractivityClassGroups {

    private InteractivityClassGroups() {
    }

    static void addTo(Map<String, ClassGroup> groups) {
        // Accent Color: https://tailwindcss.com/docs/accent-color
        groups.put("accent", ClassGroup.withBase("accent", COLOR_SCALE));
        // Appearance: https://tailwindcss.com/docs/appearance
        groups.put("appearance", ClassGroup.withBase("appearance", "none", "auto"));
        // Caret Color: https://tailwindcss.com/docs/caret-color
        groups.put("caret-color", ClassGroup.withBase("caret", COLOR_SCALE));
        // Color Scheme: https://tailwindcss.com/docs/color-scheme
        groups.put("color-scheme", ClassGroup.withBase("scheme",
                "normal",
                "dark",
                "light",
                "light-dark",
                "only-dark",
                "only-light"));
        // Cursor: https://tailwindcss.com/docs/cursor
        groups.put("cursor", ClassGroup.withBase("cursor",
                "auto",
                "default",
                "pointer",
                "wait",
                "text",
                "move",
                "help",
                "not-allowed",
                "none",
                "context-menu",
                "progress",
                "cell",
                "crosshair",
                "vertical-text",
                "alias",
                "copy",
                "no-drop",
                "grab",
                "grabbing",
                "all-scroll",
                "col-resize",
                "row-resize",
                "n-resize",
                "e-resize",
                "s-resize",
                "w-resize",
                "ne-resize",
                "nw-resize",
                "se-resize",
                "sw-resize",
                "ew-resize",
                "ns-resize",
                "nesw-resize",
                "nwse-resize",
                "zoom-in",
                "zoom-out",
                IS_ARBITRARY_VALUE,
                IS_ARBITRARY_VARIABLE));
        // Field Sizing: https://tailwindcss.com/docs/field-sizing
        groups.put("field-sizing", ClassGroup.withBase("field-sizing", "fixed", "content"));
        // Pointer Events: https://tailwindcss.com/docs/pointer-events
        groups.put("pointer-events", ClassGroup.withBase("pointer-events", "auto", "none"));
        // Resize: https://tailwindcss.com/docs/resize
        groups.put("resize", ClassGroup.withBase("resize", "none", "", "x", "y"));
        // Scroll Behavior: https://tailwindcss.com/docs/scroll-behavior
        groups.put("scroll-behavior", ClassGroup.withBase("scroll", "auto", "smooth"));
        // Scrollbar Thumb Color: https://tailwindcss.com/docs/scrollbar-color
        groups.put("scrollbar-thumb-color", ClassGroup.withBase("scrollbar-thumb", COLOR_SCALE));
        // Scrollbar Track Color: https://tailwindcss.com/docs/scrollbar-color
        groups.put("scrollbar-track-color", ClassGroup.withBase("scrollbar-track", COLOR_SCALE));
        // Scrollbar Gutter: https://tailwindcss.com/docs/scrollbar-gutter
        groups.put("scrollbar-gutter", ClassGroup.withBase("scrollbar-gutter", "auto", "stable", "both"));
        // Scrollbar Width: https://tailwindcss.com/docs/scrollbar-width
        groups.put("scrollbar-w", ClassGroup.withBase("scrollbar", "auto", "thin", "none"));

        // Scroll Margin: https://tailwindcss.com/docs/scroll-margin
        groups.put("scroll-m", ClassGroup.withBase("scroll-m", UNAMBIGUOUS_SPACING_SCALE));
        groups.put("scroll-mx", ClassGroup.withBase("scroll-mx", UNAMBIGUOUS_SPACING_SCALE));
        groups.put("scroll-my", ClassGroup.withBase("scroll-my", UNAMBIGUOUS_SPACING_SCALE));
        groups.put("scroll-ms", ClassGroup.withBase("scroll-ms", UNAMBIGUOUS_SPACING_SCALE));
        groups.put("scroll-me", ClassGroup.withBase("scroll-me", UNAMBIGUOUS_SPACING_SCALE));
        groups.put("scroll-mbs", ClassGroup.withBase("scroll-mbs", UNAMBIGUOUS_SPACING_SCALE));
        groups.put("scroll-mbe", ClassGroup.withBase("scroll-mbe", UNAMBIGUOUS_SPACING_SCALE));
        groups.put("scroll-mt", ClassGroup.withBase("scroll-mt", UNAMBIGUOUS_SPACING_SCALE));
        groups.put("scroll-mr", ClassGroup.withBase("scroll-mr", UNAMBIGUOUS_SPACING_SCALE));
        groups.put("scroll-mb", ClassGroup.withBase("scroll-mb", UNAMBIGUOUS_SPACING_SCALE));
        groups.put("scroll-ml", ClassGroup.withBase("scroll-ml", UNAMBIGUOUS_SPACING_SCALE));

        // Scroll Padding: https://tailwindcss.com/docs/scroll-padding
        groups.put("scroll-p", ClassGroup.withBase("scroll-p", UNAMBIGUOUS_SPACING_SCALE));
        groups.put("scroll-px", ClassGroup.withBase("scroll-px", UNAMBIGUOUS_SPACING_SCALE));
        groups.put("scroll-py", ClassGroup.withBase("scroll-py", UNAMBIGUOUS_SPACING_SCALE));
        groups.put("scroll-ps", ClassGroup.withBase("scroll-ps", UNAMBIGUOUS_SPACING_SCALE));
        groups.put("scroll-pe", ClassGroup.withBase("scroll-pe", UNAMBIGUOUS_SPACING_SCALE));
        groups.put("scroll-pbs", ClassGroup.withBase("scroll-pbs", UNAMBIGUOUS_SPACING_SCALE));
        groups.put("scroll-pbe", ClassGroup.withBase("scroll-pbe", UNAMBIGUOUS_SPACING_SCALE));
        groups.put("scroll-pt", ClassGroup.withBase("scroll-pt", UNAMBIGUOUS_SPACING_SCALE));
        groups.put("scroll-pr", ClassGroup.withBase("scroll-pr", UNAMBIGUOUS_SPACING_SCALE));
        groups.put("scroll-pb", ClassGroup.withBase("scroll-pb", UNAMBIGUOUS_SPACING_SCALE));
        groups.put("scroll-pl", ClassGroup.withBase("scroll-pl", UNAMBIGUOUS_SPACING_SCALE));

        // Scroll Snap Align: https://tailwindcss.com/docs/scroll-snap-align
        groups.put("snap-align", ClassGroup.withBase("snap", "start", "end", "center", "align-none"));
        // Scroll Snap Stop: https://tailwindcss.com/docs/scroll-snap-stop
        groups.put("snap-stop", ClassGroup.withBase("snap", "normal", "always"));
        // Scroll Snap Type: https://tailwindcss.com/docs/scroll-snap-type
        groups.put("snap-type", ClassGroup.withBase("snap", "none", "x", "y", "both"));
        // Scroll Snap Type Strictness: https://tailwindcss.com/docs/scroll-snap-type
        groups.put("snap-strictness", ClassGroup.withBase("snap", "mandatory", "proximity"));
        // Touch Action: https://tailwindcss.com/docs/touch-action
        groups.put("touch", ClassGroup.withBase("touch", "auto", "none", "manipulation"));
        groups.put("touch-x", ClassGroup.withBase("touch-pan", "x", "left", "right"));
        groups.put("touch-y", ClassGroup.withBase("touch-pan", "y", "up", "down"));
        groups.put("touch-pz", ClassGroup.of("touch-pinch-zoom"));
        // User Select: https://tailwindcss.com/docs/user-select
        groups.put("select", ClassGroup.withBase("select", "none", "text", "all", "auto"));
        // Will Change: https://tailwindcss.com/docs/will-change
        groups.put("will-change", ClassGroup.withBase("will-change",
                "auto",
                "scroll",
                "contents",
                "transform",
                IS_ARBITRARY_VALUE,
                IS_ARBITRARY_VARIABLE));
    }
}
