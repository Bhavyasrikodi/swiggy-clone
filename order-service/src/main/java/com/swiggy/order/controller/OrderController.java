package com.swiggy.order.controller;

import com.swiggy.order.dto.*;
import com.swiggy.order.entity.Order.OrderStatus;
import com.swiggy.order.service.IOrderService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/orders")
@RequiredArgsConstructor
public class OrderController {

    private final IOrderService orderService;

    @PostMapping
    public ResponseEntity<OrderResponse> placeOrder(
            @Valid @RequestBody PlaceOrderRequest request) {
        log.info("POST /api/orders - customer: {}, restaurant: {}",
                request.getCustomerId(), request.getRestaurantId());
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(orderService.placeOrder(request));
    }

    @GetMapping("/{orderId}")
    public ResponseEntity<OrderResponse> getOrder(
            @PathVariable Long orderId) {
        log.info("GET /api/orders/{}", orderId);
        return ResponseEntity.ok(orderService.getOrderById(orderId));
    }

    @GetMapping("/customer/{customerId}")
    public ResponseEntity<List<OrderResponse>> getCustomerOrders(
            @PathVariable Long customerId) {
        log.info("GET /api/orders/customer/{}", customerId);
        return ResponseEntity.ok(
                orderService.getOrdersByCustomer(customerId));
    }

    @PatchMapping("/{orderId}/status")
    public ResponseEntity<OrderResponse> updateStatus(
            @PathVariable Long orderId,
            @RequestParam OrderStatus status) {
        log.info("PATCH /api/orders/{}/status - status: {}",
                orderId, status);
        return ResponseEntity.ok(
                orderService.updateOrderStatus(orderId, status));
    }

    @PatchMapping("/{orderId}/cancel")
    public ResponseEntity<OrderResponse> cancelOrder(
            @PathVariable Long orderId,
            @RequestParam(defaultValue = "Customer cancelled")
            String reason) {
        log.info("PATCH /api/orders/{}/cancel - reason: {}",
                orderId, reason);
        return ResponseEntity.ok(
                orderService.cancelOrder(orderId, reason));
    }
}