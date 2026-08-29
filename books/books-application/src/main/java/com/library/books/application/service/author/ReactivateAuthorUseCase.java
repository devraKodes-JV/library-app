package com.library.books.application.service.author;

import com.library.books.application.dto.command.author.ReactivateAuthorCommand;
import com.library.books.domain.port.out.AuthorRepository;
import com.library.books.domain.port.out.EditionAuthorRepository;
import com.library.books.domain.port.out.WorkAuthorRepository;

public class ReactivateAuthorUseCase {

    private final AuthorRepository authorRepository;
    private final WorkAuthorRepository workAuthorRepository;
    private final EditionAuthorRepository editionAuthorRepository;

    public ReactivateAuthorUseCase(AuthorRepository authorRepository, WorkAuthorRepository workAuthorRepository, EditionAuthorRepository editionAuthorRepository) {
        this.authorRepository = authorRepository;
        this.workAuthorRepository = workAuthorRepository;
        this.editionAuthorRepository = editionAuthorRepository;
    }

    public void execute(ReactivateAuthorCommand command) {
        authorRepository.reactivateById(command.id());
        workAuthorRepository.reactivateByAuthorId(command.id());
        editionAuthorRepository.reactivateByAuthorId(command.id());
        authorRepository.reactivateWorksByAuthorId(command.id());
    }
}
