package com.library.books.application.service.work;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;

import org.junit.jupiter.api.Test;

import com.library.books.application.dto.command.work.UpdateWorkCommand;
import com.library.books.application.dto.response.work.WorkResponseDTO;
import com.library.books.application.validation.WorkValidator;
import com.library.books.domain.exception.ValidationException;
import com.library.books.domain.exception.WorkNotFoundException;
import com.library.books.domain.model.Work;

class UpdateWorkUseCaseTest {

    @Test
    void updateWork_returnsUpdatedWork() {
        FakeWorkRepository workRepository = new FakeWorkRepository();
        FakeAuthorRepository authorRepository = new FakeAuthorRepository();
        FakeLanguageRepository languageRepository = new FakeLanguageRepository();
        FakeCategoryRepository categoryRepository = new FakeCategoryRepository();
        WorkValidator validator = new WorkValidator();
        FakeTransactional transactional = new FakeTransactional();
        UpdateWorkUseCase useCase = new UpdateWorkUseCase(workRepository, authorRepository, languageRepository, categoryRepository, validator, transactional);

        Work saved = workRepository.save(Work.withoutId("The Hobbit", null, 1L, 2L, "Old summary"));
        saved.setWorkAuthors(List.of(new com.library.books.domain.model.WorkAuthor(null, saved.getId(), 1L, 1L, null, null)));
        UpdateWorkCommand command = new UpdateWorkCommand(saved.getId(), "The Hobbit Updated", null, 1L, 2L, "New summary", List.of("1"), List.of("1"));
        WorkResponseDTO result = useCase.execute(command);

        assertEquals("The Hobbit Updated", result.title());
        assertTrue(result.id() > 0);
    }

    @Test
    void updateWork_throwsWhenNotFound() {
        FakeWorkRepository workRepository = new FakeWorkRepository();
        FakeAuthorRepository authorRepository = new FakeAuthorRepository();
        FakeLanguageRepository languageRepository = new FakeLanguageRepository();
        FakeCategoryRepository categoryRepository = new FakeCategoryRepository();
        WorkValidator validator = new WorkValidator();
        FakeTransactional transactional = new FakeTransactional();
        UpdateWorkUseCase useCase = new UpdateWorkUseCase(workRepository, authorRepository, languageRepository, categoryRepository, validator, transactional);

        UpdateWorkCommand command = new UpdateWorkCommand(999L, "The Hobbit", null, 1L, 2L, null, List.of("1"), List.of("1"));

        assertThrows(WorkNotFoundException.class, () -> useCase.execute(command));
    }

    @Test
    void updateWork_failsOnValidationError() {
        FakeWorkRepository workRepository = new FakeWorkRepository();
        FakeAuthorRepository authorRepository = new FakeAuthorRepository();
        FakeLanguageRepository languageRepository = new FakeLanguageRepository();
        FakeCategoryRepository categoryRepository = new FakeCategoryRepository();
        WorkValidator validator = new WorkValidator();
        FakeTransactional transactional = new FakeTransactional();
        UpdateWorkUseCase useCase = new UpdateWorkUseCase(workRepository, authorRepository, languageRepository, categoryRepository, validator, transactional);

        Work saved = workRepository.save(Work.withoutId("The Hobbit", null, 1L, 2L, null));
        saved.setWorkAuthors(List.of(new com.library.books.domain.model.WorkAuthor(null, saved.getId(), 1L, 1L, null, null)));
        UpdateWorkCommand command = new UpdateWorkCommand(saved.getId(), "", null, 1L, 2L, null, List.of(), List.of());

        ValidationException ex = assertThrows(ValidationException.class, () -> useCase.execute(command));
        assertTrue(ex.getFieldErrors().containsKey("title"));
        assertTrue(ex.getFieldErrors().containsKey("authors"));
    }

    @Test
    void updateWork_failsOnInvalidTitle() {
        FakeWorkRepository workRepository = new FakeWorkRepository();
        FakeAuthorRepository authorRepository = new FakeAuthorRepository();
        FakeLanguageRepository languageRepository = new FakeLanguageRepository();
        FakeCategoryRepository categoryRepository = new FakeCategoryRepository();
        WorkValidator validator = new WorkValidator();
        FakeTransactional transactional = new FakeTransactional();
        UpdateWorkUseCase useCase = new UpdateWorkUseCase(workRepository, authorRepository, languageRepository, categoryRepository, validator, transactional);

        Work saved = workRepository.save(Work.withoutId("The Hobbit", null, 1L, 2L, null));
        saved.setWorkAuthors(List.of(new com.library.books.domain.model.WorkAuthor(null, saved.getId(), 1L, 1L, null, null)));
        UpdateWorkCommand command = new UpdateWorkCommand(saved.getId(), "The Hobbit!!!", null, 1L, 2L, null, List.of("1"), List.of("1"));

        ValidationException ex = assertThrows(ValidationException.class, () -> useCase.execute(command));
        assertTrue(ex.getFieldErrors().containsKey("title"));
    }
}
