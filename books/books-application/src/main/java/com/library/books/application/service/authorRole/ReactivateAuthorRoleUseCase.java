package com.library.books.application.service.authorRole;

import com.library.books.application.dto.command.authorRole.ReactivateAuthorRoleCommand;
import com.library.books.domain.model.AuthorRole;
import com.library.books.domain.port.out.AuthorRoleRepository;

public class ReactivateAuthorRoleUseCase {

    private final com.library.books.domain.port.out.AuthorRoleRepository authorRoleRepository;

    public ReactivateAuthorRoleUseCase(com.library.books.domain.port.out.AuthorRoleRepository authorRoleRepository) {
        this.authorRoleRepository = authorRoleRepository;
    }

    public void execute(ReactivateAuthorRoleCommand command) {
        authorRoleRepository.reactivateById(command.id());
        authorRoleRepository.reactivateWorkAuthorsByRoleId(command.id());
        authorRoleRepository.reactivateEditionAuthorsByRoleId(command.id());
    }
}
