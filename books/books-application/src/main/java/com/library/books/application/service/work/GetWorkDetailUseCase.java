package com.library.books.application.service.work;

import com.library.books.application.dto.response.work.WorkDetailResponseDTO;
import com.library.books.domain.dto.query.WorkWithRelationsDTO;
import com.library.books.domain.exception.WorkNotFoundException;
import com.library.books.domain.port.out.WorkRepository;

public class GetWorkDetailUseCase {

    private final WorkRepository workRepository;

    public GetWorkDetailUseCase(WorkRepository workRepository) {
        this.workRepository = workRepository;
    }

    public WorkDetailResponseDTO execute(Long id) {
        WorkWithRelationsDTO data = workRepository.findByIdWithRelations(id);
        if (data == null) {
            throw new WorkNotFoundException(id);
        }
        return WorkDetailResponseDTO.from(data);
    }
}
