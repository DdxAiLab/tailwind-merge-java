# Análisis de una clase individual (`TwMerge.parse`)

## Motivación

`merge` ya descompone internamente cada clase (variantes, modificador `!`, modificador tras `/`, grupo de clase), pero esa
interpretación no era accesible. Quien necesitara entender una clase tenía que escribir su propio analizador, con el riesgo
de que no coincidiera con la librería.

`TwMerge.parse` expone esa interpretación como una funcionalidad pública y genérica: usa el mismo separador de partes y la
misma resolución de grupo de clase que `merge`, con la configuración de la instancia (`prefix`, `theme`, `classGroups`,
`legacyBgOpacity`). No hay un segundo analizador: la resolución del grupo se extrajo a un método privado que ahora comparten
`merge` y `parse`.

El comportamiento y la API existente de `merge` no cambian. Se mantiene Java 8 y no se añaden dependencias en tiempo de
ejecución.

## API

### `TwMerge`

```java
public ParsedClass parse(String className)
```

Lanza `IllegalArgumentException` si `className` es `null`, está en blanco o contiene más de una clase.

### `ParsedClass`

Paquete `com.github.ddxailab.tailwindmerge`. Inmutable; solo lo crea `TwMerge.parse`.

| Firma | Contenido |
| --- | --- |
| `String getClassName()` | La clase analizada, sin espacios alrededor |
| `List<String> getVariants()` | Variantes en el orden escrito, sin `:`; incluye las arbitrarias. Lista no modificable |
| `boolean isImportant()` | `!` al final (`p-4!`) o al inicio de la clase base, estilo v3 (`!p-4`) |
| `String getBaseClassName()` | Sin prefijo, variantes, `!` ni modificador. Conserva el `-` de los valores negativos |
| `boolean isNegative()` | Clase reconocida cuya clase base empieza por `-` |
| `String getModifier()` | Texto tras `/` cuando es un modificador, tal como está escrito; `""` si no hay |
| `Optional<ArbitraryValue> getArbitraryValue()` | Valor `[..]` o `(..)` de la utilidad |
| `Optional<ParsedClass.ArbitraryProperty> getArbitraryProperty()` | Declaración de una propiedad arbitraria (`[padding:1rem]`) |
| `String getClassGroupId()` | ID del grupo de clase; `""` si no se reconoce |
| `boolean isRecognized()` | `true` si pertenece a un grupo de clase |
| `boolean isExternal()` | `true` si hay `prefix` configurado y la clase no lo lleva |
| `String toString()` | Igual que `getClassName()` |

### `ParsedClass.ArbitraryProperty`

```java
public String getName()    // "padding"
public String getValue()   // "1rem"
```

### `ArbitraryValue`

Paquete `com.github.ddxailab.tailwindmerge.common`. Inmutable. Aplica las mismas expresiones regulares que
`Validators.IS_ARBITRARY_VALUE` e `IS_ARBITRARY_VARIABLE`.

```java
public static Optional<ArbitraryValue> from(String text)   // "[length:var(--x)]", "(--x)"
public ArbitraryValue.Syntax getSyntax()                   // BRACKETS o PARENTHESES
public String getLabel()                                   // "length", "family-name", ... o ""
public String getValue()                                   // sin delimitadores ni etiqueta
```

`from` devuelve un `Optional` vacío si el texto es `null` o no está encerrado entre `[]` o `()` con contenido.

## Ejemplos

```java
TwMerge twMerge = TwMerge.getDefault();

ParsedClass parsed = twMerge.parse("md:hover:bg-red-300/50!");
parsed.getVariants();       // ["md", "hover"]
parsed.isImportant();       // true
parsed.getBaseClassName();  // "bg-red-300"
parsed.getModifier();       // "50"
parsed.getClassGroupId();   // "bg-color"

ArbitraryValue value = twMerge.parse("text-(length:--size)").getArbitraryValue().get();
value.getSyntax();          // PARENTHESES
value.getLabel();           // "length"
value.getValue();           // "--size"

ParsedClass.ArbitraryProperty property =
        twMerge.parse("[padding:1rem]").getArbitraryProperty().get();
property.getName();         // "padding"
property.getValue();        // "1rem"
```

| Clase | `variants` | `baseClassName` | `modifier` | `classGroupId` | Otros |
| --- | --- | --- | --- | --- | --- |
| `p-4` | `[]` | `p-4` | `""` | `p` | |
| `-m-4` | `[]` | `-m-4` | `""` | `m` | negativo |
| `!p-4` | `[]` | `p-4` | `""` | `p` | important |
| `[&>*]:underline` | `[[&>*]]` | `underline` | `""` | `text-decoration` | |
| `w-1/2` | `[]` | `w-1/2` | `""` | `w` | fracción |
| `aspect-4/3` | `[]` | `aspect-4/3` | `""` | `aspect` | fracción |
| `text-lg/7` | `[]` | `text-lg` | `7` | `font-size` | |
| `bg-red-300/50` | `[]` | `bg-red-300` | `50` | `bg-color` | |
| `w-[10px]` | `[]` | `w-[10px]` | `""` | `w` | valor `10px`, corchetes |
| `font-(family-name:--f)` | `[]` | `font-(family-name:--f)` | `""` | `font-family` | etiqueta `family-name`, paréntesis |
| `[padding:1rem]` | `[]` | `[padding:1rem]` | `""` | `arbitrary..padding` | propiedad arbitraria |
| `hover:foo/50` | `[hover]` | `foo/50` | `""` | `""` | no reconocida |

## Casos límite

### Entrada

| Entrada | Resultado |
| --- | --- |
| `null` | `IllegalArgumentException` |
| `""` o solo espacios | `IllegalArgumentException` |
| Varias clases (`"p-4 m-2"`) | `IllegalArgumentException` |
| Espacio dentro de corchetes (`"[padding: 1rem]"`) | `IllegalArgumentException`: son dos clases, como en `merge` |
| Espacios alrededor (`" p-4 "`) | Se ignoran; `getClassName()` devuelve `p-4` |

### Prefijo

Con `prefix = "tw"`:

- `tw:hover:p-4` se analiza con normalidad; `tw` no figura entre las variantes y `getClassName()` conserva la clase completa.
- `p-4` o `hover:tw:p-4` (sin el prefijo al inicio) no son un error: `isExternal()` es `true`, `isRecognized()` es `false`,
  `getBaseClassName()` es la clase completa, no hay variantes ni modificadores y `!` no se interpreta.

Sin `prefix` configurado, `isExternal()` siempre es `false`.

### Clases no reconocidas

Se rellenan la clase original, las variantes, `isImportant()` y la clase base (todo lo que queda tras variantes, prefijo y
`!`, incluida cualquier `/`). `isNegative()` es `false`, el modificador es `""` y no hay valor ni propiedad arbitraria.

"Reconocida" significa que la configuración asigna la clase a un grupo, no que Tailwind genere CSS para ella: los grupos de
color aceptan cualquier nombre, por lo que `bg-cualquiercosa` se reconoce como `bg-color`.

### Barra `/`

- No es modificador cuando forma parte del valor: el grupo declara `Validators.IS_FRACTION` (o una definición literal con
  `/`) para ese valor. Una configuración propia que acepte fracciones con otro validador verá la barra como modificador.
- No es modificador cuando `merge` resuelve el grupo con el nombre completo (`aspect-4/3`, `@container/sidebar`, que es del
  grupo `container-named`).
- Las barras dentro de `[]` o `()` nunca separan.
- Con varias barras solo cuenta la última (`w-1/2/50`: base `w-1/2`, modificador `50`).
- Una barra final (`bg-red-300/`) se deja en la clase base; el modificador es `""`.
- El modificador se entrega en bruto (`[0.5]`, `(--leading)`). Para separarlo se puede usar `ArbitraryValue.from`.

### Valores arbitrarios

- `getArbitraryValue()` informa solo del valor de la utilidad: el `[..]` o `(..)` que sigue a un `-` y termina la clase base.
  Los de variantes, modificadores y propiedades arbitrarias no aparecen ahí.
- Los valores se devuelven tal como están escritos: `_` no se convierte en espacio.
- `[foo]` (sin `:`) no es una propiedad arbitraria: la clase no se reconoce.

### Otros

- Solo se quita un `!`: `p-4!!` no se reconoce.
- Con `legacyBgOpacity` activado (valor por defecto), `bg-opacity-50` tiene el grupo `bg-opacity`, el mismo que usa `merge`.
  Ese ID no existe en `TwMergeConfig.getClassGroups()`. Desactivado, es `bg-color`.
- El resultado no se guarda en la caché de `merge`.
- `parse` es seguro entre hilos (siempre que lo sean los validadores propios de la configuración).

### Limitación: `!` inicial combinado con un modificador

Cuando una clase usa el `!` inicial de v3 **y además** un modificador, `merge` calcula la posición de la barra desplazada un
carácter. Es un comportamiento heredado de la librería de referencia. `parse` separa bien la clase base y el modificador,
pero informa del mismo grupo que usa `merge`, que puede no ser el esperado:

| Clase | Grupo con `!` inicial | Grupo con `!` final |
| --- | --- | --- |
| `text-lg/7`, `text-[14px]/6` | `text-color` | `font-size` |
| `shadow-lg/50` | `shadow-color` | `shadow` |
| `inset-shadow-sm/40` | `inset-shadow-color` | `inset-shadow` |
| `drop-shadow-lg/50` | `drop-shadow-color` | `drop-shadow` |
| `text-shadow-lg/50` | `text-shadow-color` | `text-shadow` |
| `bg-linear-45/oklch`, `bg-conic-180/oklch` | `bg-color` | `bg-image` |
| `ring-2/50` | `ring-color` | `ring-w` |
| `leading-6/2`, `[color:red]/50` | no reconocida | reconocida |

La tabla recoge casos comprobados; puede haber otros. Con el `!` final, o con el inicial sin modificador, no ocurre.
Corregirlo cambiaría el resultado de `merge`, así que queda fuera de este cambio.

## Archivos modificados

| Archivo | Cambio |
| --- | --- |
| `src/main/java/.../tailwindmerge/TwMerge.java` | Método `parse`; resolución del grupo extraída a `resolve`, compartida con `merge` |
| `src/main/java/.../tailwindmerge/ParsedClass.java` | Nuevo: resultado del análisis |
| `src/main/java/.../tailwindmerge/TwMergeContext.java` | `isSlashPartOfValue` y constante del prefijo de grupo de las propiedades arbitrarias |
| `src/main/java/.../common/ArbitraryValue.java` | Nuevo: valor o variable arbitraria con su etiqueta |
| `src/main/java/.../common/Validators.java` | Dos patrones pasan de privados a visibles en el paquete para reutilizarlos |
| `src/test/java/.../tailwindmerge/ParseTest.java` | Nuevo |
| `src/test/java/.../common/ArbitraryValueTest.java` | Nuevo |
| `pom.xml` | Versión 0.4.0 |
| `docs/api-reference.md` | Referencia de la API nueva |
| `docs/CHECKLIST.md` | Fila en la tabla de decisiones y publicación 0.4.0 |
| `docs/features-20261127-es.md` | Este documento |
| `docs/features-20261127-en.md` | Versión en inglés de este documento |

## Resultado de las pruebas

- `mvn -B -ntp clean verify` correcto en JDK 8 (1.8.0_211), 11.0.24 y 17.0.12: **1041 pruebas**, 0 fallos, 0 errores,
  0 omitidas. Antes del cambio eran 936; las 105 nuevas son 91 de `ParseTest` y 14 de `ArbitraryValueTest`.
- `javadoc -Xdoclint:all` (JDK 17) sin avisos.
- Comparación de `merge` entre el jar anterior al cambio y el código nuevo, con y sin prefijo: 600 000 llamadas con
  entradas generadas a partir de las pruebas y de fragmentos aleatorios, sin ninguna diferencia.
- `parse` sobre 283 667 entradas generadas del mismo modo, sin excepciones distintas de `IllegalArgumentException`.
- `tools/config-check`: la configuración por defecto sigue siendo idéntica a la de referencia.

No verificado: el consumo del artefacto desde JitPack (requiere publicar un tag).

## Versión

**0.4.0** (tag `v0.4.0`): añade API pública sin romper la existente.
