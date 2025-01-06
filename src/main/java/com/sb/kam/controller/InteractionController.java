package com.sb.kam.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.sb.kam.model.Interaction;
import com.sb.kam.model.InteractionType;
import com.sb.kam.model.Status;
import com.sb.kam.service.InteractionService;
import com.sb.kam.service.RestaurantService;

import jakarta.persistence.EntityNotFoundException;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PathVariable;

@RestController
@RequestMapping("/interactions")
@CrossOrigin(origins = "http://localhost:3000")
public class InteractionController {
	@Autowired
	private InteractionService interactionService;

	// Create a new interaction
	@PostMapping
	public ResponseEntity<Interaction> addInteraction(@RequestBody Interaction interaction) {
		return new ResponseEntity<>(interactionService.saveInteraction(interaction), HttpStatus.OK);
	}

	// get all interactions
	@GetMapping
	public ResponseEntity<List<Interaction>> getAllInteractions() {
		List<Interaction> allInteractions = interactionService.getAllInteractions();
		return new ResponseEntity<>(allInteractions, HttpStatus.OK);
	}

	// get all interaction types
	@GetMapping("/interactionTypes")
	public ResponseEntity<List<String>> getAllInteractionTypes() {
		List<String> listOfInteractionTypes = new ArrayList<>();
		for (InteractionType type : InteractionType.values()) {
			listOfInteractionTypes.add(type.name());
		}
		return ResponseEntity.ok(listOfInteractionTypes);
	}

	// Get interaction by ID
	@GetMapping("/{id}")
	public ResponseEntity<Interaction> getInteractionById(@PathVariable Long id) {
		Interaction interaction = interactionService.getInteractionById(id);
		return new ResponseEntity<>(interaction, HttpStatus.OK);
	}

	// Get interactions by restaurant ID
	@GetMapping("/restaurant/{restaurantId}")
	public List<Interaction> getInteractionsByRestaurant(@PathVariable Long restaurantId) {
		return interactionService.getInteractionsByRestaurant(restaurantId);
	}

	// Delete an interaction by ID
	@DeleteMapping("/{id}")
	public ResponseEntity<Void> deleteInteraction(@PathVariable Long id) {
		interactionService.deleteInteraction(id);
		return new ResponseEntity<>(HttpStatus.NO_CONTENT);
	}

	// update interaction type
	@PutMapping("{interactionId}/interactionType")
	public ResponseEntity<Interaction> updateInteracitonType(@PathVariable Long interactionId,
			@RequestBody Map<String, String> requestBody) {
		System.out.println(requestBody);
		String interactionType = requestBody.get("interactionType");
		Interaction interaction = interactionService.updateInteractionType(interactionId, interactionType);

		return ResponseEntity.ok(interaction);
	}

	// update follow up required
	@PutMapping("/{interactionId}/follow-up")
	public ResponseEntity<Interaction> updateFollowUpRequired(@PathVariable Long interactionId,
			@RequestBody Map<String, Object> requestBody) {
		try {
			boolean followUpRequired = Boolean.parseBoolean(requestBody.get("followUpRequired").toString());
			Interaction updatedInteraction = interactionService.updateFollowUpRequired(interactionId, followUpRequired);
			return ResponseEntity.ok(updatedInteraction);
		} catch (EntityNotFoundException e) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
		}
	}

	// update interaction by id
	@PutMapping("/{interactionId}")
	public ResponseEntity<Interaction> updateInteraction(@PathVariable Long interactionId,
			@RequestBody Interaction interaction) {
		Interaction newInteraction = interactionService.updateInteractionDetails(interactionId, interaction);
		return ResponseEntity.ok(newInteraction);
	}

}