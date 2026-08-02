package com.example.library.web

import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController

/**
 * Demonstrates Spring 7 API versioning. Both handlers share the path
 * `/api/greeting` but are selected by the `X-API-Version` request header
 * (configured in [WebConfig]):
 *
 *   curl localhost:8080/api/greeting -H "X-API-Version: 1"
 *   curl localhost:8080/api/greeting -H "X-API-Version: 2"
 *
 * With no header, the default version ("1") is used.
 */
@RestController
class GreetingController {

    @GetMapping("/api/greeting", version = "1")
    fun greetingV1(): Map<String, Any> = mapOf(
        "version" to "1",
        "message" to "Hello from the v1 API",
    )

    @GetMapping("/api/greeting", version = "2")
    fun greetingV2(): Map<String, Any> = mapOf(
        "version" to "2",
        "message" to "Hello from the v2 API",
        "docs" to "https://spring.io",
    )
}
