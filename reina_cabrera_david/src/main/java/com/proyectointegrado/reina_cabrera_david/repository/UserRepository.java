package com.proyectointegrado.reina_cabrera_david.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.proyectointegrado.reina_cabrera_david.entity.UserEntity;

public interface UserRepository extends JpaRepository<UserEntity, Integer> {
	
	@Query("SELECT u FROM UserEntity u WHERE u.rol.id != 1")
	List<UserEntity> getAllUsersExceptDirectors();
	
	@Query("SELECT u FROM UserEntity u WHERE u.id = :id")
	Integer findUserById(@Param("id") Integer id);
	
	
}
