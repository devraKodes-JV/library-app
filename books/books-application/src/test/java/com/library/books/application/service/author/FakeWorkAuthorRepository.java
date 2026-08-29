package com.library.books.application.service.author;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.LinkedHashMap;

import com.library.books.domain.model.WorkAuthor;
import com.library.books.domain.port.out.WorkAuthorRepository;

class FakeWorkAuthorRepository implements WorkAuthorRepository {

    private final Map<Long, WorkAuthor> store = new LinkedHashMap<>();
    private long nextId = 1L;

    @Override
    public WorkAuthor save(WorkAuthor workAuthor) {
        if (workAuthor.getId() == null) {
            WorkAuthor withId = new WorkAuthor(nextId++, workAuthor.getWorkId(), workAuthor.getAuthorId(),
                    workAuthor.getAuthorRoleId(), workAuthor.getCreatedAt(), workAuthor.getUpdatedAt());
            store.put(withId.getId(), withId);
            return withId;
        }
        store.put(workAuthor.getId(), workAuthor);
        return workAuthor;
    }

    @Override
    public void deleteById(Long id) {
        store.remove(id);
    }

    @Override
    public List<WorkAuthor> findByWorkId(Long workId) {
        return store.values().stream()
                .filter(wa -> wa.getWorkId().equals(workId))
                .toList();
    }

    @Override
    public List<WorkAuthor> findByAuthorId(Long authorId) {
        return store.values().stream()
                .filter(wa -> wa.getAuthorId().equals(authorId))
                .toList();
    }

    @Override
    public void deleteByWorkId(Long workId) {
        store.values().removeIf(wa -> wa.getWorkId().equals(workId));
    }

    @Override
    public void deleteByAuthorId(Long authorId) {
        store.values().removeIf(wa -> wa.getAuthorId().equals(authorId));
    }

    @Override
    public List<WorkAuthor> findByWorkIds(List<Long> workIds) {
        return store.values().stream()
                .filter(wa -> workIds.contains(wa.getWorkId()))
                .toList();
    }

    @Override
    public void saveWorkAuthor(Long workId, Long authorId, Long authorRoleId) {
        WorkAuthor wa = new WorkAuthor(null, workId, authorId, authorRoleId, null, null);
        save(wa);
    }

    @Override
    public void softDeleteByAuthorId(Long authorId) {
    }

    @Override
    public List<Long> findWorkIdsByAuthorId(Long authorId) {
        return store.values().stream()
                .filter(wa -> wa.getAuthorId().equals(authorId))
                .map(WorkAuthor::getWorkId)
                .distinct()
                .toList();
    }

    @Override
    public long countActiveAuthorsByWorkId(Long workId) {
        return 0;
    }

    @Override
    public void reactivateByAuthorId(Long authorId) {}

    @Override
    public void reactivateByWorkId(Long workId) {}
}
