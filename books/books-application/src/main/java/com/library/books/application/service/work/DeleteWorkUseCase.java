package com.library.books.application.service.work;

import com.library.books.application.dto.command.work.DeleteWorkCommand;
import com.library.books.domain.exception.WorkNotFoundException;
import com.library.books.domain.model.Work;
import com.library.books.domain.port.out.WorkRepository;

public class DeleteWorkUseCase {

    private final WorkRepository workRepository;

    public DeleteWorkUseCase(WorkRepository workRepository) {
        this.workRepository = workRepository;
    }

    public void execute(DeleteWorkCommand command) {
        Work existing = workRepository.findById(command.id())
                .orElseThrow(() -> new WorkNotFoundException(command.id()));
        workRepository.deleteById(command.id());
    }
}
