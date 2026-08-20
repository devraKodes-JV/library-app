package com.library.books.application.service.edition;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import com.library.books.domain.model.EditionAuthor;
import com.library.books.domain.port.out.EditionAuthorRepository;

class FakeEditionAuthorRepository implements EditionAuthorRepository {

    private final Map<Long, List<EditionAuthor>> store = new LinkedHashMap<>();
    private long nextId = 1L;

    @Override
    public EditionAuthor save(EditionAuthor editionAuthor) {
        if (editionAuthor.getId() == null) {
            editionAuthor.setId(nextId++);
        }
        store.computeIfAbsent(editionAuthor.getEditionId(), k -> new ArrayList<>()).add(editionAuthor);
        return editionAuthor;
    }

    @Override
    public void deleteById(Long id) {
    }

    @Override
    public List<EditionAuthor> findByEditionId(Long editionId) {
        return store.getOrDefault(editionId, List.of());
    }

    @Override
    public List<EditionAuthor> findByAuthorId(Long authorId) {
        return List.of();
    }

    @Override
    public void deleteByEditionId(Long editionId) {
        store.remove(editionId);
    }

    @Override
    public void deleteByAuthorId(Long authorId) {
    }

    @Override
    public void saveEditionAuthor(Long editionId, Long authorId, Long authorRoleId) {
        EditionAuthor ea = new EditionAuthor(null, editionId, authorId, authorRoleId, null, null);
        save(ea);
    }
}
