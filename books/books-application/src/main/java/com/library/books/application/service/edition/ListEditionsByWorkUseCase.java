package com.library.books.application.service.edition;

import com.library.books.domain.model.Edition;
import com.library.books.domain.port.out.EditionRepository;

import com.library.books.application.dto.response.edition.EditionResponseDTO;

import java.util.List;

public class ListEditionsByWorkUseCase {

    private final EditionRepository editionRepository;

    public ListEditionsByWorkUseCase(EditionRepository editionRepository) {
        this.editionRepository = editionRepository;
    }

    public List<EditionResponseDTO> execute(Long workId) {
        return editionRepository.findByWorkId(workId).stream()
                .map(EditionResponseDTO::of)
                .toList();
    }
}
