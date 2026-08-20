package com.library.books.application.service.publisher;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;

import org.junit.jupiter.api.Test;

import com.library.books.application.dto.command.publisher.DeletePublisherCommand;
import com.library.books.domain.exception.PublisherNotFoundException;
import com.library.books.domain.exception.ValidationException;
import com.library.books.domain.model.Edition;
import com.library.books.domain.model.Publisher;

class DeletePublisherUseCaseTest {

    @Test
    void deletePublisher_removesPublisher() {
        FakePublisherRepository publisherRepository = new FakePublisherRepository();
        FakeEditionRepository editionRepository = new FakeEditionRepository();
        DeletePublisherUseCase useCase = new DeletePublisherUseCase(publisherRepository, editionRepository);

        Publisher saved = publisherRepository.save(Publisher.withoutId("Penguin", "UK", "https://penguin.com"));
        useCase.execute(new DeletePublisherCommand(saved.getId()));

        assertTrue(publisherRepository.findById(saved.getId()).isEmpty());
    }

    @Test
    void deletePublisher_throwsWhenHasActiveEditions() {
        FakePublisherRepository publisherRepository = new FakePublisherRepository();
        FakeEditionRepository editionRepository = new FakeEditionRepository();
        DeletePublisherUseCase useCase = new DeletePublisherUseCase(publisherRepository, editionRepository);

        Publisher saved = publisherRepository.save(Publisher.withoutId("Penguin", "UK", "https://penguin.com"));
        Edition edition = Edition.withoutId(null, saved.getId(), null, null, "ISBN", 100, 2020, "1st");
        editionRepository.save(edition);

        ValidationException ex = assertThrows(ValidationException.class,
                () -> useCase.execute(new DeletePublisherCommand(saved.getId())));
        assertEquals("Cannot delete this publisher because it has active editions. Delete the editions first.",
                ex.getFieldErrors().get("publisherId"));
    }

    @Test
    void deletePublisher_throwsWhenNotFound() {
        FakePublisherRepository publisherRepository = new FakePublisherRepository();
        FakeEditionRepository editionRepository = new FakeEditionRepository();
        DeletePublisherUseCase useCase = new DeletePublisherUseCase(publisherRepository, editionRepository);

        assertThrows(PublisherNotFoundException.class,
                () -> useCase.execute(new DeletePublisherCommand(999L)));
    }
}
