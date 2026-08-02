package com.example.library.web;

import com.example.library.domain.Author;
import com.example.library.domain.Book;
import com.example.library.service.AuthorService;
import com.example.library.service.BookService;
import com.example.library.service.UnknownAuthorException;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * Spring MVC REST controller for authors, written in Java.
 *
 * <p>INTEROP HIGHLIGHTS (Java calling Kotlin):
 * <ul>
 *   <li>It injects the Kotlin {@link BookService} bean like any Java bean.</li>
 *   <li>{@code bookService.findByAuthor(id)} calls a Kotlin method that has a
 *       default argument — callable here WITHOUT the second parameter only
 *       because the Kotlin side is annotated {@code @JvmOverloads}.</li>
 *   <li>It reads Kotlin {@link Book} data classes via generated JavaBean
 *       getters ({@code book.title()}? no — {@code book.getTitle()}).</li>
 * </ul>
 */
@RestController
@RequestMapping("/api/authors")
public class AuthorController {

    private final AuthorService authorService;
    private final BookService bookService;

    public AuthorController(AuthorService authorService, BookService bookService) {
        this.authorService = authorService;
        this.bookService = bookService;
    }

    @GetMapping
    public List<Author> all() {
        return authorService.findAll();
    }

    @GetMapping("/{id}")
    public Author byId(@PathVariable long id) {
        // Throwing a Kotlin-defined exception from Java code.
        return authorService.findById(id)
                .orElseThrow(() -> new UnknownAuthorException(id));
    }

    @GetMapping("/{id}/books")
    public List<BookSummary> booksByAuthor(@PathVariable long id) {
        // Calls the Kotlin @JvmOverloads method without the default argument,
        // then reads Kotlin data-class getters to build a Java view record.
        List<Book> books = bookService.findByAuthor(id);
        return books.stream()
                .map(b -> new BookSummary(b.getId(), b.getTitle(), b.getYear()))
                .toList();
    }

    /** A Java record used as a lightweight response view. */
    public record BookSummary(long id, String title, int year) {
    }
}
