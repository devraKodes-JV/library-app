package com.library.books.application.service.edition;

import com.library.books.domain.model.Edition;
import com.library.books.domain.port.out.EditionRepository;

import com.library.books.application.dto.response.edition.EditionResponseDTO;

import java.util.List;

public class ListEditionsByFormatUseCase {

    private final EditionRepository editionRepository;

    public ListEditionsByFormatUseCase(EditionRepository editionRepository) {
        this.editionRepository = editionRepository;
    }

    public List<EditionResponseDTO> execute(Long formatId) {
        return editionRepository.findByFormatId(formatId).stream()
                .map(EditionResponseDTO::of)
                .toList();
    }
}
