package com.github.ddxailab.tailwindmerge;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.github.ddxailab.tailwindmerge.model.ClassGroup;
import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.function.Predicate;
import org.junit.jupiter.api.Test;

class TwMergeContextTest {

    private final TwMergeContext context = new TwMergeContext(new TwMergeConfig());

    // ---- getClassGroupId ----

    @Test
    void getClassGroupIdFindsClassGroups() {
        assertEquals("display", context.getClassGroupId("block"));
        assertEquals("p", context.getClassGroupId("p-4"));
        assertEquals("font-size", context.getClassGroupId("text-lg"));
        assertEquals("bg-color", context.getClassGroupId("bg-red-500"));
        assertEquals("border-w-t", context.getClassGroupId("border-t-2"));
        assertEquals("w", context.getClassGroupId("w-1/2"));
    }

    @Test
    void getClassGroupIdReturnsNullForUnknownClasses() {
        assertNull(context.getClassGroupId("foo"));
        assertNull(context.getClassGroupId("foo-bar"));
        assertNull(context.getClassGroupId("p"));
        assertNull(context.getClassGroupId(""));
        assertNull(context.getClassGroupId("-"));
    }

    @Test
    void getClassGroupIdIgnoresTheEmptyPartOfNegativeValues() {
        assertEquals("inset", context.getClassGroupId("-inset-1"));
        assertEquals("inset-x", context.getClassGroupId("-inset-x-px"));
        assertEquals("m", context.getClassGroupId("-m-4"));
    }

    @Test
    void getClassGroupIdResolvesArbitraryProperties() {
        assertEquals("arbitrary..color", context.getClassGroupId("[color:red]"));
        assertEquals("arbitrary..--my-var", context.getClassGroupId("[--my-var:20px]"));
        assertEquals("arbitrary..mask-type", context.getClassGroupId("[mask-type:luminance]"));
    }

    @Test
    void getClassGroupIdReturnsNullForBracketsWithoutProperty() {
        // The C# implementation throws on these; the context answers "no class group"
        assertNull(context.getClassGroupId("[foo]"));
        assertNull(context.getClassGroupId("[]"));
        assertNull(context.getClassGroupId("[color:red"));
    }

    @Test
    void getClassGroupIdPrefersTheClassMapOverArbitraryProperties() {
        assertEquals("bg-color", context.getClassGroupId("bg-[#fff]"));
        assertEquals("w", context.getClassGroupId("w-[10px]"));
    }

    @Test
    void getClassGroupIdBacktracksFromNestedNodesToValidators() {
        // "border-" leads to nested nodes, but "border-red-500" is only matched by a validator
        assertEquals("border-color", context.getClassGroupId("border-red-500"));
        assertEquals("border-w", context.getClassGroupId("border"));
    }

    @Test
    void getClassGroupIdUsesTheFirstMatchingValidator() {
        TwMergeConfig config = emptyConfig();
        config.getClassGroups().put("first", ClassGroup.withBase("foo", (Predicate<String>) v -> v.length() > 2));
        config.getClassGroups().put("second", ClassGroup.withBase("foo", (Predicate<String>) v -> true));

        TwMergeContext custom = new TwMergeContext(config);

        assertEquals("first", custom.getClassGroupId("foo-abc"));
        assertEquals("second", custom.getClassGroupId("foo-a"));
    }

    @Test
    void getClassGroupIdPassesTheRestOfTheClassNameToValidators() {
        TwMergeConfig config = emptyConfig();
        config.getClassGroups().put("named", ClassGroup.withBase("foo", (Predicate<String>) v -> v.equals("bar-baz-")));

        TwMergeContext custom = new TwMergeContext(config);

        assertEquals("named", custom.getClassGroupId("foo-bar-baz-"));
        assertNull(custom.getClassGroupId("foo-bar-baz"));
    }

    // ---- getConflictingClassGroupIds / isPostfixLookupClassGroup ----

    @Test
    void getConflictingClassGroupIdsReturnsTheConfiguredConflicts() {
        assertEquals(Arrays.asList("inset-x", "inset-y", "inset-bs", "inset-be", "start", "end", "top", "right",
                "bottom", "left"), context.getConflictingClassGroupIds("inset", false));
        assertEquals(Collections.singletonList("leading"), context.getConflictingClassGroupIds("font-size", false));
    }

    @Test
    void getConflictingClassGroupIdsIsEmptyWithoutConflicts() {
        assertTrue(context.getConflictingClassGroupIds("display", false).isEmpty());
        assertTrue(context.getConflictingClassGroupIds("display", true).isEmpty());
        assertTrue(context.getConflictingClassGroupIds("unknown", true).isEmpty());
    }

    @Test
    void getConflictingClassGroupIdsAddsTheModifierConflictsOnlyWithPostfixModifier() {
        TwMergeConfig config = emptyConfig();
        config.getConflictingClassGroups().put("a", Arrays.asList("b"));
        config.getConflictingClassGroupModifiers().put("a", Arrays.asList("c"));
        config.getConflictingClassGroupModifiers().put("d", Arrays.asList("e"));
        TwMergeContext custom = new TwMergeContext(config);

        assertEquals(Arrays.asList("b"), custom.getConflictingClassGroupIds("a", false));
        assertEquals(Arrays.asList("b", "c"), custom.getConflictingClassGroupIds("a", true));
        // modifier conflicts without regular conflicts (a null spread in the C# code)
        assertEquals(Arrays.asList("e"), custom.getConflictingClassGroupIds("d", true));
        assertTrue(custom.getConflictingClassGroupIds("d", false).isEmpty());
    }

    @Test
    void isPostfixLookupClassGroupUsesTheConfiguredGroups() {
        assertTrue(context.isPostfixLookupClassGroup("container-type"));
        assertFalse(context.isPostfixLookupClassGroup("font-size"));
    }

    @Test
    void contextIgnoresChangesMadeToTheConfigurationAfterwards() {
        TwMergeConfig config = new TwMergeConfig();
        TwMergeContext custom = new TwMergeContext(config);

        config.getConflictingClassGroups().put("display", Arrays.asList("p"));
        config.getPostfixLookupClassGroups().add("display");
        config.getOrderSensitiveModifiers().add("hover");
        config.getClassGroups().remove("display");

        assertTrue(custom.getConflictingClassGroupIds("display", false).isEmpty());
        assertFalse(custom.isPostfixLookupClassGroup("display"));
        assertEquals(Arrays.asList("focus", "hover"), custom.sortModifiers(Arrays.asList("hover", "focus")));
        assertEquals("display", custom.getClassGroupId("block"));
    }

    // ---- splitModifiers ----

    @Test
    void splitModifiersWithoutModifiers() {
        ClassModifiersInfo info = context.splitModifiers("bg-red-500");

        assertEquals(Collections.<String>emptyList(), info.getModifiers());
        assertFalse(info.hasImportantModifier());
        assertEquals("bg-red-500", info.getBaseClassName());
        assertNull(info.getPostfixModifierPosition());
        assertFalse(info.isExternal());
    }

    @Test
    void splitModifiersSplitsEveryVariant() {
        ClassModifiersInfo info = context.splitModifiers("dark:lg:hover:underline");

        assertEquals(Arrays.asList("dark", "lg", "hover"), info.getModifiers());
        assertEquals("underline", info.getBaseClassName());
    }

    @Test
    void splitModifiersDetectsTheImportantModifierAtTheEnd() {
        ClassModifiersInfo info = context.splitModifiers("focus:block!");

        assertEquals(Arrays.asList("focus"), info.getModifiers());
        assertTrue(info.hasImportantModifier());
        assertEquals("block", info.getBaseClassName());
    }

    @Test
    void splitModifiersSupportsTheLegacyImportantModifierAtTheStart() {
        ClassModifiersInfo info = context.splitModifiers("focus:!block");

        assertTrue(info.hasImportantModifier());
        assertEquals("block", info.getBaseClassName());

        ClassModifiersInfo withoutVariant = context.splitModifiers("!-inset-x-px");
        assertTrue(withoutVariant.hasImportantModifier());
        assertEquals("-inset-x-px", withoutVariant.getBaseClassName());
    }

    @Test
    void splitModifiersPrefersTheTrailingImportantModifier() {
        ClassModifiersInfo info = context.splitModifiers("!block!");

        assertTrue(info.hasImportantModifier());
        assertEquals("!block", info.getBaseClassName());
    }

    @Test
    void splitModifiersKeepsTheImportantModifierOfArbitraryProperties() {
        ClassModifiersInfo trailing = context.splitModifiers("[--my-var:20px]!");
        ClassModifiersInfo leading = context.splitModifiers("![--my-var:20px]");

        assertTrue(trailing.hasImportantModifier());
        assertEquals("[--my-var:20px]", trailing.getBaseClassName());
        assertTrue(leading.hasImportantModifier());
        assertEquals("[--my-var:20px]", leading.getBaseClassName());
    }

    @Test
    void splitModifiersLocatesThePostfixModifier() {
        ClassModifiersInfo info = context.splitModifiers("hover:text-lg/7");

        assertEquals(Arrays.asList("hover"), info.getModifiers());
        assertEquals("text-lg/7", info.getBaseClassName());
        // relative to the base class name
        assertEquals(Integer.valueOf(7), info.getPostfixModifierPosition());
    }

    @Test
    void splitModifiersUsesTheLastSlash() {
        ClassModifiersInfo info = context.splitModifiers("w-1/2/3");

        assertEquals(Integer.valueOf(5), info.getPostfixModifierPosition());
    }

    @Test
    void splitModifiersIgnoresASlashBeforeTheLastVariant() {
        ClassModifiersInfo info = context.splitModifiers("group/name:block");

        assertEquals(Arrays.asList("group/name"), info.getModifiers());
        assertEquals("block", info.getBaseClassName());
        assertNull(info.getPostfixModifierPosition());
    }

    @Test
    void splitModifiersIgnoresASlashAtTheStartOfTheBaseClassName() {
        assertNull(context.splitModifiers("/foo").getPostfixModifierPosition());
        assertNull(context.splitModifiers("hover:/foo").getPostfixModifierPosition());
    }

    @Test
    void splitModifiersDoesNotSplitInsideBrackets() {
        ClassModifiersInfo info = context.splitModifiers("hover:[&>*]:underline");
        assertEquals(Arrays.asList("hover", "[&>*]"), info.getModifiers());
        assertEquals("underline", info.getBaseClassName());

        ClassModifiersInfo complex = context.splitModifiers("[@media_screen{@media(hover:hover)}]:underline");
        assertEquals(Arrays.asList("[@media_screen{@media(hover:hover)}]"), complex.getModifiers());

        ClassModifiersInfo supports = context.splitModifiers("supports-[display:grid]:flex");
        assertEquals(Arrays.asList("supports-[display:grid]"), supports.getModifiers());
        assertEquals("flex", supports.getBaseClassName());

        ClassModifiersInfo property = context.splitModifiers("hover:[color:red]");
        assertEquals(Arrays.asList("hover"), property.getModifiers());
        assertEquals("[color:red]", property.getBaseClassName());
    }

    @Test
    void splitModifiersDoesNotSplitInsideParentheses() {
        ClassModifiersInfo info = context.splitModifiers("bg-(--my-color:red):block");
        assertEquals(Arrays.asList("bg-(--my-color:red)"), info.getModifiers());

        ClassModifiersInfo slash = context.splitModifiers("w-(--a/b)");
        assertNull(slash.getPostfixModifierPosition());
        assertEquals("w-(--a/b)", slash.getBaseClassName());
    }

    @Test
    void splitModifiersDoesNotTreatASlashInsideBracketsAsPostfix() {
        ClassModifiersInfo info = context.splitModifiers("bg-[url(/img.png)]");

        assertNull(info.getPostfixModifierPosition());
    }

    @Test
    void splitModifiersWithEmptyClassName() {
        ClassModifiersInfo info = context.splitModifiers("");

        assertEquals("", info.getBaseClassName());
        assertFalse(info.hasImportantModifier());
    }

    @Test
    void splitModifiersWithPrefix() {
        TwMergeConfig config = new TwMergeConfig();
        config.setPrefix("tw");
        TwMergeContext prefixed = new TwMergeContext(config);

        ClassModifiersInfo info = prefixed.splitModifiers("tw:hover:text-lg/7!");

        assertFalse(info.isExternal());
        assertEquals(Arrays.asList("hover"), info.getModifiers());
        assertTrue(info.hasImportantModifier());
        assertEquals("text-lg/7", info.getBaseClassName());
        assertEquals(Integer.valueOf(7), info.getPostfixModifierPosition());
    }

    @Test
    void splitModifiersMarksClassesWithoutPrefixAsExternal() {
        TwMergeConfig config = new TwMergeConfig();
        config.setPrefix("tw");
        TwMergeContext prefixed = new TwMergeContext(config);

        for (String className : new String[] {"hover:block", "tw-block", "twblock", "block!"}) {
            ClassModifiersInfo info = prefixed.splitModifiers(className);

            assertTrue(info.isExternal(), className);
            assertEquals(className, info.getBaseClassName());
            assertTrue(info.getModifiers().isEmpty());
            assertFalse(info.hasImportantModifier());
            assertNull(info.getPostfixModifierPosition());
        }
    }

    @Test
    void splitModifiersIgnoresAnEmptyPrefix() {
        TwMergeConfig config = new TwMergeConfig();
        config.setPrefix("");
        TwMergeContext custom = new TwMergeContext(config);

        assertFalse(custom.splitModifiers("block").isExternal());
    }

    // ---- sortModifiers ----

    @Test
    void sortModifiersReturnsShortListsUnchanged() {
        assertEquals(Collections.<String>emptyList(), context.sortModifiers(Collections.<String>emptyList()));
        assertEquals(Arrays.asList("hover"), context.sortModifiers(Arrays.asList("hover")));
    }

    @Test
    void sortModifiersSortsOrderInsensitiveModifiers() {
        assertEquals(Arrays.asList("dark", "hover", "lg"), context.sortModifiers(Arrays.asList("lg", "hover", "dark")));
        assertEquals(context.sortModifiers(Arrays.asList("hover", "focus")),
                context.sortModifiers(Arrays.asList("focus", "hover")));
    }

    @Test
    void sortModifiersKeepsOrderSensitiveModifiersInPlace() {
        // "before" and "after" are order sensitive
        assertEquals(Arrays.asList("before", "after"), context.sortModifiers(Arrays.asList("before", "after")));
        assertEquals(Arrays.asList("after", "before"), context.sortModifiers(Arrays.asList("after", "before")));
        // only the segments between them are sorted
        assertEquals(Arrays.asList("c", "d", "before", "x", "y"),
                context.sortModifiers(Arrays.asList("d", "c", "before", "y", "x")));
        assertEquals(Arrays.asList("c", "d", "e"), context.sortModifiers(Arrays.asList("d", "c", "e")));
        assertEquals(Arrays.asList("x", "y", "*", "z"), context.sortModifiers(Arrays.asList("y", "x", "*", "z")));
    }

    @Test
    void sortModifiersKeepsArbitraryVariantsInPlace() {
        assertEquals(Arrays.asList("dark", "hover", "[&>*]", "lg"),
                context.sortModifiers(Arrays.asList("hover", "dark", "[&>*]", "lg")));
        assertEquals(Arrays.asList("[&>*]", "dark", "hover"),
                context.sortModifiers(Arrays.asList("[&>*]", "hover", "dark")));
        assertEquals(Arrays.asList("[b]", "[a]"), context.sortModifiers(Arrays.asList("[b]", "[a]")));
    }

    @Test
    void sortModifiersDoesNotModifyItsArgument() {
        List<String> modifiers = Arrays.asList("lg", "hover", "dark");

        context.sortModifiers(modifiers);

        assertEquals(Arrays.asList("lg", "hover", "dark"), modifiers);
    }

    @Test
    void sortModifiersUsesTheConfiguredOrderSensitiveModifiers() {
        TwMergeConfig config = new TwMergeConfig();
        config.getOrderSensitiveModifiers().add("a");
        TwMergeContext custom = new TwMergeContext(config);

        assertEquals(Arrays.asList("c", "a", "b"), custom.sortModifiers(Arrays.asList("c", "a", "b")));

        TwMergeConfig overridden = new TwMergeConfig();
        overridden.setOrderSensitiveModifiers(new LinkedHashSet<String>(Arrays.asList("a")));
        TwMergeContext replaced = new TwMergeContext(overridden);

        // "before" is no longer order sensitive
        assertEquals(Arrays.asList("after", "before"), replaced.sortModifiers(Arrays.asList("before", "after")));
    }

    private static TwMergeConfig emptyConfig() {
        TwMergeConfig config = new TwMergeConfig();
        config.getClassGroups().clear();
        config.getConflictingClassGroups().clear();
        config.getConflictingClassGroupModifiers().clear();
        config.getPostfixLookupClassGroups().clear();
        config.getOrderSensitiveModifiers().clear();
        return config;
    }
}
