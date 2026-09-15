package com.library.config.application.service;

import com.library.config.domain.model.Setting;
import com.library.config.domain.repository.SettingRepository;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class ConfigService {

    private final SettingRepository settingRepository;
    private final Map<String, String> cache = new java.util.concurrent.ConcurrentHashMap<>();
    private final Map<String, String> typeCache = new java.util.concurrent.ConcurrentHashMap<>();

    public ConfigService(SettingRepository settingRepository) {
        this.settingRepository = settingRepository;
        reload();
    }

    public void reload() {
        cache.clear();
        typeCache.clear();
        List<Setting> all = settingRepository.findAll();
        for (Setting s : all) {
            cache.put(key(s.getModule(), s.getKey()), s.getValue());
            typeCache.put(key(s.getModule(), s.getKey()), s.getType());
        }
    }

    public String get(String module, String key, String defaultValue) {
        String v = cache.get(key(module, key));
        return v != null ? v : defaultValue;
    }

    public int getInt(String module, String key, int defaultValue) {
        String v = get(module, key, String.valueOf(defaultValue));
        try { return Integer.parseInt(v); } catch (Exception e) { return defaultValue; }
    }

    public BigDecimal getDecimal(String module, String key, BigDecimal defaultValue) {
        String v = get(module, key, defaultValue != null ? defaultValue.toPlainString() : "0");
        try { return new BigDecimal(v); } catch (Exception e) { return defaultValue != null ? defaultValue : BigDecimal.ZERO; }
    }

    public boolean getBoolean(String module, String key, boolean defaultValue) {
        String v = get(module, key, String.valueOf(defaultValue));
        return Boolean.parseBoolean(v);
    }

    public Map<String, String> getModuleSettings(String module) {
        Map<String, String> result = new LinkedHashMap<>();
        for (Map.Entry<String, String> e : cache.entrySet()) {
            if (e.getKey().startsWith(module + ".")) {
                result.put(e.getKey(), e.getValue());
            }
        }
        return result;
    }

    public void update(String module, String key, String value, String updatedBy) {
        Setting setting = settingRepository.findByModuleAndKey(module, key)
                .orElseGet(() -> {
                    Setting s = new Setting(null, module, key, value, "STRING", null, LocalDateTime.now(), updatedBy);
                    return settingRepository.save(s);
                });
        setting.setValue(value);
        setting.setUpdatedAt(LocalDateTime.now());
        setting.setUpdatedBy(updatedBy);
        settingRepository.save(setting);
        cache.put(key(module, key), value);
        typeCache.put(key(module, key), setting.getType());
    }

    public String getType(String module, String key) {
        String fullKey = key(module, key);
        if (typeCache.containsKey(fullKey)) {
            return typeCache.get(fullKey);
        }
        return typeCache.getOrDefault(key, "STRING");
    }

    private static String key(String module, String key) {
        return module + "." + key;
    }
}
