package com.swiggy.order.kafka;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;
import java.time.LocalDateTime;
import java.util.Map;

@Slf4j
@Component
@RequiredArgsConstructor
public class OrderEventProducer {

    private final KafkaTemplate<String, Object> kafkaTemplate;

    private static final String ORDER_TOPIC = "order-events";

    public void publishOrderPlaced(Long orderId, Long customerId,
                                   Long restaurantId, Double totalAmount) {
        Map<String, Object> event = Map.of(
                "eventType", "ORDER_PLACED",
                "orderId", orderId,
                "customerId", customerId,
                "restaurantId", restaurantId,
                "totalAmount", totalAmount,
                "timestamp", LocalDateTime.now().toString()
        );
        log.info("Publishing ORDER_PLACED event for orderId: {}", orderId);
        kafkaTemplate.send(ORDER_TOPIC,
                String.valueOf(orderId), event);
        log.info("ORDER_PLACED event published successfully");
    }

    public void publishOrderStatusChanged(Long orderId,
                                          String oldStatus,
                                          String newStatus) {
        Map<String, Object> event = Map.of(
                "eventType", "ORDER_STATUS_CHANGED",
                "orderId", orderId,
                "oldStatus", oldStatus,
                "newStatus", newStatus,
                "timestamp", LocalDateTime.now().toString()
        );
        log.info("Publishing ORDER_STATUS_CHANGED event - " +
                "orderId: {}, {} -> {}", orderId, oldStatus, newStatus);
        kafkaTemplate.send(ORDER_TOPIC,
                String.valueOf(orderId), event);
    }

    public void publishOrderCancelled(Long orderId, Long customerId,
                                      String reason) {
        Map<String, Object> event = Map.of(
                "eventType", "ORDER_CANCELLED",
                "orderId", orderId,
                "customerId", customerId,
                "reason", reason,
                "timestamp", LocalDateTime.now().toString()
        );
        log.info("Publishing ORDER_CANCELLED event for orderId: {}",
                orderId);
        kafkaTemplate.send(ORDER_TOPIC,
                String.valueOf(orderId), event);
    }
}