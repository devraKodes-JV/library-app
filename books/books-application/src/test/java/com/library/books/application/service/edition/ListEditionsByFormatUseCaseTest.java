package com.library.books.application.service.edition;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;

import org.junit.jupiter.api.Test;

import com.library.books.application.dto.response.edition.EditionResponseDTO;
import com.library.books.domain.model.Edition;

class ListEditionsByFormatUseCaseTest {

    @Test
    void listEditionsByFormat_returnsEditionsForFormat() {
        FakeEditionRepository editionRepository = new FakeEditionRepository();
        ListEditionsByFormatUseCase useCase = new ListEditionsByFormatUseCase(editionRepository);

        Edition edition = Edition.withoutId(1L, 2L, 3L, 4L, "1234567890", 300, 2020, "1st");
        editionRepository.save(edition);

        List<EditionResponseDTO> result = useCase.execute(3L);

        assertEquals(1, result.size());
        assertEquals("1st", result.get(0).editionNumber());
    }

    @Test
    void listEditionsByFormat_returnsEmptyWhenNoEditions() {
        FakeEditionRepository editionRepository = new FakeEditionRepository();
        ListEditionsByFormatUseCase useCase = new ListEditionsByFormatUseCase(editionRepository);

        List<EditionResponseDTO> result = useCase.execute(3L);

        assertTrue(result.isEmpty());
    }
}
