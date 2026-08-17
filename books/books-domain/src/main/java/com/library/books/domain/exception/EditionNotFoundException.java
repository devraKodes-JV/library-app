package com.library.books.domain.exception;

public class EditionNotFoundException extends RuntimeException {
    public EditionNotFoundException(Long id) {
        super("Edition not found: " + id);
    }
}