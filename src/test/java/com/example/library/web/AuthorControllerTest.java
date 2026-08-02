package com.example.library.web;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * The same application exercised from a Java test using the classic MockMvc
 * fluent API — hitting the Java controller which itself calls the Kotlin
 * BookService underneath.
 */
@SpringBootTest
@AutoConfigureMockMvc
class AuthorControllerTest {

    @Autowired
    MockMvc mockMvc;

    @Test
    void listsSeededAuthors() throws Exception {
        mockMvc.perform(get("/api/authors"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2));
    }

    @Test
    void returnsBooksForAuthorViaKotlinService() throws Exception {
        mockMvc.perform(get("/api/authors/1/books"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].title").exists());
    }

    @Test
    void unknownAuthorProducesProblemDetail404() throws Exception {
        mockMvc.perform(get("/api/authors/9999"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.authorId").value(9999));
    }
}
