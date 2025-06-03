package com.proyectointegrado.reina_cabrera_david.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.proyectointegrado.reina_cabrera_david.entity.TeacherEntity;

/**
 * The Interface TeachersRepository.
 * Repository interface for TeacherEntity with basic CRUD operations and custom existence checks.
 */
public interface TeachersRepository extends JpaRepository<TeacherEntity, Integer> {

    /**
     * Checks if a teacher exists by their NIF.
     *
     * @param nif the teacher's NIF
     * @return true if a teacher with the given NIF exists, false otherwise
     */
    boolean existsByNif(String nif);

    /**
     * Checks if a teacher exists by their mail.
     *
     * @param mail the teacher's mail
     * @return true if a teacher with the given mail exists, false otherwise
     */
    boolean existsByMail(String mail);
}
