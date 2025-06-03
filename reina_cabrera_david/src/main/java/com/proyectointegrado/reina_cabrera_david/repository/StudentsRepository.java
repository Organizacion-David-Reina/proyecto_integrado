package com.proyectointegrado.reina_cabrera_david.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.proyectointegrado.reina_cabrera_david.entity.StudentEntity;

/**
 * The Interface StudentsRepository.
 * Repository interface for StudentEntity providing CRUD operations and custom queries.
 */
public interface StudentsRepository extends JpaRepository<StudentEntity, Integer> {

    /**
     * Checks if a student exists by their NIF.
     *
     * @param nif the NIF of the student
     * @return true if a student with the given NIF exists, false otherwise
     */
    boolean existsByNif(String nif);

    /**
     * Finds a student by their NIF.
     *
     * @param nif the NIF of the student
     * @return an Optional containing the found StudentEntity, or empty if not found
     */
    Optional<StudentEntity> findByNif(String nif);

    /**
     * Finds students associated with a specific class ID.
     *
     * @param classId the ID of the class
     * @return a list of StudentEntity objects enrolled in the specified class
     */
    @Query("""
            SELECT r.studentEntity FROM ReservationEntity r
            WHERE r.classEntity.id = :classId
            """)
    List<StudentEntity> findStudentsByClassId(@Param("classId") int classId);

}
