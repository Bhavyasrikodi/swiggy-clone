package com.swiggy.order.service;

import com.swiggy.order.dto.*;
import com.swiggy.order.entity.Order;
import com.swiggy.order.entity.Order.OrderStatus;
import com.swiggy.order.entity.OrderItem;
import com.swiggy.order.feign.RestaurantClient;
import com.swiggy.order.kafka.OrderEventProducer;
import com.swiggy.order.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements IOrderService {

    private final OrderRepository orderRepository;
    private final OrderEventProducer eventProducer;
    private final RestaurantClient restaurantClient;

    @Override
    @Transactional
    public OrderResponse placeOrder(PlaceOrderRequest request) {
        log.info("Placing order for customer: {} at restaurant: {}",
                request.getCustomerId(), request.getRestaurantId());

        // fetch menu from restaurant-service via Feign
        List<MenuItemDto> menu = restaurantClient
                .getMenu(request.getRestaurantId());
        log.debug("Fetched {} menu items from restaurant-service",
                menu.size());

        // build a map for quick lookup
        Map<Long, MenuItemDto> menuMap = menu.stream()
                .collect(Collectors.toMap(MenuItemDto::getId, m -> m));

        // validate items and calculate total
        List<OrderItem> orderItems = request.getItems().stream()
                .map(itemReq -> {
                    MenuItemDto menuItem = menuMap.get(itemReq.getMenuItemId());
                    if (menuItem == null) {
                        throw new RuntimeException(
                                "Menu item not found: " + itemReq.getMenuItemId());
                    }
                    if (!menuItem.isAvailable()) {
                        throw new RuntimeException(
                                "Menu item not available: " + menuItem.getName());
                    }
                    double totalPrice = menuItem.getPrice() * itemReq.getQuantity();
                    return OrderItem.builder()
                            .menuItemId(menuItem.getId())
                            .itemName(menuItem.getName())
                            .quantity(itemReq.getQuantity())
                            .price(menuItem.getPrice())
                            .totalPrice(totalPrice)
                            .build();
                })
                .collect(Collectors.toList());

        double totalAmount = orderItems.stream()
                .mapToDouble(OrderItem::getTotalPrice)
                .sum();

        // create order
        Order order = Order.builder()
                .customerId(request.getCustomerId())
                .restaurantId(request.getRestaurantId())
                .deliveryAddress(request.getDeliveryAddress())
                .deliveryLat(request.getDeliveryLat())
                .deliveryLng(request.getDeliveryLng())
                .specialInstructions(request.getSpecialInstructions())
                .status(OrderStatus.PLACED)
                .totalAmount(totalAmount)
                .deliveryFee(20.0)
                .build();

        // link items to order
        orderItems.forEach(item -> item.setOrder(order));
        order.setItems(orderItems);

        Order saved = orderRepository.save(order);
        log.info("Order saved with id: {}, total: {}",
                saved.getId(), saved.getTotalAmount());

        // publish Kafka event
        eventProducer.publishOrderPlaced(
                saved.getId(),
                saved.getCustomerId(),
                saved.getRestaurantId(),
                saved.getTotalAmount()
        );

        return toResponse(saved);
    }

    @Override
    @Transactional(readOnly = true)
    public OrderResponse getOrderById(Long orderId) {
        log.info("Fetching order: {}", orderId);
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() ->
                        new RuntimeException("Order not found: " + orderId));
        return toResponse(order);
    }

    @Override
    @Transactional(readOnly = true)
    public List<OrderResponse> getOrdersByCustomer(Long customerId) {
        log.info("Fetching orders for customer: {}", customerId);
        return orderRepository
                .findByCustomerIdOrderByCreatedAtDesc(customerId)
                .stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public OrderResponse updateOrderStatus(Long orderId,
                                           OrderStatus newStatus) {
        log.info("Updating order {} status to {}", orderId, newStatus);
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() ->
                        new RuntimeException("Order not found: " + orderId));

        String oldStatus = order.getStatus().name();
        order.setStatus(newStatus);
        Order saved = orderRepository.save(order);

        eventProducer.publishOrderStatusChanged(
                orderId, oldStatus, newStatus.name());

        return toResponse(saved);
    }

    @Override
    @Transactional
    public OrderResponse cancelOrder(Long orderId, String reason) {
        log.info("Cancelling order: {} reason: {}", orderId, reason);
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() ->
                        new RuntimeException("Order not found: " + orderId));

        if (order.getStatus() == OrderStatus.PICKED_UP ||
                order.getStatus() == OrderStatus.DELIVERED) {
            throw new RuntimeException(
                    "Cannot cancel order in status: " + order.getStatus());
        }

        order.setStatus(OrderStatus.CANCELLED);
        Order saved = orderRepository.save(order);

        eventProducer.publishOrderCancelled(
                orderId, order.getCustomerId(), reason);

        return toResponse(saved);
    }

    private OrderResponse toResponse(Order order) {
        List<OrderItemResponse> itemResponses = order.getItems()
                .stream()
                .map(item -> OrderItemResponse.builder()
                        .menuItemId(item.getMenuItemId())
                        .itemName(item.getItemName())
                        .quantity(item.getQuantity())
                        .price(item.getPrice())
                        .totalPrice(item.getTotalPrice())
                        .build())
                .collect(Collectors.toList());

        return OrderResponse.builder()
                .orderId(order.getId())
                .customerId(order.getCustomerId())
                .restaurantId(order.getRestaurantId())
                .status(order.getStatus())
                .totalAmount(order.getTotalAmount())
                .deliveryFee(order.getDeliveryFee())
                .deliveryAddress(order.getDeliveryAddress())
                .specialInstructions(order.getSpecialInstructions())
                .items(itemResponses)
                .createdAt(order.getCreatedAt())
                .build();
    }
}