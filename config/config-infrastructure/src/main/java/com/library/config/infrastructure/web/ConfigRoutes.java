package com.library.config.infrastructure.web;

import com.library.config.infrastructure.web.controller.ConfigController;

import io.javalin.config.JavalinConfig;

public class ConfigRoutes {

    public static void register(JavalinConfig config, ConfigController configController) {
        config.routes.get("/config", configController::showSettings);
        config.routes.post("/config/settings", configController::updateSetting);
    }
}
