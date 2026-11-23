package com.github.ddxailab.tailwindmerge;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.github.ddxailab.tailwindmerge.common.Validators;
import com.github.ddxailab.tailwindmerge.model.ClassGroup;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import org.junit.jupiter.api.Test;

class FlexboxGridClassGroupsTest {

    private static final List<String> EXPECTED_IDS = Arrays.asList(
            "basis", "flex-direction", "flex-wrap", "flex", "grow", "shrink", "order", "grid-cols", "col-start-end",
            "col-start", "col-end", "grid-rows", "row-start-end", "row-start", "row-end", "grid-flow", "auto-cols",
            "auto-rows", "gap", "gap-x", "gap-y", "justify-content", "justify-items", "justify-self",
            "align-content", "align-items", "align-self", "place-content", "place-items", "place-self", "p", "px",
            "py", "ps", "pe", "pbs", "pbe", "pt", "pr", "pb", "pl", "m", "mx", "my", "ms", "me", "mbs", "mbe", "mt",
            "mr", "mb", "ml", "space-x", "space-x-reverse", "space-y", "space-y-reverse");

    private static Map<String, ClassGroup> flexboxGrid() {
        Map<String, ClassGroup> groups = new LinkedHashMap<>();
        FlexboxGridClassGroups.addTo(groups);
        return groups;
    }

    @Test
    void definesEveryGroupInReferenceOrder() {
        assertEquals(EXPECTED_IDS, new ArrayList<>(flexboxGrid().keySet()));
    }

    @Test
    void theDefaultConfigIncludesTheGroups() {
        assertTrue(new TwMergeConfig().getClassGroups().keySet().containsAll(EXPECTED_IDS));
    }

    @Test
    void sharedBaseClassNamesAreKept() {
        Map<String, ClassGroup> groups = flexboxGrid();

        assertEquals("flex", groups.get("flex-direction").getBaseClassName());
        assertEquals("flex", groups.get("flex-wrap").getBaseClassName());
        assertEquals("flex", groups.get("flex").getBaseClassName());
        assertEquals("col", groups.get("col-start-end").getBaseClassName());
        assertEquals("row", groups.get("row-start-end").getBaseClassName());
        assertEquals("justify", groups.get("justify-content").getBaseClassName());
        assertEquals("content", groups.get("align-content").getBaseClassName());
        assertEquals("items", groups.get("align-items").getBaseClassName());
        assertEquals("self", groups.get("align-self").getBaseClassName());
    }

    @Test
    void basisKeepsTheReferenceOrder() {
        List<Object> definitions = flexboxGrid().get("basis").getDefinitions();

        assertEquals(Arrays.<Object>asList(
                "full", "auto", DefaultConfigScales.CONTAINER_THEME,
                Validators.IS_ARBITRARY_VARIABLE, Validators.IS_ARBITRARY_VALUE, DefaultConfigScales.SPACING_THEME,
                Validators.IS_FRACTION), definitions);
    }

    @Test
    void alignmentGroupsSpreadTheirScales() {
        Map<String, ClassGroup> groups = flexboxGrid();

        List<Object> justify = groups.get("justify-content").getDefinitions();
        assertEquals("normal", justify.get(0));
        assertEquals(1 + DefaultConfigScales.ALIGN_PRIMARY_AXIS_SCALE.length, justify.size());

        List<Object> items = groups.get("align-items").getDefinitions();
        assertEquals(1 + DefaultConfigScales.ALIGN_SECONDARY_AXIS_SCALE.length, items.size());
        ClassGroup baseline = (ClassGroup) items.get(0);
        assertEquals("baseline", baseline.getBaseClassName());
        assertEquals(Arrays.<Object>asList("", "last"), baseline.getDefinitions());

        List<Object> self = groups.get("align-self").getDefinitions();
        assertEquals("auto", self.get(0));
        assertEquals("baseline", ((ClassGroup) self.get(1)).getBaseClassName());
        assertEquals(2 + DefaultConfigScales.ALIGN_SECONDARY_AXIS_SCALE.length, self.size());

        assertEquals("baseline", groups.get("place-content").getDefinitions().get(0));
        assertEquals("baseline", groups.get("place-items").getDefinitions().get(0));
        assertEquals("auto", groups.get("place-self").getDefinitions().get(0));
    }

    @Test
    void spacingGroupsUseTheirScales() {
        Map<String, ClassGroup> groups = flexboxGrid();

        for (String id : Arrays.asList("p", "px", "py", "ps", "pe", "pbs", "pbe", "pt", "pr", "pb", "pl",
                "gap", "gap-x", "gap-y", "space-x", "space-y")) {
            assertEquals(Arrays.asList(DefaultConfigScales.UNAMBIGUOUS_SPACING_SCALE),
                    groups.get(id).getDefinitions(), id);
        }
        for (String id : Arrays.asList("m", "mx", "my", "ms", "me", "mbs", "mbe", "mt", "mr", "mb", "ml")) {
            assertEquals(Arrays.asList(DefaultConfigScales.MARGIN_SCALE), groups.get(id).getDefinitions(), id);
        }
    }

    @Test
    void reverseSpaceGroupsAreStandalone() {
        Map<String, ClassGroup> groups = flexboxGrid();

        assertNull(groups.get("space-x-reverse").getBaseClassName());
        assertEquals(Arrays.<Object>asList("space-x-reverse"), groups.get("space-x-reverse").getDefinitions());
        assertEquals(Arrays.<Object>asList("space-y-reverse"), groups.get("space-y-reverse").getDefinitions());
    }

    @Test
    void growAndShrinkAcceptTheEmptyClassName() {
        Map<String, ClassGroup> groups = flexboxGrid();

        assertEquals(Arrays.<Object>asList(
                "", Validators.IS_NUMBER, Validators.IS_ARBITRARY_VALUE, Validators.IS_ARBITRARY_VARIABLE),
                groups.get("grow").getDefinitions());
        assertEquals(groups.get("grow").getDefinitions(), groups.get("shrink").getDefinitions());
    }
}
