package com.proyectointegrado.reina_cabrera_david.bean;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * The Class Role.
 */
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class Role {
    
    /**
     * The id.
     */
    private int id;

    /**
     * The rol.
     */
    private String rol;
}
