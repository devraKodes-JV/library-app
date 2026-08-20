package com.library.books.application.service.bookFormat;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import com.library.books.domain.model.BookFormat;
import com.library.books.domain.port.out.BookFormatRepository;

class FakeBookFormatRepository implements BookFormatRepository {

    private final Map<Long, BookFormat> store = new LinkedHashMap<>();
    private final Map<String, BookFormat> byCode = new LinkedHashMap<>();
    private long nextId = 1L;

    @Override
    public Optional<BookFormat> findById(Long id) {
        return Optional.ofNullable(store.get(id));
    }

    @Override
    public List<BookFormat> findAll() {
        return new ArrayList<>(store.values());
    }

    @Override
    public Optional<BookFormat> findByCode(String code) {
        return Optional.ofNullable(byCode.get(code));
    }

    @Override
    public BookFormat save(BookFormat format) {
        if (format.getId() == null) {
            BookFormat withId = new BookFormat(nextId++, format.getCode(), format.getName(),
                    format.getDescription(), format.getCreatedAt(), format.getUpdatedAt());
            store.put(withId.getId(), withId);
            byCode.put(withId.getCode(), withId);
            return withId;
        }
        store.put(format.getId(), format);
        byCode.put(format.getCode(), format);
        return format;
    }

    @Override
    public void deleteById(Long id) {
        BookFormat removed = store.remove(id);
        if (removed != null) {
            byCode.remove(removed.getCode());
        }
    }

    @Override
    public Map<Long, String> findNamesByIds(List<Long> ids) {
        Map<Long, String> result = new LinkedHashMap<>();
        for (Long id : ids) {
            BookFormat fmt = store.get(id);
            if (fmt != null) {
                result.put(id, fmt.getName());
            }
        }
        return result;
    }
}
