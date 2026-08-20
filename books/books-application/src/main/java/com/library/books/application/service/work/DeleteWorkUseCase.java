package com.library.books.application.service.work;

import com.library.books.application.dto.command.work.DeleteWorkCommand;
import com.library.books.domain.exception.WorkNotFoundException;
import com.library.books.domain.exception.ValidationException;
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

        long activeEditions = editionRepository.countActiveByWorkId(command.id());
        if (activeEditions > 0) {
            throw new ValidationException(java.util.Map.of(
                    "workId", "Cannot delete this work because it has active editions. Delete the editions first."
            ));
        }

        workRepository.deleteById(command.id());
    }
}
