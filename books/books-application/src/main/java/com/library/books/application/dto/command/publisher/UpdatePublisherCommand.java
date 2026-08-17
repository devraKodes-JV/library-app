package com.library.books.application.dto.command.publisher;

public record UpdatePublisherCommand(
        Long id,
        String name,
        String country,
        String website) {
}
