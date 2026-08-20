package com.library.books.application.service.publisher;

import java.util.Map;

import com.library.books.application.dto.command.publisher.CreatePublisherCommand;
import com.library.books.application.dto.response.publisher.PublisherResponseDTO;
import com.library.books.application.validation.PublisherValidator;
import com.library.books.domain.exception.ValidationException;
import com.library.books.domain.model.Publisher;
import com.library.books.domain.port.out.PublisherRepository;

public class CreatePublisherUseCase {

    private final PublisherRepository publisherRepository;
    private final PublisherValidator publisherValidator;

    public CreatePublisherUseCase(PublisherRepository publisherRepository, PublisherValidator publisherValidator) {
        this.publisherRepository = publisherRepository;
        this.publisherValidator = publisherValidator;
    }

    public PublisherResponseDTO execute(CreatePublisherCommand command) {
        if (publisherRepository.findByCode(command.name()).isPresent()) {
            throw new ValidationException(Map.of("name", "Name already exists"));
        }
        Publisher publisher = Publisher.withoutId(command.name(), command.country(), command.website());
        publisherValidator.validate(publisher);
        Publisher saved = publisherRepository.save(publisher);
        return PublisherResponseDTO.of(saved);
    }
}
