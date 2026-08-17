package com.library.books.application.service.author;

import java.util.List;

import com.library.books.application.dto.response.author.AuthorResponseDTO;
import com.library.books.domain.port.out.AuthorRepository;

public class ListAuthorsUseCase {

    private final AuthorRepository authorRepository;

    public ListAuthorsUseCase(AuthorRepository authorRepository) {
        this.authorRepository = authorRepository;
    }

    public List<AuthorResponseDTO> execute() {
        return authorRepository.findAll().stream()
                .map(AuthorResponseDTO::of)
                .toList();
    }
}
