package com.library.books.application.service.edition;

import java.util.List;

import com.library.books.application.dto.command.edition.CreateEditionCommand;
import com.library.books.application.dto.response.edition.EditionResponseDTO;
import com.library.books.application.validation.EditionValidator;
import com.library.books.domain.exception.WorkNotFoundException;
import com.library.books.domain.model.BookFormat;
import com.library.books.domain.model.Edition;
import com.library.books.domain.model.EditionAuthor;
import com.library.books.domain.model.Language;
import com.library.books.domain.model.Publisher;
import com.library.books.domain.model.Work;
import com.library.books.domain.port.out.BookFormatRepository;
import com.library.books.domain.port.out.EditionAuthorRepository;
import com.library.books.domain.port.out.EditionRepository;
import com.library.books.domain.port.out.LanguageRepository;
import com.library.books.domain.port.out.PublisherRepository;
import com.library.books.domain.port.out.WorkRepository;

import com.library.kernel.transaction.Transactional;

public class CreateEditionUseCase {

    private final EditionRepository editionRepository;
    private final EditionValidator editionValidator;
    private final Transactional transactional;
    private final WorkRepository workRepository;
    private final PublisherRepository publisherRepository;
    private final BookFormatRepository bookFormatRepository;
    private final LanguageRepository languageRepository;
    private final EditionAuthorRepository editionAuthorRepository;

    public CreateEditionUseCase(EditionRepository editionRepository, EditionValidator editionValidator, Transactional transactional, WorkRepository workRepository, PublisherRepository publisherRepository, BookFormatRepository bookFormatRepository, LanguageRepository languageRepository, EditionAuthorRepository editionAuthorRepository) {
        this.editionRepository = editionRepository;
        this.editionValidator = editionValidator;
        this.transactional = transactional;
        this.workRepository = workRepository;
        this.publisherRepository = publisherRepository;
        this.bookFormatRepository = bookFormatRepository;
        this.languageRepository = languageRepository;
        this.editionAuthorRepository = editionAuthorRepository;
    }

    public EditionResponseDTO execute(CreateEditionCommand command) {
        if (command.workId() == null) {
            throw new WorkNotFoundException(null);
        }

        List<Long> parsedAuthorIds = command.authorIds() == null ? List.of() : command.authorIds().stream()
                .filter(id -> id != null && !id.isBlank())
                .map(Long::parseLong)
                .toList();
        List<Long> parsedAuthorRoleIds = command.authorRoleIds() == null ? List.of() : command.authorRoleIds().stream()
                .filter(id -> id != null && !id.isBlank())
                .map(Long::parseLong)
                .toList();

        Edition edition = Edition.withoutId(
                command.workId(),
                command.publisherId(),
                command.formatId(),
                command.languageId(),
                command.isbn(),
                command.pages(),
                command.publicationYear(),
                command.editionNumber());
        edition.setEditionAuthors(parsedAuthorIds.stream()
                .map(authorId -> {
                    Long roleId = parsedAuthorRoleIds.isEmpty() ? null : parsedAuthorRoleIds.get(parsedAuthorIds.indexOf(authorId));
                    return new EditionAuthor(null, null, authorId, roleId, null, null);
                })
                .toList());
        editionValidator.validate(edition);
        Edition saved = editionRepository.save(edition);

        for (int i = 0; i < parsedAuthorIds.size(); i++) {
            Long authorId = parsedAuthorIds.get(i);
            Long roleId = parsedAuthorRoleIds.isEmpty() || i >= parsedAuthorRoleIds.size() ? null : parsedAuthorRoleIds.get(i);
            editionAuthorRepository.saveEditionAuthor(saved.getId(), authorId, roleId);
        }

        String workTitle = workRepository.findById(saved.getWorkId())
                .map(Work::getTitle)
                .orElse(null);
        String publisherName = publisherRepository.findById(saved.getPublisherId())
                .map(Publisher::getName)
                .orElse(null);
        String formatName = bookFormatRepository.findById(saved.getFormatId())
                .map(BookFormat::getName)
                .orElse(null);
        String languageName = languageRepository.findById(saved.getLanguageId())
                .map(Language::getName)
                .orElse(null);

        return EditionResponseDTO.of(saved, workTitle, publisherName, formatName, languageName);
    }
}
