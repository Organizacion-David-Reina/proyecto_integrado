package com.proyectointegrado.reina_cabrera_david.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.proyectointegrado.reina_cabrera_david.entity.StudentEntity;

public interface StudentsRepository extends JpaRepository<StudentEntity, Integer> {

	boolean existsByNif(String nif);

	Optional<StudentEntity> findByNif(String nif);

	@Query("""
			    SELECT r.studentEntity FROM ReservationEntity r
			    WHERE r.classEntity.id = :classId
			""")
	List<StudentEntity> findStudentsByClassId(@Param("classId") int classId);

}
