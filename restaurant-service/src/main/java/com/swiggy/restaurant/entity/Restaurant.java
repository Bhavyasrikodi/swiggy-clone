package com.swiggy.restaurant.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
import org.hibernate.annotations.UpdateTimestamp;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "restaurants")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class Restaurant implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    private String description;

    @Column(name = "owner_id", nullable = false)
    private Long ownerId;

    private String phone;
    private String email;

    @Column(name = "address_line")
    private String addressLine;

    private String city;
    private String pincode;

    private Double latitude;
    private Double longitude;

    @Column(name = "is_active")
    private boolean active = true;

    @Column(name = "is_open")
    private boolean open = false;

    @Column(name = "opening_time")
    private String openingTime;    // store as "09:00" string

    @Column(name = "closing_time")
    private String closingTime;

    @Column(name = "avg_delivery_time")
    private Integer avgDeliveryTime = 30;

    @Column(name = "min_order_amount")
    private Double minOrderAmount = 0.0;

    @OneToMany(mappedBy = "restaurant",
            cascade = CascadeType.ALL,
            orphanRemoval = true,
            fetch = FetchType.EAGER)
    @JsonManagedReference
    private List<Category> categories = new ArrayList<>();

    @JsonIgnore
    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @JsonIgnore
    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
}