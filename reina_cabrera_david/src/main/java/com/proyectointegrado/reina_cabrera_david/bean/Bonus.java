package com.proyectointegrado.reina_cabrera_david.bean;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * The Class Bonus.
 */
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class Bonus {

    /**
     * The id.
     */
    private int id;
    
    /**
     * The bondType.
     */
    private String bondType;
    
    /**
     * The price.
     */
    private double price;
}
