package com.proyectointegrado.reina_cabrera_david.repository;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.proyectointegrado.reina_cabrera_david.entity.ClassEntity;

public interface ClassRepository extends JpaRepository<ClassEntity, Integer> {

	@Query("""
			SELECT c FROM ClassEntity c
			LEFT JOIN FETCH c.style
			LEFT JOIN FETCH c.teacher
			LEFT JOIN FETCH c.room
			""")
	List<ClassEntity> findAllWithRelations();

	@Query("""
			    SELECT CASE WHEN COUNT(c) > 0 THEN true ELSE false END
			    FROM ClassEntity c
			    WHERE FUNCTION('DATE', c.day) = FUNCTION('DATE', :day)
			      AND c.room.id = :roomId
			      AND c.startTime < :endTime
			      AND c.endTime > :startTime
			""")
	boolean existsByRoomAndDayAndTimeOverlap(@Param("roomId") int roomId, @Param("day") LocalDate day,
			@Param("startTime") LocalTime startTime, @Param("endTime") LocalTime endTime);

	@Query("""
			    SELECT CASE WHEN COUNT(c) > 0 THEN true ELSE false END
			    FROM ClassEntity c
			    WHERE FUNCTION('DATE', c.day) = FUNCTION('DATE', :day)
			      AND c.teacher.id = :teacherId
			      AND c.startTime < :endTime
			      AND c.endTime > :startTime
			""")
	boolean existsByTeacherAndDayAndTimeOverlap(@Param("teacherId") int teacherId, @Param("day") LocalDate day,
			@Param("startTime") LocalTime startTime, @Param("endTime") LocalTime endTime);

	@Query("SELECT CASE WHEN COUNT(c) > 0 THEN true ELSE false END FROM ClassEntity c "
			+ "WHERE c.room.id = :roomId AND c.day = :day "
			+ "AND ((c.startTime < :endTime AND c.endTime > :startTime)) " + "AND c.id <> :classId")
	boolean existsByRoomAndDayAndTimeOverlapExcludingId(int roomId, LocalDate day, LocalTime startTime,
			LocalTime endTime, int classId);

	@Query("SELECT CASE WHEN COUNT(c) > 0 THEN true ELSE false END FROM ClassEntity c "
			+ "WHERE c.teacher.id = :teacherId AND c.day = :day "
			+ "AND ((c.startTime < :endTime AND c.endTime > :startTime)) " + "AND c.id <> :classId")
	boolean existsByTeacherAndDayAndTimeOverlapExcludingId(int teacherId, LocalDate day, LocalTime startTime,
			LocalTime endTime, int classId);

	
	void deleteByTeacherId(int teacherId);

	List<ClassEntity> findByTeacherId(int teacherId);
}
