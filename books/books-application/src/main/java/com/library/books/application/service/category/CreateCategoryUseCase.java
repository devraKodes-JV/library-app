package com.library.books.application.service.category;

import java.util.Map;

import com.library.books.application.dto.command.category.CreateCategoryCommand;
import com.library.books.application.dto.response.category.CategoryResponseDTO;
import com.library.books.application.validation.CategoryValidator;
import com.library.books.domain.exception.ValidationException;
import com.library.books.domain.model.Category;
import com.library.books.domain.port.out.CategoryRepository;
import com.library.kernel.generation.CodeGenerationService;

public class CreateCategoryUseCase {

    private final CategoryRepository categoryRepository;
    private final CategoryValidator categoryValidator;
    private final CodeGenerationService codeGenerationService;

    public CreateCategoryUseCase(CategoryRepository categoryRepository, CategoryValidator categoryValidator,
                                 CodeGenerationService codeGenerationService) {
        this.categoryRepository = categoryRepository;
        this.categoryValidator = categoryValidator;
        this.codeGenerationService = codeGenerationService;
    }

    public CategoryResponseDTO execute(CreateCategoryCommand command) {
        String code = codeGenerationService.generate("CAT");
        while (categoryRepository.findByCode(code).isPresent()) {
            code = codeGenerationService.generate("CAT");
        }
        Category category = Category.withoutId(code, command.name(), command.description(), command.parentId());
        categoryValidator.validate(category);
        Category saved = categoryRepository.save(category);
        return CategoryResponseDTO.of(saved);
    }
}
