package com.library.books.application.service.author;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;

import com.library.books.application.dto.response.author.AuthorResponseDTO;
import com.library.books.domain.exception.AuthorNotFoundException;
import com.library.books.domain.model.Author;

class GetAuthorUseCaseTest {

    @Test
    void getAuthor_returnsAuthor() {
        FakeAuthorRepository authorRepository = new FakeAuthorRepository();
        GetAuthorUseCase useCase = new GetAuthorUseCase(authorRepository);

        Author saved = authorRepository.save(Author.withoutId("John", "Doe", "Famous writer", "1980-01-01", null));
        AuthorResponseDTO result = useCase.execute(saved.getId());

        assertEquals("John", result.firstName());
        assertEquals("Doe", result.lastName());
        assertEquals("John Doe", result.fullName());
    }

    @Test
    void getAuthor_throwsWhenNotFound() {
        FakeAuthorRepository authorRepository = new FakeAuthorRepository();
        GetAuthorUseCase useCase = new GetAuthorUseCase(authorRepository);

        assertThrows(AuthorNotFoundException.class, () -> useCase.execute(999L));
    }
}
