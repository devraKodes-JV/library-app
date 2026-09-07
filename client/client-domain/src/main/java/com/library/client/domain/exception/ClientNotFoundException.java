package com.library.client.domain.exception;

public class ClientNotFoundException extends RuntimeException {
    public ClientNotFoundException(Long id) {
        super("Client not found: " + id);
    }

    public ClientNotFoundException(String message) {
        super(message);
    }
}
