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

class EffectsClassGroupsTest {

    private static final List<String> EXPECTED_IDS = Arrays.asList(
            "shadow", "shadow-color", "inset-shadow", "inset-shadow-color", "ring-w", "ring-w-inset", "ring-color",
            "ring-offset-w", "ring-offset-color", "inset-ring-w", "inset-ring-color", "text-shadow",
            "text-shadow-color", "opacity", "mix-blend", "bg-blend", "mask-clip", "mask-composite",
            "mask-image-linear-pos", "mask-image-linear-from-pos", "mask-image-linear-to-pos",
            "mask-image-linear-from-color", "mask-image-linear-to-color", "mask-image-t-from-pos",
            "mask-image-t-to-pos", "mask-image-t-from-color", "mask-image-t-to-color", "mask-image-r-from-pos",
            "mask-image-r-to-pos", "mask-image-r-from-color", "mask-image-r-to-color", "mask-image-b-from-pos",
            "mask-image-b-to-pos", "mask-image-b-from-color", "mask-image-b-to-color", "mask-image-l-from-pos",
            "mask-image-l-to-pos", "mask-image-l-from-color", "mask-image-l-to-color", "mask-image-x-from-pos",
            "mask-image-x-to-pos", "mask-image-x-from-color", "mask-image-x-to-color", "mask-image-y-from-pos",
            "mask-image-y-to-pos", "mask-image-y-from-color", "mask-image-y-to-color", "mask-image-radial",
            "mask-image-radial-from-pos", "mask-image-radial-to-pos", "mask-image-radial-from-color",
            "mask-image-radial-to-color", "mask-image-radial-shape", "mask-image-radial-size",
            "mask-image-radial-pos", "mask-image-conic-pos", "mask-image-conic-from-pos", "mask-image-conic-to-pos",
            "mask-image-conic-from-color", "mask-image-conic-to-color", "mask-mode", "mask-origin", "mask-position",
            "mask-repeat", "mask-size", "mask-type", "mask-image");

    private static Map<String, ClassGroup> effects() {
        Map<String, ClassGroup> groups = new LinkedHashMap<>();
        EffectsClassGroups.addTo(groups);
        return groups;
    }

    @Test
    void definesEveryGroupInReferenceOrder() {
        assertEquals(67, EXPECTED_IDS.size());
        assertEquals(EXPECTED_IDS, new ArrayList<>(effects().keySet()));
    }

    @Test
    void theDefaultConfigIncludesTheGroups() {
        assertTrue(new TwMergeConfig().getClassGroups().keySet().containsAll(EXPECTED_IDS));
    }

    @Test
    void shadowKeepsTheDeprecatedEntriesFirst() {
        List<Object> definitions = effects().get("shadow").getDefinitions();

        assertEquals(Arrays.<Object>asList(
                "", "inner", "none", DefaultConfigScales.SHADOW_THEME, Validators.IS_ARBITRARY_SHADOW,
                Validators.IS_ARBITRARY_VARIABLE_SHADOW), definitions);
        assertEquals("shadow", effects().get("shadow").getBaseClassName());
        assertEquals("shadow", effects().get("shadow-color").getBaseClassName());
    }

    @Test
    void shadowGroupsUseTheirThemeGetters() {
        Map<String, ClassGroup> groups = effects();

        assertEquals(Arrays.<Object>asList(
                "none", DefaultConfigScales.INSET_SHADOW_THEME, Validators.IS_ARBITRARY_SHADOW,
                Validators.IS_ARBITRARY_VARIABLE_SHADOW), groups.get("inset-shadow").getDefinitions());
        assertEquals(Arrays.<Object>asList(
                "none", DefaultConfigScales.TEXT_SHADOW_THEME, Validators.IS_ARBITRARY_SHADOW,
                Validators.IS_ARBITRARY_VARIABLE_SHADOW), groups.get("text-shadow").getDefinitions());
        assertEquals("inset-shadow", groups.get("inset-shadow-color").getBaseClassName());
        assertEquals("text-shadow", groups.get("text-shadow-color").getBaseClassName());
    }

    @Test
    void ringGroups() {
        Map<String, ClassGroup> groups = effects();

        assertEquals("ring", groups.get("ring-w").getBaseClassName());
        assertEquals(Arrays.asList(DefaultConfigScales.BORDER_WIDTH_SCALE), groups.get("ring-w").getDefinitions());
        assertNull(groups.get("ring-w-inset").getBaseClassName());
        assertEquals(Arrays.<Object>asList("ring-inset"), groups.get("ring-w-inset").getDefinitions());
        assertEquals("ring", groups.get("ring-color").getBaseClassName());
        assertEquals("ring-offset", groups.get("ring-offset-w").getBaseClassName());
        assertEquals(Arrays.<Object>asList(Validators.IS_NUMBER, Validators.IS_ARBITRARY_LENGTH),
                groups.get("ring-offset-w").getDefinitions());
        assertEquals("ring-offset", groups.get("ring-offset-color").getBaseClassName());
        assertEquals("inset-ring", groups.get("inset-ring-w").getBaseClassName());
        assertEquals("inset-ring", groups.get("inset-ring-color").getBaseClassName());
    }

    @Test
    void blendModeGroups() {
        Map<String, ClassGroup> groups = effects();
        List<Object> blendModes = Arrays.asList(DefaultConfigScales.BLEND_MODE_SCALE);

        List<Object> mix = new ArrayList<>(Arrays.<Object>asList("plus-darker", "plus-lighter"));
        mix.addAll(blendModes);
        assertEquals(mix, groups.get("mix-blend").getDefinitions());
        assertEquals(blendModes, groups.get("bg-blend").getDefinitions());
    }

    @Test
    void maskClipNestsTheClipGroup() {
        List<Object> definitions = effects().get("mask-clip").getDefinitions();

        assertEquals("mask", effects().get("mask-clip").getBaseClassName());
        assertEquals("no-clip", definitions.get(0));
        ClassGroup clip = (ClassGroup) definitions.get(1);
        assertEquals("clip", clip.getBaseClassName());
        assertEquals(Arrays.<Object>asList("border", "padding", "content", "fill", "stroke", "view"),
                clip.getDefinitions());
    }

    @Test
    void maskImageDirectionalGroupsUsePositionAndColorScales() {
        Map<String, ClassGroup> groups = effects();

        for (String dir : Arrays.asList("linear", "t", "r", "b", "l", "x", "y", "radial", "conic")) {
            for (String edge : Arrays.asList("from", "to")) {
                String base = "mask-" + dir + "-" + edge;
                ClassGroup pos = groups.get("mask-image-" + dir + "-" + edge + "-pos");
                ClassGroup color = groups.get("mask-image-" + dir + "-" + edge + "-color");

                assertEquals(base, pos.getBaseClassName(), base);
                assertEquals(Arrays.asList(DefaultConfigScales.MASK_IMAGE_POSITION_SCALE), pos.getDefinitions(), base);
                assertEquals(base, color.getBaseClassName(), base);
                assertEquals(Arrays.asList(DefaultConfigScales.COLOR_SCALE), color.getDefinitions(), base);
            }
        }
    }

    @Test
    void maskImageAngleAndRadialGroups() {
        Map<String, ClassGroup> groups = effects();

        assertEquals("mask-linear", groups.get("mask-image-linear-pos").getBaseClassName());
        assertEquals(Arrays.<Object>asList(Validators.IS_NUMBER), groups.get("mask-image-linear-pos").getDefinitions());
        assertEquals("mask-conic", groups.get("mask-image-conic-pos").getBaseClassName());
        assertEquals(Arrays.<Object>asList(Validators.IS_NUMBER), groups.get("mask-image-conic-pos").getDefinitions());

        assertEquals("mask-radial", groups.get("mask-image-radial").getBaseClassName());
        assertEquals(Arrays.<Object>asList(Validators.IS_ARBITRARY_VALUE, Validators.IS_ARBITRARY_VARIABLE),
                groups.get("mask-image-radial").getDefinitions());
        assertEquals("mask-radial", groups.get("mask-image-radial-shape").getBaseClassName());
        assertEquals(Arrays.<Object>asList("circle", "ellipse"), groups.get("mask-image-radial-shape").getDefinitions());
        assertEquals("mask-radial-at", groups.get("mask-image-radial-pos").getBaseClassName());
        assertEquals(Arrays.asList(DefaultConfigScales.POSITION_SCALE),
                groups.get("mask-image-radial-pos").getDefinitions());

        List<Object> size = groups.get("mask-image-radial-size").getDefinitions();
        assertEquals("mask-radial", groups.get("mask-image-radial-size").getBaseClassName());
        assertEquals(2, size.size());
        assertEquals("closest", ((ClassGroup) size.get(0)).getBaseClassName());
        assertEquals("farthest", ((ClassGroup) size.get(1)).getBaseClassName());
        assertEquals(Arrays.<Object>asList("side", "corner"), ((ClassGroup) size.get(0)).getDefinitions());
        assertEquals(Arrays.<Object>asList("side", "corner"), ((ClassGroup) size.get(1)).getDefinitions());
    }

    @Test
    void remainingMaskGroups() {
        Map<String, ClassGroup> groups = effects();

        assertEquals("mask", groups.get("mask-composite").getBaseClassName());
        assertEquals(Arrays.<Object>asList("add", "subtract", "intersect", "exclude"),
                groups.get("mask-composite").getDefinitions());
        assertEquals(Arrays.<Object>asList("alpha", "luminance", "match"), groups.get("mask-mode").getDefinitions());
        assertEquals("mask-origin", groups.get("mask-origin").getBaseClassName());
        assertEquals(6, groups.get("mask-origin").getDefinitions().size());
        assertEquals(Arrays.asList(DefaultConfigScales.BG_POSITION_SCALE), groups.get("mask-position").getDefinitions());
        assertEquals(Arrays.asList(DefaultConfigScales.BG_REPEAT_SCALE), groups.get("mask-repeat").getDefinitions());
        assertEquals(Arrays.asList(DefaultConfigScales.BG_SIZE_SCALE), groups.get("mask-size").getDefinitions());
        assertEquals("mask-type", groups.get("mask-type").getBaseClassName());
        assertEquals(Arrays.<Object>asList("alpha", "luminance"), groups.get("mask-type").getDefinitions());
        assertEquals(Arrays.<Object>asList("none", Validators.IS_ARBITRARY_VALUE, Validators.IS_ARBITRARY_VARIABLE),
                groups.get("mask-image").getDefinitions());
    }

    @Test
    void opacityAcceptsNumbersAndArbitraryValues() {
        assertEquals("opacity", effects().get("opacity").getBaseClassName());
        assertEquals(Arrays.<Object>asList(
                Validators.IS_NUMBER, Validators.IS_ARBITRARY_VALUE, Validators.IS_ARBITRARY_VARIABLE),
                effects().get("opacity").getDefinitions());
    }
}
