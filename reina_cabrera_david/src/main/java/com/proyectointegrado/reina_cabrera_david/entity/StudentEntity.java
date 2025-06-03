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

@Entity
@Table(name = "students")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class StudentEntity {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
	
	private String name; 
	
	private String lastname;
	
	private String nif;
	
	@Column(name = "phone_number")
	private String phoneNumber;
	
	private String address;
	
	@Column(name = "date_of_birth")
	private LocalDate dayOfBirth;
	
	@ManyToOne
	@JoinColumn(name = "bond_id")
	private BonusEntity bonus;
}
