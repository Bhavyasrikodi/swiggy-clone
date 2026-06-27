package com.swiggy.order.feign;

import com.swiggy.order.dto.MenuItemDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import java.util.List;

@FeignClient(name = "restaurant-service")
public interface RestaurantClient {

    @GetMapping("/api/restaurants/{id}/menu")
    List<MenuItemDto> getMenu(@PathVariable("id") Long restaurantId);
}