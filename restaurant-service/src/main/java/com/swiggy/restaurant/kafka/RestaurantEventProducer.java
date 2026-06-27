package com.swiggy.restaurant.kafka;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;
import java.util.Map;

@Slf4j
@Component
@RequiredArgsConstructor
public class RestaurantEventProducer {

    private final KafkaTemplate<String, Object> kafkaTemplate;

    private static final String TOPIC = "restaurant-events";

    public void sendRestaurantRegistered(Long restaurantId, String name) {
        Map<String, Object> event = Map.of(
                "eventType", "RESTAURANT_REGISTERED",
                "restaurantId", restaurantId,
                "name", name
        );
        log.info("Publishing RESTAURANT_REGISTERED event for id: {}",
                restaurantId);
        kafkaTemplate.send(TOPIC, String.valueOf(restaurantId), event);
    }

    public void sendMenuUpdated(Long restaurantId) {
        Map<String, Object> event = Map.of(
                "eventType", "MENU_UPDATED",
                "restaurantId", restaurantId
        );
        log.info("Publishing MENU_UPDATED event for restaurantId: {}",
                restaurantId);
        kafkaTemplate.send(TOPIC, String.valueOf(restaurantId), event);
    }
}