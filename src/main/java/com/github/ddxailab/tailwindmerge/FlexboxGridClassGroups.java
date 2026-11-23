package com.github.ddxailab.tailwindmerge;

import static com.github.ddxailab.tailwindmerge.DefaultConfigScales.ALIGN_PRIMARY_AXIS_SCALE;
import static com.github.ddxailab.tailwindmerge.DefaultConfigScales.ALIGN_SECONDARY_AXIS_SCALE;
import static com.github.ddxailab.tailwindmerge.DefaultConfigScales.CONTAINER_THEME;
import static com.github.ddxailab.tailwindmerge.DefaultConfigScales.GRID_AUTO_COLS_ROWS_SCALE;
import static com.github.ddxailab.tailwindmerge.DefaultConfigScales.GRID_COL_ROW_START_AND_END_SCALE;
import static com.github.ddxailab.tailwindmerge.DefaultConfigScales.GRID_COL_ROW_START_OR_END_SCALE;
import static com.github.ddxailab.tailwindmerge.DefaultConfigScales.GRID_TEMPLATE_COLS_ROWS_SCALE;
import static com.github.ddxailab.tailwindmerge.DefaultConfigScales.MARGIN_SCALE;
import static com.github.ddxailab.tailwindmerge.DefaultConfigScales.UNAMBIGUOUS_SPACING_SCALE;
import static com.github.ddxailab.tailwindmerge.DefaultConfigScales.concat;
import static com.github.ddxailab.tailwindmerge.common.Validators.IS_ARBITRARY_VALUE;
import static com.github.ddxailab.tailwindmerge.common.Validators.IS_ARBITRARY_VARIABLE;
import static com.github.ddxailab.tailwindmerge.common.Validators.IS_FRACTION;
import static com.github.ddxailab.tailwindmerge.common.Validators.IS_INTEGER;
import static com.github.ddxailab.tailwindmerge.common.Validators.IS_NUMBER;

import com.github.ddxailab.tailwindmerge.model.ClassGroup;
import java.util.Map;

/**
 * The "Flexbox and Grid" section of the default class groups (spacing groups included, as in the reference).
 */
final class FlexboxGridClassGroups {

    private FlexboxGridClassGroups() {
    }

    static void addTo(Map<String, ClassGroup> groups) {
        // Flex Basis: https://tailwindcss.com/docs/flex-basis
        groups.put("basis", ClassGroup.withBase("basis", concat(
                "full",
                "auto",
                CONTAINER_THEME,
                UNAMBIGUOUS_SPACING_SCALE,
                IS_FRACTION)));
        // Flex Direction: https://tailwindcss.com/docs/flex-direction
        groups.put("flex-direction", ClassGroup.withBase("flex", "row", "row-reverse", "col", "col-reverse"));
        // Flex Wrap: https://tailwindcss.com/docs/flex-wrap
        groups.put("flex-wrap", ClassGroup.withBase("flex", "nowrap", "wrap", "wrap-reverse"));
        // Flex: https://tailwindcss.com/docs/flex
        groups.put("flex", ClassGroup.withBase("flex",
                "auto",
                "initial",
                "none",
                IS_NUMBER,
                IS_FRACTION,
                IS_ARBITRARY_VALUE));
        // Flex Grow: https://tailwindcss.com/docs/flex-grow
        groups.put("grow", ClassGroup.withBase("grow", "", IS_NUMBER, IS_ARBITRARY_VALUE, IS_ARBITRARY_VARIABLE));
        // Flex Shrink: https://tailwindcss.com/docs/flex-shrink
        groups.put("shrink", ClassGroup.withBase("shrink", "", IS_NUMBER, IS_ARBITRARY_VALUE, IS_ARBITRARY_VARIABLE));
        // Order: https://tailwindcss.com/docs/order
        groups.put("order", ClassGroup.withBase("order",
                "first",
                "last",
                "none",
                IS_INTEGER,
                IS_ARBITRARY_VALUE,
                IS_ARBITRARY_VARIABLE));
        // Grid Template Columns: https://tailwindcss.com/docs/grid-template-columns
        groups.put("grid-cols", ClassGroup.withBase("grid-cols", GRID_TEMPLATE_COLS_ROWS_SCALE));
        // Grid Column Start / End: https://tailwindcss.com/docs/grid-column
        groups.put("col-start-end", ClassGroup.withBase("col", GRID_COL_ROW_START_AND_END_SCALE));
        // Grid Column Start: https://tailwindcss.com/docs/grid-column
        groups.put("col-start", ClassGroup.withBase("col-start", GRID_COL_ROW_START_OR_END_SCALE));
        // Grid Column End: https://tailwindcss.com/docs/grid-column
        groups.put("col-end", ClassGroup.withBase("col-end", GRID_COL_ROW_START_OR_END_SCALE));
        // Grid Template Rows: https://tailwindcss.com/docs/grid-template-rows
        groups.put("grid-rows", ClassGroup.withBase("grid-rows", GRID_TEMPLATE_COLS_ROWS_SCALE));
        // Grid Row Start / End: https://tailwindcss.com/docs/grid-row
        groups.put("row-start-end", ClassGroup.withBase("row", GRID_COL_ROW_START_AND_END_SCALE));
        // Grid Row Start: https://tailwindcss.com/docs/grid-row
        groups.put("row-start", ClassGroup.withBase("row-start", GRID_COL_ROW_START_OR_END_SCALE));
        // Grid Row End: https://tailwindcss.com/docs/grid-row
        groups.put("row-end", ClassGroup.withBase("row-end", GRID_COL_ROW_START_OR_END_SCALE));
        // Grid Auto Flow: https://tailwindcss.com/docs/grid-auto-flow
        groups.put("grid-flow", ClassGroup.withBase("grid-flow", "row", "col", "dense", "row-dense", "col-dense"));
        // Grid Auto Columns: https://tailwindcss.com/docs/grid-auto-columns
        groups.put("auto-cols", ClassGroup.withBase("auto-cols", GRID_AUTO_COLS_ROWS_SCALE));
        // Grid Auto Rows: https://tailwindcss.com/docs/grid-auto-rows
        groups.put("auto-rows", ClassGroup.withBase("auto-rows", GRID_AUTO_COLS_ROWS_SCALE));
        // Gap: https://tailwindcss.com/docs/gap
        groups.put("gap", ClassGroup.withBase("gap", UNAMBIGUOUS_SPACING_SCALE));
        // Gap X: https://tailwindcss.com/docs/gap
        groups.put("gap-x", ClassGroup.withBase("gap-x", UNAMBIGUOUS_SPACING_SCALE));
        // Gap Y: https://tailwindcss.com/docs/gap
        groups.put("gap-y", ClassGroup.withBase("gap-y", UNAMBIGUOUS_SPACING_SCALE));
        // Justify Content: https://tailwindcss.com/docs/justify-content
        groups.put("justify-content", ClassGroup.withBase("justify", concat("normal", ALIGN_PRIMARY_AXIS_SCALE)));
        // Justify Items: https://tailwindcss.com/docs/justify-items
        groups.put("justify-items", ClassGroup.withBase("justify-items", concat("normal", ALIGN_SECONDARY_AXIS_SCALE)));
        // Justify Self: https://tailwindcss.com/docs/justify-self
        groups.put("justify-self", ClassGroup.withBase("justify-self", concat("auto", ALIGN_SECONDARY_AXIS_SCALE)));
        // Align Content: https://tailwindcss.com/docs/align-content
        groups.put("align-content", ClassGroup.withBase("content", concat("normal", ALIGN_PRIMARY_AXIS_SCALE)));
        // Align Items: https://tailwindcss.com/docs/align-items
        groups.put("align-items", ClassGroup.withBase("items", concat(
                ClassGroup.withBase("baseline", "", "last"),
                ALIGN_SECONDARY_AXIS_SCALE)));
        // Align Self: https://tailwindcss.com/docs/align-self
        groups.put("align-self", ClassGroup.withBase("self", concat(
                "auto",
                ClassGroup.withBase("baseline", "", "last"),
                ALIGN_SECONDARY_AXIS_SCALE)));
        // Place Content: https://tailwindcss.com/docs/place-content
        groups.put("place-content", ClassGroup.withBase("place-content", concat("baseline", ALIGN_PRIMARY_AXIS_SCALE)));
        // Place Items: https://tailwindcss.com/docs/place-items
        groups.put("place-items", ClassGroup.withBase("place-items", concat("baseline", ALIGN_SECONDARY_AXIS_SCALE)));
        // Place Self: https://tailwindcss.com/docs/place-self
        groups.put("place-self", ClassGroup.withBase("place-self", concat("auto", ALIGN_SECONDARY_AXIS_SCALE)));

        // Padding: https://tailwindcss.com/docs/padding
        groups.put("p", ClassGroup.withBase("p", UNAMBIGUOUS_SPACING_SCALE));
        groups.put("px", ClassGroup.withBase("px", UNAMBIGUOUS_SPACING_SCALE));
        groups.put("py", ClassGroup.withBase("py", UNAMBIGUOUS_SPACING_SCALE));
        groups.put("ps", ClassGroup.withBase("ps", UNAMBIGUOUS_SPACING_SCALE));
        groups.put("pe", ClassGroup.withBase("pe", UNAMBIGUOUS_SPACING_SCALE));
        groups.put("pbs", ClassGroup.withBase("pbs", UNAMBIGUOUS_SPACING_SCALE));
        groups.put("pbe", ClassGroup.withBase("pbe", UNAMBIGUOUS_SPACING_SCALE));
        groups.put("pt", ClassGroup.withBase("pt", UNAMBIGUOUS_SPACING_SCALE));
        groups.put("pr", ClassGroup.withBase("pr", UNAMBIGUOUS_SPACING_SCALE));
        groups.put("pb", ClassGroup.withBase("pb", UNAMBIGUOUS_SPACING_SCALE));
        groups.put("pl", ClassGroup.withBase("pl", UNAMBIGUOUS_SPACING_SCALE));

        // Margin: https://tailwindcss.com/docs/margin
        groups.put("m", ClassGroup.withBase("m", MARGIN_SCALE));
        groups.put("mx", ClassGroup.withBase("mx", MARGIN_SCALE));
        groups.put("my", ClassGroup.withBase("my", MARGIN_SCALE));
        groups.put("ms", ClassGroup.withBase("ms", MARGIN_SCALE));
        groups.put("me", ClassGroup.withBase("me", MARGIN_SCALE));
        groups.put("mbs", ClassGroup.withBase("mbs", MARGIN_SCALE));
        groups.put("mbe", ClassGroup.withBase("mbe", MARGIN_SCALE));
        groups.put("mt", ClassGroup.withBase("mt", MARGIN_SCALE));
        groups.put("mr", ClassGroup.withBase("mr", MARGIN_SCALE));
        groups.put("mb", ClassGroup.withBase("mb", MARGIN_SCALE));
        groups.put("ml", ClassGroup.withBase("ml", MARGIN_SCALE));

        // Space Between: https://tailwindcss.com/docs/space
        groups.put("space-x", ClassGroup.withBase("space-x", UNAMBIGUOUS_SPACING_SCALE));
        groups.put("space-x-reverse", ClassGroup.of("space-x-reverse"));
        groups.put("space-y", ClassGroup.withBase("space-y", UNAMBIGUOUS_SPACING_SCALE));
        groups.put("space-y-reverse", ClassGroup.of("space-y-reverse"));
    }
}
