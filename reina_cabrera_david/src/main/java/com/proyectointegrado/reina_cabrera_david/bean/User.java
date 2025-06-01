package com.proyectointegrado.reina_cabrera_david.bean;

import java.time.LocalDate;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
@ToString
public class User {
	
	private int id;
	
	private String name;
	
	private String lastname;
	
	private String corporateMail;
	
	private String phoneNumber;
	
	private String address;
	
	private LocalDate dayOfBirth;
	
	private Role role;
}
