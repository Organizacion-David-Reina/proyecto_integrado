package com.proyectointegrado.reina_cabrera_david.bean;

import java.time.LocalDate;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

/**
 * The Class DanceClass.
 */
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
@ToString
public class DanceClass {

    /**
     * The id.
     */
    private Integer id;
    
    /**
     * The style.
     */
    private Style style;
    
    /**
     * The teacher.
     */
    private Teacher teacher;
    
    /**
     * The room.
     */
    private Room room;
    
    /**
     * The reservations.
     */
    private int reservations;
    
    /**
     * The level.
     */
    private String level;
    
    /**
     * The day.
     */
    private LocalDate day;
    
    /**
     * The startTime.
     */
    private String startTime;
    
    /**
     * The endTime.
     */
    private String endTime;
}
