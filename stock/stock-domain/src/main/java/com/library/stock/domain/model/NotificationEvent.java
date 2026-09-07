package com.library.stock.domain.model;

import java.time.Instant;

public record NotificationEvent(
        String type,
        String message,
        Long actorId,
        String actorName,
        Instant at) {

    public static NotificationEvent of(String type, String message, Long actorId, String actorName) {
        return new NotificationEvent(type, message, actorId, actorName, Instant.now());
    }
}
