package com.library.books.application.service.work;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.library.books.application.dto.command.work.UpdateWorkCommand;
import com.library.books.application.dto.response.work.WorkResponseDTO;
import com.library.books.application.validation.WorkValidator;
import com.library.books.domain.exception.CategoryNotFoundException;
import com.library.books.domain.exception.LanguageNotFoundException;
import com.library.books.domain.exception.WorkNotFoundException;
import com.library.books.domain.model.Work;
import com.library.books.domain.model.WorkAuthor;
import com.library.books.domain.port.out.AuthorRepository;
import com.library.books.domain.port.out.CategoryRepository;
import com.library.books.domain.port.out.LanguageRepository;
import com.library.books.domain.port.out.WorkRepository;

import com.library.kernel.transaction.Transactional;

public class UpdateWorkUseCase {

    private final WorkRepository workRepository;
    private final AuthorRepository authorRepository;
    private final LanguageRepository languageRepository;
    private final CategoryRepository categoryRepository;
    private final WorkValidator workValidator;
    private final Transactional transactional;

    public UpdateWorkUseCase(WorkRepository workRepository, AuthorRepository authorRepository, LanguageRepository languageRepository, CategoryRepository categoryRepository, WorkValidator workValidator, Transactional transactional) {
        this.workRepository = workRepository;
        this.authorRepository = authorRepository;
        this.languageRepository = languageRepository;
        this.categoryRepository = categoryRepository;
        this.workValidator = workValidator;
        this.transactional = transactional;
    }

    public WorkResponseDTO execute(UpdateWorkCommand command) {
        Work existing = workRepository.findById(command.id())
                .orElseThrow(() -> new WorkNotFoundException(command.id()));
        if (command.originalLanguageId() != null && !workRepository.existsLanguage(command.originalLanguageId())) {
            throw new LanguageNotFoundException(String.valueOf(command.originalLanguageId()));
        }
        if (command.categoryId() != null && !workRepository.existsCategory(command.categoryId())) {
            throw new CategoryNotFoundException(String.valueOf(command.categoryId()));
        }

        List<Long> parsedAuthorIds = command.authorIds().stream()
                .filter(aid -> aid != null && !aid.isBlank())
                .map(Long::parseLong)
                .toList();
        List<Long> parsedAuthorRoleIds = command.authorRoleIds() == null ? List.of() : command.authorRoleIds().stream()
                .filter(id -> id != null && !id.isBlank())
                .map(Long::parseLong)
                .toList();

        return transactional.execute(() -> {
            existing.setTitle(command.title());
            existing.setSubtitle(command.subtitle());
            existing.setOriginalLanguageId(command.originalLanguageId());
            existing.setCategoryId(command.categoryId());
            existing.setSummary(command.summary());
            existing.setWorkAuthors(parsedAuthorIds.stream()
                    .map(authorId -> {
                        Long roleId = parsedAuthorRoleIds.isEmpty() ? null : parsedAuthorRoleIds.get(parsedAuthorIds.indexOf(authorId));
                        return new WorkAuthor(null, null, authorId, roleId, null, null);
                    })
                    .toList());
            workValidator.validate(existing);
            Work saved = workRepository.save(existing);
            if (!parsedAuthorIds.isEmpty()) {
                workRepository.deleteWorkAuthorsByWorkId(command.id());
                for (int i = 0; i < parsedAuthorIds.size(); i++) {
                    Long authorId = parsedAuthorIds.get(i);
                    Long roleId = parsedAuthorRoleIds.isEmpty() || i >= parsedAuthorRoleIds.size() ? null : parsedAuthorRoleIds.get(i);
                    workRepository.saveWorkAuthor(command.id(), authorId, roleId);
                }
            }
            return toWorkDTO(saved);
        });
    }

    private WorkResponseDTO toWorkDTO(Work work) {
        List<WorkAuthor> authors = work.getWorkAuthors() != null ? work.getWorkAuthors() : List.of();
        String languageName = work.getOriginalLanguageId() != null
                ? languageRepository.findNamesByIds(List.of(work.getOriginalLanguageId()))
                    .getOrDefault(work.getOriginalLanguageId(), "")
                : "";
        String categoryName = work.getCategoryId() != null
                ? categoryRepository.findNamesByIds(List.of(work.getCategoryId()))
                    .getOrDefault(work.getCategoryId(), "")
                : "";
        return WorkResponseDTO.of(
                work.getId(),
                work.getTitle(),
                work.getSubtitle(),
                languageName,
                categoryName,
                authors
        );
    }
}
