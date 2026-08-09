package com.example.library.service

import com.example.library.domain.Book
import com.example.library.dto.NewBookRequest
import com.example.library.model.DataInitializer
import org.springframework.stereotype.Service
import java.util.concurrent.ConcurrentHashMap
import java.util.concurrent.atomic.AtomicLong

/**
 * Book business logic (Kotlin) that DEPENDS ON the Java [AuthorService].
 *
 * INTEROP HIGHLIGHTS (Kotlin calling Java):
 *  - Constructor injection of a Java bean works exactly like injecting a
 *    Kotlin one.
 *  - `authorService.findById(...)` returns a Java `Optional<Author>`; Kotlin
 *    consumes it fluently with `.orElseThrow { ... }`.
 *  - The resulting [com.example.library.domain.Author] is a Java record used
 *    seamlessly inside a Kotlin data class ([Book]).
 */
@Service
class BookService(
    private val authorService: AuthorService,
) {
    private val books = ConcurrentHashMap<Long, Book>()
    private val ids = AtomicLong(0)

    init {
        val orderedTitles = DataInitializer.BOOK_NAMES.sorted()
        if (orderedTitles.size >= 3) {
            create(NewBookRequest(orderedTitles[0], 1, 1968, listOf("fantasy", "classic")))
            create(NewBookRequest(orderedTitles[2], 1, 1969, listOf("sci-fi")))
            create(NewBookRequest(orderedTitles[1], 2, 1993))
        }
    }

    fun create(request: NewBookRequest): Book {
        // Kotlin fluently handling a Java Optional returned across the boundary.
        val author = authorService.findById(request.authorId)
            .orElseThrow { UnknownAuthorException(request.authorId) }

        val book = Book(
            id = ids.incrementAndGet(),
            title = request.title,
            author = author,          // Java record -> Kotlin data class field
            year = request.year,
            tags = request.tags,
        )
        books[book.id] = book
        return book
    }

    fun findById(id: Long): Book =
        books[id] ?: throw BookNotFoundException(id)

    fun findAll(): List<Book> = books.values.sortedBy { it.id }

    /**
     * Uses a default argument (`includeTags`). Because Kotlin default args are
     * invisible to Java, this method is also exported with `@JvmOverloads` so a
     * Java caller can invoke `findByAuthor(id)` without the second argument.
     */
    @JvmOverloads
    fun findByAuthor(authorId: Long, includeTags: Boolean = true): List<Book> =
        books.values
            .filter { it.author.id() == authorId }   // it.author.id() -> Java record accessor
            .map { if (includeTags) it else it.copy(tags = emptyList()) }
            .sortedBy { it.id }

    /** Demonstrates `data class` immutability + `copy()` for updates. */
    fun retitle(id: Long, newTitle: String): Book {
        val existing = findById(id)
        val updated = existing.copy(title = newTitle)
        books[id] = updated
        return updated
    }
}
