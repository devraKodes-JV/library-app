package com.library.books.application.service.authorRole;

import com.library.books.application.dto.response.authorRole.AuthorRoleResponseDTO;
import com.library.books.domain.exception.AuthorRoleNotFoundException;
import com.library.books.domain.model.AuthorRole;
import com.library.books.domain.port.out.AuthorRoleRepository;

public class GetAuthorRoleUseCase {

    private final AuthorRoleRepository authorRoleRepository;

    public GetAuthorRoleUseCase(AuthorRoleRepository authorRoleRepository) {
        this.authorRoleRepository = authorRoleRepository;
    }

    public AuthorRoleResponseDTO execute(Long id) {
        AuthorRole authorRole = authorRoleRepository.findById(id)
                .orElseThrow(() -> new AuthorRoleNotFoundException(id));
        return AuthorRoleResponseDTO.of(authorRole);
    }
}
