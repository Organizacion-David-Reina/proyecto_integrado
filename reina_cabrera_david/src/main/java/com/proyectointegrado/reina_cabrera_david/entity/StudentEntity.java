package com.proyectointegrado.reina_cabrera_david.entity;

import java.time.LocalDate;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * The Class StudentEntity.
 */
@Entity
@Table(name = "students")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class StudentEntity {
	
	/** The id */
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
	
	/** The name */
	private String name; 
	
	/** The lastname */
	private String lastname;
	
	/** The nif */
	private String nif;
	
	/** The phone number */
	@Column(name = "phone_number")
	private String phoneNumber;
	
	/** The address */
	private String address;
	
	/** The day of birth */
	@Column(name = "date_of_birth")
	private LocalDate dayOfBirth;
	
	/** The bonus */
	@ManyToOne
	@JoinColumn(name = "bond_id")
	private BonusEntity bonus;
	
	/**The mail */
	private String mail;
}
