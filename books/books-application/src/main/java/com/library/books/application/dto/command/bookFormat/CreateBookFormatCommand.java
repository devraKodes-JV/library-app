package com.library.books.application.dto.command.bookFormat;

public record CreateBookFormatCommand(
        String code,
        String name,
        String description) {
}
