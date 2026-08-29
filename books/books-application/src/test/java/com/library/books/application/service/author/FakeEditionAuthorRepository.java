package com.library.books.application.service.author;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.LinkedHashMap;

import com.library.books.domain.model.EditionAuthor;
import com.library.books.domain.port.out.EditionAuthorRepository;

class FakeEditionAuthorRepository implements EditionAuthorRepository {

    private final Map<Long, EditionAuthor> store = new LinkedHashMap<>();
    private long nextId = 1L;

    @Override
    public EditionAuthor save(EditionAuthor editionAuthor) {
        if (editionAuthor.getId() == null) {
            EditionAuthor withId = new EditionAuthor(nextId++, editionAuthor.getEditionId(), editionAuthor.getAuthorId(),
                    editionAuthor.getAuthorRoleId(), editionAuthor.getCreatedAt(), editionAuthor.getUpdatedAt());
            store.put(withId.getId(), withId);
            return withId;
        }
        store.put(editionAuthor.getId(), editionAuthor);
        return editionAuthor;
    }

    @Override
    public void deleteById(Long id) {
        store.remove(id);
    }

    @Override
    public List<EditionAuthor> findByEditionId(Long editionId) {
        return store.values().stream()
                .filter(ea -> ea.getEditionId().equals(editionId))
                .toList();
    }

    @Override
    public List<EditionAuthor> findByAuthorId(Long authorId) {
        return store.values().stream()
                .filter(ea -> ea.getAuthorId().equals(authorId))
                .toList();
    }

    @Override
    public void deleteByEditionId(Long editionId) {
        store.values().removeIf(ea -> ea.getEditionId().equals(editionId));
    }

    @Override
    public void deleteByAuthorId(Long authorId) {
        store.values().removeIf(ea -> ea.getAuthorId().equals(authorId));
    }

    @Override
    public void saveEditionAuthor(Long editionId, Long authorId, Long authorRoleId) {
        EditionAuthor ea = new EditionAuthor(null, editionId, authorId, authorRoleId, null, null);
        save(ea);
    }

    @Override
    public void softDeleteByAuthorId(Long authorId) {
    }

    @Override
    public void softDeleteByEditionId(Long editionId) {
    }

    @Override
    public void softDeleteByEditionIds(List<Long> editionIds) {
    }

    @Override
    public List<Long> findEditionIdsByAuthorId(Long authorId) {
        return store.values().stream()
                .filter(ea -> ea.getAuthorId().equals(authorId))
                .map(EditionAuthor::getEditionId)
                .distinct()
                .toList();
    }

    @Override
    public void reactivateByAuthorId(Long authorId) {}

    @Override
    public void reactivateByEditionId(Long editionId) {}
}
