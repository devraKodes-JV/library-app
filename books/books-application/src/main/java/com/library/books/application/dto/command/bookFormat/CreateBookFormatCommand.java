package com.library.books.application.dto.command.bookFormat;

public record CreateBookFormatCommand(
        String name,
        String description) {
}
