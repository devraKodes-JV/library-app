package com.library.stock.infrastructure.notification;

import com.library.iam.infrastructure.notification.SseNotificationService;
import com.library.stock.domain.model.NotificationEvent;
import com.library.stock.domain.port.out.StockNotificationService;

public class StockNotificationServiceImpl implements StockNotificationService {

    private final SseNotificationService sseNotificationService;

    public StockNotificationServiceImpl(SseNotificationService sseNotificationService) {
        this.sseNotificationService = sseNotificationService;
    }

    @Override
    public void publish(NotificationEvent event) {
        sseNotificationService.publish(new com.library.iam.domain.model.NotificationEvent(
                event.type(),
                event.message(),
                event.actorId(),
                event.actorName(),
                null,
                event.at()));
    }
}
