package com.library.books.application.service.work;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import com.library.books.domain.model.Author;
import com.library.books.domain.port.out.AuthorRepository;

class FakeAuthorRepository implements AuthorRepository {

    private final Map<Long, Author> store = new LinkedHashMap<>();
    private long nextId = 1L;

    @Override
    public Optional<Author> findById(Long id) {
        return Optional.ofNullable(store.get(id));
    }

    @Override
    public List<Author> findAll() {
        return new ArrayList<>(store.values());
    }

    @Override
    public Author save(Author author) {
        if (author.getId() == null) {
            author.setId(nextId++);
        }
        store.put(author.getId(), author);
        return author;
    }

    @Override
    public void deleteById(Long id) {
        store.remove(id);
    }

    @Override
    public Map<Long, String> findNamesByIds(List<Long> ids) {
        Map<Long, String> result = new LinkedHashMap<>();
        for (Long id : ids) {
            Author author = store.get(id);
            if (author != null) {
                result.put(id, author.getFirstName());
            }
        }
        return result;
    }

    @Override
    public Map<Long, String> findFullNamesByIds(List<Long> ids) {
        Map<Long, String> result = new LinkedHashMap<>();
        for (Long id : ids) {
            Author author = store.get(id);
            if (author != null) {
                String fullName = author.getFirstName() + " " + author.getLastName();
                result.put(id, fullName);
            }
        }
        return result;
    }

    @Override
    public com.library.books.domain.dto.query.AuthorWithWorksDTO findByIdWithWorks(Long id) {
        return null;
    }
}
