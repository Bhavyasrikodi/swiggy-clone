package com.swiggy.notification.kafka;

import com.swiggy.notification.dto.RestaurantEvent;
import com.swiggy.notification.service.NotificationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class RestaurantEventConsumer {

    private final NotificationService notificationService;

    @KafkaListener(
            topics = "restaurant-events",
            groupId = "notification-service-group"
    )
    public void consumeRestaurantEvent(RestaurantEvent event) {
        log.info("Received restaurant event: type={}, restaurantId={}",
                event.getEventType(), event.getRestaurantId());

        if ("RESTAURANT_REGISTERED".equals(event.getEventType())) {
            notificationService.handleRestaurantRegistered(event);
        }
    }
}