package com.swiggy.notification.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class OrderEvent {
    private String eventType;
    private Long orderId;
    private Long customerId;
    private Long restaurantId;
    private Double totalAmount;
    private String oldStatus;
    private String newStatus;
    private String reason;
    private String timestamp;
}