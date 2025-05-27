package com.proyectointegrado.reina_cabrera_david.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "styles")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class StyleEntity {

	@Id
	private int id;
	
	private String style;
}
