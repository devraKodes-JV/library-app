package com.library.books.application.service.bookFormat;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import com.library.books.domain.dto.response.edition.EditionWithNamesDTO;
import com.library.books.domain.model.Edition;
import com.library.books.domain.model.EditionAuthor;
import com.library.books.domain.port.out.EditionRepository;

class FakeEditionRepository implements EditionRepository {

    private final List<Edition> editions = new ArrayList<>();
    private long nextId = 1L;

    @Override
    public Optional<Edition> findById(Long id) {
        return editions.stream().filter(e -> e.getId().equals(id)).findFirst();
    }

    @Override
    public List<Edition> findAll() {
        return new ArrayList<>(editions);
    }

    @Override
    public List<Edition> findByWorkId(Long workId) {
        return List.of();
    }

    @Override
    public List<Edition> findByPublisherId(Long publisherId) {
        return List.of();
    }

    @Override
    public List<Edition> findByFormatId(Long formatId) {
        List<Edition> result = new ArrayList<>();
        for (Edition edition : editions) {
            if (formatId.equals(edition.getFormatId())) {
                result.add(edition);
            }
        }
        return result;
    }

    @Override
    public Edition save(Edition edition) {
        if (edition.getId() == null) {
            edition.setId(nextId++);
        }
        editions.add(edition);
        return edition;
    }

    @Override
    public void deleteById(Long id) {
        editions.removeIf(e -> e.getId().equals(id));
    }

    @Override
    public long countActiveByWorkId(Long workId) {
        return 0;
    }

    @Override
    public long countActiveByPublisherId(Long publisherId) {
        return 0;
    }

    @Override
    public long countActiveByFormatId(Long formatId) {
        return editions.stream()
                .filter(e -> formatId.equals(e.getFormatId()))
                .count();
    }

    @Override
    public long countActiveByLanguageId(Long languageId) {
        return 0;
    }

    @Override
    public List<EditionWithNamesDTO> findByWorkIdWithDetails(Long workId) {
        return List.of();
    }

    @Override
    public List<EditionAuthor> findEditionAuthorsByEditionId(Long editionId) {
        return List.of();
    }
}
