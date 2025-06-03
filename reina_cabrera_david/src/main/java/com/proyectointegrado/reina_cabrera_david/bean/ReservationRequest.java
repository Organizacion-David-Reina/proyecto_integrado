package com.proyectointegrado.reina_cabrera_david.bean;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

/**
 * The Class ReservationRequest.
 */
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
@ToString
public class ReservationRequest {

    /**
     * The classId.
     */
    private int classId;
    
    /**
     * The studentNif.
     */
    private String studentNif;
}
