package com.swiggy.order.dto;

import com.swiggy.order.entity.Order.OrderStatus;
import lombok.*;
import java.time.LocalDateTime;
import java.util.List;

@Data @Builder @NoArgsConstructor @AllArgsConstructor
public class OrderResponse {
    private Long orderId;
    private Long customerId;
    private Long restaurantId;
    private OrderStatus status;
    private Double totalAmount;
    private Double deliveryFee;
    private String deliveryAddress;
    private String specialInstructions;
    private List<OrderItemResponse> items;
    private LocalDateTime createdAt;
}