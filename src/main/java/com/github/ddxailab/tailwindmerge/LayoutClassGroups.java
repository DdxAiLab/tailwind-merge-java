package com.github.ddxailab.tailwindmerge;

import static com.github.ddxailab.tailwindmerge.DefaultConfigScales.ASPECT_THEME;
import static com.github.ddxailab.tailwindmerge.DefaultConfigScales.CONTAINER_THEME;
import static com.github.ddxailab.tailwindmerge.DefaultConfigScales.BREAK_SCALE;
import static com.github.ddxailab.tailwindmerge.DefaultConfigScales.INSET_SCALE;
import static com.github.ddxailab.tailwindmerge.DefaultConfigScales.OVERFLOW_SCALE;
import static com.github.ddxailab.tailwindmerge.DefaultConfigScales.OVERSCROLL_SCALE;
import static com.github.ddxailab.tailwindmerge.DefaultConfigScales.POSITION_WITH_ARBITRARY_SCALE;
import static com.github.ddxailab.tailwindmerge.common.Validators.IS_ARBITRARY_VALUE;
import static com.github.ddxailab.tailwindmerge.common.Validators.IS_ARBITRARY_VARIABLE;
import static com.github.ddxailab.tailwindmerge.common.Validators.IS_FRACTION;
import static com.github.ddxailab.tailwindmerge.common.Validators.IS_INTEGER;
import static com.github.ddxailab.tailwindmerge.common.Validators.IS_NAMED_CONTAINER_QUERY;
import static com.github.ddxailab.tailwindmerge.common.Validators.IS_NUMBER;

import com.github.ddxailab.tailwindmerge.model.ClassGroup;
import java.util.Map;

/**
 * The "Layout" section of the default class groups.
 */
final class LayoutClassGroups {

    private LayoutClassGroups() {
    }

    static void addTo(Map<String, ClassGroup> groups) {
        /*
         * Aspect Ratio
         * See https://tailwindcss.com/docs/aspect-ratio
         */
        groups.put("aspect", ClassGroup.withBase("aspect",
                "auto",
                "square",
                ASPECT_THEME,
                IS_FRACTION,
                IS_ARBITRARY_VALUE,
                IS_ARBITRARY_VARIABLE));
        /*
         * Container
         * See https://tailwindcss.com/docs/container
         *
         * Deprecated since Tailwind CSS v4.0.0
         */
        groups.put("container", ClassGroup.of("container"));
        /*
         * Container Type
         * See https://tailwindcss.com/docs/responsive-design#container-queries
         */
        groups.put("container-type", ClassGroup.withBase("@container",
                "", "normal", "size", IS_ARBITRARY_VARIABLE, IS_ARBITRARY_VALUE));
        /*
         * Container Named
         * See https://tailwindcss.com/docs/responsive-design#named-containers
         */
        groups.put("container-named", ClassGroup.of(IS_NAMED_CONTAINER_QUERY));
        /*
         * Columns
         * See https://tailwindcss.com/docs/columns
         */
        groups.put("columns", ClassGroup.withBase("columns",
                "auto",
                CONTAINER_THEME,
                IS_NUMBER,
                IS_ARBITRARY_VALUE,
                IS_ARBITRARY_VARIABLE));
        /*
         * Break After
         * See https://tailwindcss.com/docs/break-after
         */
        groups.put("break-after", ClassGroup.withBase("break-after", BREAK_SCALE));
        /*
         * Break Before
         * See https://tailwindcss.com/docs/break-before
         */
        groups.put("break-before", ClassGroup.withBase("break-before", BREAK_SCALE));
        /*
         * Break Inside
         * See https://tailwindcss.com/docs/break-inside
         */
        groups.put("break-inside", ClassGroup.withBase("break-inside",
                "auto", "avoid", "avoid-page", "avoid-column"));
        /*
         * Box Decoration Break
         * See https://tailwindcss.com/docs/box-decoration-break
         */
        groups.put("box-decoration", ClassGroup.withBase("box-decoration", "slice", "clone"));
        /*
         * Box Sizing
         * See https://tailwindcss.com/docs/box-sizing
         */
        groups.put("box", ClassGroup.withBase("box", "border", "content"));
        /*
         * Display
         * See https://tailwindcss.com/docs/display
         */
        groups.put("display", ClassGroup.of(
                "block",
                "inline-block",
                "inline",
                "flex",
                "inline-flex",
                "table",
                "inline-table",
                "table-caption",
                "table-cell",
                "table-column",
                "table-column-group",
                "table-footer-group",
                "table-header-group",
                "table-row-group",
                "table-row",
                "flow-root",
                "grid",
                "inline-grid",
                "contents",
                "list-item",
                "hidden"));
        /*
         * Screen Reader Only
         * See https://tailwindcss.com/docs/display#screen-reader-only
         */
        groups.put("sr", ClassGroup.of("sr-only", "not-sr-only"));
        /*
         * Floats
         * See https://tailwindcss.com/docs/float
         */
        groups.put("float", ClassGroup.withBase("float", "right", "left", "none", "start", "end"));
        /*
         * Clear
         * See https://tailwindcss.com/docs/clear
         */
        groups.put("clear", ClassGroup.withBase("clear", "left", "right", "both", "none", "start", "end"));
        /*
         * Isolation
         * See https://tailwindcss.com/docs/isolation
         */
        groups.put("isolation", ClassGroup.of("isolate", "isolation-auto"));
        /*
         * Object Fit
         * See https://tailwindcss.com/docs/object-fit
         */
        groups.put("object-fit", ClassGroup.withBase("object", "contain", "cover", "fill", "none", "scale-down"));
        /*
         * Object Position
         * See https://tailwindcss.com/docs/object-position
         */
        groups.put("object-position", ClassGroup.withBase("object", POSITION_WITH_ARBITRARY_SCALE));
        /*
         * Overflow
         * See https://tailwindcss.com/docs/overflow
         */
        groups.put("overflow", ClassGroup.withBase("overflow", OVERFLOW_SCALE));
        /*
         * Overflow X
         * See https://tailwindcss.com/docs/overflow
         */
        groups.put("overflow-x", ClassGroup.withBase("overflow-x", OVERFLOW_SCALE));
        /*
         * Overflow Y
         * See https://tailwindcss.com/docs/overflow
         */
        groups.put("overflow-y", ClassGroup.withBase("overflow-y", OVERFLOW_SCALE));
        /*
         * Overscroll Behavior
         * See https://tailwindcss.com/docs/overscroll-behavior
         */
        groups.put("overscroll", ClassGroup.withBase("overscroll", OVERSCROLL_SCALE));
        /*
         * Overscroll Behavior X
         * See https://tailwindcss.com/docs/overscroll-behavior
         */
        groups.put("overscroll-x", ClassGroup.withBase("overscroll-x", OVERSCROLL_SCALE));
        /*
         * Overscroll Behavior Y
         * See https://tailwindcss.com/docs/overscroll-behavior
         */
        groups.put("overscroll-y", ClassGroup.withBase("overscroll-y", OVERSCROLL_SCALE));
        /*
         * Position
         * See https://tailwindcss.com/docs/position
         */
        groups.put("position", ClassGroup.of("static", "fixed", "absolute", "relative", "sticky"));
        /*
         * Inset
         * See https://tailwindcss.com/docs/top-right-bottom-left
         */
        groups.put("inset", ClassGroup.withBase("inset", INSET_SCALE));
        /*
         * Inset Inline
         * See https://tailwindcss.com/docs/top-right-bottom-left
         */
        groups.put("inset-x", ClassGroup.withBase("inset-x", INSET_SCALE));
        /*
         * Inset Block
         * See https://tailwindcss.com/docs/top-right-bottom-left
         */
        groups.put("inset-y", ClassGroup.withBase("inset-y", INSET_SCALE));
        /*
         * Inset Inline Start
         * See https://tailwindcss.com/docs/top-right-bottom-left
         */
        groups.put("start", ClassGroup.of(
                ClassGroup.withBase("inset-s", INSET_SCALE),
                // Deprecated since Tailwind CSS v4.2.0 in favor of `inset-s-*` utilities.
                // See https://github.com/tailwindlabs/tailwindcss/pull/19613
                ClassGroup.withBase("start", INSET_SCALE)));
        /*
         * Inset Inline End
         * See https://tailwindcss.com/docs/top-right-bottom-left
         */
        groups.put("end", ClassGroup.of(
                ClassGroup.withBase("inset-e", INSET_SCALE),
                // Deprecated since Tailwind CSS v4.2.0 in favor of `inset-e-*` utilities.
                // See https://github.com/tailwindlabs/tailwindcss/pull/19613
                ClassGroup.withBase("end", INSET_SCALE)));
        /*
         * Inset Block Start
         * See https://tailwindcss.com/docs/top-right-bottom-left
         */
        groups.put("inset-bs", ClassGroup.withBase("inset-bs", INSET_SCALE));
        /*
         * Inset Block End
         * See https://tailwindcss.com/docs/top-right-bottom-left
         */
        groups.put("inset-be", ClassGroup.withBase("inset-be", INSET_SCALE));
        /*
         * Top
         * See https://tailwindcss.com/docs/top-right-bottom-left
         */
        groups.put("top", ClassGroup.withBase("top", INSET_SCALE));
        /*
         * Right
         * See https://tailwindcss.com/docs/top-right-bottom-left
         */
        groups.put("right", ClassGroup.withBase("right", INSET_SCALE));
        /*
         * Bottom
         * See https://tailwindcss.com/docs/top-right-bottom-left
         */
        groups.put("bottom", ClassGroup.withBase("bottom", INSET_SCALE));
        /*
         * Left
         * See https://tailwindcss.com/docs/top-right-bottom-left
         */
        groups.put("left", ClassGroup.withBase("left", INSET_SCALE));
        /*
         * Visibility
         * See https://tailwindcss.com/docs/visibility
         */
        groups.put("visibility", ClassGroup.of("visible", "invisible", "collapse"));
        /*
         * Z-Index
         * See https://tailwindcss.com/docs/z-index
         */
        groups.put("z", ClassGroup.withBase("z", "auto", IS_INTEGER, IS_ARBITRARY_VALUE, IS_ARBITRARY_VARIABLE));
    }
}
