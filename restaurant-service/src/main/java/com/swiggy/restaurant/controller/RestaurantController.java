package com.swiggy.restaurant.controller;

import com.swiggy.restaurant.dto.*;
import com.swiggy.restaurant.entity.*;
import com.swiggy.restaurant.service.IRestaurantService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/restaurants")
@RequiredArgsConstructor
public class RestaurantController {

    private final IRestaurantService restaurantService;

    @PostMapping
    public ResponseEntity<Restaurant> register(
            @Valid @RequestBody RestaurantRequest request) {
        log.info("POST /api/restaurants - name: {}", request.getName());
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(restaurantService.registerRestaurant(request));
    }

    @GetMapping("/city/{city}")
    public ResponseEntity<List<RestaurantSummaryDto>> getByCity(
            @PathVariable String city) {
        log.info("GET /api/restaurants/city/{}", city);
        return ResponseEntity.ok(
                restaurantService.getRestaurantsByCity(city));
    }

    @GetMapping("/{id}")
    public ResponseEntity<Restaurant> getById(@PathVariable Long id) {
        log.info("GET /api/restaurants/{}", id);
        return ResponseEntity.ok(
                restaurantService.getRestaurantById(id));
    }

    @PatchMapping("/{id}/toggle-open")
    public ResponseEntity<Restaurant> toggleOpen(
            @PathVariable Long id,
            @RequestParam boolean open) {
        log.info("PATCH /api/restaurants/{}/toggle-open - open: {}",
                id, open);
        return ResponseEntity.ok(
                restaurantService.toggleOpen(id, open));
    }

    @PostMapping("/categories")
    public ResponseEntity<Category> addCategory(
            @Valid @RequestBody CategoryRequest request) {
        log.info("POST /api/restaurants/categories - name: {}",
                request.getName());
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(restaurantService.addCategory(request));
    }

    @GetMapping("/{id}/menu")
    public ResponseEntity<List<MenuItem>> getMenu(
            @PathVariable Long id) {
        log.info("GET /api/restaurants/{}/menu", id);
        return ResponseEntity.ok(restaurantService.getMenu(id));
    }

    @PostMapping("/{id}/menu")
    public ResponseEntity<MenuItem> addMenuItem(
            @PathVariable Long id,
            @Valid @RequestBody MenuItemRequest request) {
        log.info("POST /api/restaurants/{}/menu - item: {}",
                id, request.getName());
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(restaurantService.addMenuItem(request, id));
    }
}