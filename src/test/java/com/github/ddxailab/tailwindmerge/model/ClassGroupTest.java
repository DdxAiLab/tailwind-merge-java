package com.github.ddxailab.tailwindmerge.model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.Arrays;
import java.util.function.Predicate;
import org.junit.jupiter.api.Test;

class ClassGroupTest {

    @Test
    void ofCreatesAGroupWithoutBaseClassName() {
        ClassGroup group = ClassGroup.of("container");

        assertNull(group.getBaseClassName());
        assertEquals(Arrays.<Object>asList("container"), group.getDefinitions());
    }

    @Test
    void withBaseKeepsBaseClassNameAndDefinitionsInOrder() {
        Predicate<String> validator = value -> true;
        ClassGroup nested = ClassGroup.of("nested");

        ClassGroup group = ClassGroup.withBase("aspect", "auto", validator, nested);

        assertEquals("aspect", group.getBaseClassName());
        assertEquals(3, group.getDefinitions().size());
        assertEquals("auto", group.getDefinitions().get(0));
        assertSame(validator, group.getDefinitions().get(1));
        assertSame(nested, group.getDefinitions().get(2));
    }

    @Test
    void definitionsAreIsolatedFromTheCallerArray() {
        Object[] definitions = {"a", "b"};

        ClassGroup group = ClassGroup.of(definitions);
        definitions[0] = "changed";

        assertEquals("a", group.getDefinitions().get(0));
    }

    @Test
    void definitionsAreUnmodifiable() {
        ClassGroup group = ClassGroup.of("a");

        assertThrows(UnsupportedOperationException.class, () -> group.getDefinitions().add("b"));
    }

    @Test
    void nullDefinitionsArrayIsRejected() {
        assertThrows(NullPointerException.class, () -> ClassGroup.of((Object[]) null));
    }
}
