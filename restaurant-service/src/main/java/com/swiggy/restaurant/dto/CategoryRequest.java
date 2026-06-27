package com.swiggy.restaurant.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class CategoryRequest {

    @NotBlank(message = "Category name is required")
    private String name;

    private String description;

    @NotNull(message = "Restaurant ID is required")
    private Long restaurantId;
}