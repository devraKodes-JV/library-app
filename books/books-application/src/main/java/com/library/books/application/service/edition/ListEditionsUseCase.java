package com.library.books.application.service.edition;

import com.library.books.domain.model.Edition;
import com.library.books.domain.port.out.EditionRepository;

import com.library.books.application.dto.response.edition.EditionResponseDTO;

import java.util.List;

public class ListEditionsUseCase {

    private final EditionRepository editionRepository;

    public ListEditionsUseCase(EditionRepository editionRepository) {
        this.editionRepository = editionRepository;
    }

    public List<EditionResponseDTO> execute() {
        return editionRepository.findAll().stream()
                .map(EditionResponseDTO::of)
                .toList();
    }
}
