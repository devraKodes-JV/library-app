package com.library.bootstrap.config.hibernate;

import com.library.config.infrastructure.persistence.entity.SettingEntity;
import org.hibernate.cfg.Configuration;

public class ConfigAnnotatedClases {

    private ConfigAnnotatedClases() {}

    public static void annotate(Configuration cfg) {
        cfg.addAnnotatedClass(SettingEntity.class);
    }
}
