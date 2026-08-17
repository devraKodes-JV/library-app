package com.library.books.application.dto.command.author;

public record UpdateAuthorCommand(
        Long id,
        String firstName,
        String lastName,
        String biography,
        String birthDate,
        String deathDate) {
}
