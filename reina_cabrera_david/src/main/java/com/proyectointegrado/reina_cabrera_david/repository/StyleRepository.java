package com.proyectointegrado.reina_cabrera_david.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.proyectointegrado.reina_cabrera_david.entity.StyleEntity;

/**
 * The Interface StyleRepository.
 * Provides CRUD operations for StyleEntity.
 */
public interface StyleRepository extends JpaRepository<StyleEntity, Integer> {

}
