package com.library.books.application.service.edition;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;

import org.junit.jupiter.api.Test;

import com.library.books.application.dto.response.edition.EditionResponseDTO;
import com.library.books.domain.dto.response.edition.EditionWithNamesDTO;
import com.library.books.domain.model.Edition;

class ListEditionsByWorkUseCaseTest {

    @Test
    void listEditionsByWork_returnsEditionsForWork() {
        FakeEditionRepository editionRepository = new FakeEditionRepository();
        ListEditionsByWorkUseCase useCase = new ListEditionsByWorkUseCase(editionRepository);

        Edition edition = Edition.withoutId(1L, 2L, 3L, 4L, "1234567890", 300, 2020, "1st");
        editionRepository.save(edition);
        editionRepository.putDetails(1L, List.of(new EditionWithNamesDTO(
                edition.getId(), 1L, "The Hobbit", 2L, "Penguin", 3L, "Hardcover", 4L, "English",
                "1234567890", 300, 2020, "1st"
        )));

        List<EditionResponseDTO> result = useCase.execute(1L);

        assertEquals(1, result.size());
        assertEquals("The Hobbit", result.get(0).workTitle());
    }

    @Test
    void listEditionsByWork_returnsEmptyWhenNoEditions() {
        FakeEditionRepository editionRepository = new FakeEditionRepository();
        ListEditionsByWorkUseCase useCase = new ListEditionsByWorkUseCase(editionRepository);

        List<EditionResponseDTO> result = useCase.execute(1L);

        assertTrue(result.isEmpty());
    }
}
