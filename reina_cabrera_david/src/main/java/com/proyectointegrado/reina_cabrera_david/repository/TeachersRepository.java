package com.proyectointegrado.reina_cabrera_david.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.proyectointegrado.reina_cabrera_david.entity.TeacherEntity;

public interface TeachersRepository extends JpaRepository<TeacherEntity, Integer>{

	boolean existsByNif(String nif);
	
	boolean existsByMail(String mail);
}
