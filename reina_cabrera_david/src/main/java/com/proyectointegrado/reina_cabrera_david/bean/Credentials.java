package com.proyectointegrado.reina_cabrera_david.bean;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * The Class Credentials.
 */
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class Credentials {

    /**
     * The nif.
     */
    private String nif;
    
    /**
     * The corporateMail.
     */
    private String corporateMail;
    
    /**
     * The password.
     */
    private String password;
}
