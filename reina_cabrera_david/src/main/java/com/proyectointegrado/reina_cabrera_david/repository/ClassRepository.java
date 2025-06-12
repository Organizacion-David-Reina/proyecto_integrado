package com.proyectointegrado.reina_cabrera_david.repository;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.proyectointegrado.reina_cabrera_david.entity.ClassEntity;

/**
 * The Interface ClassRepository.
 * Provides database operations for ClassEntity.
 */
public interface ClassRepository extends JpaRepository<ClassEntity, Integer> {

    /**
     * Finds all ClassEntity instances with their associated style, teacher, and room fetched eagerly.
     *
     * @return the list of ClassEntity with relations loaded
     */
    @Query("""
            SELECT c FROM ClassEntity c
            LEFT JOIN FETCH c.style
            LEFT JOIN FETCH c.teacher
            LEFT JOIN FETCH c.room
            """)
    List<ClassEntity> findAllWithRelations();

    /**
     * Checks if there exists a class that overlaps in time with the specified room and day.
     *
     * @param roomId the room id
     * @param day the day of the class
     * @param startTime the start time of the interval to check
     * @param endTime the end time of the interval to check
     * @return true if such overlapping class exists, false otherwise
     */
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

    /**
     * Checks if there exists a class that overlaps in time with the specified teacher and day.
     *
     * @param teacherId the teacher id
     * @param day the day of the class
     * @param startTime the start time of the interval to check
     * @param endTime the end time of the interval to check
     * @return true if such overlapping class exists, false otherwise
     */
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

    /**
     * Checks if there exists a class that overlaps in time with the specified room and day,
     * excluding a specific class id.
     *
     * @param roomId the room id
     * @param day the day of the class
     * @param startTime the start time of the interval to check
     * @param endTime the end time of the interval to check
     * @param classId the class id to exclude from the check
     * @return true if such overlapping class exists excluding the specified id, false otherwise
     */
    @Query("SELECT CASE WHEN COUNT(c) > 0 THEN true ELSE false END FROM ClassEntity c "
            + "WHERE c.room.id = :roomId AND c.day = :day "
            + "AND ((c.startTime < :endTime AND c.endTime > :startTime)) "
            + "AND c.id <> :classId")
    boolean existsByRoomAndDayAndTimeOverlapExcludingId(int roomId, LocalDate day, LocalTime startTime,
            LocalTime endTime, int classId);

    /**
     * Checks if there exists a class that overlaps in time with the specified teacher and day,
     * excluding a specific class id.
     *
     * @param teacherId the teacher id
     * @param day the day of the class
     * @param startTime the start time of the interval to check
     * @param endTime the end time of the interval to check
     * @param classId the class id to exclude from the check
     * @return true if such overlapping class exists excluding the specified id, false otherwise
     */
    @Query("SELECT CASE WHEN COUNT(c) > 0 THEN true ELSE false END FROM ClassEntity c "
            + "WHERE c.teacher.id = :teacherId AND c.day = :day "
            + "AND ((c.startTime < :endTime AND c.endTime > :startTime)) "
            + "AND c.id <> :classId")
    boolean existsByTeacherAndDayAndTimeOverlapExcludingId(int teacherId, LocalDate day, LocalTime startTime,
            LocalTime endTime, int classId);

    /**
     * Deletes all classes associated with the specified teacher id.
     *
     * @param teacherId the teacher id whose classes are to be deleted
     */
    void deleteByTeacherId(int teacherId);

    /**
     * Finds all classes associated with the specified teacher id.
     *
     * @param teacherId the teacher id
     * @return list of classes for the teacher
     */
    List<ClassEntity> findByTeacherId(int teacherId);
}
