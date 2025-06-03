package com.proyectointegrado.reina_cabrera_david.entity;

import java.time.LocalDate;
import java.time.LocalTime;

import jakarta.persistence.Column;
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
 * The Class ClassEntity.
 * Represents a dance class entity mapped to the "classes" table in the database.
 */
@Entity
@Table(name = "classes")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class ClassEntity {
	
	/**
	 * The id.
	 * Primary key of the class entity.
	 */
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
	
	/**
	 * The style.
	 * The style of the dance class, linked to the StyleEntity.
	 */
	@ManyToOne
	@JoinColumn(name = "style_id")
	private StyleEntity style;
	
	/**
	 * The teacher.
	 * The teacher assigned to the class, linked to the TeacherEntity.
	 */
	@ManyToOne
	@JoinColumn(name = "teacher_id")
	private TeacherEntity teacher;
	
	/**
	 * The room.
	 * The room where the class takes place, linked to the RoomEntity.
	 */
	@ManyToOne
	@JoinColumn(name = "room_id")
	private RoomEntity room;
	
	/**
	 * The level.
	 * The difficulty level of the class.
	 */
	private String level;
	
	/**
	 * The day.
	 * The date when the class is held.
	 */
	private LocalDate day;

	/**
	 * The start time.
	 * The time when the class begins.
	 */
	@Column(name = "start_time")
	private LocalTime startTime;
	
	/**
	 * The end time.
	 * The time when the class ends.
	 */
	@Column(name = "end_time")
	private LocalTime endTime;
}
