package com.swiggy.restaurant.service;

import com.swiggy.restaurant.dto.*;
import com.swiggy.restaurant.entity.*;
import java.util.List;

public interface IRestaurantService {
    Restaurant registerRestaurant(RestaurantRequest request);
    Restaurant getRestaurantById(Long id);
    Restaurant toggleOpen(Long id, boolean open);
    Category addCategory(CategoryRequest request);
    List<MenuItem> getMenu(Long restaurantId);
    MenuItem addMenuItem(MenuItemRequest request, Long restaurantId);
    List<RestaurantSummaryDto> getRestaurantsByCity(String city);
}