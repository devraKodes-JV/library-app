package com.library.books.application.dto.command.publisher;

public record CreatePublisherCommand(
        String name,
        String country,
        String website) {
}
