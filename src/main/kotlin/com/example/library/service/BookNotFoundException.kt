package com.example.library.service

/**
 * Thrown (by Kotlin) when a book id is unknown, and handled (by Java) in the
 * `GlobalExceptionHandler` — an exception that crosses the language boundary.
 */
class BookNotFoundException(val bookId: Long) :
    RuntimeException("Book $bookId was not found")

/** Thrown when a book references an author that does not exist. */
class UnknownAuthorException(val authorId: Long) :
    RuntimeException("Author $authorId was not found")
