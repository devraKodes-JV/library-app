package com.library.books.application.service.edition;

import com.library.books.domain.model.Edition;
import com.library.books.domain.port.out.EditionRepository;
import com.library.books.domain.port.out.WorkRepository;
import com.library.books.domain.port.out.PublisherRepository;
import com.library.books.domain.port.out.BookFormatRepository;
import com.library.books.domain.port.out.LanguageRepository;

import com.library.books.application.dto.response.edition.EditionResponseDTO;
import com.library.books.domain.model.BookFormat;
import com.library.books.domain.model.Language;
import com.library.books.domain.model.Publisher;
import com.library.books.domain.model.Work;

import java.util.List;

public class ListEditionsUseCase {

    private final EditionRepository editionRepository;
    private final WorkRepository workRepository;
    private final PublisherRepository publisherRepository;
    private final BookFormatRepository bookFormatRepository;
    private final LanguageRepository languageRepository;

    public ListEditionsUseCase(EditionRepository editionRepository, WorkRepository workRepository, PublisherRepository publisherRepository, BookFormatRepository bookFormatRepository, LanguageRepository languageRepository) {
        this.editionRepository = editionRepository;
        this.workRepository = workRepository;
        this.publisherRepository = publisherRepository;
        this.bookFormatRepository = bookFormatRepository;
        this.languageRepository = languageRepository;
    }

    public List<EditionResponseDTO> execute() {
        return execute("active");
    }

    public List<EditionResponseDTO> execute(String status) {
        return editionRepository.findAll(status).stream()
                .map(edition -> {
                    String workTitle = workRepository.findById(edition.getWorkId())
                            .map(Work::getTitle)
                            .orElse(null);
                    String publisherName = edition.getPublisherId() != null ? publisherRepository.findById(edition.getPublisherId())
                            .map(Publisher::getName)
                            .orElse(null) : null;
                    String formatName = edition.getFormatId() != null ? bookFormatRepository.findById(edition.getFormatId())
                            .map(BookFormat::getName)
                            .orElse(null) : null;
                    String languageName = edition.getLanguageId() != null ? languageRepository.findById(edition.getLanguageId())
                            .map(Language::getName)
                            .orElse(null) : null;
                    return EditionResponseDTO.of(edition, workTitle, publisherName, formatName, languageName);
                })
                .toList();
    }
}
