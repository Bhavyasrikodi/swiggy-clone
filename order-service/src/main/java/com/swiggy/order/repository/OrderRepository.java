package com.swiggy.order.repository;

import com.swiggy.order.entity.Order;
import com.swiggy.order.entity.Order.OrderStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {
    List<Order> findByCustomerIdOrderByCreatedAtDesc(Long customerId);
    List<Order> findByRestaurantIdAndStatus(Long restaurantId,
                                            OrderStatus status);
    List<Order> findByCustomerIdAndStatus(Long customerId,
                                          OrderStatus status);
}