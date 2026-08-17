package com.library.books.application.dto.command.bookFormat;

public record UpdateBookFormatCommand(
        Long id,
        String code,
        String name,
        String description) {
}
