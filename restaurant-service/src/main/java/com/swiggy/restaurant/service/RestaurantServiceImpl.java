package com.swiggy.restaurant.service;

import com.swiggy.restaurant.dto.*;
import com.swiggy.restaurant.entity.*;
import com.swiggy.restaurant.kafka.RestaurantEventProducer;
import com.swiggy.restaurant.repository.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class RestaurantServiceImpl implements IRestaurantService {

    private final RestaurantRepository restaurantRepository;
    private final CategoryRepository categoryRepository;
    private final MenuItemRepository menuItemRepository;
    private final RestaurantEventProducer eventProducer;

    @Transactional
    public Restaurant registerRestaurant(RestaurantRequest request) {
        log.info("Registering restaurant: {}", request.getName());

        Restaurant restaurant = Restaurant.builder()
                .name(request.getName())
                .description(request.getDescription())
                .ownerId(request.getOwnerId())
                .phone(request.getPhone())
                .email(request.getEmail())
                .addressLine(request.getAddressLine())
                .city(request.getCity())
                .pincode(request.getPincode())
                .latitude(request.getLatitude())
                .longitude(request.getLongitude())
                .openingTime(request.getOpeningTime())
                .closingTime(request.getClosingTime())
                .avgDeliveryTime(request.getAvgDeliveryTime() != null
                        ? request.getAvgDeliveryTime() : 30)
                .minOrderAmount(request.getMinOrderAmount() != null
                        ? request.getMinOrderAmount() : 0.0)
                .active(true)
                .build();

        restaurant = restaurantRepository.save(restaurant);
        log.info("Restaurant registered with id: {}", restaurant.getId());

        eventProducer.sendRestaurantRegistered(
                restaurant.getId(), restaurant.getName());

        return restaurant;
    }

    @Override
    @Transactional(readOnly = true)
    public Restaurant getRestaurantById(Long id) {
        log.info("Fetching restaurant by id: {}", id);
        return restaurantRepository.findById(id)
                .orElseThrow(() ->
                        new RuntimeException("Restaurant not found: " + id));
    }

    @Override
    @Transactional(readOnly = true)
    public List<MenuItem> getMenu(Long restaurantId) {
        log.info("Fetching menu for restaurant: {} (from DB)", restaurantId);
        return menuItemRepository.findByRestaurantIdAndAvailableTrue(restaurantId);
    }

    @Override
    @Transactional
    @CacheEvict(value = "restaurants", allEntries = true)  // ← clear all city caches
    public Restaurant toggleOpen(Long id, boolean open) {
        log.info("Setting restaurant {} open status to: {}", id, open);
        Restaurant restaurant = restaurantRepository.findById(id)
                .orElseThrow(() ->
                        new RuntimeException("Restaurant not found: " + id));
        restaurant.setOpen(open);
        Restaurant saved = restaurantRepository.save(restaurant);
        log.info("Restaurant {} is now {}", id, open ? "OPEN" : "CLOSED");
        return saved;
    }

    @Transactional
    public Category addCategory(CategoryRequest request) {
        log.info("Adding category: {} to restaurant: {}",
                request.getName(), request.getRestaurantId());

        Restaurant restaurant = getRestaurantById(request.getRestaurantId());

        Category category = Category.builder()
                .name(request.getName())
                .description(request.getDescription())
                .restaurant(restaurant)
                .active(true)
                .build();

        return categoryRepository.save(category);
    }

    @Transactional
    @CacheEvict(value = "menu", key = "#request.categoryId")
    public MenuItem addMenuItem(MenuItemRequest request, Long restaurantId) {
        log.info("Adding menu item: {} to restaurant: {}",
                request.getName(), restaurantId);

        Restaurant restaurant = getRestaurantById(restaurantId);
        Category category = categoryRepository.findById(request.getCategoryId())
                .orElseThrow(() ->
                        new RuntimeException("Category not found"));

        MenuItem item = MenuItem.builder()
                .name(request.getName())
                .description(request.getDescription())
                .price(request.getPrice())
                .veg(request.isVeg())
                .category(category)
                .restaurant(restaurant)
                .available(true)
                .build();

        MenuItem saved = menuItemRepository.save(item);
        log.info("Menu item added with id: {}", saved.getId());

        eventProducer.sendMenuUpdated(restaurantId);
        return saved;
    }

    @Override
    @Cacheable(value = "restaurants", key = "#city")
    @Transactional(readOnly = true)
    public List<RestaurantSummaryDto> getRestaurantsByCity(String city) {
        log.info("Fetching open restaurants for city: {} (from DB)", city);
        List<Restaurant> restaurants = restaurantRepository
                .findOpenRestaurantsByCity(city);
        return restaurants.stream()
                .map(this::toSummaryDto)
                .collect(java.util.stream.Collectors.toList());
    }

    private RestaurantSummaryDto toSummaryDto(Restaurant r) {
        return RestaurantSummaryDto.builder()
                .id(r.getId())
                .name(r.getName())
                .description(r.getDescription())
                .city(r.getCity())
                .addressLine(r.getAddressLine())
                .latitude(r.getLatitude())
                .longitude(r.getLongitude())
                .avgDeliveryTime(r.getAvgDeliveryTime())
                .minOrderAmount(r.getMinOrderAmount())
                .open(r.isOpen())
                .openingTime(r.getOpeningTime())
                .closingTime(r.getClosingTime())
                .build();
    }
}