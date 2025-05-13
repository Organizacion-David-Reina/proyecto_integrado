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
@Table(name = "bonuses")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class BonusEntity {

	@Id
	@Column(name = "id")
	private int id;
	
	@Column(name = "bond_type")
	private String bondType;
	
	private double price;
}
