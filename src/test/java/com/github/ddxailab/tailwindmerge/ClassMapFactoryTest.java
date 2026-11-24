package com.github.ddxailab.tailwindmerge;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;
import org.junit.jupiter.api.Test;

class ClassMapFactoryTest {

    @Test
    void createDefaultConfigHasCorrectClassGroupsAtFirstNode() {
        ClassNameNode classMap = TwMergeMapFactory.create(new TwMergeConfig());

        Map<String, List<String>> classGroupsByFirstNode = new TreeMap<>();
        for (Map.Entry<String, ClassNameNode> entry : classMap.getNext().entrySet()) {
            List<String> sorted = new ArrayList<>(getClassGroupsInClassNameNode(entry.getValue()));
            Collections.sort(sorted);
            classGroupsByFirstNode.put(entry.getKey(), sorted);
        }

        Map<String, List<String>> expected = new TreeMap<>();
        expected.put("@container", Arrays.asList("container-type"));
        expected.put("absolute", Arrays.asList("position"));
        expected.put("accent", Arrays.asList("accent"));
        expected.put("align", Arrays.asList("vertical-align"));
        expected.put("animate", Arrays.asList("animate"));
        expected.put("antialiased", Arrays.asList("font-smoothing"));
        expected.put("appearance", Arrays.asList("appearance"));
        expected.put("aspect", Arrays.asList("aspect"));
        expected.put("auto", Arrays.asList("auto-cols", "auto-rows"));
        expected.put("backdrop", Arrays.asList(
                "backdrop-blur",
                "backdrop-brightness",
                "backdrop-contrast",
                "backdrop-filter",
                "backdrop-grayscale",
                "backdrop-hue-rotate",
                "backdrop-invert",
                "backdrop-opacity",
                "backdrop-saturate",
                "backdrop-sepia"));
        expected.put("backface", Arrays.asList("backface"));
        expected.put("basis", Arrays.asList("basis"));
        expected.put("bg", Arrays.asList(
                "bg-attachment",
                "bg-blend",
                "bg-clip",
                "bg-color",
                "bg-image",
                "bg-origin",
                "bg-position",
                "bg-repeat",
                "bg-size"));
        expected.put("block", Arrays.asList("block-size", "display"));
        expected.put("blur", Arrays.asList("blur"));
        expected.put("border", Arrays.asList(
                "border-collapse",
                "border-color",
                "border-color-b",
                "border-color-be",
                "border-color-bs",
                "border-color-e",
                "border-color-l",
                "border-color-r",
                "border-color-s",
                "border-color-t",
                "border-color-x",
                "border-color-y",
                "border-spacing",
                "border-spacing-x",
                "border-spacing-y",
                "border-style",
                "border-w",
                "border-w-b",
                "border-w-be",
                "border-w-bs",
                "border-w-e",
                "border-w-l",
                "border-w-r",
                "border-w-s",
                "border-w-t",
                "border-w-x",
                "border-w-y"));
        expected.put("bottom", Arrays.asList("bottom"));
        expected.put("box", Arrays.asList("box", "box-decoration"));
        expected.put("break", Arrays.asList("break", "break-after", "break-before", "break-inside"));
        expected.put("brightness", Arrays.asList("brightness"));
        expected.put("capitalize", Arrays.asList("text-transform"));
        expected.put("caption", Arrays.asList("caption"));
        expected.put("caret", Arrays.asList("caret-color"));
        expected.put("clear", Arrays.asList("clear"));
        expected.put("col", Arrays.asList("col-end", "col-start", "col-start-end"));
        expected.put("collapse", Arrays.asList("visibility"));
        expected.put("container", Arrays.asList("container"));
        expected.put("columns", Arrays.asList("columns"));
        expected.put("content", Arrays.asList("align-content", "content"));
        expected.put("contents", Arrays.asList("display"));
        expected.put("contrast", Arrays.asList("contrast"));
        expected.put("cursor", Arrays.asList("cursor"));
        expected.put("decoration", Arrays.asList(
                "text-decoration-color",
                "text-decoration-style",
                "text-decoration-thickness"));
        expected.put("delay", Arrays.asList("delay"));
        expected.put("diagonal", Arrays.asList("fvn-fraction"));
        expected.put("divide", Arrays.asList(
                "divide-color",
                "divide-style",
                "divide-x",
                "divide-x-reverse",
                "divide-y",
                "divide-y-reverse"));
        expected.put("drop", Arrays.asList("drop-shadow", "drop-shadow-color"));
        expected.put("duration", Arrays.asList("duration"));
        expected.put("ease", Arrays.asList("ease"));
        expected.put("end", Arrays.asList("end"));
        expected.put("field", Arrays.asList("field-sizing"));
        expected.put("fill", Arrays.asList("fill"));
        expected.put("filter", Arrays.asList("filter"));
        expected.put("fixed", Arrays.asList("position"));
        expected.put("flex", Arrays.asList("display", "flex", "flex-direction", "flex-wrap"));
        expected.put("float", Arrays.asList("float"));
        expected.put("flow", Arrays.asList("display"));
        expected.put("font", Arrays.asList("font-family", "font-features", "font-stretch", "font-weight"));
        expected.put("forced", Arrays.asList("forced-color-adjust"));
        expected.put("from", Arrays.asList("gradient-from", "gradient-from-pos"));
        expected.put("gap", Arrays.asList("gap", "gap-x", "gap-y"));
        expected.put("grayscale", Arrays.asList("grayscale"));
        expected.put("grid", Arrays.asList("display", "grid-cols", "grid-flow", "grid-rows"));
        expected.put("grow", Arrays.asList("grow"));
        expected.put("h", Arrays.asList("h"));
        expected.put("hidden", Arrays.asList("display"));
        expected.put("hue", Arrays.asList("hue-rotate"));
        expected.put("hyphens", Arrays.asList("hyphens"));
        expected.put("indent", Arrays.asList("indent"));
        expected.put("inline", Arrays.asList("display", "inline-size"));
        expected.put("inset", Arrays.asList(
                "end",
                "inset",
                "inset-be",
                "inset-bs",
                "inset-ring-color",
                "inset-ring-w",
                "inset-shadow",
                "inset-shadow-color",
                "inset-x",
                "inset-y",
                "start"));
        expected.put("invert", Arrays.asList("invert"));
        expected.put("invisible", Arrays.asList("visibility"));
        expected.put("isolate", Arrays.asList("isolation"));
        expected.put("isolation", Arrays.asList("isolation"));
        expected.put("italic", Arrays.asList("font-style"));
        expected.put("items", Arrays.asList("align-items"));
        expected.put("justify", Arrays.asList("justify-content", "justify-items", "justify-self"));
        expected.put("leading", Arrays.asList("leading"));
        expected.put("left", Arrays.asList("left"));
        expected.put("line", Arrays.asList("line-clamp", "text-decoration"));
        expected.put("lining", Arrays.asList("fvn-figure"));
        expected.put("list", Arrays.asList("display", "list-image", "list-style-position", "list-style-type"));
        expected.put("lowercase", Arrays.asList("text-transform"));
        expected.put("m", Arrays.asList("m"));
        expected.put("mask", Arrays.asList(
                "mask-clip",
                "mask-composite",
                "mask-image",
                "mask-image-b-from-color",
                "mask-image-b-from-pos",
                "mask-image-b-to-color",
                "mask-image-b-to-pos",
                "mask-image-conic-from-color",
                "mask-image-conic-from-pos",
                "mask-image-conic-pos",
                "mask-image-conic-to-color",
                "mask-image-conic-to-pos",
                "mask-image-l-from-color",
                "mask-image-l-from-pos",
                "mask-image-l-to-color",
                "mask-image-l-to-pos",
                "mask-image-linear-from-color",
                "mask-image-linear-from-pos",
                "mask-image-linear-pos",
                "mask-image-linear-to-color",
                "mask-image-linear-to-pos",
                "mask-image-r-from-color",
                "mask-image-r-from-pos",
                "mask-image-r-to-color",
                "mask-image-r-to-pos",
                "mask-image-radial",
                "mask-image-radial-from-color",
                "mask-image-radial-from-pos",
                "mask-image-radial-pos",
                "mask-image-radial-shape",
                "mask-image-radial-size",
                "mask-image-radial-to-color",
                "mask-image-radial-to-pos",
                "mask-image-t-from-color",
                "mask-image-t-from-pos",
                "mask-image-t-to-color",
                "mask-image-t-to-pos",
                "mask-image-x-from-color",
                "mask-image-x-from-pos",
                "mask-image-x-to-color",
                "mask-image-x-to-pos",
                "mask-image-y-from-color",
                "mask-image-y-from-pos",
                "mask-image-y-to-color",
                "mask-image-y-to-pos",
                "mask-mode",
                "mask-origin",
                "mask-position",
                "mask-repeat",
                "mask-size",
                "mask-type"));
        expected.put("max", Arrays.asList("max-block-size", "max-h", "max-inline-size", "max-w"));
        expected.put("mb", Arrays.asList("mb"));
        expected.put("mbe", Arrays.asList("mbe"));
        expected.put("mbs", Arrays.asList("mbs"));
        expected.put("me", Arrays.asList("me"));
        expected.put("min", Arrays.asList("min-block-size", "min-h", "min-inline-size", "min-w"));
        expected.put("mix", Arrays.asList("mix-blend"));
        expected.put("ml", Arrays.asList("ml"));
        expected.put("mr", Arrays.asList("mr"));
        expected.put("ms", Arrays.asList("ms"));
        expected.put("mt", Arrays.asList("mt"));
        expected.put("mx", Arrays.asList("mx"));
        expected.put("my", Arrays.asList("my"));
        expected.put("no", Arrays.asList("text-decoration"));
        expected.put("normal", Arrays.asList("fvn-normal", "text-transform"));
        expected.put("not", Arrays.asList("font-style", "sr"));
        expected.put("object", Arrays.asList("object-fit", "object-position"));
        expected.put("oldstyle", Arrays.asList("fvn-figure"));
        expected.put("opacity", Arrays.asList("opacity"));
        expected.put("order", Arrays.asList("order"));
        expected.put("ordinal", Arrays.asList("fvn-ordinal"));
        expected.put("origin", Arrays.asList("transform-origin"));
        expected.put("outline", Arrays.asList("outline-color", "outline-offset", "outline-style", "outline-w"));
        expected.put("overflow", Arrays.asList("overflow", "overflow-x", "overflow-y"));
        expected.put("overline", Arrays.asList("text-decoration"));
        expected.put("overscroll", Arrays.asList("overscroll", "overscroll-x", "overscroll-y"));
        expected.put("p", Arrays.asList("p"));
        expected.put("pb", Arrays.asList("pb"));
        expected.put("pbe", Arrays.asList("pbe"));
        expected.put("pbs", Arrays.asList("pbs"));
        expected.put("pe", Arrays.asList("pe"));
        expected.put("perspective", Arrays.asList("perspective", "perspective-origin"));
        expected.put("pl", Arrays.asList("pl"));
        expected.put("place", Arrays.asList("place-content", "place-items", "place-self"));
        expected.put("pointer", Arrays.asList("pointer-events"));
        expected.put("pr", Arrays.asList("pr"));
        expected.put("proportional", Arrays.asList("fvn-spacing"));
        expected.put("ps", Arrays.asList("ps"));
        expected.put("pt", Arrays.asList("pt"));
        expected.put("px", Arrays.asList("px"));
        expected.put("py", Arrays.asList("py"));
        expected.put("relative", Arrays.asList("position"));
        expected.put("resize", Arrays.asList("resize"));
        expected.put("right", Arrays.asList("right"));
        expected.put("ring", Arrays.asList(
                "ring-color",
                "ring-offset-color",
                "ring-offset-w",
                "ring-w",
                "ring-w-inset"));
        expected.put("rotate", Arrays.asList("rotate", "rotate-x", "rotate-y", "rotate-z"));
        expected.put("rounded", Arrays.asList(
                "rounded",
                "rounded-b",
                "rounded-bl",
                "rounded-br",
                "rounded-e",
                "rounded-ee",
                "rounded-es",
                "rounded-l",
                "rounded-r",
                "rounded-s",
                "rounded-se",
                "rounded-ss",
                "rounded-t",
                "rounded-tl",
                "rounded-tr"));
        expected.put("row", Arrays.asList("row-end", "row-start", "row-start-end"));
        expected.put("saturate", Arrays.asList("saturate"));
        expected.put("scale", Arrays.asList("scale", "scale-3d", "scale-x", "scale-y", "scale-z"));
        expected.put("scheme", Arrays.asList("color-scheme"));
        expected.put("scroll", Arrays.asList(
                "scroll-behavior",
                "scroll-m",
                "scroll-mb",
                "scroll-mbe",
                "scroll-mbs",
                "scroll-me",
                "scroll-ml",
                "scroll-mr",
                "scroll-ms",
                "scroll-mt",
                "scroll-mx",
                "scroll-my",
                "scroll-p",
                "scroll-pb",
                "scroll-pbe",
                "scroll-pbs",
                "scroll-pe",
                "scroll-pl",
                "scroll-pr",
                "scroll-ps",
                "scroll-pt",
                "scroll-px",
                "scroll-py"));
        expected.put("scrollbar", Arrays.asList(
                "scrollbar-gutter",
                "scrollbar-thumb-color",
                "scrollbar-track-color",
                "scrollbar-w"));
        expected.put("select", Arrays.asList("select"));
        expected.put("self", Arrays.asList("align-self"));
        expected.put("sepia", Arrays.asList("sepia"));
        expected.put("shadow", Arrays.asList("shadow", "shadow-color"));
        expected.put("shrink", Arrays.asList("shrink"));
        expected.put("size", Arrays.asList("size"));
        expected.put("skew", Arrays.asList("skew", "skew-x", "skew-y"));
        expected.put("slashed", Arrays.asList("fvn-slashed-zero"));
        expected.put("snap", Arrays.asList("snap-align", "snap-stop", "snap-strictness", "snap-type"));
        expected.put("space", Arrays.asList("space-x", "space-x-reverse", "space-y", "space-y-reverse"));
        expected.put("sr", Arrays.asList("sr"));
        expected.put("stacked", Arrays.asList("fvn-fraction"));
        expected.put("start", Arrays.asList("start"));
        expected.put("static", Arrays.asList("position"));
        expected.put("sticky", Arrays.asList("position"));
        expected.put("stroke", Arrays.asList("stroke", "stroke-w"));
        expected.put("subpixel", Arrays.asList("font-smoothing"));
        expected.put("tab", Arrays.asList("tab-size"));
        expected.put("table", Arrays.asList("display", "table-layout"));
        expected.put("tabular", Arrays.asList("fvn-spacing"));
        expected.put("text", Arrays.asList(
                "font-size",
                "text-alignment",
                "text-color",
                "text-overflow",
                "text-shadow",
                "text-shadow-color",
                "text-wrap"));
        expected.put("to", Arrays.asList("gradient-to", "gradient-to-pos"));
        expected.put("top", Arrays.asList("top"));
        expected.put("touch", Arrays.asList("touch", "touch-pz", "touch-x", "touch-y"));
        expected.put("tracking", Arrays.asList("tracking"));
        expected.put("transform", Arrays.asList("transform", "transform-style"));
        expected.put("transition", Arrays.asList("transition", "transition-behavior"));
        expected.put("translate", Arrays.asList(
                "translate",
                "translate-none",
                "translate-x",
                "translate-y",
                "translate-z"));
        expected.put("truncate", Arrays.asList("text-overflow"));
        expected.put("underline", Arrays.asList("text-decoration", "underline-offset"));
        expected.put("uppercase", Arrays.asList("text-transform"));
        expected.put("via", Arrays.asList("gradient-via", "gradient-via-pos"));
        expected.put("visible", Arrays.asList("visibility"));
        expected.put("w", Arrays.asList("w"));
        expected.put("whitespace", Arrays.asList("whitespace"));
        expected.put("will", Arrays.asList("will-change"));
        expected.put("wrap", Arrays.asList("wrap"));
        expected.put("zoom", Arrays.asList("zoom"));
        expected.put("z", Arrays.asList("z"));

        assertNull(classMap.getClassGroupId());
        List<String> rootValidators = new ArrayList<>();
        for (ClassValidator validator : classMap.getValidators()) {
            rootValidators.add(validator.getClassGroupId());
        }
        assertEquals(Arrays.asList("container-named"), rootValidators);
        assertEquals(expected, classGroupsByFirstNode);
    }

    private static Set<String> getClassGroupsInClassNameNode(ClassNameNode node) {
        Set<String> classGroups = new TreeSet<>();

        if (node.getClassGroupId() != null && !node.getClassGroupId().isEmpty()) {
            classGroups.add(node.getClassGroupId());
        }

        if (node.getValidators() != null) {
            for (ClassValidator validator : node.getValidators()) {
                classGroups.add(validator.getClassGroupId());
            }
        }

        if (node.getNext() != null) {
            for (ClassNameNode next : node.getNext().values()) {
                classGroups.addAll(getClassGroupsInClassNameNode(next));
            }
        }

        return classGroups;
    }
}
