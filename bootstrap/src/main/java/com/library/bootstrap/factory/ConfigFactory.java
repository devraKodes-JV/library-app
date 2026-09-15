package com.library.bootstrap.factory;

import org.hibernate.SessionFactory;

import com.library.config.application.service.ConfigService;
import com.library.config.domain.repository.SettingRepository;
import com.library.config.infrastructure.persistence.adapter.SettingPersistenceAdapter;
import com.library.config.infrastructure.persistence.repository.hibernate.HibernateSettingRepository;
import com.library.config.infrastructure.web.ConfigRoutes;
import com.library.config.infrastructure.web.controller.ConfigController;
import com.library.kernel.web.WebControllerContext;

import io.javalin.config.JavalinConfig;

public final class ConfigFactory {

    private ConfigFactory() {}

    public static void register(JavalinConfig config,
                                SessionFactory sessionFactory,
                                WebControllerContext webContext) {
        SettingRepository settingRepository = new SettingPersistenceAdapter(
                new HibernateSettingRepository(sessionFactory));
        ConfigService configService = new ConfigService(settingRepository);

        ConfigController configController = new ConfigController(configService, webContext);
        ConfigRoutes.register(config, configController);
    }
}
