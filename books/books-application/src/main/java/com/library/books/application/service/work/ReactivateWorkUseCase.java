package com.library.books.application.service.work;

import java.util.List;

import com.library.books.application.dto.command.work.ReactivateWorkCommand;
import com.library.books.domain.exception.ReactivationException;
import com.library.books.domain.model.Edition;
import com.library.books.domain.port.out.EditionAuthorRepository;
import com.library.books.domain.port.out.EditionRepository;
import com.library.books.domain.port.out.WorkAuthorRepository;
import com.library.books.domain.port.out.WorkRepository;

public class ReactivateWorkUseCase {

    private final WorkRepository workRepository;
    private final WorkAuthorRepository workAuthorRepository;
    private final EditionRepository editionRepository;
    private final EditionAuthorRepository editionAuthorRepository;

    public ReactivateWorkUseCase(WorkRepository workRepository, WorkAuthorRepository workAuthorRepository, EditionRepository editionRepository, EditionAuthorRepository editionAuthorRepository) {
        this.workRepository = workRepository;
        this.workAuthorRepository = workAuthorRepository;
        this.editionRepository = editionRepository;
        this.editionAuthorRepository = editionAuthorRepository;
    }

    public void execute(ReactivateWorkCommand command) {
        workAuthorRepository.reactivateByWorkId(command.id());

        long activeAuthors = workRepository.countActiveAuthorsByWorkId(command.id());
        if (activeAuthors == 0) {
            throw new ReactivationException("Cannot reactivate work: at least one associated author must be active.");
        }

        workRepository.reactivateById(command.id());
        workRepository.reactivateEditionsByWorkId(command.id());

        List<Edition> editions = editionRepository.findByWorkId(command.id());
        for (Edition edition : editions) {
            editionAuthorRepository.reactivateByEditionId(edition.getId());
        }
    }
}
