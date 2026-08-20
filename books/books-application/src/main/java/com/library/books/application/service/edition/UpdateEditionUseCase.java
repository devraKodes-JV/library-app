package com.library.books.application.service.edition;

import java.util.List;

import com.library.books.application.dto.command.edition.UpdateEditionCommand;
import com.library.books.application.dto.response.edition.EditionResponseDTO;
import com.library.books.application.validation.EditionValidator;
import com.library.books.domain.exception.EditionNotFoundException;
import com.library.books.domain.exception.WorkNotFoundException;
import com.library.books.domain.model.BookFormat;
import com.library.books.domain.model.Edition;
import com.library.books.domain.model.EditionAuthor;
import com.library.books.domain.model.Language;
import com.library.books.domain.model.Publisher;
import com.library.books.domain.port.out.BookFormatRepository;
import com.library.books.domain.port.out.EditionAuthorRepository;
import com.library.books.domain.port.out.EditionRepository;
import com.library.books.domain.port.out.LanguageRepository;
import com.library.books.domain.port.out.PublisherRepository;

public class UpdateEditionUseCase {

    private final EditionRepository editionRepository;
    private final EditionValidator editionValidator;
    private final PublisherRepository publisherRepository;
    private final BookFormatRepository bookFormatRepository;
    private final LanguageRepository languageRepository;
    private final EditionAuthorRepository editionAuthorRepository;

    public UpdateEditionUseCase(EditionRepository editionRepository, EditionValidator editionValidator, PublisherRepository publisherRepository, BookFormatRepository bookFormatRepository, LanguageRepository languageRepository, EditionAuthorRepository editionAuthorRepository) {
        this.editionRepository = editionRepository;
        this.editionValidator = editionValidator;
        this.publisherRepository = publisherRepository;
        this.bookFormatRepository = bookFormatRepository;
        this.languageRepository = languageRepository;
        this.editionAuthorRepository = editionAuthorRepository;
    }

    public EditionResponseDTO execute(UpdateEditionCommand command) {
        Edition existing = editionRepository.findById(command.id())
                .orElseThrow(() -> new EditionNotFoundException(command.id()));
        if (command.workId() == null) {
            throw new WorkNotFoundException(null);
        }

        List<Long> parsedAuthorIds = command.authorIds() == null ? List.of() : command.authorIds().stream()
                .filter(aid -> aid != null && !aid.isBlank())
                .map(Long::parseLong)
                .toList();
        List<Long> parsedAuthorRoleIds = command.authorRoleIds() == null ? List.of() : command.authorRoleIds().stream()
                .filter(id -> id != null && !id.isBlank())
                .map(Long::parseLong)
                .toList();

        existing.setWorkId(command.workId());
        existing.setPublisherId(command.publisherId());
        existing.setFormatId(command.formatId());
        existing.setLanguageId(command.languageId());
        existing.setIsbn(command.isbn());
        existing.setPages(command.pages());
        existing.setPublicationYear(command.publicationYear());
        existing.setEditionNumber(command.editionNumber());
        existing.setEditionAuthors(parsedAuthorIds.stream()
                .map(authorId -> {
                    Long roleId = parsedAuthorRoleIds.isEmpty() ? null : parsedAuthorRoleIds.get(parsedAuthorIds.indexOf(authorId));
                    return new EditionAuthor(null, null, authorId, roleId, null, null);
                })
                .toList());
        editionValidator.validate(existing);
        Edition saved = editionRepository.save(existing);

        if (!parsedAuthorIds.isEmpty()) {
            editionAuthorRepository.deleteByEditionId(command.id());
            for (int i = 0; i < parsedAuthorIds.size(); i++) {
                Long authorId = parsedAuthorIds.get(i);
                Long roleId = parsedAuthorRoleIds.isEmpty() || i >= parsedAuthorRoleIds.size() ? null : parsedAuthorRoleIds.get(i);
                editionAuthorRepository.saveEditionAuthor(command.id(), authorId, roleId);
            }
        }

        String publisherName = publisherRepository.findById(saved.getPublisherId())
                .map(Publisher::getName)
                .orElse(null);
        String formatName = bookFormatRepository.findById(saved.getFormatId())
                .map(BookFormat::getName)
                .orElse(null);
        String languageName = languageRepository.findById(saved.getLanguageId())
                .map(Language::getName)
                .orElse(null);

        return EditionResponseDTO.of(saved, null, publisherName, formatName, languageName);
    }
}
