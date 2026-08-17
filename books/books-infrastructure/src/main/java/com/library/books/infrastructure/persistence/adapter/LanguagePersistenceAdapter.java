package com.library.books.infrastructure.persistence.adapter;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import com.library.books.domain.model.Language;
import com.library.books.domain.port.out.LanguageRepository;
import com.library.books.infrastructure.persistence.entity.LanguageEntity;
import com.library.books.infrastructure.persistence.mapper.LanguageMapper;
import com.library.books.infrastructure.persistence.repository.jpa.LanguageJpaRepository;

public class LanguagePersistenceAdapter implements LanguageRepository {

    private final LanguageJpaRepository<LanguageEntity, Long> languageJpaRepository;

    public LanguagePersistenceAdapter(LanguageJpaRepository<LanguageEntity, Long> languageJpaRepository) {
        this.languageJpaRepository = languageJpaRepository;
    }

    @Override
    public Optional<Language> findById(Long id) {
        return languageJpaRepository.findById(id)
                .map(LanguageMapper::toDomain);
    }

    @Override
    public List<Language> findAll() {
        return languageJpaRepository.findAll().stream()
                .map(LanguageMapper::toDomain)
                .toList();
    }

    @Override
    public Optional<Language> findByCode(String code) {
        return languageJpaRepository.findByCode(code)
                .map(LanguageMapper::toDomain);
    }

    @Override
    public Language save(Language language) {
        LanguageEntity entity = LanguageMapper.toEntity(language);
        LanguageEntity saved = languageJpaRepository.save(entity);
        return LanguageMapper.toDomain(saved);
    }

    @Override
    public void deleteById(Long id) {
        languageJpaRepository.deleteById(id);
    }

    @Override
    public Map<Long, String> findNamesByIds(List<Long> ids) {
        return languageJpaRepository.findNamesByIds(ids);
    }
}
