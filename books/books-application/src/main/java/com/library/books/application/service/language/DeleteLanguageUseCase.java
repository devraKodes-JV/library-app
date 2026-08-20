package com.library.books.application.service.language;

import com.library.books.application.dto.command.language.DeleteLanguageCommand;
import com.library.books.domain.exception.ValidationException;
import com.library.books.domain.exception.LanguageNotFoundException;
import com.library.books.domain.model.Language;
import com.library.books.domain.port.out.EditionRepository;
import com.library.books.domain.port.out.LanguageRepository;
import com.library.books.domain.port.out.WorkRepository;

public class DeleteLanguageUseCase {

    private final LanguageRepository languageRepository;
    private final EditionRepository editionRepository;
    private final WorkRepository workRepository;

    public DeleteLanguageUseCase(LanguageRepository languageRepository, EditionRepository editionRepository, WorkRepository workRepository) {
        this.languageRepository = languageRepository;
        this.editionRepository = editionRepository;
        this.workRepository = workRepository;
    }

    public void execute(DeleteLanguageCommand command) {
        Language existing = languageRepository.findById(command.id())
                .orElseThrow(() -> new LanguageNotFoundException(String.valueOf(command.id())));

        long activeEditions = editionRepository.countActiveByLanguageId(command.id());
        if (activeEditions > 0) {
            throw new ValidationException(java.util.Map.of(
                    "languageId", "Cannot delete this language because it has active editions. Delete the editions first."
            ));
        }

        languageRepository.deleteById(command.id());
        workRepository.nullifyOriginalLanguage(command.id());
    }
}
