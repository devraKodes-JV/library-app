package com.library.books.application.service.publisher;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;

import com.library.books.application.dto.response.publisher.PublisherResponseDTO;
import com.library.books.domain.exception.PublisherNotFoundException;
import com.library.books.domain.model.Publisher;

class GetPublisherUseCaseTest {

    @Test
    void getPublisher_returnsPublisher() {
        FakePublisherRepository publisherRepository = new FakePublisherRepository();
        GetPublisherUseCase useCase = new GetPublisherUseCase(publisherRepository);

        Publisher saved = publisherRepository.save(Publisher.withoutId("Penguin", "UK", "https://penguin.com"));
        PublisherResponseDTO result = useCase.execute(saved.getId());

        assertEquals("Penguin", result.name());
        assertEquals("UK", result.country());
    }

    @Test
    void getPublisher_throwsWhenNotFound() {
        FakePublisherRepository publisherRepository = new FakePublisherRepository();
        GetPublisherUseCase useCase = new GetPublisherUseCase(publisherRepository);

        assertThrows(PublisherNotFoundException.class, () -> useCase.execute(999L));
    }
}
