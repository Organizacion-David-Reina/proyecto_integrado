package com.proyectointegrado.reina_cabrera_david.entity;

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
 * The Class BonusEntity.
 */
@Entity
@Table(name = "bonuses")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class BonusEntity {

    /**
     * The id.
     */
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    
    /**
     * The bondType.
     */
    @Column(name = "bond_type")
    private String bondType;
    
    /**
     * The price.
     */
    private double price;
}
