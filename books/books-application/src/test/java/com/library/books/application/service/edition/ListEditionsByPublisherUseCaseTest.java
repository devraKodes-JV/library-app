package com.library.books.application.service.edition;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;

import org.junit.jupiter.api.Test;

import com.library.books.application.dto.response.edition.EditionResponseDTO;
import com.library.books.domain.model.Edition;

class ListEditionsByPublisherUseCaseTest {

    @Test
    void listEditionsByPublisher_returnsEditionsForPublisher() {
        FakeEditionRepository editionRepository = new FakeEditionRepository();
        ListEditionsByPublisherUseCase useCase = new ListEditionsByPublisherUseCase(editionRepository);

        Edition edition = Edition.withoutId(1L, 2L, 3L, 4L, "1234567890", 300, 2020, "1st");
        editionRepository.save(edition);

        List<EditionResponseDTO> result = useCase.execute(2L);

        assertEquals(1, result.size());
        assertEquals("1st", result.get(0).editionNumber());
    }

    @Test
    void listEditionsByPublisher_returnsEmptyWhenNoEditions() {
        FakeEditionRepository editionRepository = new FakeEditionRepository();
        ListEditionsByPublisherUseCase useCase = new ListEditionsByPublisherUseCase(editionRepository);

        List<EditionResponseDTO> result = useCase.execute(2L);

        assertTrue(result.isEmpty());
    }
}
