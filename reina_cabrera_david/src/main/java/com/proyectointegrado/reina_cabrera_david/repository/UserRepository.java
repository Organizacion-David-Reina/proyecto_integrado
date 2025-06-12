package com.proyectointegrado.reina_cabrera_david.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.proyectointegrado.reina_cabrera_david.entity.UserEntity;

/**
 * The Interface UserRepository.
 * Provides database operations for UserEntity.
 */
public interface UserRepository extends JpaRepository<UserEntity, Integer> {
	
	/**
	 * Gets all users except those with role id 1 (Directors).
	 *
	 * @return the list of UserEntity excluding directors
	 */
	@Query("SELECT u FROM UserEntity u WHERE u.rol.id != 1")
	List<UserEntity> getAllUsersExceptDirectors();
	
	/**
	 * Finds a user by their id.
	 *
	 * @param id the user id
	 * @return the Integer representing the user id (Note: usually you'd return the entity or Optional)
	 */
	@Query("SELECT u FROM UserEntity u WHERE u.id = :id")
	Integer findUserById(@Param("id") Integer id);
	
}
