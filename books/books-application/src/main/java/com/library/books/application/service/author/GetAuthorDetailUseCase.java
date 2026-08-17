package com.library.books.application.service.author;

import java.util.List;

import com.library.books.domain.dto.common.WorkSummaryDTO;
import com.library.books.domain.dto.query.AuthorWithWorksDTO;
import com.library.books.application.dto.response.author.AuthorDetailResponseDTO;
import com.library.books.domain.exception.AuthorNotFoundException;
import com.library.books.domain.port.out.AuthorRepository;

public class GetAuthorDetailUseCase {

    private final AuthorRepository authorRepository;

    public GetAuthorDetailUseCase(AuthorRepository authorRepository) {
        this.authorRepository = authorRepository;
    }

    public AuthorDetailResponseDTO execute(Long id) {
        AuthorWithWorksDTO data = authorRepository.findByIdWithWorks(id);
        if (data == null) {
            throw new AuthorNotFoundException(id);
        }
        return AuthorDetailResponseDTO.from(data);
    }
}
