package com.library.books.application.dto.command.authorRole;

public record UpdateAuthorRoleCommand(
        Long id,
        String name,
        String description) {
}
