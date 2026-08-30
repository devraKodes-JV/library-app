package com.library.books.application.service.language;

import com.library.books.application.dto.command.language.DeleteLanguageCommand;
import com.library.books.domain.exception.LanguageNotFoundException;
import com.library.books.domain.model.Language;
import com.library.books.domain.port.out.EditionRepository;
import com.library.books.domain.port.out.LanguageRepository;

public class DeleteLanguageUseCase {

    private final LanguageRepository languageRepository;
    private final EditionRepository editionRepository;

    public DeleteLanguageUseCase(LanguageRepository languageRepository, EditionRepository editionRepository) {
        this.languageRepository = languageRepository;
        this.editionRepository = editionRepository;
    }

    public void execute(DeleteLanguageCommand command) {
        Language existing = languageRepository.findById(command.id())
                .orElseThrow(() -> new LanguageNotFoundException(String.valueOf(command.id())));

        languageRepository.softDeleteEditionsByLanguageId(command.id());
        languageRepository.deleteById(command.id());
    }
}
