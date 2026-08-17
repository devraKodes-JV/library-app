package com.library.books.application.dto.command.author;

public record CreateAuthorCommand(
        String firstName,
        String lastName,
        String biography,
        String birthDate,
        String deathDate) {
}
