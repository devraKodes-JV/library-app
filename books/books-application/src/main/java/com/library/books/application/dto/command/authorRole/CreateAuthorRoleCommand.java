package com.library.books.application.dto.command.authorRole;

public record CreateAuthorRoleCommand(
        String code,
        String name,
        String description) {
}
