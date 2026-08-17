package com.library.books.application.service.author;

import com.library.books.domain.exception.AuthorNotFoundException;
import com.library.books.domain.model.Author;
import com.library.books.domain.port.out.AuthorRepository;

import com.library.books.application.dto.response.author.AuthorResponseDTO;

public class GetAuthorUseCase {

    private final AuthorRepository authorRepository;

    public GetAuthorUseCase(AuthorRepository authorRepository) {
        this.authorRepository = authorRepository;
    }

    public AuthorResponseDTO execute(Long id) {
        Author author = authorRepository.findById(id)
                .orElseThrow(() -> new AuthorNotFoundException(id));
        return AuthorResponseDTO.of(author);
    }
}
