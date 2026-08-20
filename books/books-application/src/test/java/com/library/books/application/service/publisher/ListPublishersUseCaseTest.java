package com.library.books.application.service.publisher;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;

import org.junit.jupiter.api.Test;

import com.library.books.application.dto.response.publisher.PublisherResponseDTO;
import com.library.books.domain.model.Publisher;

class ListPublishersUseCaseTest {

    @Test
    void listPublishers_returnsAll() {
        FakePublisherRepository publisherRepository = new FakePublisherRepository();
        ListPublishersUseCase useCase = new ListPublishersUseCase(publisherRepository);

        publisherRepository.save(Publisher.withoutId("Penguin", "UK", "https://penguin.com"));
        publisherRepository.save(Publisher.withoutId("HarperCollins", "USA", null));

        List<PublisherResponseDTO> result = useCase.execute();

        assertEquals(2, result.size());
        assertEquals("Penguin", result.get(0).name());
        assertEquals("HarperCollins", result.get(1).name());
    }

    @Test
    void listPublishers_returnsEmptyWhenNone() {
        FakePublisherRepository publisherRepository = new FakePublisherRepository();
        ListPublishersUseCase useCase = new ListPublishersUseCase(publisherRepository);

        List<PublisherResponseDTO> result = useCase.execute();

        assertTrue(result.isEmpty());
    }
}
