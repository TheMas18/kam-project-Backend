package com.sb.kam.service;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import com.sb.kam.model.CallFrequency;
import com.sb.kam.model.Restaurant;

public interface RestaurantService {

	Restaurant saveRestaurant(Restaurant restaurant);

	Restaurant getRestaurantById(Long id);

	List<Restaurant> getAllRestaurants();

	void deleteRestaurant(Long id);

	Restaurant updateStatus(Long id, String newStatus);

	Restaurant updateCallFrequency(Long id, String newCallFrequency);

	Restaurant updateRestaurantDetails(Long id, Restaurant updatedRestaurant);

	Restaurant updateCallDetails(Long id, LocalDate lastCallDate, CallFrequency callFrequency);

	List<Restaurant> getRestaurantsRequiringCallsToday();

	List<Restaurant> getWellPerformingRestaurants();

	List<Restaurant> getUnderperformingRestaurants();

	List<Restaurant> getPendingFollowUps();

//	long getOrderCountLastMonth();
//
//	long getInteractionCountLastMonth();

	long getTotalRestaurants();

	long getTotalContacts();

	long getTotalInteractions();

	long getTotalOrders();

	Map<String, Long> getRestaurantStatusCounts();

}
