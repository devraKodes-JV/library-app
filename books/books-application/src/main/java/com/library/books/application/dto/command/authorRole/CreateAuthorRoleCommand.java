package com.library.books.application.dto.command.authorRole;

public record CreateAuthorRoleCommand(
        String name,
        String description) {
}
