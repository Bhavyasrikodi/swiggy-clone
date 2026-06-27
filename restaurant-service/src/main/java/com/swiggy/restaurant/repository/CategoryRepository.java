package com.swiggy.restaurant.repository;

import com.swiggy.restaurant.entity.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Long> {
    List<Category> findByRestaurantIdAndActiveTrue(Long restaurantId);
}