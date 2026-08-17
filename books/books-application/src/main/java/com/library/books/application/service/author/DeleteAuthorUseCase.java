package com.library.books.application.service.author;

import com.library.books.application.dto.command.author.DeleteAuthorCommand;
import com.library.books.domain.exception.AuthorNotFoundException;
import com.library.books.domain.model.Author;
import com.library.books.domain.port.out.AuthorRepository;

public class DeleteAuthorUseCase {

    private final AuthorRepository authorRepository;

    public DeleteAuthorUseCase(AuthorRepository authorRepository) {
        this.authorRepository = authorRepository;
    }

    public void execute(DeleteAuthorCommand command) {
        Author existing = authorRepository.findById(command.id())
                .orElseThrow(() -> new AuthorNotFoundException(command.id()));
        authorRepository.deleteById(command.id());
    }
}
