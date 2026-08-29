package com.library.books.infrastructure.persistence.repository.jpa;

import java.util.List;
import com.library.books.domain.model.Language;
import com.library.books.domain.model.Work;
import com.library.kernel.jpa.CrudRepository;
import com.library.kernel.jpa.FindableByCode;

public interface LanguageJpaRepository<T, ID> extends CrudRepository<T, ID>, FindableByCode<T> {
    java.util.Map<Long, String> findNamesByIds(java.util.List<Long> ids);
    java.util.Optional<Language> findDetailById(Long id);
    java.util.List<Work> findRelatedWorks(Long languageId);
    void softDeleteEditionsByLanguageId(Long languageId);
    void reactivateById(Long id);
    void reactivateEditionsByLanguageId(Long languageId);
    List<T> findAll(String status);

}
