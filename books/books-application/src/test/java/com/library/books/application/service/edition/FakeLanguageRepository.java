package com.library.books.application.service.edition;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import com.library.books.domain.model.Language;
import com.library.books.domain.port.out.LanguageRepository;

class FakeLanguageRepository implements LanguageRepository {

    private final Map<Long, Language> store = new LinkedHashMap<>();
    private final Map<String, Language> byCode = new LinkedHashMap<>();
    private long nextId = 1L;

    @Override
    public Optional<Language> findById(Long id) {
        return Optional.ofNullable(store.get(id));
    }

    @Override
    public List<Language> findAll() {
        return new ArrayList<>(store.values());
    }

    @Override
    public Optional<Language> findByCode(String code) {
        return Optional.ofNullable(byCode.get(code));
    }

    @Override
    public Language save(Language language) {
        if (language.getId() == null) {
            Language withId = new Language(nextId++, language.getCode(), language.getName(),
                    language.getCreatedAt(), language.getUpdatedAt());
            store.put(withId.getId(), withId);
            byCode.put(withId.getCode(), withId);
            return withId;
        }
        store.put(language.getId(), language);
        byCode.put(language.getCode(), language);
        return language;
    }

    @Override
    public void deleteById(Long id) {
        Language removed = store.remove(id);
        if (removed != null) {
            byCode.remove(removed.getCode());
        }
    }

    @Override
    public Map<Long, String> findNamesByIds(List<Long> ids) {
        Map<Long, String> result = new LinkedHashMap<>();
        for (Long id : ids) {
            Language lang = store.get(id);
            if (lang != null) {
                result.put(id, lang.getName());
            }
        }
        return result;
    }
}
