package com.library.books.domain.port.out;

import java.util.List;
import java.util.Optional;

import com.library.books.domain.model.Publisher;

public interface PublisherRepository {
    Optional<Publisher> findById(Long id);
    List<Publisher> findAll();
    Publisher save(Publisher publisher);
    void deleteById(Long id);
    java.util.Map<Long, String> findNamesByIds(java.util.List<Long> ids);
}
