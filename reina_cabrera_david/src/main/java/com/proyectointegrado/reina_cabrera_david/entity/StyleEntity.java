package com.proyectointegrado.reina_cabrera_david.entity;

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
 * The Class StyleEntity.
 * Represents the style entity mapped to the "styles" database table.
 */
@Entity
@Table(name = "styles")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class StyleEntity {

    /**
     * The id.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    
    /**
     * The style.
     */
    private String style;
}
