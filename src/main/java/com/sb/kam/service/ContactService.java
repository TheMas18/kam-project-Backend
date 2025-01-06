package com.sb.kam.service;

import java.util.List;
import java.util.Optional;

import com.sb.kam.model.Contact;
import com.sb.kam.model.Restaurant;

public interface ContactService {
	Contact saveContact(Contact contact);

	Contact getContactById(Long id);
	
	List<Contact> getAllContacts();
	
	List<Contact> getContactsByRestaurant(Long restaurantId);

	Contact updateRole(Long id, String role);
	
	void deleteContact(Long id);
	
	Contact updateContactDetails(Long id, Contact updatedContact);
}