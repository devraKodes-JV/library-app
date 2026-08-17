package com.library.books.application.service.publisher;

import com.library.books.domain.model.Publisher;
import com.library.books.domain.port.out.PublisherRepository;

import com.library.books.application.dto.response.publisher.PublisherResponseDTO;

import java.util.List;

public class ListPublishersUseCase {

    private final PublisherRepository publisherRepository;

    public ListPublishersUseCase(PublisherRepository publisherRepository) {
        this.publisherRepository = publisherRepository;
    }

    public List<PublisherResponseDTO> execute() {
        return publisherRepository.findAll().stream()
                .map(PublisherResponseDTO::of)
                .toList();
    }
}
