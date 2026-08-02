package com.example.library.web

import com.example.library.domain.Book
import com.example.library.dto.NewBookRequest
import com.example.library.service.BookService
import jakarta.validation.Valid
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PatchMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestController

/**
 * Spring MVC REST controller for books, written in Kotlin.
 *
 * Uses the classic MVC servlet stack (`spring-boot-starter-web`): the
 * `@RestController` maps HTTP requests to handler methods and returns objects
 * that Jackson 3 serialises to JSON. Returned [Book]s are Kotlin data classes
 * whose nested `author` is a Java record — both serialise cleanly thanks to
 * the Jackson 3 Kotlin module.
 */
@RestController
@RequestMapping("/api/books")
class BookController(
    private val bookService: BookService,
) {
    @GetMapping
    fun all(): List<Book> = bookService.findAll()

    @GetMapping("/{id}")
    fun byId(@PathVariable id: Long): Book = bookService.findById(id)

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    fun create(@Valid @RequestBody request: NewBookRequest): ResponseEntity<Book> {
        val created = bookService.create(request)
        return ResponseEntity
            .created(java.net.URI.create("/api/books/${created.id}"))
            .body(created)
    }

    @PatchMapping("/{id}/title")
    fun retitle(
        @PathVariable id: Long,
        @RequestParam value: String,
    ): Book = bookService.retitle(id, value)
}
