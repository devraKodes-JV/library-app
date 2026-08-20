package com.library.books.application.service.edition;

import java.util.List;

import com.library.books.application.dto.response.edition.EditionResponseDTO;
import com.library.books.domain.dto.response.edition.EditionWithNamesDTO;
import com.library.books.domain.port.out.EditionRepository;

public class ListEditionsByWorkUseCase {

    private final EditionRepository editionRepository;

    public ListEditionsByWorkUseCase(EditionRepository editionRepository) {
        this.editionRepository = editionRepository;
    }

    public List<EditionResponseDTO> execute(Long workId) {
        return editionRepository.findByWorkIdWithDetails(workId).stream()
                .map(EditionResponseDTO::from)
                .toList();
    }
}
