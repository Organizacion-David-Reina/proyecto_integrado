package com.proyectointegrado.reina_cabrera_david.bean;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

/**
 * The Class Room.
 */
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
@ToString
public class Room {

    /**
     * The id.
     */
    private int id;
    
    /**
     * The roomName.
     */
    private String roomName;
    
    /**
     * The capacity.
     */
    private int capacity;
}
