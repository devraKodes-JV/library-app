package com.library.books.domain.port.out;

import java.util.List;
import java.util.Optional;

import com.library.books.domain.model.BookFormat;

public interface BookFormatRepository {
    Optional<BookFormat> findById(Long id);
    List<BookFormat> findAll();
    List<BookFormat> findAll(String status);
    Optional<BookFormat> findByCode(String code);
    BookFormat save(BookFormat format);
    void deleteById(Long id);
    java.util.Map<Long, String> findNamesByIds(java.util.List<Long> ids);
    void softDeleteEditionsByFormatId(Long formatId);
    void reactivateById(Long id);
    void reactivateEditionsByFormatId(Long formatId);
}
