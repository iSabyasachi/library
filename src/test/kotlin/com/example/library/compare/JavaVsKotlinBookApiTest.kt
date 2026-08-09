package com.example.library.compare

import com.example.library.model.DataInitializer
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
    fun `java and kotlin endpoints return same titles from model seed`() {
        val author = DataInitializer.AUTHOR_NAMES.sorted()[0]
        val expectedFirstTitle = DataInitializer.BOOK_NAMES.sorted()[0]
        val expectedSecondTitle = DataInitializer.BOOK_NAMES.sorted()[2]

        mockMvc.get("/api/compare/java/books") {
            param("author", author)
        }.andExpect {
            status { isOk() }
            jsonPath("$[0].title") { value(expectedFirstTitle) }
            jsonPath("$[1].title") { value(expectedSecondTitle) }
        }

        mockMvc.get("/api/compare/kotlin/books") {
            param("author", author)
        }.andExpect {
            status { isOk() }
            jsonPath("$[0].title") { value(expectedFirstTitle) }
            jsonPath("$[1].title") { value(expectedSecondTitle) }
        }
    }
}

