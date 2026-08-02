package com.example.library.web

import org.springframework.context.annotation.Configuration
import org.springframework.web.servlet.config.annotation.ApiVersionConfigurer
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer

/**
 * Enables Spring MVC **API versioning** — new in Spring Framework 7 (shipped
 * with Spring Boot 4). Clients pick a version via the `X-API-Version` header;
 * requests without it fall back to the default version.
 *
 * See [GreetingController] for handlers mapped to specific versions.
 */
@Configuration
class WebConfig : WebMvcConfigurer {

    override fun configureApiVersioning(configurer: ApiVersionConfigurer) {
        configurer
            .useRequestHeader("X-API-Version")
            .addSupportedVersions("1", "2")
            .setVersionRequired(false)
            .setDefaultVersion("1")
    }
}
