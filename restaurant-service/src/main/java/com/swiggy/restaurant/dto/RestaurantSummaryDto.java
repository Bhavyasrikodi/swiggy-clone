package com.swiggy.restaurant.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.io.Serializable;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RestaurantSummaryDto implements Serializable {
    private static final long serialVersionUID = 1L;
    private Long id;
    private String name;
    private String description;
    private String city;
    private String addressLine;
    private Double latitude;
    private Double longitude;
    private Integer avgDeliveryTime;
    private Double minOrderAmount;
    private boolean open;
    private String openingTime;
    private String closingTime;
}