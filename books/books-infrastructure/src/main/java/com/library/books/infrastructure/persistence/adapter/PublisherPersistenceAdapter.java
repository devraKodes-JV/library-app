package com.library.books.infrastructure.persistence.adapter;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import com.library.books.domain.model.Publisher;
import com.library.books.domain.port.out.PublisherRepository;
import com.library.books.infrastructure.persistence.entity.PublisherEntity;
import com.library.books.infrastructure.persistence.mapper.PublisherMapper;
import com.library.books.infrastructure.persistence.repository.jpa.PublisherJpaRepository;

public class PublisherPersistenceAdapter implements PublisherRepository {

    private final PublisherJpaRepository<PublisherEntity, Long> publisherJpaRepository;

    public PublisherPersistenceAdapter(PublisherJpaRepository<PublisherEntity, Long> publisherJpaRepository) {
        this.publisherJpaRepository = publisherJpaRepository;
    }

    @Override
    public Optional<Publisher> findById(Long id) {
        return publisherJpaRepository.findById(id)
                .map(PublisherMapper::toDomain);
    }

    @Override
    public List<Publisher> findAll() {
        return publisherJpaRepository.findAll().stream()
                .map(PublisherMapper::toDomain)
                .toList();
    }

    @Override
    public Publisher save(Publisher publisher) {
        PublisherEntity entity = PublisherMapper.toEntity(publisher);
        PublisherEntity saved = publisherJpaRepository.save(entity);
        return PublisherMapper.toDomain(saved);
    }

    @Override
    public void deleteById(Long id) {
        publisherJpaRepository.deleteById(id);
    }

    @Override
    public Map<Long, String> findNamesByIds(List<Long> ids) {
        return publisherJpaRepository.findNamesByIds(ids);
    }
}
