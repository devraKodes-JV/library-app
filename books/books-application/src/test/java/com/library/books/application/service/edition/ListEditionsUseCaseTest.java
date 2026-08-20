package com.library.books.application.service.edition;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;

import org.junit.jupiter.api.Test;

import com.library.books.application.dto.response.edition.EditionResponseDTO;
import com.library.books.domain.model.Edition;

class ListEditionsUseCaseTest {

    @Test
    void listEditions_returnsAllEditions() {
        FakeEditionRepository editionRepository = new FakeEditionRepository();
        ListEditionsUseCase useCase = new ListEditionsUseCase(editionRepository);

        editionRepository.save(Edition.withoutId(1L, 2L, 3L, 4L, "1234567890", 300, 2020, "1st"));
        editionRepository.save(Edition.withoutId(1L, 2L, 3L, 4L, "1234567890123", 350, 2021, "2nd"));

        List<EditionResponseDTO> result = useCase.execute();

        assertEquals(2, result.size());
        assertEquals("1st", result.get(0).editionNumber());
        assertEquals("2nd", result.get(1).editionNumber());
    }

    @Test
    void listEditions_returnsEmptyWhenNone() {
        FakeEditionRepository editionRepository = new FakeEditionRepository();
        ListEditionsUseCase useCase = new ListEditionsUseCase(editionRepository);

        List<EditionResponseDTO> result = useCase.execute();

        assertTrue(result.isEmpty());
    }
}
