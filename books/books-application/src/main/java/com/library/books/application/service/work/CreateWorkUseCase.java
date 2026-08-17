package com.library.books.application.service.work;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.library.books.application.dto.command.work.CreateWorkCommand;
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

public class CreateWorkUseCase {

    private final WorkRepository workRepository;
    private final AuthorRepository authorRepository;
    private final LanguageRepository languageRepository;
    private final CategoryRepository categoryRepository;
    private final WorkValidator workValidator;
    private final Transactional transactional;

    public CreateWorkUseCase(WorkRepository workRepository, AuthorRepository authorRepository, LanguageRepository languageRepository, CategoryRepository categoryRepository, WorkValidator workValidator, Transactional transactional) {
        this.workRepository = workRepository;
        this.authorRepository = authorRepository;
        this.languageRepository = languageRepository;
        this.categoryRepository = categoryRepository;
        this.workValidator = workValidator;
        this.transactional = transactional;
    }

    public WorkResponseDTO execute(CreateWorkCommand command) {
        if (command.originalLanguageId() != null && !workRepository.existsLanguage(command.originalLanguageId())) {
            throw new LanguageNotFoundException(String.valueOf(command.originalLanguageId()));
        }
        if (command.categoryId() != null && !workRepository.existsCategory(command.categoryId())) {
            throw new CategoryNotFoundException(String.valueOf(command.categoryId()));
        }

        List<Long> parsedAuthorIds = command.authorIds().stream()
                .filter(id -> id != null && !id.isBlank())
                .map(Long::parseLong)
                .toList();

        return transactional.execute(() -> {
            Work work = Work.withoutId(
                    command.title(),
                    command.subtitle(),
                    command.originalLanguageId(),
                    command.categoryId(),
                    command.summary());
            work.setWorkAuthors(parsedAuthorIds.stream()
                    .map(authorId -> new WorkAuthor(null, null, authorId, "Author", null, null))
                    .toList());
            workValidator.validate(work);
            Work saved = workRepository.save(work);
            for (Long authorId : parsedAuthorIds) {
                workRepository.saveWorkAuthor(saved.getId(), authorId);
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
