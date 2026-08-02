package com.example.library.web

import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.get
import org.springframework.test.web.servlet.post

/**
 * MVC slice exercised end-to-end with the Kotlin MockMvc DSL. Also proves the
 * Jackson 3 Kotlin module serialises the Kotlin [com.example.library.domain.Book]
 * data class (with its nested Java `Author` record) to JSON.
 */
@SpringBootTest
@AutoConfigureMockMvc
class BookControllerTest(
    // Explicit `@param:` use-site target keeps the annotation on the
    // constructor parameter (where Spring's test runner looks) and avoids a
    // Kotlin forward-compatibility warning — the same use-site-target lesson
    // as in NewBookRequest.
    @param:Autowired val mockMvc: MockMvc,
) {

    @Test
    fun `lists seeded books with nested author record`() {
        mockMvc.get("/api/books").andExpect {
            status { isOk() }
            jsonPath("$[0].title") { exists() }
            jsonPath("$[0].author.name") { exists() }
        }
    }

    @Test
    fun `creating a book with an existing author returns 201`() {
        mockMvc.post("/api/books") {
            contentType = MediaType.APPLICATION_JSON
            content = """{"title":"Tehanu","authorId":1,"year":1990,"tags":["fantasy"]}"""
        }.andExpect {
            status { isCreated() }
            jsonPath("$.title") { value("Tehanu") }
            jsonPath("$.author.id") { value(1) }
        }
    }

    @Test
    fun `invalid book yields a ProblemDetail 400 with field errors`() {
        mockMvc.post("/api/books") {
            contentType = MediaType.APPLICATION_JSON
            content = """{"title":"","authorId":0,"year":1000}"""
        }.andExpect {
            status { isBadRequest() }
            jsonPath("$.title") { value("Validation failed") }
            jsonPath("$.errors.title") { exists() }
        }
    }

    @Test
    fun `unknown book yields a ProblemDetail 404`() {
        mockMvc.get("/api/books/9999").andExpect {
            status { isNotFound() }
            jsonPath("$.bookId") { value(9999) }
        }
    }
}
