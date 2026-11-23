package com.github.ddxailab.tailwindmerge;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.github.ddxailab.tailwindmerge.model.ClassGroup;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import org.junit.jupiter.api.Test;

class SizingClassGroupsTest {

    private static final List<String> EXPECTED_IDS = Arrays.asList(
            "size", "inline-size", "min-inline-size", "max-inline-size", "block-size", "min-block-size",
            "max-block-size", "w", "min-w", "max-w", "h", "min-h", "max-h");

    private static Map<String, ClassGroup> sizing() {
        Map<String, ClassGroup> groups = new LinkedHashMap<>();
        SizingClassGroups.addTo(groups);
        return groups;
    }

    @Test
    void definesEveryGroupInReferenceOrder() {
        assertEquals(EXPECTED_IDS, new ArrayList<>(sizing().keySet()));
    }

    @Test
    void theDefaultConfigIncludesTheGroups() {
        assertTrue(new TwMergeConfig().getClassGroups().keySet().containsAll(EXPECTED_IDS));
    }

    @Test
    void baseClassNamesOfTheLogicalSizesAreShortened() {
        Map<String, ClassGroup> groups = sizing();

        assertEquals("size", groups.get("size").getBaseClassName());
        assertEquals("inline", groups.get("inline-size").getBaseClassName());
        assertEquals("min-inline", groups.get("min-inline-size").getBaseClassName());
        assertEquals("max-inline", groups.get("max-inline-size").getBaseClassName());
        assertEquals("block", groups.get("block-size").getBaseClassName());
        assertEquals("min-block", groups.get("min-block-size").getBaseClassName());
        assertEquals("max-block", groups.get("max-block-size").getBaseClassName());
    }

    @Test
    void sizeUsesTheSizingScaleAsIs() {
        assertEquals(Arrays.asList(DefaultConfigScales.SIZING_SCALE), sizing().get("size").getDefinitions());
    }

    @Test
    void inlineAndBlockSizesPrependTheirKeywordToTheScale() {
        Map<String, ClassGroup> groups = sizing();

        assertPrepended("auto", DefaultConfigScales.SIZING_INLINE_SCALE, groups.get("inline-size"));
        assertPrepended("auto", DefaultConfigScales.SIZING_INLINE_SCALE, groups.get("min-inline-size"));
        assertPrepended("none", DefaultConfigScales.SIZING_INLINE_SCALE, groups.get("max-inline-size"));
        assertPrepended("auto", DefaultConfigScales.SIZING_BLOCK_SCALE, groups.get("block-size"));
        assertPrepended("auto", DefaultConfigScales.SIZING_BLOCK_SCALE, groups.get("min-block-size"));
        assertPrepended("none", DefaultConfigScales.SIZING_BLOCK_SCALE, groups.get("max-block-size"));
    }

    private static void assertPrepended(String keyword, Object[] scale, ClassGroup group) {
        List<Object> expected = new ArrayList<>();
        expected.add(keyword);
        expected.addAll(Arrays.asList(scale));
        assertEquals(expected, group.getDefinitions());
    }

    @Test
    void widthGroupsKeepTheReferenceOrder() {
        Map<String, ClassGroup> groups = sizing();
        List<Object> sizingScale = Arrays.asList(DefaultConfigScales.SIZING_SCALE);

        List<Object> w = new ArrayList<>(Arrays.<Object>asList("screen", DefaultConfigScales.CONTAINER_THEME));
        w.addAll(sizingScale);
        assertEquals(w, groups.get("w").getDefinitions());

        List<Object> minW = new ArrayList<>(Arrays.<Object>asList("screen", DefaultConfigScales.CONTAINER_THEME, "none"));
        minW.addAll(sizingScale);
        assertEquals(minW, groups.get("min-w").getDefinitions());

        List<Object> maxW = groups.get("max-w").getDefinitions();
        assertEquals(Arrays.<Object>asList("screen", "none", DefaultConfigScales.CONTAINER_THEME, "prose"),
                maxW.subList(0, 4));
        ClassGroup screen = (ClassGroup) maxW.get(4);
        assertEquals("screen", screen.getBaseClassName());
        assertEquals(1, screen.getDefinitions().size());
        assertSame(DefaultConfigScales.BREAKPOINT_THEME, screen.getDefinitions().get(0));
        assertEquals(sizingScale, maxW.subList(5, maxW.size()));
    }

    @Test
    void heightGroupsKeepTheReferenceOrder() {
        Map<String, ClassGroup> groups = sizing();
        List<Object> sizingScale = Arrays.asList(DefaultConfigScales.SIZING_SCALE);

        List<Object> h = new ArrayList<>(Arrays.<Object>asList("screen", "lh"));
        h.addAll(sizingScale);
        assertEquals(h, groups.get("h").getDefinitions());

        List<Object> minH = new ArrayList<>(Arrays.<Object>asList("screen", "lh", "none"));
        minH.addAll(sizingScale);
        assertEquals(minH, groups.get("min-h").getDefinitions());
        assertEquals(minH, groups.get("max-h").getDefinitions());
    }
}
