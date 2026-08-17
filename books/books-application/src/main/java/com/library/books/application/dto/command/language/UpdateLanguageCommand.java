package com.library.books.application.dto.command.language;

public record UpdateLanguageCommand(
        Long id,
        String code,
        String name) {
}
