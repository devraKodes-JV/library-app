package com.library.books.application.service.edition;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;

import com.library.books.application.dto.response.edition.EditionResponseDTO;
import com.library.books.domain.exception.EditionNotFoundException;
import com.library.books.domain.model.Edition;

class GetEditionUseCaseTest {

    @Test
    void getEdition_returnsEdition() {
        FakeEditionRepository editionRepository = new FakeEditionRepository();
        FakeWorkRepository workRepository = new FakeWorkRepository();
        FakePublisherRepository publisherRepository = new FakePublisherRepository();
        FakeBookFormatRepository bookFormatRepository = new FakeBookFormatRepository();
        FakeLanguageRepository languageRepository = new FakeLanguageRepository();
        GetEditionUseCase useCase = new GetEditionUseCase(editionRepository, workRepository, publisherRepository, bookFormatRepository, languageRepository);

        com.library.books.domain.model.Work work = workRepository.save(com.library.books.domain.model.Work.withoutId("The Hobbit", null, null, null, null));
        com.library.books.domain.model.Publisher publisher = publisherRepository.save(com.library.books.domain.model.Publisher.withoutId("Penguin", "UK", "penguin.com"));
        com.library.books.domain.model.BookFormat format = bookFormatRepository.save(com.library.books.domain.model.BookFormat.withoutId("HC", "Hardcover", "Hardcover edition"));
        com.library.books.domain.model.Language language = languageRepository.save(com.library.books.domain.model.Language.withoutId("en", "English"));

        Edition saved = editionRepository.save(Edition.withoutId(work.getId(), publisher.getId(), format.getId(), language.getId(), "1234567890", 300, 2020, "1st"));

        EditionResponseDTO result = useCase.execute(saved.getId());

        assertEquals("The Hobbit", result.workTitle());
        assertEquals("Penguin", result.publisherName());
        assertEquals("Hardcover", result.formatName());
        assertEquals("English", result.languageName());
    }

    @Test
    void getEdition_throwsWhenNotFound() {
        FakeEditionRepository editionRepository = new FakeEditionRepository();
        FakeWorkRepository workRepository = new FakeWorkRepository();
        FakePublisherRepository publisherRepository = new FakePublisherRepository();
        FakeBookFormatRepository bookFormatRepository = new FakeBookFormatRepository();
        FakeLanguageRepository languageRepository = new FakeLanguageRepository();
        GetEditionUseCase useCase = new GetEditionUseCase(editionRepository, workRepository, publisherRepository, bookFormatRepository, languageRepository);

        assertThrows(EditionNotFoundException.class, () -> useCase.execute(999L));
    }
}
