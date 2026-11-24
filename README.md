# tailwind-merge-java

A utility to efficiently merge [Tailwind CSS](https://tailwindcss.com) classes in Java without style conflicts.

- Targets Tailwind CSS v4 up to v4.3 (same target as the reference project)
- Thread-safe LRU caching
- Compatible with Java 8 or newer
- No runtime dependencies

## Installation

The library is distributed through [JitPack](https://jitpack.io).

### Maven

```xml
<repositories>
    <repository>
        <id>jitpack.io</id>
        <url>https://jitpack.io</url>
    </repository>
</repositories>

<dependencies>
    <dependency>
        <groupId>com.github.DdxAiLab</groupId>
        <artifactId>tailwind-merge-java</artifactId>
        <version>TAG</version>
    </dependency>
</dependencies>
```

### Gradle

```groovy
repositories {
    maven { url 'https://jitpack.io' }
}

dependencies {
    implementation 'com.github.DdxAiLab:tailwind-merge-java:TAG'
}
```

Replace `TAG` with a release tag (or a commit hash / `main-SNAPSHOT`).

## Usage

```java
import com.github.ddxailab.tailwindmerge.TwMerge;

TwMerge twMerge = new TwMerge();

twMerge.merge("px-2 py-1 bg-red hover:bg-dark-red", "p-3 bg-[#B91C1C]");
// -> "hover:bg-dark-red p-3 bg-[#B91C1C]"
```

`TwMerge` is thread-safe, so create it once and share it; `TwMerge.getDefault()` gives a lazily created shared
instance with the default configuration. To adapt it to a custom Tailwind config see the
[configuration guide](docs/configuration.md).

## Get Started

- [What is it for](docs/what-is-it-for.md)
- [Features](docs/features.md)
- [Limitations](docs/limitations.md)
- [Configuration](docs/configuration.md)
- [API reference](docs/api-reference.md)

## Contributing

Contributions are always welcome!

See [CONTRIBUTING.md](CONTRIBUTING.md) for ways to get started. Please adhere to this project's [CODE_OF_CONDUCT](CODE_OF_CONDUCT.md). The [development guide](docs/DEVELOPMENT.md) describes the project layout, conventions and release process, and the [conversion checklist](docs/CHECKLIST.md) records how the port from the C# reference was done.

## Building

```bash
mvn clean verify
```

Requires Maven and JDK 8 or newer.

## License

[MIT](LICENSE)

## Acknowledgements 🙏

This project is a Java adaptation of [tailwind-merge-dotnet](https://github.com/desmondinho/tailwind-merge-dotnet) by [desmondinho](https://github.com/desmondinho), which in turn is a C# adaptation of [tailwind-merge](https://github.com/dcastil/tailwind-merge) originally developed by [dcastil](https://github.com/dcastil).
