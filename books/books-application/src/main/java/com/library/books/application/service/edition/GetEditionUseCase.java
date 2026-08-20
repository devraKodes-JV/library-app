package com.library.books.application.service.edition;

import java.util.List;

import com.library.books.application.dto.response.edition.EditionResponseDTO;
import com.library.books.domain.exception.EditionNotFoundException;
import com.library.books.domain.model.BookFormat;
import com.library.books.domain.model.Edition;
import com.library.books.domain.model.EditionAuthor;
import com.library.books.domain.model.Language;
import com.library.books.domain.model.Publisher;
import com.library.books.domain.model.Work;
import com.library.books.domain.port.out.BookFormatRepository;
import com.library.books.domain.port.out.EditionRepository;
import com.library.books.domain.port.out.LanguageRepository;
import com.library.books.domain.port.out.PublisherRepository;
import com.library.books.domain.port.out.WorkRepository;

public class GetEditionUseCase {

    private final EditionRepository editionRepository;
    private final WorkRepository workRepository;
    private final PublisherRepository publisherRepository;
    private final BookFormatRepository bookFormatRepository;
    private final LanguageRepository languageRepository;

    public GetEditionUseCase(EditionRepository editionRepository,
                             WorkRepository workRepository,
                             PublisherRepository publisherRepository,
                             BookFormatRepository bookFormatRepository,
                             LanguageRepository languageRepository) {
        this.editionRepository = editionRepository;
        this.workRepository = workRepository;
        this.publisherRepository = publisherRepository;
        this.bookFormatRepository = bookFormatRepository;
        this.languageRepository = languageRepository;
    }

    public EditionResponseDTO execute(Long id) {
        Edition edition = editionRepository.findById(id)
                .orElseThrow(() -> new EditionNotFoundException(id));

        String workTitle = workRepository.findById(edition.getWorkId())
                .map(Work::getTitle)
                .orElse(null);

        String publisherName = publisherRepository.findById(edition.getPublisherId())
                .map(Publisher::getName)
                .orElse(null);

        String formatName = bookFormatRepository.findById(edition.getFormatId())
                .map(BookFormat::getName)
                .orElse(null);

        String languageName = languageRepository.findById(edition.getLanguageId())
                .map(Language::getName)
                .orElse(null);

        List<EditionAuthor> editionAuthors = editionRepository.findEditionAuthorsByEditionId(id);

        return EditionResponseDTO.of(edition, workTitle, publisherName, formatName, languageName, editionAuthors);
    }
}
