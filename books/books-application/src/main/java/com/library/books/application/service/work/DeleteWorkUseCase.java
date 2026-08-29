package com.library.books.application.service.work;

import java.util.List;

import com.library.books.application.dto.command.work.DeleteWorkCommand;
import com.library.books.domain.exception.WorkNotFoundException;
import com.library.books.domain.exception.ValidationException;
import com.library.books.domain.model.Edition;
import com.library.books.domain.model.Work;
import com.library.books.domain.port.out.EditionRepository;
import com.library.books.domain.port.out.WorkRepository;

public class DeleteWorkUseCase {

    private final WorkRepository workRepository;
    private final EditionRepository editionRepository;

    public DeleteWorkUseCase(WorkRepository workRepository, EditionRepository editionRepository) {
        this.workRepository = workRepository;
        this.editionRepository = editionRepository;
    }

    public void execute(DeleteWorkCommand command) {
        Work existing = workRepository.findById(command.id())
                .orElseThrow(() -> new WorkNotFoundException(command.id()));

        List<Edition> editions = editionRepository.findByWorkId(command.id());
        if (!editions.isEmpty()) {
            editionRepository.softDeleteEditionsByIds(
                    editions.stream().map(Edition::getId).toList()
            );
        }

        workRepository.deleteById(command.id());
    }
}
