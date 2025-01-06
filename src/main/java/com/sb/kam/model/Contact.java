package com.sb.kam.model;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonBackReference;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
public class Contact {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	private String name;

	@Enumerated(EnumType.STRING) // Store as a string in the database
	private Role role; // Owner, Manager, etc.

	private String phoneNumber;

	private String email;

	@ManyToOne
	@JoinColumn(name = "restaurant_id")
	@JsonBackReference
	private Restaurant restaurant; // Associated lead (restaurant)

	public String getRestaurantName() {
		return restaurant != null ? restaurant.getRestaurantName() : null;
	}

	public Long getRestaurantId() {
		return restaurant != null ? restaurant.getId() : null;
	}

	public String toString() {
		return "Contact{" + "id=" + id + ", name='" + name + '\'' + ", role=" + role + ", phoneNumber='" + phoneNumber
				+ '\'' + ", email='" + email + '\'' + ", restaurantId="
				+ (restaurant != null ? restaurant.getId() : null) + '}';
	}
}