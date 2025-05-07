package com.proyectointegrado.reina_cabrera_david.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.proyectointegrado.reina_cabrera_david.entity.RoleEntity;

public interface RoleRepository extends JpaRepository<RoleEntity, Integer>{

	@Query("SELECT r FROM RoleEntity r WHERE r.id = :rol")
	RoleEntity findByRol(@Param("rol") String rol);
}
