package com.swiggy.notification.service;

import com.swiggy.notification.dto.OrderEvent;
import com.swiggy.notification.dto.RestaurantEvent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class NotificationService {

    public void handleOrderPlaced(OrderEvent event) {
        log.info("=== NOTIFICATION ===");
        log.info("SMS to customer {}: Your order #{} has been placed successfully! Total: ₹{}",
                event.getCustomerId(),
                event.getOrderId(),
                event.getTotalAmount());
        log.info("SMS to restaurant {}: New order #{} received! Amount: ₹{}",
                event.getRestaurantId(),
                event.getOrderId(),
                event.getTotalAmount());
        log.info("===================");
    }

    public void handleOrderStatusChanged(OrderEvent event) {
        log.info("=== NOTIFICATION ===");
        String message = getStatusMessage(event);
        log.info("SMS to customer {}: Order #{} update - {}",
                event.getCustomerId(),
                event.getOrderId(),
                message);
        log.info("===================");
    }

    public void handleOrderCancelled(OrderEvent event) {
        log.info("=== NOTIFICATION ===");
        log.info("SMS to customer {}: Order #{} has been cancelled. Reason: {}",
                event.getCustomerId(),
                event.getOrderId(),
                event.getReason());
        log.info("===================");
    }

    public void handleRestaurantRegistered(RestaurantEvent event) {
        log.info("=== NOTIFICATION ===");
        log.info("EMAIL to admin: New restaurant registered - {}",
                event.getName());
        log.info("===================");
    }

    private String getStatusMessage(OrderEvent event) {
        if (event.getNewStatus() == null) return "Status updated";
        return switch (event.getNewStatus()) {
            case "ACCEPTED" ->
                    "Your order has been accepted by the restaurant!";
            case "PREPARING" ->
                    "Your food is being prepared!";
            case "PICKED_UP" ->
                    "Your order has been picked up by the delivery partner!";
            case "DELIVERED" ->
                    "Your order has been delivered! Enjoy your meal!";
            case "CANCELLED" ->
                    "Your order has been cancelled.";
            default -> "Order status updated to " + event.getNewStatus();
        };
    }
}