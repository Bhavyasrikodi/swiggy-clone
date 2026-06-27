package com.swiggy.restaurant.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class MenuItemRequest {

    @NotBlank(message = "Name is required")
    private String name;

    private String description;

    @NotNull(message = "Price is required")
    private Double price;

    private boolean veg = true;

    @NotNull(message = "Category ID is required")
    private Long categoryId;
}