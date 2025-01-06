package com.sb.kam.service.impl;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.sb.kam.exception.ResourceNotFoundException;
import com.sb.kam.model.CallFrequency;
import com.sb.kam.model.Interaction;
import com.sb.kam.model.InteractionType;
import com.sb.kam.model.Restaurant;
import com.sb.kam.model.Status;
import com.sb.kam.repository.RestaurantRepository;
import com.sb.kam.service.RestaurantService;

@Service
public class RestaurantServiceImpl implements RestaurantService {

	private final RestaurantRepository restaurantRepository;

	@Autowired
	public RestaurantServiceImpl(RestaurantRepository restaurantRepository) {
		this.restaurantRepository = restaurantRepository;
	}

	@Override
	public Restaurant saveRestaurant(Restaurant restaurant) {
		return restaurantRepository.save(restaurant);
	}

	@Override
	public void deleteRestaurant(Long id) {
		restaurantRepository.deleteById(id);
	}

	@Override
	public List<Restaurant> getAllRestaurants() {
		return restaurantRepository.findAll();
	}

	@Override
	public Restaurant getRestaurantById(Long id) {
		return restaurantRepository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("Restaurant not found with the given id :" + id));
	}

	@Override
	public Restaurant updateStatus(Long id, String newStatus) {
		Restaurant restaurant = getRestaurantById(id);
		if (restaurant == null) {
			throw new ResourceNotFoundException("Restaurant not found with id: " + id);
		}
		Status status = Status.valueOf(newStatus.toUpperCase()); // Converts string to enum
		restaurant.setCurrentStatus(status);
		return restaurantRepository.save(restaurant);
	}

	@Override
	public Restaurant updateCallFrequency(Long id, String newCallFrequency) {
		Restaurant restaurant = getRestaurantById(id);
		if (restaurant == null) {
			throw new ResourceNotFoundException("Restaurant not found with id: " + id);
		}
		CallFrequency callFrequency = CallFrequency.valueOf(newCallFrequency.toUpperCase()); 
		restaurant.setCallFrequency(callFrequency);
		return restaurantRepository.save(restaurant);
	}

	@Override
	public Restaurant updateRestaurantDetails(Long id, Restaurant updatedRestaurant) {
		Restaurant existingRestaurant = getRestaurantById(id);
		if (existingRestaurant == null) {
			throw new ResourceNotFoundException("Restaurant not found with id: " + id);
		}

		existingRestaurant.setRestaurantName(updatedRestaurant.getRestaurantName());
		existingRestaurant.setAddress(updatedRestaurant.getAddress());
		existingRestaurant.setContactNumber(updatedRestaurant.getContactNumber());
		existingRestaurant.setAssignedKam(updatedRestaurant.getAssignedKam());

		if (updatedRestaurant.getCurrentStatus() != null) {
			existingRestaurant.setCurrentStatus(updatedRestaurant.getCurrentStatus());
		}
		return restaurantRepository.save(existingRestaurant);

	}

	@Override
	public Restaurant updateCallDetails(Long id, LocalDate lastCallDate, CallFrequency callFrequency) {
		Restaurant restaurant = getRestaurantById(id);
		if (restaurant == null) {
			throw new ResourceNotFoundException("Restaurant not found with id: " + id);
		}
		if (lastCallDate != null) {
			restaurant.setLastCallDate(lastCallDate);
		}
		if (callFrequency != null) {
			restaurant.setCallFrequency(callFrequency);
		}
		return restaurantRepository.save(restaurant);

	}

	@Override
	public List<Restaurant> getRestaurantsRequiringCallsToday() {
		List<Restaurant> listOfAllRestaurants = restaurantRepository.findAll();
		List<Restaurant> requiringCalls = new ArrayList<>();
		LocalDate currentDate = LocalDate.now();
		for (Restaurant restaurant : listOfAllRestaurants) {
			LocalDate lastCallDate = restaurant.getLastCallDate();
			CallFrequency callFrequency = restaurant.getCallFrequency();
			if (lastCallDate != null && callFrequency != null) {
				int days = getFrequencyInDays(callFrequency);
				LocalDate nextCallDate = lastCallDate.plusDays(days);
				if (!nextCallDate.isAfter(currentDate)) {
					requiringCalls.add(restaurant);
				}
			}
		}
		return requiringCalls;
	}

	public int getFrequencyInDays(CallFrequency callFrequency) {
		if (callFrequency == CallFrequency.WEEKLY) {
			return 7;
		} else if (callFrequency == CallFrequency.BIWEEKLY) {
			return 14;
		} else if (callFrequency == CallFrequency.MONTHLY) {
			return 30;
		}
		return 0;

	}

	@Override
	public List<Restaurant> getWellPerformingRestaurants() {
		LocalDate oneMonthAgo = LocalDate.now().minusMonths(1);

		// Get all restaurants with order interactions in the last month
		List<Restaurant> allRestaurantsWithOrders = restaurantRepository.findRestaurantsWithOrdersAfter(oneMonthAgo);
		List<Restaurant> wellPerformingRestaurants = new ArrayList<>();

		// Check if they have more than or equal to 3 orders in the last month
		for (Restaurant restaurant : allRestaurantsWithOrders) {
			int orderCount = restaurant.getOrderCountLastMonth();
			if (orderCount >= 2) {
				wellPerformingRestaurants.add(restaurant);
			}
		}

		return wellPerformingRestaurants;
	}

	@Override
	public List<Restaurant> getUnderperformingRestaurants() {
		LocalDate oneMonthAgo = LocalDate.now().minusMonths(1);
		return restaurantRepository.findRestaurantsWithoutRecentInteractions(oneMonthAgo);
	}

	@Override
	public long getTotalRestaurants() {
		return restaurantRepository.countTotalRestaurants();
	}

	@Override
	public long getTotalContacts() {
		return restaurantRepository.countTotalContacts();
	}

	@Override
	public long getTotalInteractions() {
		return restaurantRepository.countTotalInteractions();
	}

	@Override
	public long getTotalOrders() {
		return restaurantRepository.countTotalOrders();
	}

	@Override
	public List<Restaurant> getPendingFollowUps() {
		return restaurantRepository.findRestaurantsWithPendingFollowUps();
	}

	@Override
	public Map<String, Long> getRestaurantStatusCounts() {
		long activeCount = restaurantRepository.countActiveRestaurants();
		long inactiveCount = restaurantRepository.countInactiveRestaurants();

		Map<String, Long> counts = new HashMap<>();
		counts.put("active", activeCount);
		counts.put("inactive", inactiveCount);

		return counts;
	}

//	@Override
//	public long getOrderCountLastMonth() {
//		LocalDate lastMonth = LocalDate.now().minusMonths(1);
//		return restaurantRepository.countOrdersLastMonth(lastMonth);
//	}
//
//	@Override
//	public long getInteractionCountLastMonth() {
//		LocalDate lastMonth = LocalDate.now().minusMonths(1);
//		return restaurantRepository.countInteractionsLastMonth(lastMonth);
//	}
}
