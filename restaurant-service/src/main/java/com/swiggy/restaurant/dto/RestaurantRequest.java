package com.swiggy.restaurant.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import java.time.LocalTime;

@Data
public class RestaurantRequest {

    @NotBlank(message = "Name is required")
    private String name;

    private String description;

    @NotNull(message = "Owner ID is required")
    private Long ownerId;

    private String phone;
    private String email;

    @NotBlank(message = "Address is required")
    private String addressLine;

    @NotBlank(message = "City is required")
    private String city;

    private String pincode;
    private Double latitude;
    private Double longitude;
    private String openingTime;
    private String closingTime;
    private Integer avgDeliveryTime;
    private Double minOrderAmount;
}