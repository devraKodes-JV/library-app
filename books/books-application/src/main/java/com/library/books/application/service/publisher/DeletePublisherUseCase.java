package com.library.books.application.service.publisher;

import com.library.books.application.dto.command.publisher.DeletePublisherCommand;
import com.library.books.domain.exception.PublisherNotFoundException;
import com.library.books.domain.model.Publisher;
import com.library.books.domain.port.out.PublisherRepository;

public class DeletePublisherUseCase {

    private final PublisherRepository publisherRepository;

    public DeletePublisherUseCase(PublisherRepository publisherRepository) {
        this.publisherRepository = publisherRepository;
    }

    public void execute(DeletePublisherCommand command) {
        Publisher existing = publisherRepository.findById(command.id())
                .orElseThrow(() -> new PublisherNotFoundException(command.id()));
        publisherRepository.deleteById(command.id());
    }
}
