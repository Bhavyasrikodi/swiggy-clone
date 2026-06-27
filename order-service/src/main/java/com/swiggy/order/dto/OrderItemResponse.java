package com.swiggy.order.dto;

import lombok.*;

@Data @Builder @NoArgsConstructor @AllArgsConstructor
public class OrderItemResponse {
    private Long menuItemId;
    private String itemName;
    private Integer quantity;
    private Double price;
    private Double totalPrice;
}