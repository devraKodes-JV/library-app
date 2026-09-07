package com.library.iam.infrastructure.web.controller.notification;

import com.library.kernel.web.BaseController;
import com.library.kernel.web.WebControllerContext;

import io.javalin.http.Context;
import java.time.Instant;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class NotificationController extends BaseController {

    private static final List<Map<String, Object>> recentNotifications = new ArrayList<>();
    private static final int MAX_NOTIFICATIONS = 10;

    public NotificationController(WebControllerContext webContext) {
        super(webContext);
    }

    public static void addNotification(String type, String message) {
        addNotification(type, message, null);
    }

    public static void addNotification(String type, String message, String link) {
        synchronized (recentNotifications) {
            Map<String, Object> notif = new LinkedHashMap<>();
            notif.put("id", System.currentTimeMillis() + "-" + Math.random());
            notif.put("type", type);
            notif.put("message", message);
            notif.put("link", link != null ? link : "");
            notif.put("timestamp", Instant.now().toString());
            notif.put("read", false);
            recentNotifications.add(0, notif);
            if (recentNotifications.size() > MAX_NOTIFICATIONS) {
                recentNotifications.remove(recentNotifications.size() - 1);
            }
        }
    }

    public void getNotifications(Context ctx) {
        requireCan(ctx, "notifications.stream");
        String after = ctx.queryParam("after");
        List<Map<String, Object>> notifications;
        synchronized (recentNotifications) {
            if (after != null && !after.isBlank()) {
                notifications = recentNotifications.stream()
                    .filter(n -> n.get("timestamp").toString().compareTo(after) > 0)
                    .toList();
            } else {
                notifications = new ArrayList<>(recentNotifications);
            }
        }
        ctx.json(notifications);
    }

    public void markAsRead(Context ctx) {
        requireCan(ctx, "notifications.stream");
        String id = ctx.queryParam("id");
        synchronized (recentNotifications) {
            if (id != null && !id.isBlank()) {
                for (Map<String, Object> n : recentNotifications) {
                    if (id.equals(n.get("id"))) {
                        n.put("read", true);
                        break;
                    }
                }
            } else {
                recentNotifications.forEach(n -> n.put("read", true));
            }
        }
        ctx.status(200);
    }

    public void deleteNotification(Context ctx) {
        requireCan(ctx, "notifications.stream");
        String id = ctx.queryParam("id");
        if (id == null || id.isBlank()) {
            ctx.status(400);
            return;
        }
        synchronized (recentNotifications) {
            recentNotifications.removeIf(n -> id.equals(n.get("id")));
        }
        ctx.status(200);
    }
}
