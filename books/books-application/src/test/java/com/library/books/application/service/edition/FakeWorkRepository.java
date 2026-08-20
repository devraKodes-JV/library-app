package com.library.books.application.service.edition;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import com.library.books.domain.model.Work;
import com.library.books.domain.port.out.WorkRepository;

class FakeWorkRepository implements WorkRepository {

    private final Map<Long, Work> store = new LinkedHashMap<>();
    private long nextId = 1L;

    @Override
    public Optional<Work> findById(Long id) {
        return Optional.ofNullable(store.get(id));
    }

    @Override
    public List<Work> findAll() {
        return new ArrayList<>(store.values());
    }

    @Override
    public List<Work> findByCategoryId(Long categoryId) {
        return List.of();
    }

    @Override
    public List<Work> findByOriginalLanguageId(Long languageId) {
        return List.of();
    }

    @Override
    public Work save(Work work) {
        if (work.getId() == null) {
            work.setId(nextId++);
        }
        store.put(work.getId(), work);
        return work;
    }

    @Override
    public void deleteById(Long id) {
        store.remove(id);
    }

    @Override
    public List<Work> findByIds(List<Long> ids) {
        return List.of();
    }

    @Override
    public List<Work> findByAuthorId(Long authorId) {
        return List.of();
    }

    @Override
    public boolean existsLanguage(Long id) {
        return true;
    }

    @Override
    public boolean existsCategory(Long id) {
        return true;
    }

    @Override
    public void saveWorkAuthor(Long workId, Long authorId, Long authorRoleId) {
    }

    @Override
    public void deleteWorkAuthorsByWorkId(Long workId) {
    }

    @Override
    public com.library.books.domain.dto.query.WorkWithRelationsDTO findByIdWithRelations(Long id) {
        return null;
    }

    @Override
    public void nullifyOriginalLanguage(Long languageId) {
    }

    @Override
    public void nullifyCategory(Long categoryId) {
    }
}
