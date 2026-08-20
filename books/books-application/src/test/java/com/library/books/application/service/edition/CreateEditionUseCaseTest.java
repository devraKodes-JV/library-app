package com.library.books.application.service.edition;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;

import org.junit.jupiter.api.Test;

import com.library.books.application.dto.command.edition.CreateEditionCommand;
import com.library.books.application.dto.response.edition.EditionResponseDTO;
import com.library.books.application.validation.EditionValidator;
import com.library.books.domain.exception.ValidationException;
import com.library.books.domain.exception.WorkNotFoundException;
import com.library.books.domain.model.Edition;

class CreateEditionUseCaseTest {

    @Test
    void createEdition_returnsSavedEdition() {
        FakeEditionRepository editionRepository = new FakeEditionRepository();
        FakeWorkRepository workRepository = new FakeWorkRepository();
        FakePublisherRepository publisherRepository = new FakePublisherRepository();
        FakeBookFormatRepository bookFormatRepository = new FakeBookFormatRepository();
        FakeLanguageRepository languageRepository = new FakeLanguageRepository();
        EditionValidator validator = new EditionValidator();
        FakeTransactional transactional = new FakeTransactional();
        FakeEditionAuthorRepository editionAuthorRepository = new FakeEditionAuthorRepository();
        CreateEditionUseCase useCase = new CreateEditionUseCase(editionRepository, validator, transactional, workRepository, publisherRepository, bookFormatRepository, languageRepository, editionAuthorRepository);

        workRepository.save(com.library.books.domain.model.Work.withoutId("The Hobbit", null, null, null, null));
        publisherRepository.save(com.library.books.domain.model.Publisher.withoutId("Penguin", "UK", "penguin.com"));
        bookFormatRepository.save(com.library.books.domain.model.BookFormat.withoutId("HC", "Hardcover", "Hardcover edition"));
        languageRepository.save(com.library.books.domain.model.Language.withoutId("en", "English"));

        CreateEditionCommand command = new CreateEditionCommand(1L, 1L, 1L, 1L, "1234567890", 300, 2020, "1st", List.of("1"), List.of("1"));
        EditionResponseDTO result = useCase.execute(command);

        assertEquals("The Hobbit", result.workTitle());
        assertEquals("Penguin", result.publisherName());
        assertTrue(result.id() > 0);
    }

    @Test
    void createEdition_assignsId() {
        FakeEditionRepository editionRepository = new FakeEditionRepository();
        FakeWorkRepository workRepository = new FakeWorkRepository();
        FakePublisherRepository publisherRepository = new FakePublisherRepository();
        FakeBookFormatRepository bookFormatRepository = new FakeBookFormatRepository();
        FakeLanguageRepository languageRepository = new FakeLanguageRepository();
        EditionValidator validator = new EditionValidator();
        FakeTransactional transactional = new FakeTransactional();
        FakeEditionAuthorRepository editionAuthorRepository = new FakeEditionAuthorRepository();
        CreateEditionUseCase useCase = new CreateEditionUseCase(editionRepository, validator, transactional, workRepository, publisherRepository, bookFormatRepository, languageRepository, editionAuthorRepository);

        workRepository.save(com.library.books.domain.model.Work.withoutId("The Hobbit", null, null, null, null));
        publisherRepository.save(com.library.books.domain.model.Publisher.withoutId("Penguin", "UK", "penguin.com"));
        bookFormatRepository.save(com.library.books.domain.model.BookFormat.withoutId("HC", "Hardcover", "Hardcover edition"));
        languageRepository.save(com.library.books.domain.model.Language.withoutId("en", "English"));

        CreateEditionCommand command = new CreateEditionCommand(1L, 1L, 1L, 1L, "1234567890", 300, 2020, "1st", List.of("1"), List.of("1"));
        EditionResponseDTO result = useCase.execute(command);

        assertTrue(result.id() > 0);
    }

    @Test
    void createEdition_failsOnValidationError() {
        FakeEditionRepository editionRepository = new FakeEditionRepository();
        FakeWorkRepository workRepository = new FakeWorkRepository();
        FakePublisherRepository publisherRepository = new FakePublisherRepository();
        FakeBookFormatRepository bookFormatRepository = new FakeBookFormatRepository();
        FakeLanguageRepository languageRepository = new FakeLanguageRepository();
        EditionValidator validator = new EditionValidator();
        FakeTransactional transactional = new FakeTransactional();
        FakeEditionAuthorRepository editionAuthorRepository = new FakeEditionAuthorRepository();
        CreateEditionUseCase useCase = new CreateEditionUseCase(editionRepository, validator, transactional, workRepository, publisherRepository, bookFormatRepository, languageRepository, editionAuthorRepository);

        CreateEditionCommand command = new CreateEditionCommand(1L, 1L, 1L, 1L, "INVALID", 0, 1400, null, List.of("1"), List.of("1"));

        ValidationException ex = assertThrows(ValidationException.class, () -> useCase.execute(command));
        assertTrue(ex.getFieldErrors().containsKey("editionNumber"));
        assertTrue(ex.getFieldErrors().containsKey("isbn"));
        assertTrue(ex.getFieldErrors().containsKey("pages"));
        assertTrue(ex.getFieldErrors().containsKey("publicationYear"));
    }

    @Test
    void createEdition_failsOnInvalidIsbn() {
        FakeEditionRepository editionRepository = new FakeEditionRepository();
        FakeWorkRepository workRepository = new FakeWorkRepository();
        FakePublisherRepository publisherRepository = new FakePublisherRepository();
        FakeBookFormatRepository bookFormatRepository = new FakeBookFormatRepository();
        FakeLanguageRepository languageRepository = new FakeLanguageRepository();
        EditionValidator validator = new EditionValidator();
        FakeTransactional transactional = new FakeTransactional();
        FakeEditionAuthorRepository editionAuthorRepository = new FakeEditionAuthorRepository();
        CreateEditionUseCase useCase = new CreateEditionUseCase(editionRepository, validator, transactional, workRepository, publisherRepository, bookFormatRepository, languageRepository, editionAuthorRepository);

        workRepository.save(com.library.books.domain.model.Work.withoutId("The Hobbit", null, null, null, null));
        publisherRepository.save(com.library.books.domain.model.Publisher.withoutId("Penguin", "UK", "penguin.com"));
        bookFormatRepository.save(com.library.books.domain.model.BookFormat.withoutId("HC", "Hardcover", "Hardcover edition"));
        languageRepository.save(com.library.books.domain.model.Language.withoutId("en", "English"));

        CreateEditionCommand command = new CreateEditionCommand(1L, 1L, 1L, 1L, "INVALID", 300, 2020, "1st", List.of("1"), List.of("1"));

        ValidationException ex = assertThrows(ValidationException.class, () -> useCase.execute(command));
        assertTrue(ex.getFieldErrors().containsKey("isbn"));
    }

    @Test
    void createEdition_failsOnNullWorkId() {
        FakeEditionRepository editionRepository = new FakeEditionRepository();
        FakeWorkRepository workRepository = new FakeWorkRepository();
        FakePublisherRepository publisherRepository = new FakePublisherRepository();
        FakeBookFormatRepository bookFormatRepository = new FakeBookFormatRepository();
        FakeLanguageRepository languageRepository = new FakeLanguageRepository();
        EditionValidator validator = new EditionValidator();
        FakeTransactional transactional = new FakeTransactional();
        FakeEditionAuthorRepository editionAuthorRepository = new FakeEditionAuthorRepository();
        CreateEditionUseCase useCase = new CreateEditionUseCase(editionRepository, validator, transactional, workRepository, publisherRepository, bookFormatRepository, languageRepository, editionAuthorRepository);

        CreateEditionCommand command = new CreateEditionCommand(null, 1L, 1L, 1L, "1234567890", 300, 2020, "1st", List.of("1"), List.of("1"));

        assertThrows(WorkNotFoundException.class, () -> useCase.execute(command));
    }
}
