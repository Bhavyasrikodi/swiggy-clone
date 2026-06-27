package com.swiggy.notification.kafka;

import com.swiggy.notification.dto.OrderEvent;
import com.swiggy.notification.service.NotificationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class OrderEventConsumer {

    private final NotificationService notificationService;

    @KafkaListener(
            topics = "order-events",
            groupId = "notification-service-group",
            containerFactory = "kafkaListenerContainerFactory"
    )
    public void consumeOrderEvent(OrderEvent event) {
        log.info("Received order event: type={}, orderId={}",
                event.getEventType(), event.getOrderId());

        if (event.getEventType() == null) {
            log.warn("Received event with null eventType, skipping");
            return;
        }

        switch (event.getEventType()) {
            case "ORDER_PLACED" ->
                    notificationService.handleOrderPlaced(event);
            case "ORDER_STATUS_CHANGED" ->
                    notificationService.handleOrderStatusChanged(event);
            case "ORDER_CANCELLED" ->
                    notificationService.handleOrderCancelled(event);
            default ->
                    log.warn("Unknown order event type: {}",
                            event.getEventType());
        }
    }
}