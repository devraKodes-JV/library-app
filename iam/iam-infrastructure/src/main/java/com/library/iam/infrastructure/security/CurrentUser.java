package com.library.iam.infrastructure.security;


/**
 * Scoped-value holder for the currently authenticated user's username.
 *
 * <p>The {@code SessionAuthFilter} binds this for every authenticated request.
 * The {@code AuditableEntity} callbacks ({@code @PrePersist}/{@code @PreUpdate})
 * read it to populate the {@code created_by} / {@code updated_by} columns.</p>
 *
 * <p>ScopedValue is used instead of ThreadLocal so the username is confined to
 * the request scope and does not leak across asynchronous boundaries.</p>
 */
public final class CurrentUser {

    static final ScopedValue<String> USERNAME = ScopedValue.newInstance();

    private CurrentUser() {
    }

    public static String get() {
        return USERNAME.orElse(null);
    }

    public static String getOrDefault(String defaultValue) {
        return USERNAME.orElse(defaultValue);
    }

    public static boolean isBound() {
        return USERNAME.orElse(null) != null;
    }
}
