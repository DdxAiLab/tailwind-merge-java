package com.github.ddxailab.tailwindmerge;

/**
 * A utility for merging conflicting Tailwind CSS classes.
 *
 * <p><b>Scaffold stage:</b> for now this only joins the given class names and normalizes
 * whitespace. Conflict resolution is being ported step by step, see {@code docs/CHECKLIST.md}.</p>
 *
 * <p>Instances are immutable and thread-safe.</p>
 */
public final class TwMerge {

    /**
     * Merges the given class names, resolving any conflicts if present.
     *
     * @param classNames the CSS classes to be merged; {@code null} and blank entries are ignored
     * @return the merged CSS classes, or an empty string if there is nothing to merge
     */
    public String merge(String... classNames) {
        if (classNames == null) {
            return "";
        }

        StringBuilder result = new StringBuilder();
        for (String className : classNames) {
            if (className == null) {
                continue;
            }
            for (String token : className.trim().split("\\s+")) {
                if (token.isEmpty()) {
                    continue;
                }
                if (result.length() > 0) {
                    result.append(' ');
                }
                result.append(token);
            }
        }
        return result.toString();
    }
}
