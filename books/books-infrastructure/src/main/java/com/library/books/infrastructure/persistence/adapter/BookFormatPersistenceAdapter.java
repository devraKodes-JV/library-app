package com.library.books.infrastructure.persistence.adapter;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import com.library.books.domain.model.BookFormat;
import com.library.books.domain.port.out.BookFormatRepository;
import com.library.books.infrastructure.persistence.entity.BookFormatEntity;
import com.library.books.infrastructure.persistence.mapper.BookFormatMapper;
import com.library.books.infrastructure.persistence.repository.jpa.BookFormatJpaRepository;

public class BookFormatPersistenceAdapter implements BookFormatRepository {

    private final BookFormatJpaRepository<BookFormatEntity, Long> bookFormatJpaRepository;

    public BookFormatPersistenceAdapter(BookFormatJpaRepository<BookFormatEntity, Long> bookFormatJpaRepository) {
        this.bookFormatJpaRepository = bookFormatJpaRepository;
    }

    @Override
    public Optional<BookFormat> findById(Long id) {
        return bookFormatJpaRepository.findById(id)
                .map(BookFormatMapper::toDomain);
    }

    @Override
    public List<BookFormat> findAll() {
        return bookFormatJpaRepository.findAll().stream()
                .map(BookFormatMapper::toDomain)
                .toList();
    }

    @Override
    public Optional<BookFormat> findByCode(String code) {
        return bookFormatJpaRepository.findByCode(code)
                .map(BookFormatMapper::toDomain);
    }

    @Override
    public BookFormat save(BookFormat format) {
        BookFormatEntity entity = BookFormatMapper.toEntity(format);
        BookFormatEntity saved = bookFormatJpaRepository.save(entity);
        return BookFormatMapper.toDomain(saved);
    }

    @Override
    public void deleteById(Long id) {
        bookFormatJpaRepository.deleteById(id);
    }

    @Override
    public Map<Long, String> findNamesByIds(List<Long> ids) {
        return bookFormatJpaRepository.findNamesByIds(ids);
    }
}
