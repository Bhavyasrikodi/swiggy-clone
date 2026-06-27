package com.swiggy.restaurant;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.kafka.annotation.EnableKafka;

@SpringBootApplication
@EnableCaching
@EnableKafka
public class RestaurantServiceApplication {

	public static void main(String[] args) {
		System.setProperty("user.timezone", "Asia/Kolkata");
		SpringApplication.run(RestaurantServiceApplication.class, args);
	}
}