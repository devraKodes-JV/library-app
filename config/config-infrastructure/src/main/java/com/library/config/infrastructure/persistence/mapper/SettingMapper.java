package com.library.config.infrastructure.persistence.mapper;

import com.library.config.domain.model.Setting;
import com.library.config.infrastructure.persistence.entity.SettingEntity;

import java.time.LocalDateTime;

public class SettingMapper {

    public static Setting toDomain(SettingEntity entity) {
        return new Setting(
                entity.getId(),
                entity.getModule(),
                entity.getKey(),
                entity.getValue(),
                entity.getType(),
                entity.getDescription(),
                entity.getUpdatedAt(),
                entity.getUpdatedBy()
        );
    }

    public static SettingEntity toEntity(Setting setting) {
        SettingEntity entity = new SettingEntity();
        entity.setId(setting.getId());
        entity.setModule(setting.getModule());
        entity.setKey(setting.getKey());
        entity.setValue(setting.getValue());
        entity.setType(setting.getType());
        entity.setDescription(setting.getDescription());
        entity.setUpdatedAt(setting.getUpdatedAt());
        entity.setUpdatedBy(setting.getUpdatedBy());
        return entity;
    }
}
