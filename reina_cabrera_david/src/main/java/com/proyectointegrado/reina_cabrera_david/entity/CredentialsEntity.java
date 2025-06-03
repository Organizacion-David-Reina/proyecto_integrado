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

/**
 * The Class CredentialsEntity.
 * Represents the credentials table in the database.
 */
@Entity
@Table(name = "credentials")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class CredentialsEntity {

    /** The userId. */
    @Id
    @Column(name = "user_id")
    private Integer userId;

    /** The nif. */
    private String nif;

    /** The corporateMail. */
    @Column(name = "corporate_mail")
    private String corporateMail;

    /** The password. */
    private String password;
}
