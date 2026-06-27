package com.swiggy.order.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class MenuItemDto {
    private Long id;
    private String name;
    private String description;
    private Double price;
    private boolean veg;
    private boolean available;
}