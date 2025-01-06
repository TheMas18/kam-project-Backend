package com.sb.kam.repository;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.sb.kam.model.Restaurant;

@Repository
public interface RestaurantRepository extends JpaRepository<Restaurant, Long> {
	@Query("SELECT r FROM Restaurant r JOIN FETCH r.interactions i WHERE i.dateOfInteraction > :startDate AND i.interactionType = 'ORDER'")
	List<Restaurant> findRestaurantsWithOrdersAfter(@Param("startDate") LocalDate startDate);

	@Query("SELECT r FROM Restaurant r WHERE r.id NOT IN (SELECT DISTINCT i.restaurant.id FROM Interaction i WHERE i.dateOfInteraction > :startDate)")
	List<Restaurant> findRestaurantsWithoutRecentInteractions(@Param("startDate") LocalDate startDate);
	
	
	@Query("SELECT r FROM Restaurant r JOIN r.interactions i WHERE i.followUpRequired = true")
    List<Restaurant> findRestaurantsWithPendingFollowUps();

    @Query("SELECT COUNT(i) FROM Interaction i WHERE i.interactionType = 'ORDER'")
    long countTotalOrders();

    @Query("SELECT COUNT(r) FROM Restaurant r")
    long countTotalRestaurants();

    @Query("SELECT COUNT(c) FROM Contact c")
    long countTotalContacts();

    @Query("SELECT COUNT(i) FROM Interaction i")
    long countTotalInteractions();
    
    @Query("SELECT COUNT(r) FROM Restaurant r WHERE r.currentStatus = 'ACTIVE'")
    long countActiveRestaurants();

    @Query("SELECT COUNT(r) FROM Restaurant r WHERE r.currentStatus = 'INACTIVE'")
    long countInactiveRestaurants();

//    // Query to count orders in the last month for a restaurant
//    @Query("SELECT COUNT(i) FROM Interaction i WHERE  i.interactionType = 'ORDER' AND i.dateOfInteraction > :lastMonth")
//    long countOrdersLastMonth(LocalDate lastMonth);
//
//    // Query to count all interactions in the last month for a restaurant
//    @Query("SELECT COUNT(i) FROM Interaction i WHERE   i.dateOfInteraction > :lastMonth")
//    long countInteractionsLastMonth(LocalDate lastMonth);

}