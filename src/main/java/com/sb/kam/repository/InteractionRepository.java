package com.sb.kam.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.sb.kam.model.Interaction;
import com.sb.kam.model.Restaurant;

@Repository
public interface InteractionRepository extends JpaRepository<Interaction, Long> {
	 List<Interaction> findByRestaurant(Restaurant restaurant);
}