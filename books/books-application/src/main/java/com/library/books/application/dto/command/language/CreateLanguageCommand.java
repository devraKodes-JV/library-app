package com.library.books.application.dto.command.language;

public record CreateLanguageCommand(
        String code,
        String name) {
}
