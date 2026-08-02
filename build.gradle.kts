import org.jetbrains.kotlin.gradle.dsl.JvmTarget

// ---------------------------------------------------------------------------
// A mixed Java + Kotlin Spring Boot 4.1 project.
//
// The Kotlin Gradle plugin ("kotlin.jvm") also applies the `java` plugin, so
// BOTH src/main/kotlin and src/main/java are compiled in the same module.
// Compilation happens in two phases so the two languages can see each other:
//   1. Kotlin is compiled first (it can read the Java *sources*).
//   2. Java is compiled next (it can read the Kotlin *class output*).
// That two-way visibility is what makes the interop demos in this repo work.
// ---------------------------------------------------------------------------

plugins {
    kotlin("jvm") version "2.3.21"
    // Makes Spring-annotated Kotlin classes `open` automatically (Spring needs
    // to subclass them for proxies; Kotlin classes are `final` by default).
    kotlin("plugin.spring") version "2.3.21"
    id("org.springframework.boot") version "4.1.0"
    id("io.spring.dependency-management") version "1.1.7"
}

group = "com.example"
version = "0.0.1-SNAPSHOT"
description = "Learning Java <-> Kotlin interop on Spring Boot 4.1"

java {
    // Build and run on the latest installed JDK (26).
    toolchain {
        languageVersion = JavaLanguageVersion.of(26)
    }
}

repositories {
    mavenCentral()
}

dependencies {
    // --- Spring MVC (servlet stack) REST + supporting starters ---
    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation("org.springframework.boot:spring-boot-starter-validation")
    implementation("org.springframework.boot:spring-boot-starter-actuator")

    // --- Kotlin support ---
    implementation("org.jetbrains.kotlin:kotlin-reflect")
    // Spring Boot 4 uses Jackson 3 (group id `tools.jackson`, not the old
    // `com.fasterxml.jackson`). This is the Jackson-3 Kotlin module that
    // teaches Jackson how to (de)serialize Kotlin data classes.
    implementation("tools.jackson.module:jackson-module-kotlin")

    // --- Tests ---
    testImplementation("org.springframework.boot:spring-boot-starter-test")
    // Spring Boot 4 modularised the test slices: @AutoConfigureMockMvc /
    // @WebMvcTest now live here, no longer in spring-boot-starter-test.
    testImplementation("org.springframework.boot:spring-boot-webmvc-test")
    testImplementation("org.jetbrains.kotlin:kotlin-test-junit5")
    testRuntimeOnly("org.junit.platform:junit-platform-launcher")
}

kotlin {
    compilerOptions {
        // Kotlin 2.3 does not yet emit JVM 26 bytecode; target a level both
        // compilers agree on. The code still RUNS on the JDK 26 toolchain.
        jvmTarget = JvmTarget.JVM_24
        // Honour JSR-305 / JSpecify nullability annotations on Java APIs as
        // strict Kotlin nullability — improves Java->Kotlin null safety.
        freeCompilerArgs.addAll("-Xjsr305=strict")
    }
}

tasks.withType<JavaCompile>().configureEach {
    // Must match the Kotlin jvmTarget above, otherwise Gradle fails with an
    // "inconsistent JVM-target compatibility" error between the two tasks.
    options.release = 24
    // Keep parameter names in the bytecode (Spring/Jackson use them). The
    // Spring Boot plugin enables this by default, but we make it explicit.
    options.compilerArgs.add("-parameters")
}

tasks.withType<Test> {
    useJUnitPlatform()
}
