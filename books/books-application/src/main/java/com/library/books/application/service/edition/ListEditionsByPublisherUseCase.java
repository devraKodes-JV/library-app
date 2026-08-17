package com.library.books.application.service.edition;

import com.library.books.domain.model.Edition;
import com.library.books.domain.port.out.EditionRepository;

import com.library.books.application.dto.response.edition.EditionResponseDTO;

import java.util.List;

public class ListEditionsByPublisherUseCase {

    private final EditionRepository editionRepository;

    public ListEditionsByPublisherUseCase(EditionRepository editionRepository) {
        this.editionRepository = editionRepository;
    }

    public List<EditionResponseDTO> execute(Long publisherId) {
        return editionRepository.findByPublisherId(publisherId).stream()
                .map(EditionResponseDTO::of)
                .toList();
    }
}
