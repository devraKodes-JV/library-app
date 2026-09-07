package com.library.books.application.service.authorRole;

import com.library.books.application.dto.command.authorRole.UpdateAuthorRoleCommand;
import com.library.books.application.dto.response.authorRole.AuthorRoleResponseDTO;
import com.library.books.domain.exception.AuthorRoleNotFoundException;
import com.library.books.domain.exception.ValidationException;
import com.library.books.domain.model.AuthorRole;
import com.library.books.domain.port.out.AuthorRoleRepository;
import java.util.Map;

public class UpdateAuthorRoleUseCase {

    private final AuthorRoleRepository authorRoleRepository;

    public UpdateAuthorRoleUseCase(AuthorRoleRepository authorRoleRepository) {
        this.authorRoleRepository = authorRoleRepository;
    }

    public AuthorRoleResponseDTO execute(UpdateAuthorRoleCommand command) {
        AuthorRole existing = authorRoleRepository.findById(command.id())
                .orElseThrow(() -> new AuthorRoleNotFoundException(command.id()));
        if (authorRoleRepository.findByName(command.name()).isPresent() 
                && !existing.getName().equals(command.name())) {
            throw new ValidationException(Map.of("name", "Name already exists"));
        }
        existing.setName(command.name());
        existing.setDescription(command.description());
        AuthorRole saved = authorRoleRepository.save(existing);
        return AuthorRoleResponseDTO.of(saved);
    }
}
