package com.github.ddxailab.tailwindmerge;

import java.util.function.Predicate;

/**
 * Associates a validator of a class name part with the class group it resolves to.
 */
final class ClassValidator {

    private final String classGroupId;
    private final Predicate<String> validator;

    ClassValidator(String classGroupId, Predicate<String> validator) {
        this.classGroupId = classGroupId;
        this.validator = validator;
    }

    String getClassGroupId() {
        return classGroupId;
    }

    Predicate<String> getValidator() {
        return validator;
    }
}
