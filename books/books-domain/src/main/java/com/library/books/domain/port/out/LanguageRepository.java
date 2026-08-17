package com.library.books.domain.port.out;

import java.util.List;
import java.util.Optional;

import com.library.books.domain.model.Language;

public interface LanguageRepository {
    Optional<Language> findById(Long id);
    List<Language> findAll();
    Optional<Language> findByCode(String code);
    Language save(Language language);
    void deleteById(Long id);
    java.util.Map<Long, String> findNamesByIds(java.util.List<Long> ids);
}
