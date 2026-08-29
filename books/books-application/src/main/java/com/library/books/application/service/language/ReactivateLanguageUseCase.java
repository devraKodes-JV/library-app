package com.library.books.application.service.language;

import com.library.books.application.dto.command.language.ReactivateLanguageCommand;
import com.library.books.domain.port.out.EditionRepository;
import com.library.books.domain.port.out.LanguageRepository;

public class ReactivateLanguageUseCase {

    private final LanguageRepository languageRepository;
    private final EditionRepository editionRepository;

    public ReactivateLanguageUseCase(LanguageRepository languageRepository, EditionRepository editionRepository) {
        this.languageRepository = languageRepository;
        this.editionRepository = editionRepository;
    }

    public void execute(ReactivateLanguageCommand command) {
        languageRepository.reactivateById(command.id());
        languageRepository.reactivateEditionsByLanguageId(command.id());
    }
}
