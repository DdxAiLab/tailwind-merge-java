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

class TablesTransitionsTransformsClassGroupsTest {

    private static final List<String> EXPECTED_IDS = Arrays.asList(
            // Tables
            "border-collapse", "border-spacing", "border-spacing-x", "border-spacing-y", "table-layout", "caption",
            // Transitions and animation
            "transition", "transition-behavior", "duration", "ease", "delay", "animate",
            // Transforms
            "backface", "perspective", "perspective-origin", "rotate", "rotate-x", "rotate-y", "rotate-z", "scale",
            "scale-x", "scale-y", "scale-z", "scale-3d", "skew", "skew-x", "skew-y", "transform",
            "transform-origin", "transform-style", "translate", "translate-x", "translate-y", "translate-z",
            "translate-none", "zoom");

    private static Map<String, ClassGroup> groups() {
        Map<String, ClassGroup> groups = new LinkedHashMap<>();
        TablesTransitionsTransformsClassGroups.addTo(groups);
        return groups;
    }

    @Test
    void definesEveryGroupInReferenceOrder() {
        assertEquals(36, EXPECTED_IDS.size());
        assertEquals(EXPECTED_IDS, new ArrayList<>(groups().keySet()));
    }

    @Test
    void theDefaultConfigIncludesTheGroups() {
        assertTrue(new TwMergeConfig().getClassGroups().keySet().containsAll(EXPECTED_IDS));
    }

    @Test
    void tablesGroups() {
        Map<String, ClassGroup> groups = groups();

        assertEquals("border", groups.get("border-collapse").getBaseClassName());
        assertEquals(Arrays.<Object>asList("collapse", "separate"), groups.get("border-collapse").getDefinitions());
        for (String id : Arrays.asList("border-spacing", "border-spacing-x", "border-spacing-y")) {
            assertEquals(id, groups.get(id).getBaseClassName(), id);
            assertEquals(Arrays.asList(DefaultConfigScales.UNAMBIGUOUS_SPACING_SCALE),
                    groups.get(id).getDefinitions(), id);
        }
        assertEquals("table", groups.get("table-layout").getBaseClassName());
        assertEquals(Arrays.<Object>asList("auto", "fixed"), groups.get("table-layout").getDefinitions());
        assertEquals("caption", groups.get("caption").getBaseClassName());
        assertEquals(Arrays.<Object>asList("top", "bottom"), groups.get("caption").getDefinitions());
    }

    @Test
    void transitionGroupsShareTheTransitionBaseClassName() {
        Map<String, ClassGroup> groups = groups();

        assertEquals("transition", groups.get("transition").getBaseClassName());
        assertEquals(Arrays.<Object>asList(
                "", "all", "colors", "opacity", "shadow", "transform", "none", Validators.IS_ARBITRARY_VALUE,
                Validators.IS_ARBITRARY_VARIABLE), groups.get("transition").getDefinitions());
        assertEquals("transition", groups.get("transition-behavior").getBaseClassName());
        assertEquals(Arrays.<Object>asList("normal", "discrete"), groups.get("transition-behavior").getDefinitions());
    }

    @Test
    void timingAndAnimationGroupsUseTheirThemeGetters() {
        Map<String, ClassGroup> groups = groups();

        assertEquals(Arrays.<Object>asList(
                "initial", Validators.IS_NUMBER, Validators.IS_ARBITRARY_VALUE, Validators.IS_ARBITRARY_VARIABLE),
                groups.get("duration").getDefinitions());
        assertEquals(Arrays.<Object>asList(
                "linear", "initial", DefaultConfigScales.EASE_THEME, Validators.IS_ARBITRARY_VALUE,
                Validators.IS_ARBITRARY_VARIABLE), groups.get("ease").getDefinitions());
        assertEquals(Arrays.<Object>asList(
                Validators.IS_NUMBER, Validators.IS_ARBITRARY_VALUE, Validators.IS_ARBITRARY_VARIABLE),
                groups.get("delay").getDefinitions());
        assertEquals(Arrays.<Object>asList(
                "none", DefaultConfigScales.ANIMATE_THEME, Validators.IS_ARBITRARY_VALUE,
                Validators.IS_ARBITRARY_VARIABLE), groups.get("animate").getDefinitions());
    }

    @Test
    void perspectiveAndOriginGroups() {
        Map<String, ClassGroup> groups = groups();

        assertEquals("backface", groups.get("backface").getBaseClassName());
        assertEquals(Arrays.<Object>asList("hidden", "visible"), groups.get("backface").getDefinitions());
        assertEquals(Arrays.<Object>asList(
                DefaultConfigScales.PERSPECTIVE_THEME, Validators.IS_ARBITRARY_VALUE,
                Validators.IS_ARBITRARY_VARIABLE), groups.get("perspective").getDefinitions());
        assertEquals("perspective-origin", groups.get("perspective-origin").getBaseClassName());
        assertEquals(Arrays.asList(DefaultConfigScales.POSITION_WITH_ARBITRARY_SCALE),
                groups.get("perspective-origin").getDefinitions());
        assertEquals("origin", groups.get("transform-origin").getBaseClassName());
        assertEquals(Arrays.asList(DefaultConfigScales.POSITION_WITH_ARBITRARY_SCALE),
                groups.get("transform-origin").getDefinitions());
    }

    @Test
    void rotateScaleSkewAndTranslateGroupsUseTheirScales() {
        Map<String, ClassGroup> groups = groups();

        assertScale(groups, DefaultConfigScales.ROTATE_SCALE, "rotate", "rotate-x", "rotate-y", "rotate-z");
        assertScale(groups, DefaultConfigScales.SCALE_SCALE, "scale", "scale-x", "scale-y", "scale-z");
        assertScale(groups, DefaultConfigScales.SKEW_SCALE, "skew", "skew-x", "skew-y");
        assertScale(groups, DefaultConfigScales.TRANSLATE_SCALE,
                "translate", "translate-x", "translate-y", "translate-z");
    }

    private static void assertScale(Map<String, ClassGroup> groups, Object[] scale, String... ids) {
        for (String id : ids) {
            assertEquals(id, groups.get(id).getBaseClassName(), id);
            assertEquals(Arrays.asList(scale), groups.get(id).getDefinitions(), id);
        }
    }

    @Test
    void standaloneTransformGroupsHaveNoBaseClassName() {
        Map<String, ClassGroup> groups = groups();

        for (String id : Arrays.asList("scale-3d", "translate-none")) {
            assertNull(groups.get(id).getBaseClassName(), id);
            assertEquals(Arrays.<Object>asList(id), groups.get(id).getDefinitions(), id);
        }
    }

    @Test
    void transformGroupsShareTheTransformBaseClassName() {
        Map<String, ClassGroup> groups = groups();

        assertEquals("transform", groups.get("transform").getBaseClassName());
        assertEquals(Arrays.<Object>asList(
                "", "none", "gpu", "cpu", Validators.IS_ARBITRARY_VALUE, Validators.IS_ARBITRARY_VARIABLE),
                groups.get("transform").getDefinitions());
        assertEquals("transform", groups.get("transform-style").getBaseClassName());
        assertEquals(Arrays.<Object>asList("3d", "flat"), groups.get("transform-style").getDefinitions());
    }

    @Test
    void zoomKeepsTheReferenceValidatorOrder() {
        assertEquals("zoom", groups().get("zoom").getBaseClassName());
        assertEquals(Arrays.<Object>asList(
                Validators.IS_INTEGER, Validators.IS_ARBITRARY_VARIABLE, Validators.IS_ARBITRARY_VALUE),
                groups().get("zoom").getDefinitions());
    }
}
