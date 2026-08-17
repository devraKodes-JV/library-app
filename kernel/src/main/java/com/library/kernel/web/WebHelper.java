package com.library.kernel.web;

import java.util.HashMap;
import java.util.Map;

import jakarta.servlet.http.HttpSession;

import io.javalin.http.Context;

/**
 * Helper methods for web adapters: flash messages, CSRF token extraction,
 * and common redirect patterns.
 */
public final class WebHelper {

    private WebHelper() {
    }

    public static final String FLASH_SESSION_KEY = "flashMessages";

    /**
     * Adds a flash message to the HTTP session. The message is a map with keys:
     * {@code type} (success, danger, warning, info), {@code title}, and
     * {@code message}.
     *
     * @param ctx    the Javalin HTTP context
     * @param type   the alert type
     * @param title  the optional title
     * @param message the message text
     */
    public static void flash(Context ctx, String type, String title, String message) {
        @SuppressWarnings("unchecked")
        java.util.List<Map<String, Object>> messages = (java.util.List<Map<String, Object>>) ctx.sessionAttribute(FLASH_SESSION_KEY);
        if (messages == null) {
            messages = new java.util.ArrayList<>();
            ctx.sessionAttribute(FLASH_SESSION_KEY, messages);
        }
        Map<String, Object> msg = new HashMap<>();
        msg.put("type", type);
        if (title != null && !title.isBlank()) {
            msg.put("title", title);
        }
        msg.put("message", message);
        messages.add(msg);
    }

    public static void flashSuccess(Context ctx, String message) {
        flash(ctx, "success", "Success", message);
    }

    public static void flashDanger(Context ctx, String message) {
        flash(ctx, "danger", "Error", message);
    }

    public static void flashWarning(Context ctx, String message) {
        flash(ctx, "warning", "Warning", message);
    }

    public static void flashInfo(Context ctx, String message) {
        flash(ctx, "info", null, message);
    }

    /**
     * Extracts the CSRF token from the current request (cookie or form param).
     *
     * @param ctx the Javalin HTTP context
     * @return the CSRF token, or empty string if absent
     */
    public static String csrfToken(Context ctx) {
        String token = ctx.formParam("_csrf");
        if (token == null) {
            token = ctx.cookie("__Host-XSRF-TOKEN");
        }
        if (token == null) {
            token = ctx.cookie("XSRF-TOKEN");
        }
        return token != null ? token : "";
    }
}
