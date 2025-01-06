package com.sb.kam.model;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonManagedReference;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Transient;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
public class Restaurant {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	private String restaurantName;

	private String address;

	private String contactNumber;

	@Enumerated(EnumType.STRING)
	private Status currentStatus; // NEW, ACTIVE, INACTIVE

	private String assignedKam; // Name of the KAM

	@Enumerated(EnumType.STRING)
	private CallFrequency callFrequency;

	private LocalDate lastCallDate; // Tracks the last call made

	@OneToMany(mappedBy = "restaurant", cascade = CascadeType.ALL, orphanRemoval = true)
	@JsonManagedReference
	private List<Contact> contacts; // List of contacts for this lead

	@OneToMany(mappedBy = "restaurant", cascade = CascadeType.ALL, orphanRemoval = true)
	@JsonManagedReference
	private List<Interaction> interactions; // List of interactions for this lead

	@Transient
	private Integer orderCountLastMonth;

	@Transient
	private Integer interactionCountLastMonth;

	public Integer getOrderCountLastMonth() {
		int count = 0;
		if (interactions != null) {
			for (Interaction interaction : interactions) {
				if (interaction.getInteractionType() == InteractionType.ORDER
						&& interaction.getDateOfInteraction().isAfter(LocalDate.now().minusMonths(1))) {
					count++;
				}
			}
		}
		return count;
	}

	public Integer getInteractionCountLastMonth() {
		int count = 0;
		if (interactions != null) {
			for (Interaction interaction : interactions) {
				if (interaction.getDateOfInteraction().isAfter(LocalDate.now().minusMonths(1))) {
					count++;
				}
			}
		}
		return count;
	}

}