/*
 * port-tests: generates the Java tests of the "merge" behavior tests from the C# reference.
 *
 * WHAT IT IS FOR
 *   Most test files of tailwind-merge-dotnet (tests/*Tests.cs) are long lists of
 *     [InlineData( "classes", "expected" )]
 *   over a method whose body is just `new TwMerge().Merge( classLists )` + `Assert.Equal`.
 *   Retyping hundreds of such cases by hand invites typos, and the porting rule is that the cases and the
 *   expected values must be the ones of the reference. This script reads the [InlineData] attributes and
 *   writes a JUnit 5 class (@ParameterizedTest + @MethodSource) with exactly the same strings.
 *
 * WHAT IT DOES
 *   - One C# `FooTests.cs` becomes one `FooTest.java`; one C# theory becomes one provider method plus one
 *     test method (`Merge_Foo_MergesCorrectly` -> `mergeFooMergesCorrectly`).
 *   - Handles string concatenation (`"a " + "b"`), C# escapes, `null` (written as "" because the Java API
 *     returns an empty string instead of null) and the `//` comments between the attributes (kept).
 *   - Only methods with the signature `(string x, string expected)` are converted. A method with that
 *     signature whose body is anything other than the standard `new TwMerge().Merge( arg )` +
 *     `Assert.Equal( expected, actual )` makes the script fail (exit code 1). Methods with any other
 *     signature ([Fact] tests, config extension, cache...) are skipped silently and must be ported by
 *     hand: compare the printed method count with the number of tests in the C# file.
 *   - Prints the number of methods and cases per file; compare it with
 *     `grep -c InlineData <file>` to check that nothing was lost.
 *
 * USAGE (from the repository root; needs only Node.js, no dependencies)
 *   node tools/port-tests/gen-tests.js ArbitraryProperties ClassGroups ...
 *   node tools/port-tests/gen-tests.js --tests <dir with the .cs tests> --out <output dir> Name ...
 *
 *   Name is the C# file name without the "Tests.cs" suffix. Defaults:
 *     --tests docs/references/tailwind-merge-dotnet/tests
 *     --out   src/test/java/com/github/ddxailab/tailwindmerge
 *   Existing files with the same name are overwritten, so do not run it over hand-edited tests.
 *
 * It is a development tool only: it is not part of the published library and Maven does not build it.
 */
const fs = require("fs");
const path = require("path");

const argv = process.argv.slice(2);
let testsDir = "docs/references/tailwind-merge-dotnet/tests";
let outDir = "src/test/java/com/github/ddxailab/tailwindmerge";
const names = [];
for (let k = 0; k < argv.length; k++) {
  if (argv[k] === "--tests") testsDir = argv[++k];
  else if (argv[k] === "--out") outDir = argv[++k];
  else names.push(argv[k]);
}
if (names.length === 0) {
  console.error("usage: node tools/port-tests/gen-tests.js [--tests dir] [--out dir] Name [Name ...]");
  process.exit(2);
}

function parseString(src, i) {
  // src[i] === '"'; returns [value, nextIndex]
  if (src.startsWith('"""', i)) throw new Error('raw string not supported');
  let v = '';
  i++;
  while (src[i] !== '"') {
    if (src[i] === '\\') {
      const c = src[i + 1];
      const map = { n: '\n', r: '\r', t: '\t', '"': '"', '\\': '\\', '0': '\0', "'": "'" };
      if (c === 'u') { v += String.fromCharCode(parseInt(src.substr(i + 2, 4), 16)); i += 6; continue; }
      if (!(c in map)) throw new Error('escape \\' + c);
      v += map[c]; i += 2;
    } else { v += src[i++]; }
  }
  return [v, i + 1];
}

function skipWs(src, i) { while (/\s/.test(src[i])) i++; return i; }

// Parses `( expr , expr )` after InlineData; each expr is string literals joined by + or null.
function parseInlineData(src, i) {
  i = skipWs(src, i);
  if (src[i] !== '(') throw new Error('expected (');
  i++;
  const args = [];
  for (;;) {
    i = skipWs(src, i);
    let value = null;
    let isNull = false;
    if (src.startsWith('null', i)) { isNull = true; i += 4; }
    else {
      value = '';
      for (;;) {
        i = skipWs(src, i);
        const [s, n] = parseString(src, i);
        value += s; i = skipWs(src, n);
        if (src[i] === '+') { i++; continue; }
        break;
      }
    }
    args.push(isNull ? null : value);
    i = skipWs(src, i);
    if (src[i] === ',') { i++; continue; }
    if (src[i] === ')') { i++; break; }
    throw new Error('unexpected ' + src.substr(i, 20));
  }
  i = skipWs(src, i);
  if (src[i] !== ']') throw new Error('expected ]');
  return [args, i + 1];
}

function javaString(s) {
  let out = '"';
  for (const ch of s) {
    const c = ch.codePointAt(0);
    if (ch === '"') out += '\\"';
    else if (ch === '\\') out += '\\\\';
    else if (ch === '\n') out += '\\n';
    else if (ch === '\r') out += '\\r';
    else if (ch === '\t') out += '\\t';
    else if (c < 0x20 || c > 0x7e) out += '\\u' + c.toString(16).padStart(4, '0');
    else out += ch;
  }
  return out + '"';
}

function lowerFirst(s) { return s[0].toLowerCase() + s.slice(1); }

for (const name of names) {
  const file = path.join(testsDir, name + 'Tests.cs');
  const src = fs.readFileSync(file, 'utf8').replace(/^﻿/, '').replace(/\r\n/g, '\n');
  const methodRe = /public void (\w+)\(\s*string (\w+),\s*string expected\s*\)\s*\{/g;
  const methods = [];
  let prevEnd = src.indexOf('{', src.indexOf('public class')) + 1;
  let m;
  while ((m = methodRe.exec(src))) {
    const region = src.slice(prevEnd, m.index);
    // end of method body: brace matching
    let depth = 1, j = m.index + m[0].length;
    while (depth > 0) { if (src[j] === '{') depth++; else if (src[j] === '}') depth--; j++; }
    const body = src.slice(m.index + m[0].length, j - 1).trim();
    const arg = m[2];
    const expectedBody = `// Act\n        var actual = new TwMerge().Merge( ${arg} );\n\n        // Assert\n        Assert.Equal( expected, actual );`;
    if (body.replace(/\s+/g, ' ') !== expectedBody.replace(/\s+/g, ' ')) throw new Error(name + '.' + m[1] + ': unexpected body\n' + body);
    // region: comments + [Theory] + [InlineData(...)]
    const items = [];
    let i = 0;
    while (i < region.length) {
      i = skipWs(region, i);
      if (i >= region.length) break;
      if (region.startsWith('//', i)) {
        const e = region.indexOf('\n', i);
        items.push({ comment: region.slice(i + 2, e < 0 ? region.length : e).trim() });
        i = e < 0 ? region.length : e + 1;
      } else if (region.startsWith('[Theory]', i)) { i += 8; }
      else if (region.startsWith('[InlineData', i)) {
        const [args, n] = parseInlineData(region, i + '[InlineData'.length);
        if (args.length !== 2) throw new Error('args ' + args.length);
        items.push({ args }); i = n;
      } else throw new Error(name + ': unexpected text in attributes: ' + region.slice(i, i + 40));
    }
    methods.push({ cs: m[1], items });
    prevEnd = j;
  }

  const cls = name + 'Test';
  let java = `package com.github.ddxailab.tailwindmerge;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.stream.Stream;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

/** Port of {@code ${name}Tests.cs}; the cases and the expected values are the ones of the reference. */
class ${cls} {
`;
  let total = 0;
  for (const meth of methods) {
    const jn = lowerFirst(meth.cs.replace(/_/g, ''));
    const lines = [];
    const cases = meth.items.filter((x) => x.args);
    total += cases.length;
    meth.items.forEach((it, idx) => {
      if (it.comment !== undefined) { lines.push(`                // ${it.comment}`); return; }
      const rest = meth.items.slice(idx + 1).some((x) => x.args);
      const [a, b] = it.args.map((x) => (x === null ? '""' : javaString(x)));
      lines.push(`                Arguments.of(${a}, ${b})${rest ? ',' : ''}`);
    });
    java += `
    static Stream<Arguments> ${jn}Cases() {
        return Stream.of(
${lines.join('\n')});
    }

    @ParameterizedTest
    @MethodSource("${jn}Cases")
    void ${jn}(String classLists, String expected) {
        assertEquals(expected, new TwMerge().merge(classLists));
    }
`;
  }
  java += '}\n';
  fs.writeFileSync(path.join(outDir, cls + '.java'), java);
  console.log(`${cls}: ${methods.length} methods, ${total} cases`);
}
