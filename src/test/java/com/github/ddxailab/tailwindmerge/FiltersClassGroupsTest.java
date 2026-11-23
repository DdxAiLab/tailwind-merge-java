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

class FiltersClassGroupsTest {

    private static final List<String> EXPECTED_IDS = Arrays.asList(
            "filter", "blur", "brightness", "contrast", "drop-shadow", "drop-shadow-color", "grayscale",
            "hue-rotate", "invert", "saturate", "sepia", "backdrop-filter", "backdrop-blur", "backdrop-brightness",
            "backdrop-contrast", "backdrop-grayscale", "backdrop-hue-rotate", "backdrop-invert",
            "backdrop-opacity", "backdrop-saturate", "backdrop-sepia");

    /** Groups whose definitions are just {@code number, arbitrary value, arbitrary variable}. */
    private static final List<String> NUMBER_IDS = Arrays.asList(
            "brightness", "contrast", "hue-rotate", "saturate", "backdrop-brightness", "backdrop-contrast",
            "backdrop-hue-rotate", "backdrop-opacity", "backdrop-saturate");

    /** Same as above with a leading empty string (the bare utility, e.g. {@code grayscale}). */
    private static final List<String> EMPTY_AND_NUMBER_IDS = Arrays.asList(
            "grayscale", "invert", "sepia", "backdrop-grayscale", "backdrop-invert", "backdrop-sepia");

    private static Map<String, ClassGroup> filters() {
        Map<String, ClassGroup> groups = new LinkedHashMap<>();
        FiltersClassGroups.addTo(groups);
        return groups;
    }

    @Test
    void definesEveryGroupInReferenceOrder() {
        assertEquals(EXPECTED_IDS, new ArrayList<>(filters().keySet()));
    }

    @Test
    void theDefaultConfigIncludesTheGroups() {
        assertTrue(new TwMergeConfig().getClassGroups().keySet().containsAll(EXPECTED_IDS));
    }

    @Test
    void everyGroupUsesItsOwnIdAsBaseClassNameExceptDropShadowColor() {
        Map<String, ClassGroup> groups = filters();

        for (String id : EXPECTED_IDS) {
            String expected = id.equals("drop-shadow-color") ? "drop-shadow" : id;
            assertEquals(expected, groups.get(id).getBaseClassName(), id);
        }
    }

    @Test
    void numberGroupsAcceptNumbersAndArbitraryValues() {
        Map<String, ClassGroup> groups = filters();

        for (String id : NUMBER_IDS) {
            assertEquals(Arrays.<Object>asList(
                    Validators.IS_NUMBER, Validators.IS_ARBITRARY_VALUE, Validators.IS_ARBITRARY_VARIABLE),
                    groups.get(id).getDefinitions(), id);
        }
    }

    @Test
    void groupsWithABareUtilityAcceptTheEmptyClassName() {
        Map<String, ClassGroup> groups = filters();

        for (String id : EMPTY_AND_NUMBER_IDS) {
            assertEquals(Arrays.<Object>asList(
                    "", Validators.IS_NUMBER, Validators.IS_ARBITRARY_VALUE, Validators.IS_ARBITRARY_VARIABLE),
                    groups.get(id).getDefinitions(), id);
        }
    }

    @Test
    void blurGroupsUseTheBlurScale() {
        Map<String, ClassGroup> groups = filters();

        assertEquals(Arrays.asList(DefaultConfigScales.BLUR_SCALE), groups.get("blur").getDefinitions());
        assertEquals(Arrays.asList(DefaultConfigScales.BLUR_SCALE), groups.get("backdrop-blur").getDefinitions());
    }

    @Test
    void filterGroupsAcceptNoneAndArbitraryValues() {
        Map<String, ClassGroup> groups = filters();

        for (String id : Arrays.asList("filter", "backdrop-filter")) {
            assertEquals(Arrays.<Object>asList(
                    "none", Validators.IS_ARBITRARY_VALUE, Validators.IS_ARBITRARY_VARIABLE),
                    groups.get(id).getDefinitions(), id);
        }
    }

    @Test
    void dropShadowGroups() {
        Map<String, ClassGroup> groups = filters();

        assertEquals(Arrays.<Object>asList(
                "", "none", DefaultConfigScales.DROP_SHADOW_THEME, Validators.IS_ARBITRARY_SHADOW,
                Validators.IS_ARBITRARY_VARIABLE_SHADOW), groups.get("drop-shadow").getDefinitions());
        assertEquals(Arrays.asList(DefaultConfigScales.COLOR_SCALE), groups.get("drop-shadow-color").getDefinitions());
    }

    @Test
    void everyGroupIsCoveredByAShapeAssertion() {
        List<String> covered = new ArrayList<>(NUMBER_IDS);
        covered.addAll(EMPTY_AND_NUMBER_IDS);
        covered.addAll(Arrays.asList("filter", "backdrop-filter", "blur", "backdrop-blur", "drop-shadow",
                "drop-shadow-color"));

        List<String> sortedCovered = new ArrayList<>(covered);
        List<String> sortedExpected = new ArrayList<>(EXPECTED_IDS);
        sortedCovered.sort(null);
        sortedExpected.sort(null);
        assertEquals(sortedExpected, sortedCovered);
    }
}
