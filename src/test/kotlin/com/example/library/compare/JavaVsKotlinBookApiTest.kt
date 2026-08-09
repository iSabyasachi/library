package com.example.library.compare

import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.get

@SpringBootTest
@AutoConfigureMockMvc
class JavaVsKotlinBookApiTest(
    @param:Autowired private val mockMvc: MockMvc,
) {

    @Test
    fun `java and kotlin endpoints return same titles for Orwell`() {
        mockMvc.get("/api/compare/java/books") {
            param("author", "Orwell")
        }.andExpect {
            status { isOk() }
            jsonPath("$[0].title") { value("1984") }
            jsonPath("$[1].title") { value("Animal Farm") }
        }

        mockMvc.get("/api/compare/kotlin/books") {
            param("author", "Orwell")
        }.andExpect {
            status { isOk() }
            jsonPath("$[0].title") { value("1984") }
            jsonPath("$[1].title") { value("Animal Farm") }
        }
    }
}

