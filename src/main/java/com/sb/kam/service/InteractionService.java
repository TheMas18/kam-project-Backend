package com.sb.kam.service;

import java.util.List;
import java.util.Optional;

import com.sb.kam.model.Interaction;
import com.sb.kam.model.InteractionType;

public interface InteractionService {
	Interaction saveInteraction(Interaction interaction);

	Interaction getInteractionById(Long id);

	List<Interaction> getAllInteractions();

	List<Interaction> getInteractionsByRestaurant(Long restaurantId);

	void deleteInteraction(Long id);

	Interaction updateInteractionType(Long id, String interactionType);

	Interaction updateInteractionDetails(Long id, Interaction updatedDetails);

	Interaction updateFollowUpRequired(Long id, boolean followUpRequired);

	List<Interaction> findInteractionsByRestaurantAndType(Long restaurantId, InteractionType type);
}
