package com.proyectointegrado.reina_cabrera_david.bean;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class User {
	
	private int id;
	
	private String name;
	
	private String lastname;
	
	private String corporateMail;
	
	private String role;
}
