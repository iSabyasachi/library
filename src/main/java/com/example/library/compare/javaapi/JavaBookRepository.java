package com.example.library.compare.javaapi;

import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class JavaBookRepository {

    private final List<JavaBook> books = List.of(
            new JavaBook(1L, "1984", "Orwell"),
            new JavaBook(2L, "Animal Farm", "Orwell"),
            new JavaBook(3L, "Dune", "Herbert")
    );

    public List<JavaBook> findAll() {
        return books;
    }
}

