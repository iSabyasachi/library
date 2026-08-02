package com.example.library.dto

import jakarta.validation.constraints.Min
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.Positive

/**
 * Request body for creating a book (Kotlin data class used as an MVC DTO).
 *
 * INTEROP NOTE — annotation use-site targets:
 * A Kotlin constructor `val` becomes several JVM elements (a constructor
 * parameter, a private field, a getter). By default an annotation would land
 * on only one of them. The `@field:` / `@get:` prefixes tell Kotlin exactly
 * where to place the annotation so Jakarta Bean Validation (a Java framework)
 * actually sees it. This is one of the most common Java<->Kotlin gotchas.
 */
data class NewBookRequest(
    @field:NotBlank(message = "title is required")
    val title: String,

    @field:Positive(message = "authorId must be positive")
    val authorId: Long,

    @field:Min(value = 1450, message = "year looks too early for a printed book")
    val year: Int,

    val tags: List<String> = emptyList(),
)
