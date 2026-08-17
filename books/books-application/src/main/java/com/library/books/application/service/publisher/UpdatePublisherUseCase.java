package com.library.books.application.service.publisher;

import com.library.books.application.dto.command.publisher.UpdatePublisherCommand;
import com.library.books.application.dto.response.publisher.PublisherResponseDTO;
import com.library.books.application.validation.PublisherValidator;
import com.library.books.domain.exception.PublisherNotFoundException;
import com.library.books.domain.model.Publisher;
import com.library.books.domain.port.out.PublisherRepository;

public class UpdatePublisherUseCase {

    private final PublisherRepository publisherRepository;
    private final PublisherValidator publisherValidator;

    public UpdatePublisherUseCase(PublisherRepository publisherRepository, PublisherValidator publisherValidator) {
        this.publisherRepository = publisherRepository;
        this.publisherValidator = publisherValidator;
    }

    public PublisherResponseDTO execute(UpdatePublisherCommand command) {
        Publisher existing = publisherRepository.findById(command.id())
                .orElseThrow(() -> new PublisherNotFoundException(command.id()));
        existing.setName(command.name());
        existing.setCountry(command.country());
        existing.setWebsite(command.website());
        publisherValidator.validate(existing);
        Publisher saved = publisherRepository.save(existing);
        return PublisherResponseDTO.of(saved);
    }
}
