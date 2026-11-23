package com.github.ddxailab.tailwindmerge;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.github.ddxailab.tailwindmerge.common.Validators;
import com.github.ddxailab.tailwindmerge.model.ClassGroup;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import org.junit.jupiter.api.Test;

class SvgAccessibilityClassGroupsTest {

    private static final List<String> EXPECTED_IDS =
            Arrays.asList("fill", "stroke-w", "stroke", "forced-color-adjust");

    private static Map<String, ClassGroup> groups() {
        Map<String, ClassGroup> groups = new LinkedHashMap<>();
        SvgAccessibilityClassGroups.addTo(groups);
        return groups;
    }

    @Test
    void definesEveryGroupInReferenceOrder() {
        assertEquals(EXPECTED_IDS, new ArrayList<>(groups().keySet()));
    }

    @Test
    void theDefaultConfigIncludesTheGroups() {
        assertTrue(new TwMergeConfig().getClassGroups().keySet().containsAll(EXPECTED_IDS));
    }

    @Test
    void fillAndStrokePrependNoneToTheColorScale() {
        Map<String, ClassGroup> groups = groups();
        List<Object> expected = new ArrayList<>();
        expected.add("none");
        expected.addAll(Arrays.asList(DefaultConfigScales.COLOR_SCALE));

        assertEquals("fill", groups.get("fill").getBaseClassName());
        assertEquals(expected, groups.get("fill").getDefinitions());
        assertEquals("stroke", groups.get("stroke").getBaseClassName());
        assertEquals(expected, groups.get("stroke").getDefinitions());
    }

    @Test
    void strokeWidthSharesTheStrokeBaseClassName() {
        ClassGroup strokeW = groups().get("stroke-w");

        assertEquals("stroke", strokeW.getBaseClassName());
        assertEquals(Arrays.<Object>asList(
                Validators.IS_NUMBER, Validators.IS_ARBITRARY_NUMBER, Validators.IS_ARBITRARY_LENGTH,
                Validators.IS_ARBITRARY_VARIABLE_LENGTH), strokeW.getDefinitions());
    }

    @Test
    void forcedColorAdjust() {
        ClassGroup group = groups().get("forced-color-adjust");

        assertEquals("forced-color-adjust", group.getBaseClassName());
        assertEquals(Arrays.<Object>asList("auto", "none"), group.getDefinitions());
    }
}
