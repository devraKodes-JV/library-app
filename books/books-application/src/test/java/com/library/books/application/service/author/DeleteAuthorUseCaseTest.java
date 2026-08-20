package com.library.books.application.service.author;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

import com.library.books.application.dto.command.author.DeleteAuthorCommand;
import com.library.books.domain.exception.AuthorNotFoundException;
import com.library.books.domain.model.Author;

class DeleteAuthorUseCaseTest {

    @Test
    void deleteAuthor_removesAuthor() {
        FakeAuthorRepository authorRepository = new FakeAuthorRepository();
        DeleteAuthorUseCase useCase = new DeleteAuthorUseCase(authorRepository);

        Author saved = authorRepository.save(Author.withoutId("John", "Doe", null, null, null));
        useCase.execute(new DeleteAuthorCommand(saved.getId()));

        assertTrue(authorRepository.findById(saved.getId()).isEmpty());
    }

    @Test
    void deleteAuthor_throwsWhenNotFound() {
        FakeAuthorRepository authorRepository = new FakeAuthorRepository();
        DeleteAuthorUseCase useCase = new DeleteAuthorUseCase(authorRepository);

        assertThrows(AuthorNotFoundException.class,
                () -> useCase.execute(new DeleteAuthorCommand(999L)));
    }
}
