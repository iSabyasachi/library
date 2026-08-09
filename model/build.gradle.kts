plugins {
    id("java-library")
}

group = "com.example"
version = "0.0.1-SNAPSHOT"

java {
    toolchain {
        languageVersion = JavaLanguageVersion.of(26)
    }
}

repositories {
    mavenCentral()
}

dependencies {
    // Exposed to consumers so they can compile against ImmutableSet in this module's API.
    api("com.google.guava:guava:33.6.0-jre")
}

tasks.withType<JavaCompile>().configureEach {
    options.release = 24
}

