package com.proyectointegrado.reina_cabrera_david.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "credentials")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class CredentialsEntity {

	@Id
	@Column(name = "user_id")
	private int userId;

	private String nif;
	
	@Column(name = "corporate_mail")
	private String corporateMail;

	private String password;
}
