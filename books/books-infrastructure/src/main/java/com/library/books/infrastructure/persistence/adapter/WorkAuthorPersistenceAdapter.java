package com.library.books.infrastructure.persistence.adapter;

import java.util.List;

import com.library.books.domain.model.WorkAuthor;
import com.library.books.domain.port.out.WorkAuthorRepository;
import com.library.books.infrastructure.persistence.entity.WorkAuthorEntity;
import com.library.books.infrastructure.persistence.mapper.WorkAuthorMapper;
import com.library.books.infrastructure.persistence.repository.hibernate.HibernateWorkAuthorRepository;

public class WorkAuthorPersistenceAdapter implements WorkAuthorRepository {

    private final HibernateWorkAuthorRepository workAuthorRepository;

    public WorkAuthorPersistenceAdapter(HibernateWorkAuthorRepository workAuthorRepository) {
        this.workAuthorRepository = workAuthorRepository;
    }

    @Override
    public WorkAuthor save(WorkAuthor workAuthor) {
        WorkAuthorEntity entity = WorkAuthorMapper.toEntity(workAuthor);
        WorkAuthorEntity saved = workAuthorRepository.save(entity);
        return WorkAuthorMapper.toDomain(saved);
    }

    @Override
    public void deleteById(Long id) {
        workAuthorRepository.deleteById(id);
    }

    @Override
    public List<WorkAuthor> findByWorkId(Long workId) {
        return workAuthorRepository.findByWorkId(workId).stream()
                .map(WorkAuthorMapper::toDomain)
                .toList();
    }

    @Override
    public List<WorkAuthor> findByAuthorId(Long authorId) {
        return workAuthorRepository.findByAuthorId(authorId).stream()
                .map(WorkAuthorMapper::toDomain)
                .toList();
    }

    @Override
    public void deleteByWorkId(Long workId) {
        workAuthorRepository.deleteByWorkId(workId);
    }

    @Override
    public void deleteByAuthorId(Long authorId) {
        workAuthorRepository.deleteByAuthorId(authorId);
    }

    @Override
    public java.util.List<WorkAuthor> findByWorkIds(java.util.List<Long> workIds) {
        return workAuthorRepository.findByWorkIds(workIds).stream()
                .map(WorkAuthorMapper::toDomain)
                .toList();
    }
}
