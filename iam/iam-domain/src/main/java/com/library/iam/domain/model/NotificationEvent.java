package com.library.iam.domain.model;

import java.time.Instant;

/**
 * A domain event describing a change that should be notified to users (e.g.
 * administrators) in real time.
 *
 * <p>It is a pure domain value object (Java record) with no framework
 * dependency. The application layer creates these events and publishes them
 * through the {@code NotificationService} port; the infrastructure layer
 * transports them (e.g. via Server-Sent Events) to connected clients.</p>
 *
 * @param type     the event type, e.g. "role.created", "role.updated",
 *                 "role.deleted" (used for filtering/customisation)
 * @param message  a human-readable description of what happened
 * @param actorId  the id of the user who performed the change (may be null)
 * @param actorName the username of the user who performed the change
 * @param at       the instant when the change occurred
 */
public record NotificationEvent(
        String type,
        String message,
        Long actorId,
        String actorName,
        Instant at) {

    /**
     * Convenience factory that timestamps the event with the current instant.
     *
     * @param type      the event type
     * @param message   the human-readable message
     * @param actorId   the acting user id
     * @param actorName the acting username
     * @return a new event at {@code Instant.now()}
     */
    public static NotificationEvent of(String type, String message,
                                       Long actorId, String actorName) {
        return new NotificationEvent(type, message, actorId, actorName, Instant.now());
    }
}
