package com.proyectointegrado.reina_cabrera_david.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.proyectointegrado.reina_cabrera_david.entity.UserEntity;

public interface UserRepository extends JpaRepository<UserEntity, Integer> {

	@Query("SELECT u FROM UserEntity u WHERE u.corporateMail = :corporateMail AND u.password = :password")
	UserEntity validateLogin(@Param("corporateMail") String mail, @Param("password") String password);

}
