package com.library.books.application.service.author;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

import com.library.books.application.dto.command.author.DeleteAuthorCommand;
import com.library.books.domain.exception.AuthorNotFoundException;
import com.library.books.domain.model.Author;
import com.library.books.application.service.edition.FakeEditionRepository;
import com.library.books.application.service.work.FakeWorkRepository;

class DeleteAuthorUseCaseTest {

    @Test
    void deleteAuthor_removesAuthor() {
        FakeAuthorRepository authorRepository = new FakeAuthorRepository();
        FakeWorkAuthorRepository workAuthorRepository = new FakeWorkAuthorRepository();
        FakeEditionAuthorRepository editionAuthorRepository = new FakeEditionAuthorRepository();
        FakeWorkRepository workRepository = new FakeWorkRepository();
        FakeEditionRepository editionRepository = new FakeEditionRepository();
        DeleteAuthorUseCase useCase = new DeleteAuthorUseCase(authorRepository, workAuthorRepository, editionAuthorRepository, workRepository, editionRepository);

        Author saved = authorRepository.save(Author.withoutId("John", "Doe", null, null, null));
        useCase.execute(new DeleteAuthorCommand(saved.getId()));

        assertTrue(authorRepository.findById(saved.getId()).isEmpty());
    }

    @Test
    void deleteAuthor_throwsWhenNotFound() {
        FakeAuthorRepository authorRepository = new FakeAuthorRepository();
        FakeWorkAuthorRepository workAuthorRepository = new FakeWorkAuthorRepository();
        FakeEditionAuthorRepository editionAuthorRepository = new FakeEditionAuthorRepository();
        FakeWorkRepository workRepository = new FakeWorkRepository();
        FakeEditionRepository editionRepository = new FakeEditionRepository();
        DeleteAuthorUseCase useCase = new DeleteAuthorUseCase(authorRepository, workAuthorRepository, editionAuthorRepository, workRepository, editionRepository);

        assertThrows(AuthorNotFoundException.class,
                () -> useCase.execute(new DeleteAuthorCommand(999L)));
    }
}
