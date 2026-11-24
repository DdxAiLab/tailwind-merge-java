"""Prints the default configuration of the C# reference (TwMergeConfig.cs) in the normalized text
format shared with DumpJavaConfig.java, so that both can be diffed.

Usage:  python -I dump_cs_config.py path/to/TwMergeConfig.cs > cs.txt

The script parses the constructor of TwMergeConfig (theme getters, scale variables, the Theme,
ClassGroups, ConflictingClassGroups and ConflictingClassGroupModifiers initializers, and the
PostfixLookupClassGroups / OrderSensitiveModifiers collections), expands the scale variables and the
collection-expression spreads ("[.. scale]"), and prints one line per entry:

    cacheSize=500
    prefix=None
    THEME ANIMATE = ["spin", ...]
    GROUP aspect = G<"aspect">["auto", "square", T:ASPECT_THEME, V:IS_FRACTION, ...]
    CONFLICT inset = [inset-x, inset-y, ...]
    CONFLICTMOD font-size = [leading]
    POSTFIX = [container-type]
    ORDER = [*, **, after, ...]

Definitions: a quoted string is a class name part; V:IS_* is a validator (V.IsArbitraryValue ->
V:IS_ARBITRARY_VALUE); T:*_THEME is a theme getter (fontWeightTheme -> T:FONT_WEIGHT_THEME);
G<"base">[...] is a nested ClassGroup (G<-> when it has no base class name).

Only the Python standard library is used. Development tool, not part of the library; see
tools/config-check/README.md.
"""
import re
import sys

if len(sys.argv) != 2:
    raise SystemExit("usage: dump_cs_config.py <TwMergeConfig.cs>")

src = open(sys.argv[1], encoding="utf-8").read()
start = src.index("public TwMergeConfig()")
end = src.index("public static TwMergeConfig Default()")
body = src[start:end]

# Tokenizer: drops // and /* */ comments (they may contain quotes and URLs) but keeps strings intact.
TOKEN = re.compile("|".join([
    r"(?P<ws>\s+)",
    r"(?P<lc>//[^\n]*)",
    r"(?P<bc>/\*.*?\*/)",
    r'(?P<str>"(?:[^"\\]|\\.)*")',
    r"(?P<spread>\.\.)",
    r"(?P<num>\d+)",
    r"(?P<id>[A-Za-z_][A-Za-z0-9_\.]*)",
    r"(?P<p>[\[\]\(\)\{\},;=])",
]), re.S)

toks = []
pos = 0
while pos < len(body):
    m = TOKEN.match(body, pos)
    if not m:
        raise SystemExit("cannot tokenize at: " + body[pos:pos + 60])
    pos = m.end()
    kind = m.lastgroup
    if kind in ("ws", "lc", "bc"):
        continue
    toks.append((kind, m.group()))

i = 0


def peek():
    return toks[i] if i < len(toks) else (None, None)


def take(expect=None):
    global i
    tok = toks[i]
    i += 1
    if expect is not None and tok[1] != expect:
        raise SystemExit("expected %r, got %r near %r" % (expect, tok, toks[max(0, i - 8):i + 3]))
    return tok


def snake(name):
    """camelCase / PascalCase -> UPPER_SNAKE_CASE (IsArbitraryValue -> IS_ARBITRARY_VALUE)."""
    return re.sub(r"(?<!^)([A-Z])", r"_\1", name).upper()


scales = {}  # scale variable name -> already expanded list of normalized definitions


class Group:
    def __init__(self, base, defs):
        self.base = base
        self.defs = defs


def parse_value():
    """string | identifier | [list] | new [ClassGroup]( args )."""
    kind, tok = peek()
    if kind == "str":
        take()
        return ("str", tok[1:-1])
    if tok == "[":
        return ("list", parse_list())
    if tok == "new":
        take()
        if peek()[1] == "ClassGroup":
            take()
        take("(")
        args = []
        while peek()[1] != ")":
            args.append(parse_value())
            if peek()[1] == ",":
                take()
        take(")")
        if len(args) == 1:
            return ("group", Group(None, args[0]))
        return ("group", Group(args[0][1], args[1]))
    if kind == "id":
        take()
        return ("id", tok)
    raise SystemExit("unexpected token %r" % (peek(),))


def parse_list():
    take("[")
    items = []
    while peek()[1] != "]":
        if peek()[0] == "spread":
            take()
            items.append(("spread", take()[1]))
        else:
            items.append(parse_value())
        if peek()[1] == ",":
            take()
    take("]")
    return items


def resolve(items):
    out = []
    for item in items:
        if item[0] == "spread":
            out.extend(scales[item[1]])
        else:
            out.append(resolve_one(item))
    return out


def resolve_one(item):
    kind, val = item
    if kind == "str":
        return '"%s"' % val
    if kind == "group":
        return ("G", val.base, resolve_defs(val.defs))
    if kind == "id":
        if val.startswith("V."):
            return "V:" + snake(val[2:])
        if val.endswith("Theme"):
            return "T:" + snake(val)
        raise SystemExit("unresolved identifier " + val)
    raise SystemExit("bad item %r" % (item,))


def resolve_defs(value):
    kind, val = value
    if kind == "list":
        return resolve(val)
    if kind == "id":
        return list(scales[val])
    raise SystemExit("bad definitions %r" % (value,))


def fmt(item):
    if isinstance(item, tuple):
        _, base, defs = item
        return "G<%s>[%s]" % ("-" if base is None else '"%s"' % base, ", ".join(fmt(d) for d in defs))
    return item


# Skip to the first statement after the constructor's opening brace.
while toks[i][1] != "{":
    i += 1
i += 1

lines = []

while i < len(toks):
    kind, tok = peek()
    if tok == "}":
        break
    # var colorTheme = ThemeUtility.FromTheme( ThemeKeys.Color );  (theme getters are named by suffix)
    if tok == "var":
        while peek()[1] != ";":
            take()
        take(";")
        continue
    # string[] / object[] xScale = [ ... ];
    if tok in ("string", "object"):
        take()
        take("[")
        take("]")
        name = take()[1]
        take("=")
        scales[name] = resolve(parse_list())
        take(";")
        continue
    # CacheSize = 500;  (Prefix is never set by the constructor)
    if tok == "CacheSize":
        take()
        take("=")
        lines.append("cacheSize=" + take()[1])
        take(";")
        lines.append("prefix=None")
        continue
    # Theme / ClassGroups / ConflictingClassGroups / ConflictingClassGroupModifiers = new( n ) { [key] = value, ... };
    if tok in ("Theme", "ClassGroups", "ConflictingClassGroups", "ConflictingClassGroupModifiers"):
        section = tok
        take()
        take("=")
        take("new")
        take("(")
        take()
        take(")")
        take("{")
        while peek()[1] != "}":
            take("[")
            key = take()[1]
            take("]")
            take("=")
            if section == "Theme":
                defs = resolve_defs(parse_value())
                lines.append("THEME %s = [%s]" % (snake(key.split(".")[1]), ", ".join(fmt(d) for d in defs)))
            elif section == "ClassGroups":
                group = parse_value()
                assert group[0] == "group", group
                lines.append("GROUP %s = %s" % (key[1:-1], fmt(("G", group[1].base, resolve_defs(group[1].defs)))))
            else:
                values = [item[1] for item in parse_value()[1]]
                label = "CONFLICT" if section == "ConflictingClassGroups" else "CONFLICTMOD"
                lines.append("%s %s = [%s]" % (label, key[1:-1], ", ".join(values)))
            if peek()[1] == ",":
                take()
        take("}")
        take(";")
        continue
    # PostfixLookupClassGroups = [...]; OrderSensitiveModifiers = [...];
    if tok in ("PostfixLookupClassGroups", "OrderSensitiveModifiers"):
        take()
        take("=")
        values = [item[1] for item in parse_list()]
        label = "POSTFIX" if tok == "PostfixLookupClassGroups" else "ORDER"
        lines.append("%s = [%s]" % (label, ", ".join(values)))
        take(";")
        continue
    raise SystemExit("unexpected statement start %r near %r" % (peek(), toks[i:i + 6]))

print("\n".join(lines))
