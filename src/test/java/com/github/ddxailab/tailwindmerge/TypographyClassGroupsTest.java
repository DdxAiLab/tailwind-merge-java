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

class TypographyClassGroupsTest {

    private static final List<String> EXPECTED_IDS = Arrays.asList(
            "font-size", "font-smoothing", "font-style", "font-weight", "font-stretch", "font-family",
            "font-features", "fvn-normal", "fvn-ordinal", "fvn-slashed-zero", "fvn-figure", "fvn-spacing",
            "fvn-fraction", "tracking", "line-clamp", "leading", "list-image", "list-style-position",
            "list-style-type", "text-alignment", "text-color", "text-decoration", "text-decoration-style",
            "text-decoration-thickness", "text-decoration-color", "underline-offset", "text-transform",
            "text-overflow", "text-wrap", "indent", "tab-size", "vertical-align", "whitespace", "break", "wrap",
            "hyphens", "content");

    private static Map<String, ClassGroup> typography() {
        Map<String, ClassGroup> groups = new LinkedHashMap<>();
        TypographyClassGroups.addTo(groups);
        return groups;
    }

    @Test
    void definesEveryGroupInReferenceOrder() {
        assertEquals(EXPECTED_IDS, new ArrayList<>(typography().keySet()));
    }

    @Test
    void theDefaultConfigIncludesTheGroups() {
        assertTrue(new TwMergeConfig().getClassGroups().keySet().containsAll(EXPECTED_IDS));
    }

    @Test
    void groupsSharingABaseClassNameKeepIt() {
        Map<String, ClassGroup> groups = typography();

        for (String id : Arrays.asList("font-size", "text-alignment", "text-color", "text-wrap")) {
            assertEquals("text", groups.get(id).getBaseClassName(), id);
        }
        assertEquals("font", groups.get("font-weight").getBaseClassName());
        assertEquals("font", groups.get("font-family").getBaseClassName());
        for (String id : Arrays.asList("text-decoration-style", "text-decoration-thickness",
                "text-decoration-color")) {
            assertEquals("decoration", groups.get(id).getBaseClassName(), id);
        }
        assertEquals("list", groups.get("list-style-position").getBaseClassName());
        assertEquals("list", groups.get("list-style-type").getBaseClassName());
        assertEquals("tab", groups.get("tab-size").getBaseClassName());
        assertEquals("align", groups.get("vertical-align").getBaseClassName());
    }

    @Test
    void standaloneGroupsHaveNoBaseClassName() {
        Map<String, ClassGroup> groups = typography();

        for (String id : Arrays.asList("font-smoothing", "font-style", "fvn-normal", "fvn-ordinal",
                "fvn-slashed-zero", "fvn-figure", "fvn-spacing", "fvn-fraction", "text-decoration",
                "text-transform", "text-overflow")) {
            assertNull(groups.get(id).getBaseClassName(), id);
        }
        assertEquals(Arrays.<Object>asList("lining-nums", "oldstyle-nums"), groups.get("fvn-figure").getDefinitions());
        assertEquals(Arrays.<Object>asList("truncate", "text-ellipsis", "text-clip"),
                groups.get("text-overflow").getDefinitions());
    }

    @Test
    void fontGroupsUseTheirThemeGettersAndValidators() {
        Map<String, ClassGroup> groups = typography();

        assertEquals(Arrays.<Object>asList(
                "base", DefaultConfigScales.TEXT_THEME, Validators.IS_ARBITRARY_LENGTH,
                Validators.IS_ARBITRARY_VARIABLE_LENGTH), groups.get("font-size").getDefinitions());
        assertEquals(Arrays.<Object>asList(
                DefaultConfigScales.FONT_WEIGHT_THEME, Validators.IS_ARBITRARY_WEIGHT,
                Validators.IS_ARBITRARY_VARIABLE_WEIGHT), groups.get("font-weight").getDefinitions());
        assertEquals(Arrays.<Object>asList(
                DefaultConfigScales.FONT_THEME, Validators.IS_ARBITRARY_FAMILY_NAME,
                Validators.IS_ARBITRARY_VARIABLE_FAMILY_NAME), groups.get("font-family").getDefinitions());
        assertEquals(11, groups.get("font-stretch").getDefinitions().size());
    }

    @Test
    void leadingKeepsTheReferenceOrder() {
        List<Object> expected = new ArrayList<>(Arrays.<Object>asList("none", DefaultConfigScales.LEADING_THEME));
        expected.addAll(Arrays.asList(DefaultConfigScales.UNAMBIGUOUS_SPACING_SCALE));

        assertEquals(expected, typography().get("leading").getDefinitions());
    }

    @Test
    void scalesAreSpreadWhereTheReferenceSpreadsThem() {
        Map<String, ClassGroup> groups = typography();

        assertEquals(Arrays.asList(DefaultConfigScales.COLOR_SCALE), groups.get("text-color").getDefinitions());
        assertEquals(Arrays.asList(DefaultConfigScales.COLOR_SCALE),
                groups.get("text-decoration-color").getDefinitions());
        assertEquals(Arrays.asList(DefaultConfigScales.UNAMBIGUOUS_SPACING_SCALE),
                groups.get("indent").getDefinitions());

        List<Object> style = groups.get("text-decoration-style").getDefinitions();
        assertEquals("wavy", style.get(0));
        assertEquals(Arrays.asList(DefaultConfigScales.LINE_STYLE_SCALE), style.subList(1, style.size()));
    }
}
