package com.library.config.infrastructure.web.controller;

import com.library.config.application.service.ConfigService;
import com.library.kernel.web.BaseController;
import com.library.kernel.web.WebControllerContext;

import io.javalin.http.Context;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class ConfigController extends BaseController {

    private final ConfigService configService;

    public ConfigController(ConfigService configService, WebControllerContext webContext) {
        super(webContext);
        this.configService = configService;
    }

    public void showSettings(Context ctx) {
        requireCan(ctx, "config.read");
        List<String> modules = List.of("global", "client", "reservation", "accounting");
        Map<String, List<String[]>> settingsByModule = new LinkedHashMap<>();
        for (String module : modules) {
            List<String[]> entries = new java.util.ArrayList<>();
            for (Map.Entry<String, String> e : configService.getModuleSettings(module).entrySet()) {
                entries.add(new String[]{e.getKey(), e.getValue(), configService.getType(module, e.getKey())});
            }
            settingsByModule.put(module, entries);
        }
        ctx.render("config/settings", buildListModel(ctx, Map.of(
                "settingsByModule", settingsByModule,
                "modules", modules
        )));
    }

    public void updateSetting(Context ctx) {
        requireCan(ctx, "config.update");
        String module = ctx.formParam("module");
        String key = ctx.formParam("key");
        String value = ctx.formParam("value");
        if (module == null || key == null || value == null) {
            ctx.status(400).result("Missing parameters");
            return;
        }
        if (key.startsWith(module + ".")) {
            key = key.substring(module.length() + 1);
        }
        if ("accounting.currency".equals(key) && "OTHER".equals(value)) {
            String custom = ctx.formParam("customValue");
            if (custom != null && !custom.isBlank()) {
                value = custom.trim().toUpperCase();
            }
        }
        configService.update(module, key, value, currentUserName(ctx));
        ctx.redirect("/config");
    }

    private Map<String, Object> buildListModel(Context ctx, Map<String, Object> extra) {
        var current = currentUser(ctx);
        List<?> navSections = navSections(ctx);
        Map<String, Object> model = new java.util.LinkedHashMap<>();
        model.put("user", current);
        model.put("navSections", navSections);
        model.put("canUpdate", hasPermission(ctx, "config.update"));
        model.putAll(extra);
        return model;
    }
}
