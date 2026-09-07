package com.library.reservation.infrastructure.notification;

import java.time.Instant;

import com.library.iam.domain.model.NotificationEvent;
import com.library.iam.domain.port.out.NotificationService;

public class ReservationNotificationService {

    private final NotificationService notificationService;

    public ReservationNotificationService(NotificationService notificationService) {
        this.notificationService = notificationService;
    }

    public void notifyReservationCreated(String clientName, String editionName, String actorName) {
        String message = String.format("New reservation: %s reserved '%s'", clientName, editionName);
        NotificationEvent event = new NotificationEvent(
                "reservation.created",
                message,
                null,
                actorName,
                null,
                Instant.now());
        notificationService.publish(event);
    }

    public void notifyReservationFulfilled(String clientName, String editionName, String actorName) {
        String message = String.format("Reservation fulfilled: %s picked up '%s'", clientName, editionName);
        NotificationEvent event = new NotificationEvent(
                "reservation.fulfilled",
                message,
                null,
                actorName,
                null,
                Instant.now());
        notificationService.publish(event);
    }

    public void notifyReservationReturned(String clientName, String editionName, String actorName) {
        String message = String.format("Reservation returned: %s returned '%s'", clientName, editionName);
        NotificationEvent event = new NotificationEvent(
                "reservation.returned",
                message,
                null,
                actorName,
                null,
                Instant.now());
        notificationService.publish(event);
    }

    public void notifyReservationCancelled(String clientName, String editionName, String actorName) {
        String message = String.format("Reservation cancelled: %s for '%s'", clientName, editionName);
        NotificationEvent event = new NotificationEvent(
                "reservation.cancelled",
                message,
                null,
                actorName,
                null,
                Instant.now());
        notificationService.publish(event);
    }

    public void notifyReservationOverdue(String clientName, String editionName, String actorName) {
        String message = String.format("Reservation overdue: %s has '%s' past due date", clientName, editionName);
        NotificationEvent event = new NotificationEvent(
                "reservation.overdue",
                message,
                null,
                actorName,
                null,
                Instant.now());
        notificationService.publish(event);
    }
}
