package com.example.library.web;

import com.example.library.service.BookNotFoundException;
import com.example.library.service.UnknownAuthorException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Centralised error handling (Java), returning RFC 9457 {@link ProblemDetail}
 * responses — the modern Spring way to report errors.
 *
 * <p>INTEROP NOTE: the two "not found" handlers catch exceptions DEFINED IN
 * KOTLIN ({@link BookNotFoundException}, {@link UnknownAuthorException}) and
 * even thrown from Kotlin services, proving exceptions flow transparently
 * across the language boundary.
 */
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(BookNotFoundException.class)
    public ProblemDetail handleBookNotFound(BookNotFoundException ex) {
        ProblemDetail problem = ProblemDetail.forStatusAndDetail(HttpStatus.NOT_FOUND, ex.getMessage());
        problem.setTitle("Book not found");
        problem.setProperty("bookId", ex.getBookId());
        return problem;
    }

    @ExceptionHandler(UnknownAuthorException.class)
    public ProblemDetail handleUnknownAuthor(UnknownAuthorException ex) {
        ProblemDetail problem = ProblemDetail.forStatusAndDetail(HttpStatus.NOT_FOUND, ex.getMessage());
        problem.setTitle("Author not found");
        problem.setProperty("authorId", ex.getAuthorId());
        return problem;
    }

    /** Turns Jakarta Bean Validation failures on the Kotlin DTO into a 400. */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ProblemDetail handleValidation(MethodArgumentNotValidException ex) {
        ProblemDetail problem = ProblemDetail.forStatusAndDetail(
                HttpStatus.BAD_REQUEST, "One or more fields are invalid");
        problem.setTitle("Validation failed");

        Map<String, String> errors = new LinkedHashMap<>();
        for (FieldError fe : ex.getBindingResult().getFieldErrors()) {
            errors.put(fe.getField(), fe.getDefaultMessage());
        }
        problem.setProperty("errors", errors);
        return problem;
    }
}
