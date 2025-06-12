package com.proyectointegrado.reina_cabrera_david.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * The Class RoomEntity.
 */
@Entity
@Table(name = "rooms")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class RoomEntity {

    /**
     * The id.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    /**
     * The capacity.
     */
    private int capacity;

    /**
     * The roomName.
     */
    @Column(name = "room_name")
    private String roomName;
}
