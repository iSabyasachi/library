package com.example.library.domain

/**
 * A book, modelled as a Kotlin `data class`.
 *
 * INTEROP NOTES:
 *  - `data class` auto-generates `equals`/`hashCode`/`toString`/`copy` and
 *    component functions. Java code can construct and read it like a normal
 *    POJO (`new Book(...)`, `book.getTitle()`), because Kotlin properties
 *    expose JavaBean-style getters to the JVM.
 *  - The `author` property is a Java record ([Author]) — a Kotlin class
 *    holding a Java type. Interop flows both directions.
 *  - `tags` has a default value; combined with `@JvmOverloads` the Kotlin
 *    constructor also produces overloads Java can call WITHOUT passing tags.
 *    (Java has no notion of default arguments, so without @JvmOverloads Java
 *    would be forced to always pass every parameter.)
 */
data class Book @JvmOverloads constructor(
    val id: Long,
    val title: String,
    val author: Author,
    val year: Int,
    val tags: List<String> = emptyList(),
)
