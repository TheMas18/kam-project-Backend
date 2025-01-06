package com.sb.kam.controller;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.aspectj.weaver.ast.Call;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.sb.kam.model.CallFrequency;
import com.sb.kam.model.Restaurant;
import com.sb.kam.model.Role;
import com.sb.kam.model.Status;
import com.sb.kam.service.RestaurantService;

@RestController
@RequestMapping("/restaurants")
@CrossOrigin(origins = "http://localhost:3000")
public class RestaurantController {

	private final RestaurantService restaurantService;

	@Autowired
	public RestaurantController(RestaurantService restaurantService) {
		this.restaurantService = restaurantService;
	}

	// Add new restaurant
	@PostMapping
	public ResponseEntity<Restaurant> addRestaurant(@RequestBody Restaurant restaurant) {
		Restaurant savedRestaurant = restaurantService.saveRestaurant(restaurant);
		return new ResponseEntity<>(savedRestaurant, HttpStatus.CREATED);
	}

	// Get restaurant by id
	@GetMapping("/{restaurantId}")
	public ResponseEntity<Restaurant> getRestaurant(@PathVariable Long restaurantId) {
		Restaurant restaurant = restaurantService.getRestaurantById(restaurantId);
		if (restaurant == null) {
			new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
		return ResponseEntity.ok(restaurant);
	}

	// Get all restaurants
	@GetMapping
	public ResponseEntity<List<Restaurant>> getAllRestaurants() {
		List<Restaurant> allRestaurants = restaurantService.getAllRestaurants();
		return new ResponseEntity<>(allRestaurants, HttpStatus.OK);
	}

	// Delete restaurant by id
	@DeleteMapping("/{id}")
	public ResponseEntity<Void> deleteRestaurant(@PathVariable Long id) {
		restaurantService.deleteRestaurant(id);
		return new ResponseEntity<>(HttpStatus.NO_CONTENT);
	}

	// Update restaurant status
	@PutMapping("/{id}/currentStatus")
	public ResponseEntity<Restaurant> updateRestaurantStatus(@PathVariable Long id,
			@RequestBody Map<String, String> requestBody) {
		String newStatus = requestBody.get("currentStatus");
		Restaurant updatedRestaurant = restaurantService.updateStatus(id, newStatus);
		return new ResponseEntity<>(updatedRestaurant, HttpStatus.OK);
	}

	// Update restaurant call frequency
	@PutMapping("/{id}/callFrequency")
	public ResponseEntity<Restaurant> updateRestaurantCallFrequency(@PathVariable Long id,
			@RequestBody Map<String, String> requestBody) {
		String newCallFrequency = requestBody.get("callFrequency");
		Restaurant updatedRestaurant = restaurantService.updateCallFrequency(id, newCallFrequency);
		return ResponseEntity.ok(updatedRestaurant);
	}

	// Update restaurant details
	@PutMapping("/{id}")
	public ResponseEntity<Restaurant> updateRestaurant(@PathVariable Long id,
			@RequestBody Restaurant updatedRestaurant) {
		Restaurant updatedEntity = restaurantService.updateRestaurantDetails(id, updatedRestaurant);
		return ResponseEntity.ok(updatedEntity);
	}

	// Get all status options
	@GetMapping("/allstatus")
	public ResponseEntity<List<String>> getStatus() {
		List<String> listOfStatus = new ArrayList<>();
		for (Status status : Status.values()) {
			listOfStatus.add(status.name());
		}
		return new ResponseEntity<>(listOfStatus, HttpStatus.OK);
	}

	// Update call details

	@PutMapping("/{id}/callDetails")
	public ResponseEntity<Restaurant> updateCallDetails(@PathVariable Long id,
			@RequestBody Map<String, String> requestBody) {
		LocalDate lastCallDate = null;
		if (requestBody.containsKey("lastCallDate")) {
			lastCallDate = LocalDate.parse(requestBody.get("lastCallDate"));
		}

		CallFrequency callFrequency = null;
		if (requestBody.containsKey("callFrequency")) {
			callFrequency = CallFrequency.valueOf(requestBody.get("callFrequency").toUpperCase());
		}

		Restaurant updatedRestaurant = restaurantService.updateCallDetails(id, lastCallDate, callFrequency);
		return new ResponseEntity<>(updatedRestaurant, HttpStatus.OK);
	}

	// Get restaurants requiring calls today
	@GetMapping("/requiringCalls")
	public ResponseEntity<List<Restaurant>> getRestaurantsRequiringCallsToday() {
		List<Restaurant> restaurantsRequiringCallsToday = restaurantService.getRestaurantsRequiringCallsToday();
		return new ResponseEntity<>(restaurantsRequiringCallsToday, HttpStatus.OK);
	}

	// Get all call frequencies
	@GetMapping("/allCallFrequency")
	public ResponseEntity<List<String>> getAllCallFrequency() {
		List<String> listOfCallFrequency = new ArrayList<String>();
		for (CallFrequency frequency : CallFrequency.values()) {
			listOfCallFrequency.add(frequency.name());
		}
		return new ResponseEntity<>(listOfCallFrequency, HttpStatus.OK);
	}

	// Get well-performing restaurants
	@GetMapping("/wellPerforming")
	public ResponseEntity<List<Restaurant>> getWellPerformingAccounts() {
		List<Restaurant> wellPerformingRestaurants = restaurantService.getWellPerformingRestaurants();
		return new ResponseEntity<>(wellPerformingRestaurants, HttpStatus.OK);
	}

	// Get underperforming restaurants
	@GetMapping("/underPerforming")
	public ResponseEntity<List<Restaurant>> getUnderperformingAccounts() {
		List<Restaurant> underperformingRestaurants = restaurantService.getUnderperformingRestaurants();
		return new ResponseEntity<>(underperformingRestaurants, HttpStatus.OK);
	}

	// total no of restaurants, interactions, contacts, orders
	@GetMapping("/counts")
	public ResponseEntity<Map<String, Object>> getDashboardCounts() {
		Map<String, Object> counts = new HashMap<>();
		counts.put("totalRestaurants", restaurantService.getTotalRestaurants());
		counts.put("totalContacts", restaurantService.getTotalContacts());
		counts.put("totalInteractions", restaurantService.getTotalInteractions());
		counts.put("totalOrders", restaurantService.getTotalOrders());

		return new ResponseEntity<>(counts, HttpStatus.OK);
	}

	// get list of restaurants having pending followups
	@GetMapping("/pendingFollowUps")
	public ResponseEntity<List<Restaurant>> getPendingFollowUps() {
		List<Restaurant> pendingFollowUps = restaurantService.getPendingFollowUps();
		System.out.println(pendingFollowUps);
		return new ResponseEntity<>(pendingFollowUps, HttpStatus.OK);
	}

	// get count of restaurants,contacts,interactions and order
	@GetMapping("/statusCounts")
	public ResponseEntity<Map<String, Long>> getRestaurantStatusCounts() {
		Map<String, Long> counts = restaurantService.getRestaurantStatusCounts();
		return ResponseEntity.ok(counts);
	}

//	// Get Order count for the last month
//	@GetMapping("/orderCountLastMonth")
//	public ResponseEntity<Long> getOrderCountLastMonth() {
//		Long orderCount = restaurantService.getOrderCountLastMonth();
//		return ResponseEntity.ok(orderCount);
//	}
//
//	// Get interaction count for the last month
//	@GetMapping("/interactionCountLastMonth")
//	public ResponseEntity<Long> getInteractionCountLastMonth() {
//		Long interactionCount = restaurantService.getInteractionCountLastMonth();
//		return ResponseEntity.ok(interactionCount);
//	}

}
