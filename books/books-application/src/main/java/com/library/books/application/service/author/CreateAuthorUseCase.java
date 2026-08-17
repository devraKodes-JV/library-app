package com.library.books.application.service.author;

import com.library.books.application.dto.command.author.CreateAuthorCommand;
import com.library.books.application.dto.response.author.AuthorResponseDTO;
import com.library.books.application.validation.AuthorValidator;
import com.library.books.domain.model.Author;
import com.library.books.domain.port.out.AuthorRepository;

public class CreateAuthorUseCase {

    private final AuthorRepository authorRepository;
    private final AuthorValidator authorValidator;

    public CreateAuthorUseCase(AuthorRepository authorRepository, AuthorValidator authorValidator) {
        this.authorRepository = authorRepository;
        this.authorValidator = authorValidator;
    }

    public AuthorResponseDTO execute(CreateAuthorCommand command) {
        Author author = Author.withoutId(
                command.firstName(),
                command.lastName(),
                command.biography(),
                command.birthDate(),
                command.deathDate());
        authorValidator.validate(author);
        Author saved = authorRepository.save(author);
        return AuthorResponseDTO.of(saved);
    }
}
