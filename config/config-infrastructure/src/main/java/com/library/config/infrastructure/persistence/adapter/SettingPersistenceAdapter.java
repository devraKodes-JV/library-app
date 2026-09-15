package com.library.config.infrastructure.persistence.adapter;

import com.library.config.domain.model.Setting;
import com.library.config.domain.repository.SettingRepository;
import com.library.config.infrastructure.persistence.repository.hibernate.HibernateSettingRepository;

import java.util.List;
import java.util.Optional;

public class SettingPersistenceAdapter implements SettingRepository {

    private final HibernateSettingRepository hibernateSettingRepository;

    public SettingPersistenceAdapter(HibernateSettingRepository hibernateSettingRepository) {
        this.hibernateSettingRepository = hibernateSettingRepository;
    }

    @Override
    public Setting save(Setting setting) {
        return hibernateSettingRepository.save(setting);
    }

    @Override
    public Optional<Setting> findById(Long id) {
        return hibernateSettingRepository.findById(id);
    }

    @Override
    public List<Setting> findAll() {
        return hibernateSettingRepository.findAll();
    }

    @Override
    public List<Setting> findByModule(String module) {
        return hibernateSettingRepository.findByModule(module);
    }

    @Override
    public Optional<Setting> findByModuleAndKey(String module, String key) {
        return hibernateSettingRepository.findByModuleAndKey(module, key);
    }

    @Override
    public void deleteById(Long id) {
        hibernateSettingRepository.deleteById(id);
    }
}
