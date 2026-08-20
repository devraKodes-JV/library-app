package com.library.books.application.service.authorRole;

import java.util.List;

import com.library.books.application.dto.response.authorRole.AuthorRoleResponseDTO;
import com.library.books.domain.port.out.AuthorRoleRepository;

public class ListAuthorRolesUseCase {

    private final AuthorRoleRepository authorRoleRepository;

    public ListAuthorRolesUseCase(AuthorRoleRepository authorRoleRepository) {
        this.authorRoleRepository = authorRoleRepository;
    }

    public List<AuthorRoleResponseDTO> execute() {
        return authorRoleRepository.findAll().stream()
                .map(AuthorRoleResponseDTO::of)
                .toList();
    }
}
