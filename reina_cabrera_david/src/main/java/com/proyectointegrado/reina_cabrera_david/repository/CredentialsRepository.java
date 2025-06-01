package com.proyectointegrado.reina_cabrera_david.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.proyectointegrado.reina_cabrera_david.entity.CredentialsEntity;

public interface CredentialsRepository extends JpaRepository<CredentialsEntity, Integer> {

	Optional<CredentialsEntity> findByCorporateMail(String corporateMail);
	
	@Query("SELECT c.userId FROM CredentialsEntity c WHERE c.corporateMail = :corporateMail AND c.password = :password")
	Integer authUser(@Param("corporateMail") String corporateMail, @Param("password") String password);

	@Query("SELECT c.corporateMail FROM CredentialsEntity c WHERE c.userId = :userId")
	String findMailById(@Param("userId") Integer userId);

	boolean existsByNif(String nif);

	boolean existsByCorporateMail(String corporateMail);
	
	@Modifying
	@Query("UPDATE CredentialsEntity c SET c.corporateMail = :newMail WHERE c.userId = :userId")
	void updateCorporateMail(@Param("userId") Integer userId, @Param("newMail") String newMail);
}
