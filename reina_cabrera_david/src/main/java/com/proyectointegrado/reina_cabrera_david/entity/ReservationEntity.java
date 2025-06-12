package com.proyectointegrado.reina_cabrera_david.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * The Class ReservationEntity.
 * Represents a reservation linking a student to a dance class.
 */
@Entity
@Table(name = "reservations")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class ReservationEntity {

    /**
     * The id.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    /**
     * The classEntity.
     */
    @ManyToOne
    @JoinColumn(name = "class_id")
    private ClassEntity classEntity;

    /**
     * The studentEntity.
     */
    @ManyToOne
    @JoinColumn(name = "student_id")
    private StudentEntity studentEntity;
}
