package com.library.books.application.service.authorRole;

import java.util.Map;

import com.library.books.application.dto.command.authorRole.CreateAuthorRoleCommand;
import com.library.books.application.dto.response.authorRole.AuthorRoleResponseDTO;
import com.library.books.domain.exception.ValidationException;
import com.library.books.domain.model.AuthorRole;
import com.library.books.domain.port.out.AuthorRoleRepository;

public class CreateAuthorRoleUseCase {

    private final AuthorRoleRepository authorRoleRepository;

    public CreateAuthorRoleUseCase(AuthorRoleRepository authorRoleRepository) {
        this.authorRoleRepository = authorRoleRepository;
    }

    public AuthorRoleResponseDTO execute(CreateAuthorRoleCommand command) {
        Map<String, String> errors = new java.util.LinkedHashMap<>();
        if (authorRoleRepository.findByCode(command.code()).isPresent()) {
            errors.put("code", "Code already exists");
        }
        if (authorRoleRepository.findByName(command.name()).isPresent()) {
            errors.put("name", "Name already exists");
        }
        if (!errors.isEmpty()) {
            throw new ValidationException(errors);
        }
        AuthorRole authorRole = AuthorRole.withoutId(command.code(), command.name(), command.description());
        AuthorRole saved = authorRoleRepository.save(authorRole);
        return AuthorRoleResponseDTO.of(saved);
    }
}
