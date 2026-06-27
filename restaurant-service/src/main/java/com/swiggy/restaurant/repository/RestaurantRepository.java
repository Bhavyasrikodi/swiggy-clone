package com.swiggy.restaurant.repository;

import com.swiggy.restaurant.entity.Restaurant;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface RestaurantRepository extends JpaRepository<Restaurant, Long> {

    List<Restaurant> findByActiveTrue();
    List<Restaurant> findByCityAndActiveTrue(String city);
    List<Restaurant> findByOwnerIdAndActiveTrue(Long ownerId);

    @Query("SELECT r FROM Restaurant r WHERE r.active = true " +
            "AND r.open = true AND r.city = :city")
    List<Restaurant> findOpenRestaurantsByCity(@Param("city") String city);
}