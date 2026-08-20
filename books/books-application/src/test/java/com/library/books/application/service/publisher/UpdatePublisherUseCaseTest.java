package com.library.books.application.service.publisher;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

import com.library.books.application.dto.command.publisher.UpdatePublisherCommand;
import com.library.books.application.dto.response.publisher.PublisherResponseDTO;
import com.library.books.application.validation.PublisherValidator;
import com.library.books.domain.exception.PublisherNotFoundException;
import com.library.books.domain.exception.ValidationException;
import com.library.books.domain.model.Publisher;

class UpdatePublisherUseCaseTest {

    @Test
    void updatePublisher_returnsUpdatedPublisher() {
        FakePublisherRepository publisherRepository = new FakePublisherRepository();
        PublisherValidator validator = new PublisherValidator();
        UpdatePublisherUseCase useCase = new UpdatePublisherUseCase(publisherRepository, validator);

        Publisher saved = publisherRepository.save(Publisher.withoutId("Penguin", "UK", "https://penguin.com"));
        UpdatePublisherCommand command = new UpdatePublisherCommand(saved.getId(), "Penguin Updated", "USA", "https://penguin.co.uk");
        PublisherResponseDTO result = useCase.execute(command);

        assertEquals("Penguin Updated", result.name());
        assertEquals("USA", result.country());
        assertEquals("https://penguin.co.uk", result.website());
    }

    @Test
    void updatePublisher_throwsWhenNotFound() {
        FakePublisherRepository publisherRepository = new FakePublisherRepository();
        PublisherValidator validator = new PublisherValidator();
        UpdatePublisherUseCase useCase = new UpdatePublisherUseCase(publisherRepository, validator);

        UpdatePublisherCommand command = new UpdatePublisherCommand(999L, "Penguin", "UK", null);

        assertThrows(PublisherNotFoundException.class, () -> useCase.execute(command));
    }

    @Test
    void updatePublisher_failsOnValidationError() {
        FakePublisherRepository publisherRepository = new FakePublisherRepository();
        PublisherValidator validator = new PublisherValidator();
        UpdatePublisherUseCase useCase = new UpdatePublisherUseCase(publisherRepository, validator);

        Publisher saved = publisherRepository.save(Publisher.withoutId("Penguin", "UK", null));
        UpdatePublisherCommand command = new UpdatePublisherCommand(saved.getId(), "", "", "invalid");

        ValidationException ex = assertThrows(ValidationException.class, () -> useCase.execute(command));
        assertTrue(ex.getFieldErrors().containsKey("name"));
        assertTrue(ex.getFieldErrors().containsKey("country"));
        assertTrue(ex.getFieldErrors().containsKey("website"));
    }

    @Test
    void updatePublisher_failsOnInvalidName() {
        FakePublisherRepository publisherRepository = new FakePublisherRepository();
        PublisherValidator validator = new PublisherValidator();
        UpdatePublisherUseCase useCase = new UpdatePublisherUseCase(publisherRepository, validator);

        Publisher saved = publisherRepository.save(Publisher.withoutId("Penguin", "UK", null));
        UpdatePublisherCommand command = new UpdatePublisherCommand(saved.getId(), "Penguin123", "UK", null);

        ValidationException ex = assertThrows(ValidationException.class, () -> useCase.execute(command));
        assertTrue(ex.getFieldErrors().containsKey("name"));
    }
}
