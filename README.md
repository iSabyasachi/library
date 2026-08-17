# Spring Boot 4.1 — Java + Kotlin Interop (MVC REST)

A small **Book Library** REST API built to learn two things at once:

1. **How Java and Kotlin interoperate** in a single Spring Boot module — in *both*
   directions (Java calling Kotlin and Kotlin calling Java).
2. **The latest Spring Boot 4.1 / Spring Framework 7 features.**

It uses the classic **Spring MVC** servlet stack (`spring-boot-starter-web`) with
`@RestController`s to expose the REST API.

- Spring Boot **4.1.0**, Spring Framework **7.0.x**
- Kotlin **2.3.21**, Java **26** (Gradle toolchain), Gradle **9.6.1**
- Jackson **3** (Boot 4's new default JSON stack)

---

## Prerequisites & running

Only a JDK (17+) is needed — the Gradle **wrapper** bootstraps everything else.
This repo was built and verified on **JDK 26**.

```powershell
# Windows PowerShell (JAVA_HOME must point at a JDK)
$env:JAVA_HOME = "C:\Program Files\Java\jdk-26.0.1"

.\gradlew.bat build      # compile Java + Kotlin, run all tests
.\gradlew.bat bootRun    # start the app on http://localhost:8080
```

```bash
# macOS / Linux
./gradlew build
./gradlew bootRun

tree .
./gradlew
./gradlew help
./gradlew tiTree assemble
./gradlew dep
./gradlew projectHealth
./gradlew compileJava --build-cache
./gradlew compileJava -I
./gradlew compileJava "-Dorg.gradle.caching.debug=true"
```

---

## Project layout

Both `src/main/java` and `src/main/kotlin` are compiled in the **same module**.
The Kotlin Gradle plugin compiles Kotlin first (reading Java *sources*), then Java
compiles reading the Kotlin *class output* — that two-phase build is what enables
the bidirectional interop below.

```
src/main
├── java/com/example/library
│   ├── domain/Author.java              # Java record  (consumed by Kotlin)
│   ├── service/AuthorRepository.java    # Java, returns Optional<Author>
│   ├── service/AuthorService.java       # Java service (consumed by Kotlin)
│   └── web/AuthorController.java         # Java @RestController -> Kotlin service
│       + GlobalExceptionHandler.java     # Java @RestControllerAdvice -> ProblemDetail
└── kotlin/com/example/library
    ├── LibraryApplication.kt             # Kotlin main()
    ├── domain/Book.kt                    # Kotlin data class (consumed by Java)
    ├── dto/NewBookRequest.kt             # Kotlin DTO + Jakarta validation
    ├── service/BookService.kt            # Kotlin service -> Java AuthorService
    ├── service/BookNotFoundException.kt   # Kotlin exceptions (handled by Java)
    ├── web/BookController.kt              # Kotlin @RestController
    ├── web/WebConfig.kt + GreetingController.kt   # API versioning
    └── bootstrap/DataInitializer.kt      # seeds demo data via both services
```

---

## Java ↔ Kotlin interop demonstrated

| # | Direction | Where | What to notice |
|---|-----------|-------|----------------|
| 1 | Kotlin **uses** a Java `record` | `Book.kt` holds an `Author` (Java record) | Kotlin reads record accessors (`author.id()`); records are Java's answer to `data class`. |
| 2 | Java **uses** a Kotlin `data class` | `AuthorController.java` reads `Book` | Kotlin properties expose JavaBean getters (`book.getTitle()`). |
| 3 | Kotlin **calls** a Java service | `BookService.kt` → `AuthorService` | Consumes a Java `Optional<Author>` fluently with `.orElseThrow { … }`. |
| 4 | Java **calls** a Kotlin service | `AuthorController.java` → `BookService` | Calls a Kotlin method that has a **default argument** — possible only because the Kotlin side is `@JvmOverloads`. |
| 5 | **Nullability** across the boundary | `Author.java` `@Nullable biography` | With `-Xjsr305=strict`, Kotlin treats it as `String?` instead of a lenient platform type. |
| 6 | **Exceptions** across the boundary | Kotlin throws → Java `@RestControllerAdvice` catches | `BookNotFoundException`/`UnknownAuthorException` are Kotlin classes handled by Java. |
| 7 | **Annotation use-site targets** | `NewBookRequest.kt` `@field:NotBlank`, test `@param:Autowired` | A Kotlin `val` maps to several JVM elements; `@field:`/`@param:` place the annotation where the Java framework looks. |
| 8 | **Immutability / `copy()`** | `BookService.retitle()` | Idiomatic Kotlin update of an immutable `data class`. |

---

## Java vs Kotlin (same Spring feature)

To compare readability directly, this repo now includes the same "find books by
author" feature implemented twice:

- Java API: `src/main/java/com/example/library/compare/javaapi/`
- Kotlin API: `src/main/kotlin/com/example/library/compare/kotlinapi/`

Both endpoints return the same result set:

- `GET /api/compare/java/books?author=Orwell`
- `GET /api/compare/kotlin/books?author=Orwell`

| Layer | Java example | Kotlin example | What changes most |
|---|---|---|---|
| Model | `JavaBook.java` | `KotlinBookApi.kt` (`data class KotlinBook`) | Kotlin removes POJO boilerplate (`equals/hashCode/toString` generation). |
| Repository | `JavaBookRepository.java` | `KotlinBookRepository` | Similar structure; Kotlin syntax is shorter. |
| Service | `JavaBookService.java` | `KotlinBookService` | Stream + collectors vs concise `filter { ... }`. |
| Controller | `JavaBookController.java` | `KotlinBookController` | Both use constructor injection; Kotlin is less verbose. |

Quick manual check:

```bash
curl "localhost:8080/api/compare/java/books?author=Orwell"
curl "localhost:8080/api/compare/kotlin/books?author=Orwell"
```

Automated parity test:

- `src/test/kotlin/com/example/library/compare/JavaVsKotlinBookApiTest.kt`

---

## Latest Spring Boot 4.1 / Spring 7 features demonstrated

- **Jackson 3** — Boot 4's default JSON engine (group id `tools.jackson`, not the old
  `com.fasterxml.jackson`). We add `tools.jackson.module:jackson-module-kotlin` so Kotlin
  `data class`es (de)serialize correctly. See `build.gradle.kts`.
- **API versioning** (Spring Framework 7) — `WebConfig.kt` enables header-based versioning
  (`X-API-Version`); `GreetingController.kt` maps the same path to `version = "1"` / `"2"`.
- **`ProblemDetail`** (RFC 9457) — `GlobalExceptionHandler.java` returns structured error
  bodies for both validation failures and not-found errors.
- **Modularized test slices** — in Boot 4, `@AutoConfigureMockMvc` / `@WebMvcTest` moved out
  of `spring-boot-starter-test` into `org.springframework.boot:spring-boot-webmvc-test`
  (package `org.springframework.boot.webmvc.test.autoconfigure`).
- **JSpecify nullability** — `@org.jspecify.annotations.Nullable` (bundled with Spring 7).
- **Kotlin idioms** — `runApplication<…>()`, constructor injection, the `kotlin.plugin.spring`
  compiler plugin (auto-`open`s beans for proxying).

---

## Endpoint reference

```bash
# Books (Kotlin controller)
curl localhost:8080/api/books
curl localhost:8080/api/books/1
curl -X POST localhost:8080/api/books \
  -H "Content-Type: application/json" \
  -d '{"title":"Tehanu","authorId":1,"year":1990,"tags":["fantasy"]}'
curl -X PATCH "localhost:8080/api/books/1/title?value=New+Title"

# Authors (Java controller, delegates to Kotlin service)
curl localhost:8080/api/authors
curl localhost:8080/api/authors/1
curl localhost:8080/api/authors/1/books

# API versioning (Spring 7)
curl localhost:8080/api/greeting -H "X-API-Version: 1"
curl localhost:8080/api/greeting -H "X-API-Version: 2"
curl localhost:8080/api/greeting                       # -> default version 1

# Error responses (ProblemDetail)
curl -i localhost:8080/api/books/9999                  # 404
curl -i -X POST localhost:8080/api/books \
  -H "Content-Type: application/json" -d '{"title":"","authorId":0,"year":1000}'   # 400

# Actuator
curl localhost:8080/actuator/health
curl localhost:8080/actuator/mappings
```

---

## Build notes & gotchas encountered

- **Kotlin JVM bytecode target is 24, not 26.** Kotlin 2.3 doesn't yet *emit* JVM 26
  bytecode, so `build.gradle.kts` pins both the Kotlin `jvmTarget` and Java `release` to
  24 (they must match or Gradle fails with an "inconsistent JVM-target" error). The code
  still **runs** on the JDK 26 toolchain. Bump both when Kotlin adds JVM 26 output.
- **Spring Initializr `4.1.0.RELEASE`** — Initializr labels the version with a legacy
  `.RELEASE` suffix that doesn't resolve on Maven Central (the real artifact is `4.1.0`),
  so this project was scaffolded by hand rather than downloaded from `start.spring.io`.
```
