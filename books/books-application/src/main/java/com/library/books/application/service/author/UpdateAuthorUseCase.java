package com.library.books.application.service.author;

import com.library.books.application.dto.command.author.UpdateAuthorCommand;
import com.library.books.application.dto.response.author.AuthorResponseDTO;
import com.library.books.application.validation.AuthorValidator;
import com.library.books.domain.exception.AuthorNotFoundException;
import com.library.books.domain.model.Author;
import com.library.books.domain.port.out.AuthorRepository;

public class UpdateAuthorUseCase {

    private final AuthorRepository authorRepository;
    private final AuthorValidator authorValidator;

    public UpdateAuthorUseCase(AuthorRepository authorRepository, AuthorValidator authorValidator) {
        this.authorRepository = authorRepository;
        this.authorValidator = authorValidator;
    }

    public AuthorResponseDTO execute(UpdateAuthorCommand command) {
        Author existing = authorRepository.findById(command.id())
                .orElseThrow(() -> new AuthorNotFoundException(command.id()));
        existing.setFirstName(command.firstName());
        existing.setLastName(command.lastName());
        existing.setBiography(command.biography());
        existing.setBirthDate(command.birthDate());
        existing.setDeathDate(command.deathDate());
        authorValidator.validate(existing);
        Author saved = authorRepository.save(existing);
        return AuthorResponseDTO.of(saved);
    }
}
