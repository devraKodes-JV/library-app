package com.library.config.domain.repository;

import com.library.config.domain.model.Setting;

import java.util.List;
import java.util.Optional;

public interface SettingRepository {
    Setting save(Setting setting);
    Optional<Setting> findById(Long id);
    List<Setting> findAll();
    List<Setting> findByModule(String module);
    Optional<Setting> findByModuleAndKey(String module, String key);
    void deleteById(Long id);
}
