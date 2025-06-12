package com.proyectointegrado.reina_cabrera_david.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.proyectointegrado.reina_cabrera_david.entity.CredentialsEntity;

/**
 * The Interface CredentialsRepository.
 * Repository interface for CredentialsEntity providing data access operations.
 */
public interface CredentialsRepository extends JpaRepository<CredentialsEntity, Integer> {

    /**
     * Finds a CredentialsEntity by corporate mail.
     *
     * @param corporateMail the corporate mail
     * @return an Optional containing the CredentialsEntity if found, empty otherwise
     */
    Optional<CredentialsEntity> findByCorporateMail(String corporateMail);
    
    /**
     * Authenticates a user by corporate mail and password.
     *
     * @param corporateMail the corporate mail
     * @param password the password
     * @return the user ID if credentials match, null otherwise
     */
    @Query("SELECT c.userId FROM CredentialsEntity c WHERE c.corporateMail = :corporateMail AND c.password = :password")
    Integer authUser(@Param("corporateMail") String corporateMail, @Param("password") String password);

    /**
     * Finds corporate mail by user ID.
     *
     * @param userId the user ID
     * @return the corporate mail associated with the user ID
     */
    @Query("SELECT c.corporateMail FROM CredentialsEntity c WHERE c.userId = :userId")
    String findMailById(@Param("userId") Integer userId);

    /**
     * Checks if a credential exists by NIF.
     *
     * @param nif the NIF
     * @return true if exists, false otherwise
     */
    boolean existsByNif(String nif);

    /**
     * Checks if a credential exists by corporate mail.
     *
     * @param corporateMail the corporate mail
     * @return true if exists, false otherwise
     */
    boolean existsByCorporateMail(String corporateMail);
    
    /**
     * Updates the corporate mail for a given user ID.
     *
     * @param userId the user ID
     * @param newMail the new corporate mail
     */
    @Modifying
    @Query("UPDATE CredentialsEntity c SET c.corporateMail = :newMail WHERE c.userId = :userId")
    void updateCorporateMail(@Param("userId") Integer userId, @Param("newMail") String newMail);
}
