package com.example.library.compare.javaapi;

import com.example.library.model.DataInitializer;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class JavaBookRepository {

    private final List<String> authorNames = DataInitializer.AUTHOR_NAMES.stream().sorted().toList();
    private final List<String> bookNames = DataInitializer.BOOK_NAMES.stream().sorted().toList();

    private final List<JavaBook> books = List.of(
            new JavaBook(1L, bookNames.get(0), authorNames.get(0)),
            new JavaBook(2L, bookNames.get(2), authorNames.get(0)),
            new JavaBook(3L, bookNames.get(1), authorNames.get(1))
    );

    public List<JavaBook> findAll() {
        return books;
    }
}

