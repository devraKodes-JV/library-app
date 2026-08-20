package com.library.books.application.service.authorRole;

import com.library.books.domain.exception.AuthorRoleNotFoundException;
import com.library.books.domain.model.AuthorRole;
import com.library.books.domain.port.out.AuthorRoleRepository;

public class DeleteAuthorRoleUseCase {

    private final AuthorRoleRepository authorRoleRepository;

    public DeleteAuthorRoleUseCase(AuthorRoleRepository authorRoleRepository) {
        this.authorRoleRepository = authorRoleRepository;
    }

    public void execute(Long id) {
        AuthorRole existing = authorRoleRepository.findById(id)
                .orElseThrow(() -> new AuthorRoleNotFoundException(id));
        authorRoleRepository.deleteById(id);
    }
}
