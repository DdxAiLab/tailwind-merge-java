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

class InteractivityClassGroupsTest {

    private static final List<String> EXPECTED_IDS = Arrays.asList(
            "accent", "appearance", "caret-color", "color-scheme", "cursor", "field-sizing", "pointer-events",
            "resize", "scroll-behavior", "scrollbar-thumb-color", "scrollbar-track-color", "scrollbar-gutter",
            "scrollbar-w", "scroll-m", "scroll-mx", "scroll-my", "scroll-ms", "scroll-me", "scroll-mbs",
            "scroll-mbe", "scroll-mt", "scroll-mr", "scroll-mb", "scroll-ml", "scroll-p", "scroll-px", "scroll-py",
            "scroll-ps", "scroll-pe", "scroll-pbs", "scroll-pbe", "scroll-pt", "scroll-pr", "scroll-pb", "scroll-pl",
            "snap-align", "snap-stop", "snap-type", "snap-strictness", "touch", "touch-x", "touch-y", "touch-pz",
            "select", "will-change");

    private static final List<String> SCROLL_SPACING_IDS = Arrays.asList(
            "scroll-m", "scroll-mx", "scroll-my", "scroll-ms", "scroll-me", "scroll-mbs", "scroll-mbe", "scroll-mt",
            "scroll-mr", "scroll-mb", "scroll-ml", "scroll-p", "scroll-px", "scroll-py", "scroll-ps", "scroll-pe",
            "scroll-pbs", "scroll-pbe", "scroll-pt", "scroll-pr", "scroll-pb", "scroll-pl");

    private static Map<String, ClassGroup> interactivity() {
        Map<String, ClassGroup> groups = new LinkedHashMap<>();
        InteractivityClassGroups.addTo(groups);
        return groups;
    }

    @Test
    void definesEveryGroupInReferenceOrder() {
        assertEquals(45, EXPECTED_IDS.size());
        assertEquals(EXPECTED_IDS, new ArrayList<>(interactivity().keySet()));
    }

    @Test
    void theDefaultConfigIncludesTheGroups() {
        assertTrue(new TwMergeConfig().getClassGroups().keySet().containsAll(EXPECTED_IDS));
    }

    @Test
    void colorGroupsUseTheColorScale() {
        Map<String, ClassGroup> groups = interactivity();

        assertColor(groups.get("accent"), "accent");
        assertColor(groups.get("caret-color"), "caret");
        assertColor(groups.get("scrollbar-thumb-color"), "scrollbar-thumb");
        assertColor(groups.get("scrollbar-track-color"), "scrollbar-track");
    }

    private static void assertColor(ClassGroup group, String base) {
        assertEquals(base, group.getBaseClassName());
        assertEquals(Arrays.asList(DefaultConfigScales.COLOR_SCALE), group.getDefinitions());
    }

    @Test
    void scrollMarginAndPaddingGroupsUseTheirOwnIdAsBase() {
        Map<String, ClassGroup> groups = interactivity();

        assertEquals(22, SCROLL_SPACING_IDS.size());
        for (String id : SCROLL_SPACING_IDS) {
            assertEquals(id, groups.get(id).getBaseClassName(), id);
            assertEquals(Arrays.asList(DefaultConfigScales.UNAMBIGUOUS_SPACING_SCALE),
                    groups.get(id).getDefinitions(), id);
        }
    }

    @Test
    void cursorKeepsTheReferenceValues() {
        ClassGroup cursor = interactivity().get("cursor");
        List<Object> definitions = cursor.getDefinitions();

        assertEquals("cursor", cursor.getBaseClassName());
        assertEquals(38, definitions.size());
        assertEquals(Arrays.<Object>asList("auto", "default", "pointer", "wait", "text"), definitions.subList(0, 5));
        assertEquals(Arrays.<Object>asList("nesw-resize", "nwse-resize", "zoom-in", "zoom-out"),
                definitions.subList(32, 36));
        assertEquals(Validators.IS_ARBITRARY_VALUE, definitions.get(36));
        assertEquals(Validators.IS_ARBITRARY_VARIABLE, definitions.get(37));
    }

    @Test
    void colorSchemeAndResizeKeepTheirValues() {
        Map<String, ClassGroup> groups = interactivity();

        assertEquals("scheme", groups.get("color-scheme").getBaseClassName());
        assertEquals(Arrays.<Object>asList("normal", "dark", "light", "light-dark", "only-dark", "only-light"),
                groups.get("color-scheme").getDefinitions());
        assertEquals(Arrays.<Object>asList("none", "", "x", "y"), groups.get("resize").getDefinitions());
    }

    @Test
    void scrollbarAndScrollBehaviorGroups() {
        Map<String, ClassGroup> groups = interactivity();

        assertEquals("scroll", groups.get("scroll-behavior").getBaseClassName());
        assertEquals(Arrays.<Object>asList("auto", "smooth"), groups.get("scroll-behavior").getDefinitions());
        assertEquals("scrollbar-gutter", groups.get("scrollbar-gutter").getBaseClassName());
        assertEquals(Arrays.<Object>asList("auto", "stable", "both"), groups.get("scrollbar-gutter").getDefinitions());
        assertEquals("scrollbar", groups.get("scrollbar-w").getBaseClassName());
        assertEquals(Arrays.<Object>asList("auto", "thin", "none"), groups.get("scrollbar-w").getDefinitions());
    }

    @Test
    void snapGroupsShareTheSnapBaseClassName() {
        Map<String, ClassGroup> groups = interactivity();

        for (String id : Arrays.asList("snap-align", "snap-stop", "snap-type", "snap-strictness")) {
            assertEquals("snap", groups.get(id).getBaseClassName(), id);
        }
        assertEquals(Arrays.<Object>asList("start", "end", "center", "align-none"),
                groups.get("snap-align").getDefinitions());
        assertEquals(Arrays.<Object>asList("normal", "always"), groups.get("snap-stop").getDefinitions());
        assertEquals(Arrays.<Object>asList("none", "x", "y", "both"), groups.get("snap-type").getDefinitions());
        assertEquals(Arrays.<Object>asList("mandatory", "proximity"), groups.get("snap-strictness").getDefinitions());
    }

    @Test
    void touchGroups() {
        Map<String, ClassGroup> groups = interactivity();

        assertEquals("touch", groups.get("touch").getBaseClassName());
        assertEquals(Arrays.<Object>asList("auto", "none", "manipulation"), groups.get("touch").getDefinitions());
        assertEquals("touch-pan", groups.get("touch-x").getBaseClassName());
        assertEquals(Arrays.<Object>asList("x", "left", "right"), groups.get("touch-x").getDefinitions());
        assertEquals("touch-pan", groups.get("touch-y").getBaseClassName());
        assertEquals(Arrays.<Object>asList("y", "up", "down"), groups.get("touch-y").getDefinitions());
        assertNull(groups.get("touch-pz").getBaseClassName());
        assertEquals(Arrays.<Object>asList("touch-pinch-zoom"), groups.get("touch-pz").getDefinitions());
    }

    @Test
    void remainingGroups() {
        Map<String, ClassGroup> groups = interactivity();

        assertEquals(Arrays.<Object>asList("none", "auto"), groups.get("appearance").getDefinitions());
        assertEquals(Arrays.<Object>asList("fixed", "content"), groups.get("field-sizing").getDefinitions());
        assertEquals(Arrays.<Object>asList("auto", "none"), groups.get("pointer-events").getDefinitions());
        assertEquals("select", groups.get("select").getBaseClassName());
        assertEquals(Arrays.<Object>asList("none", "text", "all", "auto"), groups.get("select").getDefinitions());
        assertEquals(Arrays.<Object>asList(
                "auto", "scroll", "contents", "transform", Validators.IS_ARBITRARY_VALUE,
                Validators.IS_ARBITRARY_VARIABLE), groups.get("will-change").getDefinitions());
    }
}
