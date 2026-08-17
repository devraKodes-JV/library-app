package com.library.books.domain.exception;

public class BookFormatNotFoundException extends RuntimeException {
    public BookFormatNotFoundException(String code) {
        super("BookFormat not found: " + code);
    }
}