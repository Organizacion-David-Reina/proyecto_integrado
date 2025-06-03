package com.proyectointegrado.reina_cabrera_david.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.proyectointegrado.reina_cabrera_david.entity.ReservationEntity;

/**
 * The Interface ReservationRepository.
 * Provides CRUD operations and custom queries for ReservationEntity.
 */
public interface ReservationRepository extends JpaRepository<ReservationEntity, Integer> {
	
	/**
	 * Counts the number of reservations for a given class ID.
	 *
	 * @param classId the class ID
	 * @return the number of reservations
	 */
	int countByClassEntityId(int classId);

	/**
	 * Checks if a reservation exists for a given class ID and student ID.
	 *
	 * @param classId the class ID
	 * @param studentId the student ID
	 * @return true if the reservation exists, false otherwise
	 */
	boolean existsByClassEntityIdAndStudentEntityId(int classId, int studentId);

	/**
	 * Deletes all reservations for a given student ID.
	 *
	 * @param studentId the student ID
	 */
	void deleteByStudentEntityId(int studentId);

	/**
	 * Deletes all reservations for a given class ID.
	 *
	 * @param classId the class ID
	 */
	void deleteByClassEntityId(int classId);
	
	/**
	 * Deletes a reservation by class ID and student ID.
	 *
	 * @param classId the class ID
	 * @param studentId the student ID
	 */
	void deleteByClassEntityIdAndStudentEntityId(int classId, int studentId);
	
	/**
	 * Finds all reservations by student ID.
	 *
	 * @param studentId the student ID
	 * @return list of ReservationEntity objects
	 */
	List<ReservationEntity> findByStudentEntityId(int studentId);
}
