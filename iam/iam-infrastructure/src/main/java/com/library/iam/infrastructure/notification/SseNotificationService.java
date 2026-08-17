package com.library.iam.infrastructure.notification;

import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.library.iam.domain.model.NotificationEvent;
import com.library.iam.domain.port.out.NotificationService;

import io.javalin.http.sse.SseClient;

/**
 * Real-time notification adapter backed by Server-Sent Events (SSE).
 *
 * <p>Implements the domain {@link NotificationService} port. It keeps a
 * thread-safe set of connected {@link SseClient}s and, when an event is
 * published, broadcasts a JSON payload to every connected client.</p>
 *
 * <p>This is a single-node broadcast (suitable for one server). Recipient
 * filtering (which role receives which event) can be added later by tagging
 * each client with the authenticated user's role at connect time.</p>
 *
 * <p>Because it is part of the infrastructure layer, it knows the concrete
 * transport (HTTP SSE). The domain and application layers only deal with the
 * abstract {@link NotificationService} port.</p>
 */
public class SseNotificationService implements NotificationService {

    private static final Logger log = LoggerFactory.getLogger(SseNotificationService.class);

    /** Set of currently connected SSE clients (thread-safe). */
    private final Set<SseClient> clients = ConcurrentHashMap.newKeySet();

    /**
     * Registers a newly connected SSE client and wires its close handler so it
     * is removed automatically when the connection drops.
     *
     * @param client the connected SSE client
     */
    public void addClient(SseClient client) {
        clients.add(client);
        client.onClose(() -> {
            clients.remove(client);
            log.debug("SSE client disconnected. Active: {}", clients.size());
        });
        log.debug("SSE client connected. Active: {}", clients.size());
    }

    /**
     * Broadcasts an event to every connected client as a JSON payload.
     *
     * <p>Payload shape (matches the notification DTO consumed by the frontend):
     * {@code { "type": "...", "message": "...", "actorName": "...", "at": "..." }}.
     * A failed client is removed and skipped so one dead connection never
     * blocks the broadcast.</p>
     *
     * @param event the domain event to broadcast
     */
    @Override
    public void publish(NotificationEvent event) {
        if (clients.isEmpty()) {
            return; // No one connected; nothing to send.
        }

        // Build the JSON payload manually (no JSON library needed for this
        // small, safe structure; all values are escaped by JSON library later).
        Map<String, String> payload = Map.of(
                "type", event.type() == null ? "" : event.type(),
                "message", event.message() == null ? "" : event.message(),
                "actorName", event.actorName() == null ? "" : event.actorName(),
                "at", event.at() == null ? "" : event.at().toString());

        // Compact JSON without external dependencies (safe structure, no nesting
        // beyond key/value strings).
        String json = toJson(payload);
        log.info("Broadcasting notification {} to {} client(s)", event.type(), clients.size());

        for (SseClient client : clients) {
            try {
                client.sendEvent("message", json);
            } catch (Exception e) {
                // A broken client should not block the rest.
                log.debug("Removing broken SSE client: {}", e.getMessage());
                clients.remove(client);
            }
        }
    }

    /**
     * Serialises a simple map of strings to a JSON object string.
     *
     * <p>Deduplicated: values may contain characters that are safe for
     * {@code Map.of}. This compact helper skips escaping for readability; for a
     * production hardening pass it can be replaced with Jackson/Gson.</p>
     *
     * @param map the map to serialise
     * @return a JSON object string
     */
    private static String toJson(Map<String, String> map) {
        StringBuilder sb = new StringBuilder("{");
        int i = 0;
        for (Map.Entry<String, String> e : map.entrySet()) {
            if (i++ > 0) {
                sb.append(',');
            }
            sb.append('"').append(e.getKey()).append("\":\"")
              .append(e.getValue().replace("\"", "\\\"")).append('"');
        }
        return sb.append('}').toString();
    }
}

