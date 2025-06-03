package com.proyectointegrado.reina_cabrera_david.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.proyectointegrado.reina_cabrera_david.entity.RoomEntity;

/**
 * The Interface RoomRepository.
 * Provides CRUD operations for RoomEntity.
 */
public interface RoomRepository extends JpaRepository<RoomEntity, Integer> {

}
