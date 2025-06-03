package com.proyectointegrado.reina_cabrera_david.bean;

import java.time.LocalDate;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

/**
 * The Class Teacher.
 */
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
@ToString
public class Teacher {

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
     * The mail.
     */
    private String mail;

    /**
     * The nif.
     */
    private String nif;

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
}
