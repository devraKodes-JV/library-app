package com.library.books.domain.exception;

public class WorkNotFoundException extends RuntimeException {
    public WorkNotFoundException(Long id) {
        super("Work not found: " + id);
    }
}