package com.proyectointegrado.reina_cabrera_david.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.proyectointegrado.reina_cabrera_david.entity.StudentEntity;

public interface StudentsRepository extends JpaRepository<StudentEntity, Integer>{
	
	boolean existsByNif(String nif);

}
