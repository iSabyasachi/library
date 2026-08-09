package com.example.library.compare.javaapi;

import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class JavaBookService {

    private final JavaBookRepository repository;

    public JavaBookService(JavaBookRepository repository) {
        this.repository = repository;
    }

    public List<JavaBook> findByAuthor(String author) {
        return repository.findAll()
                .stream()
                .filter(book -> book.getAuthor().equalsIgnoreCase(author))
                .collect(Collectors.toList());
    }
}

