package com.library.books.domain.exception;

public class PublisherNotFoundException extends RuntimeException {
    public PublisherNotFoundException(Long id) {
        super("Publisher not found: " + id);
    }
}