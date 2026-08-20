package com.library.kernel.web;

import java.util.List;
import java.util.Map;

import io.javalin.http.Context;

public abstract class BaseController {

    protected final WebControllerContext webContext;

    protected BaseController(WebControllerContext webContext) {
        this.webContext = webContext;
    }

    protected Object currentUser(Context ctx) {
        return webContext.currentUser(ctx);
    }

    @SuppressWarnings("unchecked")
    protected List<Object> navSections(Context ctx) {
        return (List<Object>) webContext.navSections(ctx);
    }

    protected boolean hasPermission(Context ctx, String permCode) {
        return webContext.hasPermission(ctx, permCode);
    }

    protected void requireCan(Context ctx, String permCode) {
        if (!webContext.hasPermission(ctx, permCode)) {
            throw new io.javalin.http.ForbiddenResponse();
        }
    }

    protected void flashSuccess(Context ctx, String message) {
        WebHelper.flashSuccess(ctx, message);
    }

    protected void flashWarning(Context ctx, String message) {
        WebHelper.flashWarning(ctx, message);
    }

    protected Map<String, Object> baseModel(Context ctx, Map<String, Object> extra) {
        var user = currentUser(ctx);
        List<?> sections = navSections(ctx);
        Map<String, Object> model = new java.util.LinkedHashMap<>();
        model.put("user", user);
        model.put("navSections", sections);
        if (extra != null) {
            model.putAll(extra);
        }
        return model;
    }
}
