package com.library.books.domain.exception;

public class CategoryNotFoundException extends RuntimeException {
    public CategoryNotFoundException(String code) {
        super("Category not found: " + code);
    }
}