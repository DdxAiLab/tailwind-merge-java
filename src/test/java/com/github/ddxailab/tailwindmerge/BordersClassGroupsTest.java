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

class BordersClassGroupsTest {

    private static final List<String> RADIUS_IDS = Arrays.asList(
            "rounded", "rounded-s", "rounded-e", "rounded-t", "rounded-r", "rounded-b", "rounded-l", "rounded-ss",
            "rounded-se", "rounded-ee", "rounded-es", "rounded-tl", "rounded-tr", "rounded-br", "rounded-bl");

    private static final List<String> BORDER_WIDTH_IDS = Arrays.asList(
            "border-w", "border-w-x", "border-w-y", "border-w-s", "border-w-e", "border-w-bs", "border-w-be",
            "border-w-t", "border-w-r", "border-w-b", "border-w-l");

    private static final List<String> BORDER_COLOR_IDS = Arrays.asList(
            "border-color", "border-color-x", "border-color-y", "border-color-s", "border-color-e",
            "border-color-bs", "border-color-be", "border-color-t", "border-color-r", "border-color-b",
            "border-color-l");

    private static final List<String> EXPECTED_IDS = new ArrayList<>();

    static {
        EXPECTED_IDS.addAll(RADIUS_IDS);
        EXPECTED_IDS.addAll(BORDER_WIDTH_IDS);
        EXPECTED_IDS.addAll(Arrays.asList("divide-x", "divide-x-reverse", "divide-y", "divide-y-reverse",
                "border-style", "divide-style"));
        EXPECTED_IDS.addAll(BORDER_COLOR_IDS);
        EXPECTED_IDS.addAll(Arrays.asList("divide-color", "outline-style", "outline-offset", "outline-w",
                "outline-color"));
    }

    private static Map<String, ClassGroup> borders() {
        Map<String, ClassGroup> groups = new LinkedHashMap<>();
        BordersClassGroups.addTo(groups);
        return groups;
    }

    @Test
    void definesEveryGroupInReferenceOrder() {
        assertEquals(48, EXPECTED_IDS.size());
        assertEquals(EXPECTED_IDS, new ArrayList<>(borders().keySet()));
    }

    @Test
    void theDefaultConfigIncludesTheGroups() {
        assertTrue(new TwMergeConfig().getClassGroups().keySet().containsAll(EXPECTED_IDS));
    }

    @Test
    void radiusGroupsUseTheirOwnBaseClassNameAndTheRadiusScale() {
        Map<String, ClassGroup> groups = borders();

        for (String id : RADIUS_IDS) {
            assertEquals(id, groups.get(id).getBaseClassName(), id);
            assertEquals(Arrays.asList(DefaultConfigScales.RADIUS_SCALE), groups.get(id).getDefinitions(), id);
        }
    }

    @Test
    void borderWidthAndColorGroupsShareTheBorderBaseClassNames() {
        Map<String, ClassGroup> groups = borders();
        List<String> sides = Arrays.asList("border", "border-x", "border-y", "border-s", "border-e", "border-bs",
                "border-be", "border-t", "border-r", "border-b", "border-l");

        for (int i = 0; i < sides.size(); i++) {
            ClassGroup width = groups.get(BORDER_WIDTH_IDS.get(i));
            ClassGroup color = groups.get(BORDER_COLOR_IDS.get(i));
            assertEquals(sides.get(i), width.getBaseClassName(), BORDER_WIDTH_IDS.get(i));
            assertEquals(sides.get(i), color.getBaseClassName(), BORDER_COLOR_IDS.get(i));
            assertEquals(Arrays.asList(DefaultConfigScales.BORDER_WIDTH_SCALE), width.getDefinitions());
            assertEquals(Arrays.asList(DefaultConfigScales.COLOR_SCALE), color.getDefinitions());
        }
    }

    @Test
    void divideWidthGroupsAndTheirReverseVariants() {
        Map<String, ClassGroup> groups = borders();

        assertEquals("divide-x", groups.get("divide-x").getBaseClassName());
        assertEquals(Arrays.asList(DefaultConfigScales.BORDER_WIDTH_SCALE), groups.get("divide-x").getDefinitions());
        assertEquals("divide-y", groups.get("divide-y").getBaseClassName());
        assertNull(groups.get("divide-x-reverse").getBaseClassName());
        assertEquals(Arrays.<Object>asList("divide-x-reverse"), groups.get("divide-x-reverse").getDefinitions());
        assertNull(groups.get("divide-y-reverse").getBaseClassName());
        assertEquals(Arrays.<Object>asList("divide-y-reverse"), groups.get("divide-y-reverse").getDefinitions());
    }

    @Test
    void styleGroupsPrependTheirKeywordsToTheLineStyleScale() {
        Map<String, ClassGroup> groups = borders();
        List<Object> lineStyles = Arrays.asList(DefaultConfigScales.LINE_STYLE_SCALE);

        assertStyle("border", Arrays.<Object>asList("hidden", "none"), lineStyles, groups.get("border-style"));
        assertStyle("divide", Arrays.<Object>asList("hidden", "none"), lineStyles, groups.get("divide-style"));
        assertStyle("outline", Arrays.<Object>asList("none", "hidden"), lineStyles, groups.get("outline-style"));
    }

    private static void assertStyle(String base, List<Object> keywords, List<Object> scale, ClassGroup group) {
        List<Object> expected = new ArrayList<>(keywords);
        expected.addAll(scale);
        assertEquals(base, group.getBaseClassName());
        assertEquals(expected, group.getDefinitions());
    }

    @Test
    void outlineGroupsShareTheOutlineBaseClassName() {
        Map<String, ClassGroup> groups = borders();

        assertEquals("outline", groups.get("outline-w").getBaseClassName());
        assertEquals(Arrays.<Object>asList(
                "", Validators.IS_NUMBER, Validators.IS_ARBITRARY_LENGTH, Validators.IS_ARBITRARY_VARIABLE_LENGTH),
                groups.get("outline-w").getDefinitions());
        assertEquals("outline", groups.get("outline-color").getBaseClassName());
        assertEquals(Arrays.asList(DefaultConfigScales.COLOR_SCALE), groups.get("outline-color").getDefinitions());
        assertEquals("outline-offset", groups.get("outline-offset").getBaseClassName());
        assertEquals(Arrays.<Object>asList(
                Validators.IS_NUMBER, Validators.IS_ARBITRARY_VALUE, Validators.IS_ARBITRARY_VARIABLE),
                groups.get("outline-offset").getDefinitions());
    }
}
