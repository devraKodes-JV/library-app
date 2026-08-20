package com.library.books.application.service.publisher;

import com.library.books.application.dto.command.publisher.DeletePublisherCommand;
import com.library.books.domain.exception.ValidationException;
import com.library.books.domain.exception.PublisherNotFoundException;
import com.library.books.domain.model.Publisher;
import com.library.books.domain.port.out.EditionRepository;
import com.library.books.domain.port.out.PublisherRepository;

public class DeletePublisherUseCase {

    private final PublisherRepository publisherRepository;
    private final EditionRepository editionRepository;

    public DeletePublisherUseCase(PublisherRepository publisherRepository, EditionRepository editionRepository) {
        this.publisherRepository = publisherRepository;
        this.editionRepository = editionRepository;
    }

    public void execute(DeletePublisherCommand command) {
        Publisher existing = publisherRepository.findById(command.id())
                .orElseThrow(() -> new PublisherNotFoundException(command.id()));

        long activeEditions = editionRepository.countActiveByPublisherId(command.id());
        if (activeEditions > 0) {
            throw new ValidationException(java.util.Map.of(
                    "publisherId", "Cannot delete this publisher because it has active editions. Delete the editions first."
            ));
        }

        publisherRepository.deleteById(command.id());
    }
}
