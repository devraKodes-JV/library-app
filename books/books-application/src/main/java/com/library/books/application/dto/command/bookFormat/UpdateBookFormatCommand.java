package com.library.books.application.dto.command.bookFormat;

public record UpdateBookFormatCommand(
        Long id,
        String name,
        String description) {
}
