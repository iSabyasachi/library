package com.example.library.compare.kotlinapi

import com.example.library.model.DataInitializer
import org.springframework.stereotype.Repository
import org.springframework.stereotype.Service
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

data class KotlinBook(
    val id: Long,
    val title: String,
    val author: String,
)

@Repository
class KotlinBookRepository {
    private val authorNames = DataInitializer.AUTHOR_NAMES.sorted()
    private val bookNames = DataInitializer.BOOK_NAMES.sorted()

    private val books = listOf(
        KotlinBook(1, bookNames[0], authorNames[0]),
        KotlinBook(2, bookNames[2], authorNames[0]),
        KotlinBook(3, bookNames[1], authorNames[1]),
    )

    fun findAll(): List<KotlinBook> = books
}

@Service
class KotlinBookService(
    private val repository: KotlinBookRepository,
) {
    fun findByAuthor(author: String): List<KotlinBook> =
        repository.findAll()
            .filter { it.author.equals(author, ignoreCase = true) }
}

@RestController
@RequestMapping("/api/compare/kotlin/books")
class KotlinBookController(
    private val service: KotlinBookService,
) {
    @GetMapping
    fun getBooks(@RequestParam author: String): List<KotlinBook> =
        service.findByAuthor(author)
}

