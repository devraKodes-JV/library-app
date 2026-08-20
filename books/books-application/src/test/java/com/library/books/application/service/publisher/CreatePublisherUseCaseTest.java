package com.library.books.application.service.publisher;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

import com.library.books.application.dto.command.publisher.CreatePublisherCommand;
import com.library.books.application.dto.response.publisher.PublisherResponseDTO;
import com.library.books.application.validation.PublisherValidator;
import com.library.books.domain.exception.ValidationException;
import com.library.books.domain.model.Publisher;

class CreatePublisherUseCaseTest {

    @Test
    void createPublisher_returnsSavedPublisher() {
        FakePublisherRepository publisherRepository = new FakePublisherRepository();
        PublisherValidator validator = new PublisherValidator();
        CreatePublisherUseCase useCase = new CreatePublisherUseCase(publisherRepository, validator);

        CreatePublisherCommand command = new CreatePublisherCommand("Penguin", "UK", "https://penguin.com");
        PublisherResponseDTO result = useCase.execute(command);

        assertEquals("Penguin", result.name());
        assertEquals("UK", result.country());
        assertEquals("https://penguin.com", result.website());
        assertTrue(result.id() > 0);
    }

    @Test
    void createPublisher_assignsId() {
        FakePublisherRepository publisherRepository = new FakePublisherRepository();
        PublisherValidator validator = new PublisherValidator();
        CreatePublisherUseCase useCase = new CreatePublisherUseCase(publisherRepository, validator);

        CreatePublisherCommand command = new CreatePublisherCommand("HarperCollins", "USA", null);
        PublisherResponseDTO result = useCase.execute(command);

        assertTrue(result.id() > 0);
    }

    @Test
    void createPublisher_failsOnValidationError() {
        FakePublisherRepository publisherRepository = new FakePublisherRepository();
        PublisherValidator validator = new PublisherValidator();
        CreatePublisherUseCase useCase = new CreatePublisherUseCase(publisherRepository, validator);

        CreatePublisherCommand command = new CreatePublisherCommand("", "", "invalid");

        ValidationException ex = assertThrows(ValidationException.class, () -> useCase.execute(command));
        assertTrue(ex.getFieldErrors().containsKey("name"));
        assertTrue(ex.getFieldErrors().containsKey("country"));
    }

    @Test
    void createPublisher_failsOnInvalidName() {
        FakePublisherRepository publisherRepository = new FakePublisherRepository();
        PublisherValidator validator = new PublisherValidator();
        CreatePublisherUseCase useCase = new CreatePublisherUseCase(publisherRepository, validator);

        CreatePublisherCommand command = new CreatePublisherCommand("Penguin123", "UK", null);

        ValidationException ex = assertThrows(ValidationException.class, () -> useCase.execute(command));
        assertTrue(ex.getFieldErrors().containsKey("name"));
    }

    @Test
    void createPublisher_failsOnInvalidCountry() {
        FakePublisherRepository publisherRepository = new FakePublisherRepository();
        PublisherValidator validator = new PublisherValidator();
        CreatePublisherUseCase useCase = new CreatePublisherUseCase(publisherRepository, validator);

        CreatePublisherCommand command = new CreatePublisherCommand("Penguin", "UK123", null);

        ValidationException ex = assertThrows(ValidationException.class, () -> useCase.execute(command));
        assertTrue(ex.getFieldErrors().containsKey("country"));
    }
}
