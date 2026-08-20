package com.library.books.application.service.work;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import com.library.books.domain.dto.query.WorkWithRelationsDTO;
import com.library.books.domain.model.Work;
import com.library.books.domain.port.out.WorkRepository;

class FakeWorkRepository implements WorkRepository {

    private final Map<Long, Work> store = new LinkedHashMap<>();
    private final Map<Long, WorkWithRelationsDTO> relationsStore = new LinkedHashMap<>();
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
        List<Work> result = new ArrayList<>();
        for (Work work : store.values()) {
            if (categoryId.equals(work.getCategoryId())) {
                result.add(work);
            }
        }
        return result;
    }

    @Override
    public List<Work> findByOriginalLanguageId(Long languageId) {
        List<Work> result = new ArrayList<>();
        for (Work work : store.values()) {
            if (languageId.equals(work.getOriginalLanguageId())) {
                result.add(work);
            }
        }
        return result;
    }

    @Override
    public List<Work> findByAuthorId(Long authorId) {
        List<Work> result = new ArrayList<>();
        for (Work work : store.values()) {
            if (work.getWorkAuthors() != null) {
                for (var wa : work.getWorkAuthors()) {
                    if (authorId.equals(wa.getAuthorId())) {
                        result.add(work);
                        break;
                    }
                }
            }
        }
        return result;
    }

    @Override
    public List<Work> findByIds(List<Long> ids) {
        List<Work> result = new ArrayList<>();
        for (Long id : ids) {
            Work work = store.get(id);
            if (work != null) {
                result.add(work);
            }
        }
        return result;
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
    public void nullifyOriginalLanguage(Long languageId) {
    }

    @Override
    public void nullifyCategory(Long categoryId) {
    }

    @Override
    public WorkWithRelationsDTO findByIdWithRelations(Long id) {
        return relationsStore.get(id);
    }

    public void putRelations(Long id, WorkWithRelationsDTO dto) {
        relationsStore.put(id, dto);
    }
}
