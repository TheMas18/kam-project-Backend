package com.sb.kam.service.impl;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.sb.kam.exception.ResourceNotFoundException;
import com.sb.kam.model.Contact;
import com.sb.kam.model.Restaurant;
import com.sb.kam.model.Role;
import com.sb.kam.model.Status;
import com.sb.kam.repository.ContactRepository;
import com.sb.kam.service.ContactService;
import com.sb.kam.service.RestaurantService;

@Service
public class ContactServiceImpl implements ContactService {

	private final ContactRepository contactRepository;
	@Autowired
	private RestaurantService restaurantService;

	@Autowired
	public ContactServiceImpl(ContactRepository contactRepository) {
		this.contactRepository = contactRepository;
	}

	@Override
	public Contact saveContact(Contact contact) {
		System.out.println("Received Contact: " + contact);
		if (contact.getRestaurantId() != null) {
			Restaurant restaurant = restaurantService.getRestaurantById(contact.getRestaurantId());
			if (restaurant == null) {
				throw new ResourceNotFoundException("Restaurant not found with id: " + contact.getRestaurantId());
			}
			contact.setRestaurant(restaurant);
		}
		return contactRepository.save(contact);
	}

	@Override
	public Contact getContactById(Long id) {
		return contactRepository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("Contact not found with given id : " + id));
	}

	@Override
	public List<Contact> getAllContacts() {
		return contactRepository.findAll();
	}

	@Override
	public List<Contact> getContactsByRestaurant(Long restaurantId) {
		return contactRepository.findByRestaurant_Id(restaurantId); // Query contacts by restaurantId
	}

	@Override
	public void deleteContact(Long id) {
		contactRepository.deleteById(id);
	}

	@Override
	public Contact updateRole(Long id, String newRole) {
		Contact contact = getContactById(id);
		if (contact == null) {
			throw new ResourceNotFoundException("Contact not found with id: " + id);
		}
		Role role = Role.valueOf(newRole.toUpperCase());
		contact.setRole(role);
		return contactRepository.save(contact);
	}

	@Override
	public Contact updateContactDetails(Long id, Contact updatedContact) {
		Contact existingContact = getContactById(id);
		if (existingContact == null) {
			throw new ResourceNotFoundException("Contact not found with id: " + id);
		}
		existingContact.setEmail(updatedContact.getEmail());
		existingContact.setName(updatedContact.getName());
		existingContact.setPhoneNumber(updatedContact.getPhoneNumber());
		existingContact.setRole(updatedContact.getRole());

		if (updatedContact.getRestaurant() != null) {
			Long restaurantId = updatedContact.getRestaurant().getId();
			Restaurant restaurant = restaurantService.getRestaurantById(restaurantId);
			existingContact.setRestaurant(restaurant);
		}
		return contactRepository.save(existingContact);
	}

}