package com.example.library.compare.kotlinapi

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
    private val books = listOf(
        KotlinBook(1, "1984", "Orwell"),
        KotlinBook(2, "Animal Farm", "Orwell"),
        KotlinBook(3, "Dune", "Herbert"),
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

