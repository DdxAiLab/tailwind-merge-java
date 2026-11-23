# tailwind-merge-java

A utility to efficiently merge [Tailwind CSS](https://tailwindcss.com) classes in Java without style conflicts.

> **Status: work in progress.** The project is being ported from [tailwind-merge-dotnet](https://github.com/desmondinho/tailwind-merge-dotnet). At the moment `TwMerge.merge` only joins class names and normalizes whitespace; conflict resolution is added step by step. Follow the progress in the [conversion checklist](docs/CHECKLIST.md).

- Targets Tailwind CSS v4 up to v4.3 (same target as the reference project)
- Thread-safe LRU caching *(planned)*
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

String classes = twMerge.merge("px-2 py-1", "bg-red-500");
// -> "px-2 py-1 bg-red-500"
```

## Get Started

- [Conversion checklist](docs/CHECKLIST.md)
- What is it for *(coming soon)*
- Features *(coming soon)*
- Limitations *(coming soon)*
- Configuration *(coming soon)*
- API reference *(coming soon)*

## Building

```bash
mvn clean verify
```

Requires Maven and JDK 8 or newer.

## License

[MIT](LICENSE)

## Acknowledgements 🙏

This project is a Java adaptation of [tailwind-merge-dotnet](https://github.com/desmondinho/tailwind-merge-dotnet) by [desmondinho](https://github.com/desmondinho), which in turn is a C# adaptation of [tailwind-merge](https://github.com/dcastil/tailwind-merge) originally developed by [dcastil](https://github.com/dcastil).
