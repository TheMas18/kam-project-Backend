package com.sb.kam.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.sb.kam.exception.ResourceNotFoundException;
import com.sb.kam.model.Interaction;
import com.sb.kam.model.InteractionType;
import com.sb.kam.model.Restaurant;
import com.sb.kam.repository.InteractionRepository;
import com.sb.kam.service.InteractionService;
import com.sb.kam.service.RestaurantService;

import java.util.List;
import java.util.Optional;

@Service
public class InteractionServiceImpl implements InteractionService {

	private final InteractionRepository interactionRepository;

	@Autowired
	private RestaurantService restaurantService;

	@Autowired
	public InteractionServiceImpl(InteractionRepository interactionRepository) {
		this.interactionRepository = interactionRepository;
	}

	@Override
	public List<Interaction> getAllInteractions() {
		List<Interaction> listOfAllInteractions = interactionRepository.findAll();
//		if (listOfAllInteractions.isEmpty()) {
//			throw new ResourceNotFoundException("No interactions found.");
//		}
		return listOfAllInteractions;

	}

	@Override
	public Interaction saveInteraction(Interaction interaction) {
		System.out.println("Received interaction: " + interaction);
		if (interaction.getRestaurantId() != null) {
			Restaurant restaurant = restaurantService.getRestaurantById(interaction.getRestaurantId());
			if (restaurant == null) {
				throw new ResourceNotFoundException("Restaurant not found with id: " + interaction.getRestaurantId());
			}
			interaction.setRestaurant(restaurant);
		}
		return interactionRepository.save(interaction);
	}

	@Override
	public Interaction getInteractionById(Long id) {
		return interactionRepository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("Interaction not found with ID: " + id));
	}

	@Override
	public List<Interaction> getInteractionsByRestaurant(Long restaurantId) {
		Restaurant restaurant = restaurantService.getRestaurantById(restaurantId);
		if (restaurant == null) {
			throw new ResourceNotFoundException("Restaurant not found with id: " + restaurantId);
		}
		return interactionRepository.findByRestaurant(restaurant);
	}

	@Override
	public void deleteInteraction(Long id) {
		if (!interactionRepository.existsById(id)) {
			throw new ResourceNotFoundException("Interaction not found with ID: " + id);
		}
		interactionRepository.deleteById(id);
	}

	@Override
	public List<Interaction> findInteractionsByRestaurantAndType(Long restaurantId, InteractionType type) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Interaction updateInteractionType(Long id, String type) {
		Interaction interaction = getInteractionById(id);
		if (interaction == null) {
			throw new ResourceNotFoundException("Interaction Not Found With ID :" + id);
		}

		try {
			InteractionType interactionType = InteractionType.valueOf(type.toUpperCase());
			interaction.setInteractionType(interactionType);
		} catch (IllegalArgumentException e) {
			throw new ResourceNotFoundException("Invalid interaction type: " + type);
		}
		return interactionRepository.save(interaction);

	}

	@Override
	public Interaction updateFollowUpRequired(Long id, boolean followUpRequired) {
		System.out.println(followUpRequired);
		Interaction interaction = getInteractionById(id);
		if (interaction == null) {
			throw new ResourceNotFoundException("Interaction Not Found With ID :" + id);
		}
		interaction.setFollowUpRequired(followUpRequired);
		return interactionRepository.save(interaction);
	}

	@Override
	public Interaction updateInteractionDetails(Long id, Interaction updatedDetails) {
		Interaction existingInteraction = getInteractionById(id);
		if (existingInteraction == null) {

			throw new ResourceNotFoundException("Interactiom not found with id: " + id);
		}
		existingInteraction.setDateOfInteraction(updatedDetails.getDateOfInteraction());
		existingInteraction.setFollowUpRequired(updatedDetails.getFollowUpRequired());
		existingInteraction.setInteractionType(updatedDetails.getInteractionType());
		existingInteraction.setLoggedBy(updatedDetails.getLoggedBy());
		existingInteraction.setNotes(updatedDetails.getNotes());
		if (updatedDetails.getRestaurant() != null) {
			Long restaurantId = updatedDetails.getRestaurant().getId();
			Restaurant restaurant = restaurantService.getRestaurantById(restaurantId);
			existingInteraction.setRestaurant(restaurant);
		}
		return interactionRepository.save(existingInteraction);
	}

}