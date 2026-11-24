package com.github.ddxailab.tailwindmerge;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotSame;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.github.ddxailab.tailwindmerge.common.ThemeKeys;
import com.github.ddxailab.tailwindmerge.common.Validators;
import java.util.Arrays;
import java.util.List;
import org.junit.jupiter.api.Test;

class TwMergeConfigTest {

    @Test
    void defaultsAreSet() {
        TwMergeConfig config = new TwMergeConfig();

        assertEquals(500, config.getCacheSize());
        assertNull(config.getPrefix());
        assertEquals(19, config.getTheme().size());
    }

    @Test
    void defaultConfigHasAllTheReferenceClassGroups() {
        // The reference initializes ClassGroups with 378 entries and none of its IDs is repeated.
        assertEquals(378, new TwMergeConfig().getClassGroups().size());
    }

    @Test
    void defaultConfigMatchesTheReferenceCounts() {
        // Counts of the C# TwMergeConfig constructor (step 4.14); a missing entry is a silent bug.
        TwMergeConfig config = new TwMergeConfig();

        assertEquals(500, config.getCacheSize());
        assertEquals(19, config.getTheme().size());
        assertEquals(378, config.getClassGroups().size());
        assertEquals(49, config.getConflictingClassGroups().size());
        assertEquals(1, config.getConflictingClassGroupModifiers().size());
        assertEquals(1, config.getPostfixLookupClassGroups().size());
        assertEquals(12, config.getOrderSensitiveModifiers().size());
    }

    @Test
    void defaultThemeHasTheReferenceScales() {
        TwMergeConfig config = new TwMergeConfig();

        assertEquals(Arrays.<Object>asList("spin", "ping", "pulse", "bounce"), config.getTheme().get(ThemeKeys.ANIMATE));
        assertEquals(Arrays.<Object>asList("video"), config.getTheme().get(ThemeKeys.ASPECT));
        assertEquals(Arrays.<Object>asList("px", Validators.IS_NUMBER), config.getTheme().get(ThemeKeys.SPACING));
        assertEquals(Arrays.<Object>asList(Validators.IS_ANY), config.getTheme().get(ThemeKeys.COLOR));
        assertEquals(Arrays.<Object>asList(Validators.IS_ANY_NON_ARBITRARY), config.getTheme().get(ThemeKeys.FONT));
        assertEquals(9, config.getTheme().get(ThemeKeys.FONT_WEIGHT).size());
        assertEquals(6, config.getTheme().get(ThemeKeys.LEADING).size());
        assertEquals(6, config.getTheme().get(ThemeKeys.PERSPECTIVE).size());
        assertEquals(6, config.getTheme().get(ThemeKeys.TRACKING).size());
    }

    @Test
    void everyConfigHasItsOwnMutableState() {
        TwMergeConfig first = new TwMergeConfig();
        TwMergeConfig second = new TwMergeConfig();

        first.getTheme().put("custom", Arrays.<Object>asList("x"));
        first.getOrderSensitiveModifiers().add("custom");
        first.setPrefix("tw");
        first.setCacheSize(10);

        assertNotSame(first.getTheme(), second.getTheme());
        assertEquals(19, second.getTheme().size());
        assertEquals(12, second.getOrderSensitiveModifiers().size());
        assertFalse(second.getOrderSensitiveModifiers().contains("custom"));
        assertNull(second.getPrefix());
        assertEquals(500, second.getCacheSize());
    }

    @Test
    void concatSpreadsArraysAndKeepsOtherValues() {
        Object[] result = DefaultConfigScales.concat("a", new Object[] {"b", "c"}, "d", new String[] {"e"});

        assertArrayEquals(new Object[] {"a", "b", "c", "d", "e"}, result);
    }

    @Test
    void derivedScalesSpreadTheirParts() {
        Object[] unambiguous = DefaultConfigScales.UNAMBIGUOUS_SPACING_SCALE;
        assertEquals(3, unambiguous.length);
        assertSame(DefaultConfigScales.SPACING_THEME, unambiguous[2]);

        assertEquals(3 + unambiguous.length, DefaultConfigScales.INSET_SCALE.length);
        assertEquals(1 + unambiguous.length, DefaultConfigScales.MARGIN_SCALE.length);
        assertEquals(12 + unambiguous.length, DefaultConfigScales.SIZING_SCALE.length);
        assertEquals(10 + unambiguous.length, DefaultConfigScales.SIZING_INLINE_SCALE.length);
        assertEquals(10 + unambiguous.length, DefaultConfigScales.SIZING_BLOCK_SCALE.length);
        assertEquals(2 + unambiguous.length, DefaultConfigScales.TRANSLATE_SCALE.length);
        assertEquals(
                DefaultConfigScales.POSITION_SCALE.length + 2, DefaultConfigScales.POSITION_WITH_ARBITRARY_SCALE.length);
        assertEquals(DefaultConfigScales.POSITION_SCALE.length + 3, DefaultConfigScales.BG_POSITION_SCALE.length);
    }

    @Test
    void plainScalesMatchTheReferenceSizes() {
        assertEquals(8, DefaultConfigScales.BREAK_SCALE.length);
        assertEquals(13, DefaultConfigScales.POSITION_SCALE.length);
        assertEquals(5, DefaultConfigScales.OVERFLOW_SCALE.length);
        assertEquals(3, DefaultConfigScales.OVERSCROLL_SCALE.length);
        assertEquals(10, DefaultConfigScales.ALIGN_PRIMARY_AXIS_SCALE.length);
        assertEquals(6, DefaultConfigScales.ALIGN_SECONDARY_AXIS_SCALE.length);
        assertEquals(4, DefaultConfigScales.LINE_STYLE_SCALE.length);
        assertEquals(16, DefaultConfigScales.BLEND_MODE_SCALE.length);
        assertEquals(5, DefaultConfigScales.GRID_TEMPLATE_COLS_ROWS_SCALE.length);
        assertEquals(5, DefaultConfigScales.GRID_COL_ROW_START_AND_END_SCALE.length);
        assertEquals(4, DefaultConfigScales.GRID_COL_ROW_START_OR_END_SCALE.length);
        assertEquals(6, DefaultConfigScales.GRID_AUTO_COLS_ROWS_SCALE.length);
        assertEquals(3, DefaultConfigScales.COLOR_SCALE.length);
        assertEquals(2, DefaultConfigScales.BG_REPEAT_SCALE.length);
        assertEquals(6, DefaultConfigScales.BG_SIZE_SCALE.length);
        assertEquals(3, DefaultConfigScales.GRADIENT_STOP_POSITION_SCALE.length);
        assertEquals(6, DefaultConfigScales.RADIUS_SCALE.length);
        assertEquals(4, DefaultConfigScales.BORDER_WIDTH_SCALE.length);
        assertEquals(4, DefaultConfigScales.MASK_IMAGE_POSITION_SCALE.length);
        assertEquals(5, DefaultConfigScales.BLUR_SCALE.length);
        assertEquals(4, DefaultConfigScales.ROTATE_SCALE.length);
        assertEquals(4, DefaultConfigScales.SCALE_SCALE.length);
        assertEquals(3, DefaultConfigScales.SKEW_SCALE.length);
        List<Object> positions = Arrays.asList(DefaultConfigScales.POSITION_SCALE);
        assertTrue(positions.contains("left-bottom"));
    }
}
