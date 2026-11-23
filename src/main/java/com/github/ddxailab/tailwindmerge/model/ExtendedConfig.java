package com.github.ddxailab.tailwindmerge.model;

import java.util.List;
import java.util.Map;

/**
 * Represents the extended configuration for the TailwindMerge utility.
 *
 * <p>Every section is optional; a {@code null} section means "nothing to add".</p>
 */
public class ExtendedConfig {

    private Map<String, List<Object>> theme;
    private Map<String, ClassGroup> classGroups;
    private Map<String, List<String>> conflictingClassGroups;
    private Map<String, List<String>> conflictingClassGroupModifiers;
    private List<String> postfixLookupClassGroups;
    private List<String> orderSensitiveModifiers;

    /**
     * Gets the theme for the configuration extension.
     *
     * @return the theme scales by key
     */
    public Map<String, List<Object>> getTheme() {
        return theme;
    }

    /**
     * Sets the theme for the configuration extension.
     *
     * @param theme the theme scales by key
     */
    public void setTheme(Map<String, List<Object>> theme) {
        this.theme = theme;
    }

    /**
     * Gets the class groups for the configuration extension.
     *
     * @return the class groups by ID
     */
    public Map<String, ClassGroup> getClassGroups() {
        return classGroups;
    }

    /**
     * Sets the class groups for the configuration extension.
     *
     * @param classGroups the class groups by ID
     */
    public void setClassGroups(Map<String, ClassGroup> classGroups) {
        this.classGroups = classGroups;
    }

    /**
     * Gets the conflicting class groups for the configuration extension.
     *
     * @return the IDs of the conflicting class groups by class group ID
     */
    public Map<String, List<String>> getConflictingClassGroups() {
        return conflictingClassGroups;
    }

    /**
     * Sets the conflicting class groups for the configuration extension.
     *
     * @param conflictingClassGroups the IDs of the conflicting class groups by class group ID
     */
    public void setConflictingClassGroups(Map<String, List<String>> conflictingClassGroups) {
        this.conflictingClassGroups = conflictingClassGroups;
    }

    /**
     * Gets the conflicting class group modifiers for the configuration extension.
     *
     * @return the IDs of the conflicting class groups by class group ID
     */
    public Map<String, List<String>> getConflictingClassGroupModifiers() {
        return conflictingClassGroupModifiers;
    }

    /**
     * Sets the conflicting class group modifiers for the configuration extension.
     *
     * @param conflictingClassGroupModifiers the IDs of the conflicting class groups by class group ID
     */
    public void setConflictingClassGroupModifiers(Map<String, List<String>> conflictingClassGroupModifiers) {
        this.conflictingClassGroupModifiers = conflictingClassGroupModifiers;
    }

    /**
     * Gets the class group IDs which should be resolved again with their postfix modifier attached.
     *
     * <p>This is needed when a slash can make the full class name belong to a different
     * class group than the part before the slash.</p>
     *
     * @return the class group IDs
     */
    public List<String> getPostfixLookupClassGroups() {
        return postfixLookupClassGroups;
    }

    /**
     * Sets the class group IDs which should be resolved again with their postfix modifier attached.
     *
     * @param postfixLookupClassGroups the class group IDs
     */
    public void setPostfixLookupClassGroups(List<String> postfixLookupClassGroups) {
        this.postfixLookupClassGroups = postfixLookupClassGroups;
    }

    /**
     * Gets the modifiers whose order among multiple modifiers is preserved
     * because their order changes which element gets targeted.
     *
     * @return the modifiers
     */
    public List<String> getOrderSensitiveModifiers() {
        return orderSensitiveModifiers;
    }

    /**
     * Sets the modifiers whose order among multiple modifiers is preserved.
     *
     * @param orderSensitiveModifiers the modifiers
     */
    public void setOrderSensitiveModifiers(List<String> orderSensitiveModifiers) {
        this.orderSensitiveModifiers = orderSensitiveModifiers;
    }
}
