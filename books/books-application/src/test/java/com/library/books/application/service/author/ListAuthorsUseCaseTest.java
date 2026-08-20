package com.library.books.application.service.author;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;

import org.junit.jupiter.api.Test;

import com.library.books.application.dto.response.author.AuthorResponseDTO;
import com.library.books.domain.model.Author;

class ListAuthorsUseCaseTest {

    @Test
    void listAuthors_returnsAll() {
        FakeAuthorRepository authorRepository = new FakeAuthorRepository();
        ListAuthorsUseCase useCase = new ListAuthorsUseCase(authorRepository);

        authorRepository.save(Author.withoutId("John", "Doe", null, null, null));
        authorRepository.save(Author.withoutId("Jane", "Smith", null, null, null));

        List<AuthorResponseDTO> result = useCase.execute();

        assertEquals(2, result.size());
        assertEquals("John", result.get(0).firstName());
        assertEquals("Doe", result.get(0).lastName());
        assertEquals("Jane", result.get(1).firstName());
        assertEquals("Smith", result.get(1).lastName());
    }

    @Test
    void listAuthors_returnsEmptyWhenNone() {
        FakeAuthorRepository authorRepository = new FakeAuthorRepository();
        ListAuthorsUseCase useCase = new ListAuthorsUseCase(authorRepository);

        List<AuthorResponseDTO> result = useCase.execute();

        assertTrue(result.isEmpty());
    }
}
