package com.example.library

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

/**
 * Application entry point, written in Kotlin.
 *
 * `runApplication<LibraryApplication>(*args)` is the idiomatic Kotlin helper
 * that wraps `SpringApplication.run(LibraryApplication::class.java, *args)`.
 * The component scan rooted here picks up beans written in BOTH Java and
 * Kotlin that live under `com.example.library`.
 */
@SpringBootApplication
class LibraryApplication

fun main(args: Array<String>) {
    runApplication<LibraryApplication>(*args)
}
