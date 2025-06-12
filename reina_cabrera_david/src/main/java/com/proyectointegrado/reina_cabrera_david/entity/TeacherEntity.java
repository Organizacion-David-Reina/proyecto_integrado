package com.proyectointegrado.reina_cabrera_david.entity;

import java.time.LocalDate;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * The Class TeacherEntity.
 */
@Entity
@Table(name = "teachers")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class TeacherEntity {
	
	/** The id. */
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
	
	/** The name. */
	private String name;
	
	/** The lastname. */
	private String lastname;
	
	/** The mail. */
	private String mail;
	
	/** The nif. */
	private String nif;
	
	/** The phoneNumber. */
	@Column(name = "phone_number")
	private String phoneNumber;
	
	/** The address. */
	private String address;
	
	/** The dayOfBirth. */
	@Column(name = "date_of_birth")
	private LocalDate dayOfBirth;
}
