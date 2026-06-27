package com.swiggy.order.service;

import com.swiggy.order.dto.*;
import com.swiggy.order.entity.Order.OrderStatus;
import java.util.List;

public interface IOrderService {
    OrderResponse placeOrder(PlaceOrderRequest request);
    OrderResponse getOrderById(Long orderId);
    List<OrderResponse> getOrdersByCustomer(Long customerId);
    OrderResponse updateOrderStatus(Long orderId, OrderStatus newStatus);
    OrderResponse cancelOrder(Long orderId, String reason);
}