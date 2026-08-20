package com.library.books.domain.exception;

public class AuthorRoleNotFoundException extends RuntimeException {
    public AuthorRoleNotFoundException(Long id) {
        super("Author role not found with id: " + id);
    }
}
