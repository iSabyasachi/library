package com.example.library.compare.javaapi;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/compare/java/books")
public class JavaBookController {

    private final JavaBookService service;

    public JavaBookController(JavaBookService service) {
        this.service = service;
    }

    @GetMapping
    public List<JavaBook> getBooks(@RequestParam String author) {
        return service.findByAuthor(author);
    }
}

