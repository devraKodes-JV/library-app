package com.library.books.infrastructure.persistence.repository.jpa;

import com.library.books.domain.model.BookFormat;
import com.library.books.domain.model.Edition;
import com.library.kernel.jpa.CrudRepository;
import com.library.kernel.jpa.FindableByCode;

public interface BookFormatJpaRepository<T, ID> extends CrudRepository<T, ID>, FindableByCode<T> {
    java.util.Map<Long, String> findNamesByIds(java.util.List<Long> ids);
    java.util.Optional<BookFormat> findDetailById(Long id);
    java.util.List<Edition> findEditionsByFormatId(Long formatId);
}
