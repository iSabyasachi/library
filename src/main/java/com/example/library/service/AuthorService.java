package com.example.library.service;

import com.example.library.domain.Author;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

/**
 * Author business logic (Java). Consumed by Kotlin ({@code BookService}) and
 * by a Java controller — a bean that crosses the language boundary.
 */
@Service
public class AuthorService {

    private final AuthorRepository repository;

    public AuthorService(AuthorRepository repository) {
        this.repository = repository;
    }

    public Author create(long id, String name, String biography) {
        return repository.save(new Author(id, name, biography));
    }

    public Optional<Author> findById(long id) {
        return repository.findById(id);
    }

    public List<Author> findAll() {
        return repository.findAll();
    }
}
