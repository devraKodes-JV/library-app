package com.library.books.application.service.bookFormat;

import com.library.books.application.dto.command.bookFormat.CreateBookFormatCommand;
import com.library.books.application.dto.response.booksFormat.BookFormatResponseDTO;
import com.library.books.application.validation.BookFormatValidator;
import com.library.books.domain.model.BookFormat;
import com.library.books.domain.port.out.BookFormatRepository;
import com.library.kernel.generation.CodeGenerationService;

public class CreateBookFormatUseCase {

    private final BookFormatRepository bookFormatRepository;
    private final BookFormatValidator bookFormatValidator;
    private final CodeGenerationService codeGenerationService;

    public CreateBookFormatUseCase(BookFormatRepository bookFormatRepository, BookFormatValidator bookFormatValidator,
                                   CodeGenerationService codeGenerationService) {
        this.bookFormatRepository = bookFormatRepository;
        this.bookFormatValidator = bookFormatValidator;
        this.codeGenerationService = codeGenerationService;
    }

    public BookFormatResponseDTO execute(CreateBookFormatCommand command) {
        String code = codeGenerationService.generate("FMT");
        while (bookFormatRepository.findByCode(code).isPresent()) {
            code = codeGenerationService.generate("FMT");
        }
        BookFormat format = BookFormat.withoutId(code, command.name(), command.description());
        bookFormatValidator.validate(format);
        BookFormat saved = bookFormatRepository.save(format);
        return BookFormatResponseDTO.of(saved);
    }
}
