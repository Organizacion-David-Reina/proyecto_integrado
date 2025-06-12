package com.proyectointegrado.reina_cabrera_david.bean;

import java.time.LocalDate;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

/**
 * The Class Student.
 */
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
@ToString
public class Student {

    /**
     * The id.
     */
    private int id;
    
    /**
     * The name.
     */
    private String name;
    
    /**
     * The lastname.
     */
    private String lastname;
    
    /**
     * The nif.
     */
    private String nif;
    
    /**
     * The bonus.
     */
    private Bonus bonus;
    
    /**
     * The phoneNumber.
     */
    private String phoneNumber;

    /**
     * The address.
     */
    private String address;

    /**
     * The dayOfBirth.
     */
    private LocalDate dayOfBirth;
    
	/**The mail */
	private String mail;
}
