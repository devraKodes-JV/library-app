package com.library.books.application.service.edition;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import com.library.books.domain.model.Edition;
import com.library.books.domain.model.EditionAuthor;
import com.library.books.domain.port.out.EditionRepository;
import com.library.books.domain.dto.response.edition.EditionWithNamesDTO;

class FakeEditionRepository implements EditionRepository {

    private final Map<Long, Edition> store = new LinkedHashMap<>();
    private final Map<Long, List<EditionWithNamesDTO>> detailsStore = new LinkedHashMap<>();
    private long nextId = 1L;

    @Override
    public Optional<Edition> findById(Long id) {
        return Optional.ofNullable(store.get(id));
    }

    @Override
    public List<Edition> findAll() {
        return new ArrayList<>(store.values());
    }

    @Override
    public List<Edition> findByWorkId(Long workId) {
        List<Edition> result = new ArrayList<>();
        for (Edition edition : store.values()) {
            if (workId.equals(edition.getWorkId())) {
                result.add(edition);
            }
        }
        return result;
    }

    @Override
    public List<Edition> findByPublisherId(Long publisherId) {
        List<Edition> result = new ArrayList<>();
        for (Edition edition : store.values()) {
            if (publisherId.equals(edition.getPublisherId())) {
                result.add(edition);
            }
        }
        return result;
    }

    @Override
    public List<Edition> findByFormatId(Long formatId) {
        List<Edition> result = new ArrayList<>();
        for (Edition edition : store.values()) {
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
        store.put(edition.getId(), edition);
        return edition;
    }

    @Override
    public void deleteById(Long id) {
        store.remove(id);
    }

    @Override
    public long countActiveByWorkId(Long workId) {
        return store.values().stream()
                .filter(e -> workId.equals(e.getWorkId()))
                .count();
    }

    @Override
    public long countActiveByPublisherId(Long publisherId) {
        return store.values().stream()
                .filter(e -> publisherId.equals(e.getPublisherId()))
                .count();
    }

    @Override
    public long countActiveByFormatId(Long formatId) {
        return store.values().stream()
                .filter(e -> formatId.equals(e.getFormatId()))
                .count();
    }

    @Override
    public long countActiveByLanguageId(Long languageId) {
        return store.values().stream()
                .filter(e -> languageId.equals(e.getLanguageId()))
                .count();
    }

    @Override
    public List<EditionWithNamesDTO> findByWorkIdWithDetails(Long workId) {
        return detailsStore.getOrDefault(workId, List.of());
    }

    public void putDetails(Long workId, List<EditionWithNamesDTO> dtos) {
        detailsStore.put(workId, dtos);
    }

    @Override
    public List<EditionAuthor> findEditionAuthorsByEditionId(Long editionId) {
        return List.of();
    }
}
