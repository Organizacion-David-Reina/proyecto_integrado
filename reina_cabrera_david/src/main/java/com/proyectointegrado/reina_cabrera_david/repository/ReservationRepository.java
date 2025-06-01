package com.proyectointegrado.reina_cabrera_david.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.proyectointegrado.reina_cabrera_david.entity.ReservationEntity;

public interface ReservationRepository extends JpaRepository<ReservationEntity, Integer>{
	
	int countByClassEntityId(int classId);

	boolean existsByClassEntityIdAndStudentEntityId(int classId, int studentId);

	void deleteByStudentEntityId(int studentId);

	void deleteByClassEntityId(int classId);
	
	void deleteByClassEntityIdAndStudentEntityId(int classId, int studentId);
}
