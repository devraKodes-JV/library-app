package com.library.books.application.service.publisher;

import com.library.books.domain.exception.PublisherNotFoundException;
import com.library.books.domain.model.Publisher;
import com.library.books.domain.port.out.PublisherRepository;

import com.library.books.application.dto.response.publisher.PublisherResponseDTO;

public class GetPublisherUseCase {

    private final PublisherRepository publisherRepository;

    public GetPublisherUseCase(PublisherRepository publisherRepository) {
        this.publisherRepository = publisherRepository;
    }

    public PublisherResponseDTO execute(Long id) {
        Publisher publisher = publisherRepository.findById(id)
                .orElseThrow(() -> new PublisherNotFoundException(id));
        return PublisherResponseDTO.of(publisher);
    }
}
