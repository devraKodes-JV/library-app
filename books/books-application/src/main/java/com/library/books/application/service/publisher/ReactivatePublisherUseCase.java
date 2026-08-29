package com.library.books.application.service.publisher;

import com.library.books.application.dto.command.publisher.ReactivatePublisherCommand;
import com.library.books.domain.port.out.EditionRepository;
import com.library.books.domain.port.out.PublisherRepository;

public class ReactivatePublisherUseCase {

    private final PublisherRepository publisherRepository;
    private final EditionRepository editionRepository;

    public ReactivatePublisherUseCase(PublisherRepository publisherRepository, EditionRepository editionRepository) {
        this.publisherRepository = publisherRepository;
        this.editionRepository = editionRepository;
    }

    public void execute(ReactivatePublisherCommand command) {
        publisherRepository.reactivateById(command.id());
        publisherRepository.reactivateEditionsByPublisherId(command.id());
    }
}
