package com.example.library.domain;

import org.jspecify.annotations.Nullable;

/**
 * An author, modelled as a Java <b>record</b> (immutable data carrier).
 *
 * <p>INTEROP NOTE: Kotlin sees the record's components as read-only
 * properties, so Kotlin code can write {@code author.name()} or simply
 * {@code author.name} — both resolve to the record accessor. Records are a
 * natural Java counterpart to Kotlin's {@code data class}.
 *
 * <p>The {@link Nullable} annotation (JSpecify) tells Kotlin that
 * {@code biography} may be {@code null}; with {@code -Xjsr305=strict} the
 * Kotlin compiler then treats it as {@code String?} instead of a lenient
 * platform type.
 */
public record Author(long id, String name, @Nullable String biography) {

    public Author {
        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("Author name must not be blank");
        }
    }
}
