package com.proyectointegrado.reina_cabrera_david.bean;

import java.time.LocalDate;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
@ToString
public class DanceClass {

	private Integer id;
	
	private Style style;
	
	private Teacher teacher;
	
	private Room room;
	
	private int reservations;
	
	private String level;
	
	private LocalDate day;
	
	private String startTime;
	
	private String endTime;
}
