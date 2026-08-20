package com.library.books.application.service.author;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import com.library.books.domain.dto.query.AuthorWithWorksDTO;
import com.library.books.domain.model.Author;
import com.library.books.domain.port.out.AuthorRepository;

class FakeAuthorRepository implements AuthorRepository {

    private final Map<Long, Author> store = new LinkedHashMap<>();
    private final Map<Long, AuthorWithWorksDTO> withWorksStore = new LinkedHashMap<>();
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
            Author withId = new Author(nextId++, author.getFirstName(), author.getLastName(),
                    author.getBiography(), author.getBirthDate(), author.getDeathDate(),
                    author.getCreatedAt(), author.getUpdatedAt());
            store.put(withId.getId(), withId);
            return withId;
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
    public AuthorWithWorksDTO findByIdWithWorks(Long id) {
        return withWorksStore.get(id);
    }

    public void putWithWorks(Long id, AuthorWithWorksDTO dto) {
        withWorksStore.put(id, dto);
    }
}
