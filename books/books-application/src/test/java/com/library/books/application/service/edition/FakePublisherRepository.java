package com.library.books.application.service.edition;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import com.library.books.domain.model.Publisher;
import com.library.books.domain.port.out.PublisherRepository;

class FakePublisherRepository implements PublisherRepository {

    private final Map<Long, Publisher> store = new LinkedHashMap<>();
    private long nextId = 1L;

    @Override
    public Optional<Publisher> findById(Long id) {
        return Optional.ofNullable(store.get(id));
    }

    @Override
    public Optional<Publisher> findByCode(String code) {
        return store.values().stream()
                .filter(p -> p.getName().equals(code))
                .findFirst();
    }

    @Override
    public List<Publisher> findAll() {
        return new ArrayList<>(store.values());
    }

    @Override
    public Publisher save(Publisher publisher) {
        if (publisher.getId() == null) {
            Publisher withId = new Publisher(nextId++, publisher.getName(), publisher.getCountry(),
                    publisher.getWebsite(), publisher.getCreatedAt(), publisher.getUpdatedAt());
            store.put(withId.getId(), withId);
            return withId;
        }
        store.put(publisher.getId(), publisher);
        return publisher;
    }

    @Override
    public void deleteById(Long id) {
        store.remove(id);
    }

    @Override
    public Map<Long, String> findNamesByIds(List<Long> ids) {
        Map<Long, String> result = new LinkedHashMap<>();
        for (Long id : ids) {
            Publisher publisher = store.get(id);
            if (publisher != null) {
                result.put(id, publisher.getName());
            }
        }
        return result;
    }
}
