package com.library.books.domain.exception;

public class LanguageNotFoundException extends RuntimeException {
    public LanguageNotFoundException(String code) {
        super("Language not found: " + code);
    }
}