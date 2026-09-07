package com.library.books.application.service.language;

import com.library.books.application.dto.command.language.CreateLanguageCommand;
import com.library.books.application.dto.response.language.LanguageResponseDTO;
import com.library.books.application.validation.LanguageValidator;
import com.library.books.domain.model.Language;
import com.library.books.domain.port.out.LanguageRepository;
import com.library.kernel.generation.CodeGenerationService;

public class CreateLanguageUseCase {

    private final LanguageRepository languageRepository;
    private final LanguageValidator languageValidator;
    private final CodeGenerationService codeGenerationService;

    public CreateLanguageUseCase(LanguageRepository languageRepository, LanguageValidator languageValidator,
                                 CodeGenerationService codeGenerationService) {
        this.languageRepository = languageRepository;
        this.languageValidator = languageValidator;
        this.codeGenerationService = codeGenerationService;
    }

    public LanguageResponseDTO execute(CreateLanguageCommand command) {
        String code = codeGenerationService.generate("LANG");
        while (languageRepository.findByCode(code).isPresent()) {
            code = codeGenerationService.generate("LANG");
        }
        Language language = Language.withoutId(code, command.name());
        languageValidator.validate(language);
        Language saved = languageRepository.save(language);
        return LanguageResponseDTO.of(saved);
    }
}
