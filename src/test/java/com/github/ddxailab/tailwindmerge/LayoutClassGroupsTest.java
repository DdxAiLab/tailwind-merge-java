package com.github.ddxailab.tailwindmerge;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.github.ddxailab.tailwindmerge.common.Validators;
import com.github.ddxailab.tailwindmerge.model.ClassGroup;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import org.junit.jupiter.api.Test;

class LayoutClassGroupsTest {

    private static final List<String> EXPECTED_IDS = Arrays.asList(
            "aspect", "container", "container-type", "container-named", "columns", "break-after", "break-before",
            "break-inside", "box-decoration", "box", "display", "sr", "float", "clear", "isolation", "object-fit",
            "object-position", "overflow", "overflow-x", "overflow-y", "overscroll", "overscroll-x", "overscroll-y",
            "position", "inset", "inset-x", "inset-y", "start", "end", "inset-bs", "inset-be", "top", "right",
            "bottom", "left", "visibility", "z");

    private static Map<String, ClassGroup> layout() {
        Map<String, ClassGroup> groups = new LinkedHashMap<>();
        LayoutClassGroups.addTo(groups);
        return groups;
    }

    @Test
    void definesEveryLayoutGroupInReferenceOrder() {
        assertEquals(EXPECTED_IDS, new ArrayList<>(layout().keySet()));
    }

    @Test
    void theDefaultConfigIncludesTheLayoutGroups() {
        assertTrue(new TwMergeConfig().getClassGroups().keySet().containsAll(EXPECTED_IDS));
    }

    @Test
    void groupsWithoutBaseClassNameAreStandalone() {
        Map<String, ClassGroup> groups = layout();

        assertNull(groups.get("container").getBaseClassName());
        assertEquals(Arrays.<Object>asList("container"), groups.get("container").getDefinitions());
        assertNull(groups.get("sr").getBaseClassName());
        assertEquals(Arrays.<Object>asList("sr-only", "not-sr-only"), groups.get("sr").getDefinitions());
        assertEquals(21, groups.get("display").getDefinitions().size());
        assertEquals(Arrays.<Object>asList(Validators.IS_NAMED_CONTAINER_QUERY),
                groups.get("container-named").getDefinitions());
    }

    @Test
    void groupsKeepTheBaseClassNameOfTheReference() {
        Map<String, ClassGroup> groups = layout();

        assertEquals("object", groups.get("object-fit").getBaseClassName());
        assertEquals("object", groups.get("object-position").getBaseClassName());
        assertEquals("@container", groups.get("container-type").getBaseClassName());
    }

    @Test
    void scalesAreSpreadIntoTheDefinitions() {
        Map<String, ClassGroup> groups = layout();

        assertEquals(Arrays.asList(DefaultConfigScales.INSET_SCALE), groups.get("top").getDefinitions());
        assertEquals(Arrays.asList(DefaultConfigScales.OVERFLOW_SCALE), groups.get("overflow-y").getDefinitions());
        assertEquals(Arrays.asList(DefaultConfigScales.BREAK_SCALE), groups.get("break-before").getDefinitions());
        assertEquals(Arrays.asList(DefaultConfigScales.POSITION_WITH_ARBITRARY_SCALE),
                groups.get("object-position").getDefinitions());
    }

    @Test
    void startAndEndNestTheCurrentAndTheDeprecatedGroup() {
        Map<String, ClassGroup> groups = layout();

        List<Object> start = groups.get("start").getDefinitions();
        assertEquals(2, start.size());
        assertEquals("inset-s", ((ClassGroup) start.get(0)).getBaseClassName());
        assertEquals("start", ((ClassGroup) start.get(1)).getBaseClassName());

        List<Object> end = groups.get("end").getDefinitions();
        assertEquals(2, end.size());
        assertEquals("inset-e", ((ClassGroup) end.get(0)).getBaseClassName());
        assertEquals("end", ((ClassGroup) end.get(1)).getBaseClassName());
    }

    @Test
    void aspectUsesTheAspectThemeGetter() {
        List<Object> definitions = layout().get("aspect").getDefinitions();

        assertEquals(Arrays.<Object>asList(
                "auto", "square", DefaultConfigScales.ASPECT_THEME, Validators.IS_FRACTION,
                Validators.IS_ARBITRARY_VALUE, Validators.IS_ARBITRARY_VARIABLE), definitions);
        assertSame(DefaultConfigScales.ASPECT_THEME, definitions.get(2));
    }
}
