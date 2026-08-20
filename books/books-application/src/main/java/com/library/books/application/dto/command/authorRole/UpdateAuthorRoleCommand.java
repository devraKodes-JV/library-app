package com.library.books.application.dto.command.authorRole;

public record UpdateAuthorRoleCommand(
        Long id,
        String code,
        String name,
        String description) {
}
