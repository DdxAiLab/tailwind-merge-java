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

class BackgroundsClassGroupsTest {

    private static final List<String> EXPECTED_IDS = Arrays.asList(
            "bg-attachment", "bg-clip", "bg-origin", "bg-position", "bg-repeat", "bg-size", "bg-image", "bg-color",
            "gradient-from-pos", "gradient-via-pos", "gradient-to-pos", "gradient-from", "gradient-via",
            "gradient-to");

    private static Map<String, ClassGroup> backgrounds() {
        Map<String, ClassGroup> groups = new LinkedHashMap<>();
        BackgroundsClassGroups.addTo(groups);
        return groups;
    }

    @Test
    void definesEveryGroupInReferenceOrder() {
        assertEquals(EXPECTED_IDS, new ArrayList<>(backgrounds().keySet()));
    }

    @Test
    void theDefaultConfigIncludesTheGroups() {
        assertTrue(new TwMergeConfig().getClassGroups().keySet().containsAll(EXPECTED_IDS));
    }

    @Test
    void backgroundGroupsShareTheBgBaseClassName() {
        Map<String, ClassGroup> groups = backgrounds();

        for (String id : Arrays.asList("bg-attachment", "bg-position", "bg-repeat", "bg-size", "bg-image",
                "bg-color")) {
            assertEquals("bg", groups.get(id).getBaseClassName(), id);
        }
        assertEquals("bg-clip", groups.get("bg-clip").getBaseClassName());
        assertEquals("bg-origin", groups.get("bg-origin").getBaseClassName());
    }

    @Test
    void gradientGroupsShareTheirBaseClassNames() {
        Map<String, ClassGroup> groups = backgrounds();

        assertEquals("from", groups.get("gradient-from").getBaseClassName());
        assertEquals("from", groups.get("gradient-from-pos").getBaseClassName());
        assertEquals("via", groups.get("gradient-via").getBaseClassName());
        assertEquals("via", groups.get("gradient-via-pos").getBaseClassName());
        assertEquals("to", groups.get("gradient-to").getBaseClassName());
        assertEquals("to", groups.get("gradient-to-pos").getBaseClassName());
    }

    @Test
    void scalesAreUsedAsTheDefinitions() {
        Map<String, ClassGroup> groups = backgrounds();

        assertEquals(Arrays.asList(DefaultConfigScales.BG_POSITION_SCALE), groups.get("bg-position").getDefinitions());
        assertEquals(Arrays.asList(DefaultConfigScales.BG_REPEAT_SCALE), groups.get("bg-repeat").getDefinitions());
        assertEquals(Arrays.asList(DefaultConfigScales.BG_SIZE_SCALE), groups.get("bg-size").getDefinitions());
        assertEquals(Arrays.asList(DefaultConfigScales.COLOR_SCALE), groups.get("bg-color").getDefinitions());
        for (String id : Arrays.asList("gradient-from-pos", "gradient-via-pos", "gradient-to-pos")) {
            assertEquals(Arrays.asList(DefaultConfigScales.GRADIENT_STOP_POSITION_SCALE),
                    groups.get(id).getDefinitions(), id);
        }
        for (String id : Arrays.asList("gradient-from", "gradient-via", "gradient-to")) {
            assertEquals(Arrays.asList(DefaultConfigScales.COLOR_SCALE), groups.get(id).getDefinitions(), id);
        }
    }

    @Test
    void bgImageNestsTheLinearRadialAndConicGroups() {
        List<Object> definitions = backgrounds().get("bg-image").getDefinitions();

        assertEquals(6, definitions.size());
        assertEquals("none", definitions.get(0));
        assertEquals(Validators.IS_ARBITRARY_IMAGE, definitions.get(4));
        assertEquals(Validators.IS_ARBITRARY_VARIABLE_IMAGE, definitions.get(5));

        ClassGroup linear = (ClassGroup) definitions.get(1);
        assertEquals("linear", linear.getBaseClassName());
        assertEquals(4, linear.getDefinitions().size());
        ClassGroup to = (ClassGroup) linear.getDefinitions().get(0);
        assertEquals("to", to.getBaseClassName());
        assertEquals(Arrays.<Object>asList("t", "tr", "r", "br", "b", "bl", "l", "tl"), to.getDefinitions());
        assertEquals(Arrays.<Object>asList(
                Validators.IS_INTEGER, Validators.IS_ARBITRARY_VALUE, Validators.IS_ARBITRARY_VARIABLE),
                linear.getDefinitions().subList(1, 4));

        ClassGroup radial = (ClassGroup) definitions.get(2);
        assertEquals("radial", radial.getBaseClassName());
        assertEquals(Arrays.<Object>asList("", Validators.IS_ARBITRARY_VALUE, Validators.IS_ARBITRARY_VARIABLE),
                radial.getDefinitions());

        ClassGroup conic = (ClassGroup) definitions.get(3);
        assertEquals("conic", conic.getBaseClassName());
        assertEquals(Arrays.<Object>asList(
                "", Validators.IS_INTEGER, Validators.IS_ARBITRARY_VALUE, Validators.IS_ARBITRARY_VARIABLE),
                conic.getDefinitions());
    }

    @Test
    void bgClipAndOriginDifferInTheirKeywords() {
        Map<String, ClassGroup> groups = backgrounds();

        assertEquals(Arrays.<Object>asList("border", "padding", "content", "text"),
                groups.get("bg-clip").getDefinitions());
        assertEquals(Arrays.<Object>asList("border", "padding", "content"), groups.get("bg-origin").getDefinitions());
    }
}
