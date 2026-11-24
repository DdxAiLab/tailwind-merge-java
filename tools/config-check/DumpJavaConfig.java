import com.github.ddxailab.tailwindmerge.TwMergeConfig;
import com.github.ddxailab.tailwindmerge.common.ThemeGetter;
import com.github.ddxailab.tailwindmerge.common.ThemeKeys;
import com.github.ddxailab.tailwindmerge.common.Validators;
import com.github.ddxailab.tailwindmerge.model.ClassGroup;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.IdentityHashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Predicate;

/**
 * Prints the default {@link TwMergeConfig} in the normalized text format shared with
 * {@code dump_cs_config.py}, so the Java config can be diffed against the C# reference.
 *
 * <p>Validators and theme getters are lambdas, so they are identified by reference: every
 * {@code Validators.IS_*} constant and every {@code DefaultConfigScales.*_THEME} getter is looked
 * up by reflection and printed as {@code V:IS_*} / {@code T:*_THEME}. A definition that matches none
 * of them is printed as {@code ?UNKNOWN?} (which can never equal the C# side).
 *
 * <p>Development tool only: it is not part of the library and is not compiled by Maven. See
 * {@code tools/config-check/README.md}.
 */
public class DumpJavaConfig {

    private static final Map<Object, String> NAMES = new IdentityHashMap<>();
    private static final Map<String, String> THEME_KEY_NAMES = new HashMap<>();

    public static void main(String[] args) throws Exception {
        for (Field field : Validators.class.getFields()) {
            if (Modifier.isStatic(field.getModifiers()) && Predicate.class.isAssignableFrom(field.getType())) {
                NAMES.put(field.get(null), "V:" + field.getName());
            }
        }
        for (Field field : ThemeKeys.class.getFields()) {
            if (Modifier.isStatic(field.getModifiers()) && field.getType() == String.class) {
                THEME_KEY_NAMES.put((String) field.get(null), field.getName());
            }
        }
        // DefaultConfigScales is package-private, hence the reflection.
        Class<?> scales = Class.forName("com.github.ddxailab.tailwindmerge.DefaultConfigScales");
        for (Field field : scales.getDeclaredFields()) {
            if (Modifier.isStatic(field.getModifiers()) && ThemeGetter.class.isAssignableFrom(field.getType())) {
                field.setAccessible(true);
                NAMES.put(field.get(null), "T:" + field.getName());
            }
        }

        TwMergeConfig config = new TwMergeConfig();
        StringBuilder out = new StringBuilder();
        out.append("cacheSize=").append(config.getCacheSize()).append('\n');
        out.append("prefix=").append(config.getPrefix() == null ? "None" : config.getPrefix()).append('\n');
        for (Map.Entry<String, List<Object>> entry : config.getTheme().entrySet()) {
            out.append("THEME ").append(THEME_KEY_NAMES.get(entry.getKey())).append(" = ");
            appendDefinitions(out, entry.getValue());
            out.append('\n');
        }
        for (Map.Entry<String, ClassGroup> entry : config.getClassGroups().entrySet()) {
            out.append("GROUP ").append(entry.getKey()).append(" = ");
            appendGroup(out, entry.getValue());
            out.append('\n');
        }
        for (Map.Entry<String, List<String>> entry : config.getConflictingClassGroups().entrySet()) {
            out.append("CONFLICT ").append(entry.getKey()).append(" = ").append(entry.getValue()).append('\n');
        }
        for (Map.Entry<String, List<String>> entry : config.getConflictingClassGroupModifiers().entrySet()) {
            out.append("CONFLICTMOD ").append(entry.getKey()).append(" = ").append(entry.getValue()).append('\n');
        }
        out.append("POSTFIX = ").append(new ArrayList<>(config.getPostfixLookupClassGroups())).append('\n');
        out.append("ORDER = ").append(new ArrayList<>(config.getOrderSensitiveModifiers())).append('\n');
        System.out.print(out);
    }

    private static void appendGroup(StringBuilder out, ClassGroup group) {
        String base = group.getBaseClassName() == null ? "-" : '"' + group.getBaseClassName() + '"';
        out.append("G<").append(base).append('>');
        appendDefinitions(out, group.getDefinitions());
    }

    private static void appendDefinitions(StringBuilder out, List<Object> definitions) {
        out.append('[');
        boolean first = true;
        for (Object definition : definitions) {
            if (!first) {
                out.append(", ");
            }
            first = false;
            if (definition instanceof String) {
                out.append('"').append(definition).append('"');
            } else if (definition instanceof ClassGroup) {
                appendGroup(out, (ClassGroup) definition);
            } else {
                out.append(NAMES.containsKey(definition) ? NAMES.get(definition) : "?UNKNOWN?");
            }
        }
        out.append(']');
    }
}
