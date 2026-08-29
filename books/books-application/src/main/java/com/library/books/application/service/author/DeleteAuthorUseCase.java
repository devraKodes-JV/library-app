package com.library.books.application.service.author;

import java.util.ArrayList;
import java.util.List;

import com.library.books.application.dto.command.author.DeleteAuthorCommand;
import com.library.books.domain.exception.AuthorNotFoundException;
import com.library.books.domain.model.Author;
import com.library.books.domain.model.Edition;
import com.library.books.domain.port.out.AuthorRepository;
import com.library.books.domain.port.out.EditionAuthorRepository;
import com.library.books.domain.port.out.EditionRepository;
import com.library.books.domain.port.out.WorkAuthorRepository;
import com.library.books.domain.port.out.WorkRepository;

public class DeleteAuthorUseCase {

    private final AuthorRepository authorRepository;
    private final WorkAuthorRepository workAuthorRepository;
    private final EditionAuthorRepository editionAuthorRepository;
    private final WorkRepository workRepository;
    private final EditionRepository editionRepository;

    public DeleteAuthorUseCase(AuthorRepository authorRepository, WorkAuthorRepository workAuthorRepository, EditionAuthorRepository editionAuthorRepository, WorkRepository workRepository, EditionRepository editionRepository) {
        this.authorRepository = authorRepository;
        this.workAuthorRepository = workAuthorRepository;
        this.editionAuthorRepository = editionAuthorRepository;
        this.workRepository = workRepository;
        this.editionRepository = editionRepository;
    }

    public void execute(DeleteAuthorCommand command) {
        Author existing = authorRepository.findById(command.id())
                .orElseThrow(() -> new AuthorNotFoundException(command.id()));

        List<Long> workIds = workAuthorRepository.findWorkIdsByAuthorId(command.id());
        workAuthorRepository.softDeleteByAuthorId(command.id());
        editionAuthorRepository.softDeleteByAuthorId(command.id());

        List<Long> worksToDelete = new ArrayList<>();
        for (Long workId : workIds) {
            long activeAuthors = workRepository.countActiveAuthorsByWorkId(workId);
            if (activeAuthors == 0) {
                worksToDelete.add(workId);
            }
        }

        if (!worksToDelete.isEmpty()) {
            List<Long> editionIdsToDelete = new ArrayList<>();
            for (Long workId : worksToDelete) {
                List<Edition> editions = editionRepository.findByWorkId(workId);
                for (Edition edition : editions) {
                    editionIdsToDelete.add(edition.getId());
                }
            }
            editionRepository.softDeleteEditionsByIds(editionIdsToDelete);
            editionAuthorRepository.softDeleteByEditionIds(editionIdsToDelete);
            workRepository.softDeleteWorksByIds(worksToDelete);
        }

        authorRepository.deleteById(command.id());
    }
}
