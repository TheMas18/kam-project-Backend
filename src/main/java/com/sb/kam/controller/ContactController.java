package com.sb.kam.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.sb.kam.model.Contact;
import com.sb.kam.model.Restaurant;
import com.sb.kam.model.Role;
import com.sb.kam.service.ContactService;
import com.sb.kam.service.RestaurantService;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/contacts")
@CrossOrigin(origins = "http://localhost:3000")
public class ContactController {

	private final ContactService contactService;

	@Autowired
	public ContactController(ContactService contactService) {
		this.contactService = contactService;
	}

	// Create a new contact
	@PostMapping
	public ResponseEntity<Contact> addContact(@RequestBody Contact contact) {
		// System.out.println("Received contact details: " + contact);
		// System.out.println("Restaurant ID: " + contact.getRestaurantId());
		Contact savedContact = contactService.saveContact(contact);
		return new ResponseEntity<>(savedContact, HttpStatus.CREATED);
	}

	// Get List of all contacts
	@GetMapping
	public ResponseEntity<List<Contact>> getAllContacts() {
		List<Contact> contacts = contactService.getAllContacts();
		return ResponseEntity.ok(contacts);
	}

	// Get contact by ID
	@GetMapping("/{id}")
	public ResponseEntity<Contact> getContactById(@PathVariable Long id) {
		// System.out.println(contactService.getContactById(id));
		try {
			Contact contact = contactService.getContactById(id);
			if (contact == null) {
				throw new RuntimeException("Contact not found with id : " + id);
			}
			return ResponseEntity.ok(contact);
		} catch (Exception e) {
			throw new RuntimeException("Error fetching contact by ID: " + e.getMessage());
		}
	}

	// Get contacts by restaurant ID
	@GetMapping("/restaurant/{restaurantId}")
	public ResponseEntity<List<Contact>> getContactsByRestaurant(@PathVariable Long restaurantId) {
		List<Contact> contacts = contactService.getContactsByRestaurant(restaurantId);
		return ResponseEntity.ok(contacts);
	}

	// Delete a contact by ID
	@DeleteMapping("/{id}")
	public ResponseEntity<Void> deleteContact(@PathVariable Long id) {
		contactService.deleteContact(id);
		return new ResponseEntity<Void>(HttpStatus.NO_CONTENT);
	}

	// get all roles
	@GetMapping("/roles")
	public ResponseEntity<List<String>> getRoles() {
		List<String> roles = new ArrayList<>();
		for (Role role : Role.values()) {
			roles.add(role.name());
		}
		return new ResponseEntity<>(roles, HttpStatus.OK);
	}

	// update role
	@PutMapping("/{id}/role")
	public ResponseEntity<Contact> updateRole(@PathVariable Long id, @RequestBody Map<String, String> requestBody) {
		String newRole = requestBody.get("role");
		Contact updateContact = contactService.updateRole(id, newRole);
		return ResponseEntity.ok(updateContact);
	}

	// update contact
	@PutMapping("/{contactId}")
	public ResponseEntity<Contact> updateContact(@PathVariable Long contactId, @RequestBody Contact contact) {
		System.out.println("Updating contact with ID: " + contactId);
		System.out.println("Received contact details: " + contact);
		System.out.println("update" + contact.getRestaurantName() + contact.getRestaurant() + contact.getRestaurantId());
		Contact newContact = contactService.updateContactDetails(contactId, contact);
		return ResponseEntity.ok(newContact);
	}

}
