package com.library.bootstrap.config;

import java.io.IOException;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import io.javalin.http.Context;
import io.javalin.rendering.FileRenderer;
import io.pebbletemplates.pebble.PebbleEngine;
import io.pebbletemplates.pebble.loader.ClasspathLoader;
import io.pebbletemplates.pebble.template.PebbleTemplate;

/**
 * Javalin {@link FileRenderer} implementation backed by the Pebble template
 * engine.
 *
 * <p>Automatically injects into every model:
 * <ul>
 *   <li>{@code csrfToken}: the current CSRF token from the {@code XSRF-TOKEN} cookie</li>
 *   <li>{@code flashMessages}: a list of flash messages stored in the HTTP session</li>
 * </ul>
 * Flash messages are consumed from the session on each render so they appear
 * exactly once.
 */
public final class PebbleFileRenderer implements FileRenderer {

    /** Pebble engine, thread-safe and cached. Loads templates from the classpath. */
    private final PebbleEngine engine;

    /**
     * Builds the Pebble engine configured to resolve templates from the
     * classpath {@code /templates} directory (which is packaged in the fat JAR).
     */
    public PebbleFileRenderer() {
        ClasspathLoader loader = new ClasspathLoader();
        loader.setPrefix("templates/");
        loader.setSuffix(".peb");

        this.engine = new PebbleEngine.Builder()
                .loader(loader)
                .autoEscaping(false)
                .build();
    }

    /**
     * Renders a Pebble template using the given model.
     *
     * <p>The model is extended with {@code csrfToken} and {@code flashMessages}
     * before evaluation.</p>
     *
     * @param templatePath the template name passed to {@code ctx.render(...)}
     * @param model        a map of variables exposed to the template
     * @param ctx          the Javalin HTTP context
     * @return the rendered HTML string
     */
    @Override
    public String render(String templatePath, Map<String, ? extends Object> model, Context ctx) {
        try {
            PebbleTemplate template = engine.getTemplate(templatePath);
            StringWriter writer = new StringWriter();
            Map<String, Object> modelCasted = new java.util.HashMap<>(model);

            injectCsrfToken(ctx, modelCasted);
            injectFlashMessages(ctx, modelCasted);
            injectSecurityNonce(ctx, modelCasted);

            template.evaluate(writer, modelCasted);
            ctx.contentType("text/html; charset=UTF-8");
            return writer.toString();
        } catch (IOException e) {
            throw new IllegalStateException("Could not render template: " + templatePath, e);
        }
    }

    /**
     * Reads the CSRF token from the request context attribute (set by the
     * CsrfProtectionFilter before-handler). Falls back to the cookie value.
     * If neither is present, an empty string is used.
     */
    private void injectCsrfToken(Context ctx, Map<String, Object> model) {
        String token = ctx.attribute("com.library.security.csrfToken");
        if (token == null) {
            token = readCookieFromRequest(ctx.req(), "__Host-XSRF-TOKEN");
        }
        if (token == null) {
            token = readCookieFromRequest(ctx.req(), "XSRF-TOKEN");
        }
        if (token == null) {
            token = "";
        }
        model.put("csrfToken", token);
    }

    private void injectSecurityNonce(Context ctx, Map<String, Object> model) {
        String nonce = ctx.attribute("securityNonce");
        if (nonce != null) {
            model.put("securityNonce", nonce);
        }
    }

    private static String readCookieFromRequest(jakarta.servlet.http.HttpServletRequest req, String name) {
        if (req.getCookies() != null) {
            for (jakarta.servlet.http.Cookie cookie : req.getCookies()) {
                if (name.equals(cookie.getName())) {
                    return cookie.getValue();
                }
            }
        }
        return null;
    }

    /**
     * Reads {@code flashMessages} from the HTTP session, adds them to the model,
     * and clears them from the session so they are shown only once.
     */
    @SuppressWarnings("unchecked")
    private void injectFlashMessages(Context ctx, Map<String, Object> model) {
        Object raw = ctx.sessionAttribute("flashMessages");
        System.out.println("FLASH: raw=" + raw + " class=" + (raw != null ? raw.getClass().getName() : "null"));
        if (raw instanceof List<?> list) {
            List<Map<String, Object>> messages = new ArrayList<>();
            for (Object item : list) {
                if (item instanceof Map<?, ?> map) {
                    Map<String, Object> safe = new java.util.HashMap<>();
                    for (Map.Entry<?, ?> entry : map.entrySet()) {
                        if (entry.getKey() instanceof String key && entry.getValue() != null) {
                            safe.put(key, entry.getValue());
                        }
                    }
                    if (!safe.isEmpty()) {
                        messages.add(safe);
                    }
                }
            }
            model.put("flashMessages", messages);
        } else {
            model.put("flashMessages", List.of());
        }

        ctx.sessionAttribute("flashMessages", null);
    }
}
