package com.library.books.application.service.work;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;

import org.junit.jupiter.api.Test;

import com.library.books.application.dto.command.work.CreateWorkCommand;
import com.library.books.application.dto.response.work.WorkResponseDTO;
import com.library.books.application.validation.WorkValidator;
import com.library.books.domain.exception.CategoryNotFoundException;
import com.library.books.domain.exception.LanguageNotFoundException;
import com.library.books.domain.exception.ValidationException;
import com.library.books.domain.model.Work;

class CreateWorkUseCaseTest {

    @Test
    void createWork_returnsSavedWork() {
        FakeWorkRepository workRepository = new FakeWorkRepository();
        FakeAuthorRepository authorRepository = new FakeAuthorRepository();
        FakeLanguageRepository languageRepository = new FakeLanguageRepository();
        FakeCategoryRepository categoryRepository = new FakeCategoryRepository();
        WorkValidator validator = new WorkValidator();
        FakeTransactional transactional = new FakeTransactional();
        CreateWorkUseCase useCase = new CreateWorkUseCase(workRepository, authorRepository, languageRepository, categoryRepository, validator, transactional);

        CreateWorkCommand command = new CreateWorkCommand("The Hobbit", null, 1L, 2L, "A great adventure", List.of("1"), List.of("1"));
        WorkResponseDTO result = useCase.execute(command);

        assertEquals("The Hobbit", result.title());
        assertTrue(result.id() > 0);
    }

    @Test
    void createWork_assignsId() {
        FakeWorkRepository workRepository = new FakeWorkRepository();
        FakeAuthorRepository authorRepository = new FakeAuthorRepository();
        FakeLanguageRepository languageRepository = new FakeLanguageRepository();
        FakeCategoryRepository categoryRepository = new FakeCategoryRepository();
        WorkValidator validator = new WorkValidator();
        FakeTransactional transactional = new FakeTransactional();
        CreateWorkUseCase useCase = new CreateWorkUseCase(workRepository, authorRepository, languageRepository, categoryRepository, validator, transactional);

        CreateWorkCommand command = new CreateWorkCommand("The Hobbit", null, 1L, 2L, null, List.of("1"), List.of("1"));
        WorkResponseDTO result = useCase.execute(command);

        assertTrue(result.id() > 0);
    }

    @Test
    void createWork_failsOnValidationError() {
        FakeWorkRepository workRepository = new FakeWorkRepository();
        FakeAuthorRepository authorRepository = new FakeAuthorRepository();
        FakeLanguageRepository languageRepository = new FakeLanguageRepository();
        FakeCategoryRepository categoryRepository = new FakeCategoryRepository();
        WorkValidator validator = new WorkValidator();
        FakeTransactional transactional = new FakeTransactional();
        CreateWorkUseCase useCase = new CreateWorkUseCase(workRepository, authorRepository, languageRepository, categoryRepository, validator, transactional);

        CreateWorkCommand command = new CreateWorkCommand("", null, 1L, 2L, null, List.of(), List.of());

        ValidationException ex = assertThrows(ValidationException.class, () -> useCase.execute(command));
        assertTrue(ex.getFieldErrors().containsKey("title"));
        assertTrue(ex.getFieldErrors().containsKey("authors"));
    }

    @Test
    void createWork_failsOnInvalidTitle() {
        FakeWorkRepository workRepository = new FakeWorkRepository();
        FakeAuthorRepository authorRepository = new FakeAuthorRepository();
        FakeLanguageRepository languageRepository = new FakeLanguageRepository();
        FakeCategoryRepository categoryRepository = new FakeCategoryRepository();
        WorkValidator validator = new WorkValidator();
        FakeTransactional transactional = new FakeTransactional();
        CreateWorkUseCase useCase = new CreateWorkUseCase(workRepository, authorRepository, languageRepository, categoryRepository, validator, transactional);

        CreateWorkCommand command = new CreateWorkCommand("The Hobbit!!!", null, 1L, 2L, null, List.of("1"), List.of("1"));

        ValidationException ex = assertThrows(ValidationException.class, () -> useCase.execute(command));
        assertTrue(ex.getFieldErrors().containsKey("title"));
    }

    @Test
    void createWork_failsOnSummaryTooLong() {
        FakeWorkRepository workRepository = new FakeWorkRepository();
        FakeAuthorRepository authorRepository = new FakeAuthorRepository();
        FakeLanguageRepository languageRepository = new FakeLanguageRepository();
        FakeCategoryRepository categoryRepository = new FakeCategoryRepository();
        WorkValidator validator = new WorkValidator();
        FakeTransactional transactional = new FakeTransactional();
        CreateWorkUseCase useCase = new CreateWorkUseCase(workRepository, authorRepository, languageRepository, categoryRepository, validator, transactional);

        String longSummary = "A".repeat(2001);
        CreateWorkCommand command = new CreateWorkCommand("The Hobbit", null, 1L, 2L, longSummary, List.of("1"), List.of("1"));

        ValidationException ex = assertThrows(ValidationException.class, () -> useCase.execute(command));
        assertTrue(ex.getFieldErrors().containsKey("summary"));
    }
}
