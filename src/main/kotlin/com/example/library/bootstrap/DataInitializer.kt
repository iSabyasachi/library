package com.example.library.bootstrap

import com.example.library.dto.NewBookRequest
import com.example.library.service.AuthorService
import com.example.library.service.BookService
import org.springframework.boot.ApplicationArguments
import org.springframework.boot.ApplicationRunner
import org.springframework.stereotype.Component

/**
 * Seeds in-memory demo data at startup. Written in Kotlin, it drives both the
 * Java [AuthorService] and the Kotlin [BookService].
 */
@Component
class DataInitializer(
    private val authorService: AuthorService,
    private val bookService: BookService,
) : ApplicationRunner {

    override fun run(args: ApplicationArguments) {
        authorService.create(1, "Ursula K. Le Guin", "Author of the Earthsea series")
        authorService.create(2, "Andrzej Sapkowski", null) // null biography -> Java @Nullable

        bookService.create(NewBookRequest(
            title = "A Wizard of Earthsea",
            authorId = 1,
            year = 1968,
            tags = listOf("fantasy", "classic"),
        ))
        bookService.create(NewBookRequest(
            title = "The Left Hand of Darkness",
            authorId = 1,
            year = 1969,
            tags = listOf("sci-fi"),
        ))
        bookService.create(NewBookRequest(
            title = "The Last Wish",
            authorId = 2,
            year = 1993,
        ))
    }
}
