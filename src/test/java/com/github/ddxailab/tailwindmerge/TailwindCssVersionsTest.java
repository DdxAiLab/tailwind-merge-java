package com.github.ddxailab.tailwindmerge;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.stream.Stream;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

/** Port of {@code TailwindCssVersionsTests.cs}; the cases and the expected values are the ones of the reference. */
class TailwindCssVersionsTest {

    static Stream<Arguments> mergeTailwindCssV33ClassesMergesCorrectlyCases() {
        return Stream.of(
                Arguments.of("text-red text-lg/7 text-lg/8", "text-red text-lg/8"),
                Arguments.of("start-0 start-1 end-0 end-1 ps-0 ps-1 pe-0 pe-1 ms-0 ms-1 me-0 me-1 rounded-s-sm rounded-s-md rounded-e-sm rounded-e-md rounded-ss-sm rounded-ss-md rounded-ee-sm rounded-ee-md", "start-1 end-1 ps-1 pe-1 ms-1 me-1 rounded-s-md rounded-e-md rounded-ss-md rounded-ee-md"),
                Arguments.of("start-0 end-0 inset-0 ps-0 pe-0 p-0 ms-0 me-0 m-0 rounded-ss rounded-es rounded-s", "inset-0 p-0 m-0 rounded-s"),
                Arguments.of("hyphens-auto hyphens-manual", "hyphens-manual"),
                Arguments.of("from-0% from-10% from-[12.5%] via-0% via-10% via-[12.5%] to-0% to-10% to-[12.5%]", "from-[12.5%] via-[12.5%] to-[12.5%]"),
                Arguments.of("from-0% from-red", "from-0% from-red"),
                Arguments.of("list-image-none list-image-[url(./my-image.png)] list-image-[var(--value)]", "list-image-[var(--value)]"),
                Arguments.of("caption-top caption-bottom", "caption-bottom"),
                Arguments.of("line-clamp-2 line-clamp-none line-clamp-[10]", "line-clamp-[10]"),
                Arguments.of("delay-150 delay-0 duration-150 duration-0", "delay-0 duration-0"),
                Arguments.of("justify-normal justify-center justify-stretch", "justify-stretch"),
                Arguments.of("content-normal content-center content-stretch", "content-stretch"),
                Arguments.of("whitespace-nowrap whitespace-break-spaces", "whitespace-break-spaces"));
    }

    @ParameterizedTest
    @MethodSource("mergeTailwindCssV33ClassesMergesCorrectlyCases")
    void mergeTailwindCssV33ClassesMergesCorrectly(String classLists, String expected) {
        assertEquals(expected, new TwMerge().merge(classLists));
    }

    static Stream<Arguments> mergeTailwindCssV34ClassesMergesCorrectlyCases() {
        return Stream.of(
                Arguments.of("h-svh h-dvh w-svw w-dvw", "h-dvh w-dvw"),
                Arguments.of("has-[[data-potato]]:p-1 has-[[data-potato]]:p-2 group-has-[:checked]:grid group-has-[:checked]:flex", "has-[[data-potato]]:p-2 group-has-[:checked]:flex"),
                Arguments.of("text-wrap text-pretty", "text-pretty"),
                Arguments.of("w-5 h-3 size-10 w-12", "size-10 w-12"),
                Arguments.of("grid-cols-2 grid-cols-subgrid grid-rows-5 grid-rows-subgrid", "grid-cols-subgrid grid-rows-subgrid"),
                Arguments.of("min-w-0 min-w-50 min-w-px max-w-0 max-w-50 max-w-px", "min-w-px max-w-px"),
                Arguments.of("forced-color-adjust-none forced-color-adjust-auto", "forced-color-adjust-auto"),
                Arguments.of("appearance-none appearance-auto", "appearance-auto"),
                Arguments.of("float-start float-end clear-start clear-end", "float-end clear-end"),
                Arguments.of("*:p-10 *:p-20 hover:*:p-10 hover:*:p-20", "*:p-20 hover:*:p-20"));
    }

    @ParameterizedTest
    @MethodSource("mergeTailwindCssV34ClassesMergesCorrectlyCases")
    void mergeTailwindCssV34ClassesMergesCorrectly(String classLists, String expected) {
        assertEquals(expected, new TwMerge().merge(classLists));
    }

    static Stream<Arguments> mergeTailwindCssV4ClassesMergesCorrectlyCases() {
        return Stream.of(
                Arguments.of("transform-3d transform-flat", "transform-flat"),
                Arguments.of("rotate-12 rotate-x-2 rotate-none rotate-y-3", "rotate-x-2 rotate-none rotate-y-3"),
                Arguments.of("perspective-dramatic perspective-none perspective-midrange", "perspective-midrange"),
                Arguments.of("perspective-origin-center perspective-origin-top-left", "perspective-origin-top-left"),
                Arguments.of("bg-linear-to-r bg-linear-45", "bg-linear-45"),
                Arguments.of("bg-linear-to-r bg-radial-[something] bg-conic-10", "bg-conic-10"),
                Arguments.of("bg-conic bg-conic-10", "bg-conic-10"),
                Arguments.of("bg-conic-10 bg-conic", "bg-conic"),
                Arguments.of("bg-radial bg-conic/decreasing", "bg-conic/decreasing"),
                Arguments.of("bg-red-500 bg-conic", "bg-red-500 bg-conic"),
                Arguments.of("ring-4 ring-orange inset-ring inset-ring-3 inset-ring-blue", "ring-4 ring-orange inset-ring-3 inset-ring-blue"),
                Arguments.of("field-sizing-content field-sizing-fixed", "field-sizing-fixed"),
                Arguments.of("scheme-normal scheme-dark", "scheme-dark"),
                Arguments.of("font-stretch-expanded font-stretch-[66.66%] font-stretch-50%", "font-stretch-50%"),
                Arguments.of("col-span-full col-2 row-span-3 row-4", "col-2 row-4"),
                Arguments.of("via-red-500 via-(--mobile-header-gradient)", "via-(--mobile-header-gradient)"),
                Arguments.of("via-red-500 via-(length:--mobile-header-gradient)", "via-red-500 via-(length:--mobile-header-gradient)"),
                // shadow-inner is deprecated in v4 but still sets --tw-shadow, so it conflicts with other shadow utilities and not with shadow color utilities
                Arguments.of("shadow-inner shadow-lg", "shadow-lg"),
                Arguments.of("shadow-lg shadow-inner", "shadow-inner"),
                Arguments.of("shadow-initial shadow-inner", "shadow-initial shadow-inner"),
                Arguments.of("blur-sm blur-lg", "blur-lg"),
                Arguments.of("blur blur-none blur-xs", "blur-xs"),
                Arguments.of("blur-md blur-[2px] blur-(--my-blur)", "blur-(--my-blur)"),
                Arguments.of("blur-[2px] blur-[var(--my-blur)]", "blur-[var(--my-blur)]"),
                Arguments.of("backdrop-blur-sm backdrop-blur-lg", "backdrop-blur-lg"),
                Arguments.of("backdrop-blur-md backdrop-blur-[2px] backdrop-blur-(--my-blur)", "backdrop-blur-(--my-blur)"),
                Arguments.of("blur-sm backdrop-blur-lg", "blur-sm backdrop-blur-lg"));
    }

    @ParameterizedTest
    @MethodSource("mergeTailwindCssV4ClassesMergesCorrectlyCases")
    void mergeTailwindCssV4ClassesMergesCorrectly(String classLists, String expected) {
        assertEquals(expected, new TwMerge().merge(classLists));
    }

    static Stream<Arguments> mergeTailwindCssV41ClassesMergesCorrectlyCases() {
        return Stream.of(
                Arguments.of("items-baseline items-baseline-last", "items-baseline-last"),
                Arguments.of("self-baseline self-baseline-last", "self-baseline-last"),
                Arguments.of("place-content-center place-content-end-safe place-content-center-safe", "place-content-center-safe"),
                Arguments.of("items-center-safe items-baseline items-end-safe", "items-end-safe"),
                Arguments.of("wrap-break-word wrap-normal wrap-anywhere", "wrap-anywhere"),
                Arguments.of("text-shadow-none text-shadow-2xl", "text-shadow-2xl"),
                Arguments.of("text-shadow-none text-shadow-md text-shadow-red text-shadow-red-500 shadow-red shadow-3xs", "text-shadow-md text-shadow-red-500 shadow-red shadow-3xs"),
                Arguments.of("mask-add mask-subtract", "mask-subtract"),
                Arguments.of("mask-type-luminance mask-type-alpha", "mask-type-alpha"),
                Arguments.of("mask-clip-border mask-no-clip", "mask-no-clip"),
                Arguments.of("mask-(--something) mask-[something] mask-top-left mask-center mask-(position:--var) mask-[position:1px_1px] mask-position-(--var) mask-position-[1px_1px]", "mask-[something] mask-position-[1px_1px]"),
                Arguments.of("mask-(--something) mask-[something] mask-auto mask-[size:foo] mask-(size:--foo) mask-size-[foo] mask-size-(--foo) mask-cover mask-contain", "mask-[something] mask-contain"),
                Arguments.of("mask-(--foo) mask-[foo] mask-none mask-linear-1 mask-linear-2 mask-linear-from-[position:test] mask-linear-from-3 mask-linear-to-[position:test] mask-linear-to-3 mask-linear-from-color-red mask-linear-from-color-3 mask-linear-to-color-red mask-linear-to-color-3 mask-t-from-[position:test] mask-t-from-3 mask-t-to-[position:test] mask-t-to-3 mask-t-from-color-red mask-t-from-color-3 mask-radial-(--test) mask-radial-[test] mask-radial-from-[position:test] mask-radial-from-3 mask-radial-to-[position:test] mask-radial-to-3 mask-radial-from-color-red mask-radial-from-color-3 ", "mask-none mask-linear-2 mask-linear-from-3 mask-linear-to-3 mask-linear-from-color-3 mask-linear-to-color-3 mask-t-from-3 mask-t-to-3 mask-t-from-color-3 mask-radial-[test] mask-radial-from-3 mask-radial-to-3 mask-radial-from-color-3"),
                Arguments.of("shadow-md shadow-lg/25 text-shadow-md text-shadow-lg/25", "shadow-lg/25 text-shadow-lg/25"),
                Arguments.of("drop-shadow-some-color drop-shadow-[#123456] drop-shadow-lg drop-shadow-[10px_0]", "drop-shadow-[#123456] drop-shadow-[10px_0]"),
                Arguments.of("drop-shadow-[#123456] drop-shadow-some-color", "drop-shadow-some-color"),
                Arguments.of("drop-shadow-2xl drop-shadow-[shadow:foo]", "drop-shadow-[shadow:foo]"));
    }

    @ParameterizedTest
    @MethodSource("mergeTailwindCssV41ClassesMergesCorrectlyCases")
    void mergeTailwindCssV41ClassesMergesCorrectly(String classLists, String expected) {
        assertEquals(expected, new TwMerge().merge(classLists));
    }

    static Stream<Arguments> mergeTailwindCssV415ClassesMergesCorrectlyCases() {
        return Stream.of(
                Arguments.of("h-12 h-lh", "h-lh"),
                Arguments.of("min-h-12 min-h-lh", "min-h-lh"),
                Arguments.of("max-h-12 max-h-lh", "max-h-lh"));
    }

    @ParameterizedTest
    @MethodSource("mergeTailwindCssV415ClassesMergesCorrectlyCases")
    void mergeTailwindCssV415ClassesMergesCorrectly(String classLists, String expected) {
        assertEquals(expected, new TwMerge().merge(classLists));
    }

    static Stream<Arguments> mergeTailwindCssV42ClassesMergesCorrectlyCases() {
        return Stream.of(
                Arguments.of("inset-s-1 inset-s-2", "inset-s-2"),
                Arguments.of("inset-e-1 inset-e-2", "inset-e-2"),
                Arguments.of("inset-bs-1 inset-bs-2", "inset-bs-2"),
                Arguments.of("inset-be-1 inset-be-2", "inset-be-2"),
                Arguments.of("start-1 inset-s-2", "inset-s-2"),
                Arguments.of("inset-s-1 start-2", "start-2"),
                Arguments.of("end-1 inset-e-2", "inset-e-2"),
                Arguments.of("inset-e-1 end-2", "end-2"),
                Arguments.of("inset-s-1 inset-e-2 inset-bs-3 inset-be-4 inset-0", "inset-0"),
                Arguments.of("inset-0 inset-s-1 inset-bs-1", "inset-0 inset-s-1 inset-bs-1"),
                Arguments.of("inset-y-1 inset-bs-2 inset-be-3", "inset-y-1 inset-bs-2 inset-be-3"),
                Arguments.of("top-1 inset-bs-2 bottom-3 inset-be-4", "top-1 inset-bs-2 bottom-3 inset-be-4"),
                Arguments.of("pbs-1 pbs-2", "pbs-2"),
                Arguments.of("pbe-1 pbe-2", "pbe-2"),
                Arguments.of("mbs-1 mbs-2", "mbs-2"),
                Arguments.of("mbe-1 mbe-2", "mbe-2"),
                Arguments.of("pt-1 pbs-2", "pt-1 pbs-2"),
                Arguments.of("pb-1 pbe-2", "pb-1 pbe-2"),
                Arguments.of("mt-1 mbs-2", "mt-1 mbs-2"),
                Arguments.of("mb-1 mbe-2", "mb-1 mbe-2"),
                Arguments.of("p-0 pbs-1 pbe-1", "p-0 pbs-1 pbe-1"),
                Arguments.of("pbs-1 pbe-1 p-0", "p-0"),
                Arguments.of("m-0 mbs-1 mbe-1", "m-0 mbs-1 mbe-1"),
                Arguments.of("mbs-1 mbe-1 m-0", "m-0"),
                Arguments.of("py-1 pbs-2 pbe-3", "py-1 pbs-2 pbe-3"),
                Arguments.of("my-1 mbs-2 mbe-3", "my-1 mbs-2 mbe-3"),
                Arguments.of("scroll-pbs-1 scroll-pbs-2", "scroll-pbs-2"),
                Arguments.of("scroll-pbe-1 scroll-pbe-2", "scroll-pbe-2"),
                Arguments.of("scroll-mbs-1 scroll-mbs-2", "scroll-mbs-2"),
                Arguments.of("scroll-mbe-1 scroll-mbe-2", "scroll-mbe-2"),
                Arguments.of("scroll-pt-1 scroll-pbs-2", "scroll-pt-1 scroll-pbs-2"),
                Arguments.of("scroll-pb-1 scroll-pbe-2", "scroll-pb-1 scroll-pbe-2"),
                Arguments.of("scroll-mt-1 scroll-mbs-2", "scroll-mt-1 scroll-mbs-2"),
                Arguments.of("scroll-mb-1 scroll-mbe-2", "scroll-mb-1 scroll-mbe-2"),
                Arguments.of("scroll-p-0 scroll-pbs-1 scroll-pbe-1", "scroll-p-0 scroll-pbs-1 scroll-pbe-1"),
                Arguments.of("scroll-pbs-1 scroll-pbe-1 scroll-p-0", "scroll-p-0"),
                Arguments.of("scroll-m-0 scroll-mbs-1 scroll-mbe-1", "scroll-m-0 scroll-mbs-1 scroll-mbe-1"),
                Arguments.of("scroll-mbs-1 scroll-mbe-1 scroll-m-0", "scroll-m-0"),
                Arguments.of("scroll-py-1 scroll-pbs-2 scroll-pbe-3", "scroll-py-1 scroll-pbs-2 scroll-pbe-3"),
                Arguments.of("scroll-my-1 scroll-mbs-2 scroll-mbe-3", "scroll-my-1 scroll-mbs-2 scroll-mbe-3"),
                Arguments.of("border-bs-1 border-bs-2", "border-bs-2"),
                Arguments.of("border-be-1 border-be-2", "border-be-2"),
                Arguments.of("border-bs-red border-bs-blue", "border-bs-blue"),
                Arguments.of("border-be-red border-be-blue", "border-be-blue"),
                Arguments.of("border-2 border-bs-4 border-be-6", "border-2 border-bs-4 border-be-6"),
                Arguments.of("border-bs-4 border-be-6 border-2", "border-2"),
                Arguments.of("border-red border-bs-blue border-be-green", "border-red border-bs-blue border-be-green"),
                Arguments.of("border-bs-blue border-be-green border-red", "border-red"),
                Arguments.of("border-y-2 border-bs-4 border-be-6", "border-y-2 border-bs-4 border-be-6"),
                Arguments.of("border-t-2 border-bs-4 border-b-6 border-be-8", "border-t-2 border-bs-4 border-b-6 border-be-8"),
                Arguments.of("border-y-red border-bs-blue border-be-green", "border-y-red border-bs-blue border-be-green"),
                Arguments.of("inline-1/2 inline-3/4", "inline-3/4"),
                Arguments.of("block-1/2 block-3/4", "block-3/4"),
                Arguments.of("min-inline-auto min-inline-full", "min-inline-full"),
                Arguments.of("max-inline-none max-inline-10", "max-inline-10"),
                Arguments.of("min-block-auto min-block-lh min-block-10", "min-block-10"),
                Arguments.of("max-block-none max-block-lh max-block-10", "max-block-10"),
                Arguments.of("inline-2xl inline-3xl", "inline-3xl"),
                Arguments.of("min-inline-xs min-inline-1/2", "min-inline-1/2"),
                Arguments.of("max-inline-svw max-inline-xl", "max-inline-xl"),
                Arguments.of("w-10 inline-20", "w-10 inline-20"),
                Arguments.of("h-10 block-20", "h-10 block-20"),
                Arguments.of("size-10 inline-20 block-30", "size-10 inline-20 block-30"),
                Arguments.of("min-w-10 min-inline-20", "min-w-10 min-inline-20"),
                Arguments.of("max-h-10 max-block-20", "max-h-10 max-block-20"),
                Arguments.of("font-features-[\"smcp\"] font-features-[\"onum\"]", "font-features-[\"onum\"]"),
                Arguments.of("font-features-[var(--font-features)] font-features-[\"liga\",\"dlig\"]", "font-features-[\"liga\",\"dlig\"]"),
                Arguments.of("tabular-nums font-features-[\"smcp\"]", "tabular-nums font-features-[\"smcp\"]"),
                Arguments.of("font-features-[\"smcp\"] normal-nums", "font-features-[\"smcp\"] normal-nums"),
                Arguments.of("font-sans font-features-[\"smcp\"]", "font-sans font-features-[\"smcp\"]"),
                Arguments.of("aspect-8/11 aspect-8.5/11", "aspect-8.5/11"),
                Arguments.of("w-8/11 w-8.5/11", "w-8.5/11"),
                Arguments.of("inset-1/2 inset-1.25/2.5", "inset-1.25/2.5"));
    }

    @ParameterizedTest
    @MethodSource("mergeTailwindCssV42ClassesMergesCorrectlyCases")
    void mergeTailwindCssV42ClassesMergesCorrectly(String classLists, String expected) {
        assertEquals(expected, new TwMerge().merge(classLists));
    }

    static Stream<Arguments> mergeTailwindCssV43ClassesMergesCorrectlyCases() {
        return Stream.of(
                Arguments.of("scrollbar-auto scrollbar-thin scrollbar-none", "scrollbar-none"),
                Arguments.of("scrollbar-gutter-auto scrollbar-gutter-stable scrollbar-gutter-both", "scrollbar-gutter-both"),
                Arguments.of("scrollbar-thumb-red-500 scrollbar-thumb-blue-500", "scrollbar-thumb-blue-500"),
                Arguments.of("scrollbar-thumb-red-500 scrollbar-thumb-red-500/50", "scrollbar-thumb-red-500/50"),
                Arguments.of("scrollbar-thumb-[#0088cc] scrollbar-thumb-(--thumb-color)", "scrollbar-thumb-(--thumb-color)"),
                Arguments.of("scrollbar-track-red-500 scrollbar-track-blue-500", "scrollbar-track-blue-500"),
                Arguments.of("scrollbar-track-red-500 scrollbar-track-[color:var(--track-color)]", "scrollbar-track-[color:var(--track-color)]"),
                Arguments.of("scrollbar-thin scrollbar-thumb-red-500 scrollbar-track-blue-500", "scrollbar-thin scrollbar-thumb-red-500 scrollbar-track-blue-500"),
                Arguments.of("zoom-50 zoom-100", "zoom-100"),
                Arguments.of("zoom-100 zoom-[var(--zoom)]", "zoom-[var(--zoom)]"),
                Arguments.of("zoom-[1.5] zoom-(--zoom)", "zoom-(--zoom)"),
                Arguments.of("zoom-50 scale-125", "zoom-50 scale-125"),
                Arguments.of("tab-2 tab-8", "tab-8"),
                Arguments.of("tab-8 tab-[12px]", "tab-[12px]"),
                Arguments.of("tab-[3] tab-(--tab-size)", "tab-(--tab-size)"),
                Arguments.of("tab-4 tabular-nums", "tab-4 tabular-nums"),
                Arguments.of("@container @container-normal @container-size", "@container-size"),
                Arguments.of("@container-[inline-size] @container-(--container-type)", "@container-(--container-type)"),
                Arguments.of("@container @container-size/sidebar", "@container-size/sidebar"),
                Arguments.of("@container-normal @container-size/sidebar", "@container-size/sidebar"),
                Arguments.of("@container-size/sidebar @container", "@container-size/sidebar @container"),
                Arguments.of("@container/sidebar @container-normal", "@container/sidebar @container-normal"),
                Arguments.of("@container/sidebar @container-normal/sidebar @container-size/content", "@container-size/content"),
                Arguments.of("@container/sidebar @container-normal/content @container-size", "@container-normal/content @container-size"),
                Arguments.of("@container-size @container/sidebar", "@container/sidebar"),
                Arguments.of("@container-size/[sidebar] @container-normal/(--container-name)", "@container-normal/(--container-name)"),
                Arguments.of("hover:@container hover:@container-size/sidebar", "hover:@container-size/sidebar"),
                Arguments.of("hover:@container-size/sidebar hover:@container", "hover:@container-size/sidebar hover:@container"),
                Arguments.of("@container! @container-size/sidebar!", "@container-size/sidebar!"),
                Arguments.of("@container-size/sidebar! @container!", "@container-size/sidebar! @container!"),
                Arguments.of("@container-foo/sidebar @container-size/sidebar", "@container-foo/sidebar @container-size/sidebar"));
    }

    @ParameterizedTest
    @MethodSource("mergeTailwindCssV43ClassesMergesCorrectlyCases")
    void mergeTailwindCssV43ClassesMergesCorrectly(String classLists, String expected) {
        assertEquals(expected, new TwMerge().merge(classLists));
    }
}
