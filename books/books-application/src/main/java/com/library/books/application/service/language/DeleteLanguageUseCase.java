package com.library.books.application.service.language;

import com.library.books.application.dto.command.language.DeleteLanguageCommand;
import com.library.books.domain.exception.LanguageNotFoundException;
import com.library.books.domain.model.Language;
import com.library.books.domain.port.out.LanguageRepository;

public class DeleteLanguageUseCase {

    private final LanguageRepository languageRepository;

    public DeleteLanguageUseCase(LanguageRepository languageRepository) {
        this.languageRepository = languageRepository;
    }

    public void execute(DeleteLanguageCommand command) {
        Language existing = languageRepository.findById(command.id())
                .orElseThrow(() -> new LanguageNotFoundException(String.valueOf(command.id())));
        languageRepository.deleteById(command.id());
    }
}
