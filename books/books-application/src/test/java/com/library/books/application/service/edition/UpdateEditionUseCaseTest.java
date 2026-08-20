package com.library.books.application.service.edition;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;

import org.junit.jupiter.api.Test;

import com.library.books.application.dto.command.edition.UpdateEditionCommand;
import com.library.books.application.dto.response.edition.EditionResponseDTO;
import com.library.books.application.validation.EditionValidator;
import com.library.books.domain.exception.EditionNotFoundException;
import com.library.books.domain.exception.ValidationException;
import com.library.books.domain.model.Edition;

class UpdateEditionUseCaseTest {

    @Test
    void updateEdition_returnsUpdatedEdition() {
        FakeEditionRepository editionRepository = new FakeEditionRepository();
        FakePublisherRepository publisherRepository = new FakePublisherRepository();
        FakeBookFormatRepository bookFormatRepository = new FakeBookFormatRepository();
        FakeLanguageRepository languageRepository = new FakeLanguageRepository();
        EditionValidator validator = new EditionValidator();
        FakeEditionAuthorRepository editionAuthorRepository = new FakeEditionAuthorRepository();
        UpdateEditionUseCase useCase = new UpdateEditionUseCase(editionRepository, validator, publisherRepository, bookFormatRepository, languageRepository, editionAuthorRepository);

        Edition saved = editionRepository.save(Edition.withoutId(1L, 2L, 3L, 4L, "1234567890", 300, 2020, "1st"));
        saved.setEditionAuthors(List.of(new com.library.books.domain.model.EditionAuthor(null, saved.getId(), 1L, 1L, null, null)));
        UpdateEditionCommand command = new UpdateEditionCommand(saved.getId(), 1L, 2L, 3L, 4L, "1234567890123", 350, 2021, "2nd", List.of("1"), List.of("1"));
        EditionResponseDTO result = useCase.execute(command);

        assertEquals(350, result.pages());
        assertEquals(2021, result.publicationYear());
        assertEquals("2nd", result.editionNumber());
    }

    @Test
    void updateEdition_throwsWhenNotFound() {
        FakeEditionRepository editionRepository = new FakeEditionRepository();
        FakePublisherRepository publisherRepository = new FakePublisherRepository();
        FakeBookFormatRepository bookFormatRepository = new FakeBookFormatRepository();
        FakeLanguageRepository languageRepository = new FakeLanguageRepository();
        EditionValidator validator = new EditionValidator();
        FakeEditionAuthorRepository editionAuthorRepository = new FakeEditionAuthorRepository();
        UpdateEditionUseCase useCase = new UpdateEditionUseCase(editionRepository, validator, publisherRepository, bookFormatRepository, languageRepository, editionAuthorRepository);

        UpdateEditionCommand command = new UpdateEditionCommand(999L, 1L, 2L, 3L, 4L, "1234567890", 300, 2020, "1st", List.of("1"), List.of("1"));

        assertThrows(EditionNotFoundException.class, () -> useCase.execute(command));
    }

    @Test
    void updateEdition_failsOnValidationError() {
        FakeEditionRepository editionRepository = new FakeEditionRepository();
        FakePublisherRepository publisherRepository = new FakePublisherRepository();
        FakeBookFormatRepository bookFormatRepository = new FakeBookFormatRepository();
        FakeLanguageRepository languageRepository = new FakeLanguageRepository();
        EditionValidator validator = new EditionValidator();
        FakeEditionAuthorRepository editionAuthorRepository = new FakeEditionAuthorRepository();
        UpdateEditionUseCase useCase = new UpdateEditionUseCase(editionRepository, validator, publisherRepository, bookFormatRepository, languageRepository, editionAuthorRepository);

        Edition saved = editionRepository.save(Edition.withoutId(1L, 2L, 3L, 4L, "1234567890", 300, 2020, "1st"));
        saved.setEditionAuthors(List.of(new com.library.books.domain.model.EditionAuthor(null, saved.getId(), 1L, 1L, null, null)));
        UpdateEditionCommand command = new UpdateEditionCommand(saved.getId(), 1L, 2L, 3L, 4L, "INVALID", 0, 1400, null, List.of("1"), List.of("1"));

        ValidationException ex = assertThrows(ValidationException.class, () -> useCase.execute(command));
        assertTrue(ex.getFieldErrors().containsKey("editionNumber"));
        assertTrue(ex.getFieldErrors().containsKey("isbn"));
        assertTrue(ex.getFieldErrors().containsKey("pages"));
        assertTrue(ex.getFieldErrors().containsKey("publicationYear"));
    }

    @Test
    void updateEdition_failsOnInvalidIsbn() {
        FakeEditionRepository editionRepository = new FakeEditionRepository();
        FakePublisherRepository publisherRepository = new FakePublisherRepository();
        FakeBookFormatRepository bookFormatRepository = new FakeBookFormatRepository();
        FakeLanguageRepository languageRepository = new FakeLanguageRepository();
        EditionValidator validator = new EditionValidator();
        FakeEditionAuthorRepository editionAuthorRepository = new FakeEditionAuthorRepository();
        UpdateEditionUseCase useCase = new UpdateEditionUseCase(editionRepository, validator, publisherRepository, bookFormatRepository, languageRepository, editionAuthorRepository);

        Edition saved = editionRepository.save(Edition.withoutId(1L, 2L, 3L, 4L, "1234567890", 300, 2020, "1st"));
        saved.setEditionAuthors(List.of(new com.library.books.domain.model.EditionAuthor(null, saved.getId(), 1L, 1L, null, null)));
        UpdateEditionCommand command = new UpdateEditionCommand(saved.getId(), 1L, 2L, 3L, 4L, "INVALID", 300, 2020, "1st", List.of("1"), List.of("1"));

        ValidationException ex = assertThrows(ValidationException.class, () -> useCase.execute(command));
        assertTrue(ex.getFieldErrors().containsKey("isbn"));
    }
}
