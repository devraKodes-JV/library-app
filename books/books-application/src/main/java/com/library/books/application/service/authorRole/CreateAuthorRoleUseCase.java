package com.library.books.application.service.authorRole;

import java.util.Map;

import com.library.books.application.dto.command.authorRole.CreateAuthorRoleCommand;
import com.library.books.application.dto.response.authorRole.AuthorRoleResponseDTO;
import com.library.books.domain.exception.ValidationException;
import com.library.books.domain.model.AuthorRole;
import com.library.books.domain.port.out.AuthorRoleRepository;
import com.library.kernel.generation.CodeGenerationService;

public class CreateAuthorRoleUseCase {

    private final AuthorRoleRepository authorRoleRepository;
    private final CodeGenerationService codeGenerationService;

    public CreateAuthorRoleUseCase(AuthorRoleRepository authorRoleRepository,
                                   CodeGenerationService codeGenerationService) {
        this.authorRoleRepository = authorRoleRepository;
        this.codeGenerationService = codeGenerationService;
    }

    public AuthorRoleResponseDTO execute(CreateAuthorRoleCommand command) {
        Map<String, String> errors = new java.util.LinkedHashMap<>();
        if (authorRoleRepository.findByName(command.name()).isPresent()) {
            errors.put("name", "Name already exists");
        }
        if (!errors.isEmpty()) {
            throw new ValidationException(errors);
        }

        String code = codeGenerationService.generate("ROLE");
        while (authorRoleRepository.findByCode(code).isPresent()) {
            code = codeGenerationService.generate("ROLE");
        }
        AuthorRole authorRole = AuthorRole.withoutId(code, command.name(), command.description());
        AuthorRole saved = authorRoleRepository.save(authorRole);
        return AuthorRoleResponseDTO.of(saved);
    }
}
