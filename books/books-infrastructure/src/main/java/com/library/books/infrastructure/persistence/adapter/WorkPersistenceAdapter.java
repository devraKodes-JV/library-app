package com.library.books.infrastructure.persistence.adapter;

import java.util.List;
import java.util.Optional;

import com.library.books.domain.model.Work;
import com.library.books.domain.port.out.WorkRepository;
import com.library.books.infrastructure.persistence.entity.WorkEntity;
import com.library.books.infrastructure.persistence.mapper.WorkMapper;
import com.library.books.infrastructure.persistence.repository.jpa.WorkJpaRepository;
import com.library.books.domain.dto.common.FlatAuthorDTO;
import com.library.books.domain.dto.query.WorkWithRelationsDTO;

public class WorkPersistenceAdapter implements WorkRepository {

    private final WorkJpaRepository<WorkEntity, Long> workJpaRepository;
    private final com.library.books.infrastructure.persistence.repository.hibernate.HibernateWorkRepository hibernateWorkRepository;

    public WorkPersistenceAdapter(WorkJpaRepository<WorkEntity, Long> workJpaRepository, com.library.books.infrastructure.persistence.repository.hibernate.HibernateWorkRepository hibernateWorkRepository) {
        this.workJpaRepository = workJpaRepository;
        this.hibernateWorkRepository = hibernateWorkRepository;
    }

    @Override
    public Optional<Work> findById(Long id) {
        return workJpaRepository.findById(id)
                .map(WorkMapper::toDomain);
    }

    @Override
    public List<Work> findAll() {
        return workJpaRepository.findAll().stream()
                .map(WorkMapper::toDomain)
                .toList();
    }

    @Override
    public List<Work> findByCategoryId(Long categoryId) {
        return workJpaRepository.findByCategoryId(categoryId).stream()
                .map(WorkMapper::toDomain)
                .toList();
    }

    @Override
    public List<Work> findByOriginalLanguageId(Long languageId) {
        return workJpaRepository.findByOriginalLanguageId(languageId).stream()
                .map(WorkMapper::toDomain)
                .toList();
    }

    @Override
    public List<Work> findByAuthorId(Long authorId) {
        return workJpaRepository.findByAuthorId(authorId).stream()
                .map(WorkMapper::toDomain)
                .toList();
    }

    @Override
    public Work save(Work work) {
        WorkEntity entity = WorkMapper.toEntity(work);
        WorkEntity saved = workJpaRepository.save(entity);
        return WorkMapper.toDomain(saved);
    }

    @Override
    public void deleteById(Long id) {
        workJpaRepository.deleteById(id);
    }

    @Override
    public List<Work> findByIds(List<Long> ids) {
        return workJpaRepository.findByIds(ids).stream()
                .map(WorkMapper::toDomain)
                .toList();
    }

    @Override
    public boolean existsLanguage(Long id) {
        return workJpaRepository.existsLanguage(id);
    }

    @Override
    public boolean existsCategory(Long id) {
        return workJpaRepository.existsCategory(id);
    }

    @Override
    public void saveWorkAuthor(Long workId, Long authorId) {
        workJpaRepository.saveWorkAuthor(workId, authorId);
    }

    @Override
    public void deleteWorkAuthorsByWorkId(Long workId) {
        workJpaRepository.deleteWorkAuthorsByWorkId(workId);
    }

    @Override
    public WorkWithRelationsDTO findByIdWithRelations(Long id) {
        return hibernateWorkRepository.findByIdWithRelations(id);
    }
}
