package com.library.books.application.service.edition;

import com.library.books.domain.exception.EditionNotFoundException;
import com.library.books.domain.model.Edition;
import com.library.books.domain.port.out.EditionRepository;

import com.library.books.application.dto.response.edition.EditionResponseDTO;

public class GetEditionUseCase {

    private final EditionRepository editionRepository;

    public GetEditionUseCase(EditionRepository editionRepository) {
        this.editionRepository = editionRepository;
    }

    public EditionResponseDTO execute(Long id) {
        Edition edition = editionRepository.findById(id)
                .orElseThrow(() -> new EditionNotFoundException(id));
        return EditionResponseDTO.of(edition);
    }
}
