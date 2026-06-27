package com.swiggy.notification.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class RestaurantEvent {
    private String eventType;
    private Long restaurantId;
    private String name;
    private String timestamp;
}