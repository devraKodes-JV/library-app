package com.library.iam.domain.port.out;

import com.library.iam.domain.model.NotificationEvent;

/**
 * Output (driven) port for pushing real-time notifications to connected users.
 *
 * <p>In hexagonal architecture this is a contract that the application layer
 * uses to publish events. The concrete transport (Server-Sent Events, WebSocket,
 * e-mail, etc.) is implemented by an adapter in the infrastructure layer. The
 * application layer only declares <em>what</em> needs to be notified, never
 * <em>how</em> it is delivered.</p>
 *
 * <p>This keeps the use cases decoupled from the notification technology, so
 * the rule "which role receives which notification" can later be customised
 * without touching the domain or application layers.</p>
 */
public interface NotificationService {

    /**
     * Publishes a notification event to the relevant recipients.
     *
     * @param event the event describing the change and its metadata
     */
    void publish(NotificationEvent event);
}
