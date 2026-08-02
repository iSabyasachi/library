package com.example.library.service;

import com.example.library.domain.Author;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * A tiny in-memory author store (Java). Returns {@link Optional} so that
 * Kotlin callers get a clear "maybe absent" signal at the API boundary.
 */
@Repository
public class AuthorRepository {

    private final ConcurrentMap<Long, Author> authors = new ConcurrentHashMap<>();

    public Author save(Author author) {
        authors.put(author.id(), author);
        return author;
    }

    public Optional<Author> findById(long id) {
        return Optional.ofNullable(authors.get(id));
    }

    public List<Author> findAll() {
        return List.copyOf(authors.values());
    }
}
