package com.proyectointegrado.reina_cabrera_david.service;

import java.time.LocalTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Service;

import com.proyectointegrado.reina_cabrera_david.bean.DanceClass;
import com.proyectointegrado.reina_cabrera_david.bean.ReservationRequest;
import com.proyectointegrado.reina_cabrera_david.bean.Room;
import com.proyectointegrado.reina_cabrera_david.bean.Style;
import com.proyectointegrado.reina_cabrera_david.bean.Teacher;
import com.proyectointegrado.reina_cabrera_david.constants.ErrorConstants;
import com.proyectointegrado.reina_cabrera_david.entity.ClassEntity;
import com.proyectointegrado.reina_cabrera_david.entity.ReservationEntity;
import com.proyectointegrado.reina_cabrera_david.entity.RoomEntity;
import com.proyectointegrado.reina_cabrera_david.entity.StudentEntity;
import com.proyectointegrado.reina_cabrera_david.entity.StyleEntity;
import com.proyectointegrado.reina_cabrera_david.entity.TeacherEntity;
import com.proyectointegrado.reina_cabrera_david.exceptions.InternalServerException;
import com.proyectointegrado.reina_cabrera_david.repository.ClassRepository;
import com.proyectointegrado.reina_cabrera_david.repository.ReservationRepository;
import com.proyectointegrado.reina_cabrera_david.repository.RoomRepository;
import com.proyectointegrado.reina_cabrera_david.repository.StudentsRepository;
import com.proyectointegrado.reina_cabrera_david.repository.StyleRepository;

import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class ClassService {

	private ClassRepository classRepository;

	private ReservationRepository reservationRepository;

	private StyleRepository styleRepository;

	private RoomRepository roomRepository;

	private StudentsRepository studentsRepository;

	protected ClassService(ClassRepository classRepository, ReservationRepository reservationRepository,
			StyleRepository styleRepository, RoomRepository roomRepository, StudentsRepository studentsRepository) {
		this.classRepository = classRepository;
		this.reservationRepository = reservationRepository;
		this.styleRepository = styleRepository;
		this.roomRepository = roomRepository;
		this.studentsRepository = studentsRepository;
	}

	public void saveClass(DanceClass request) {
		log.info("saveClass - request: {}", request.toString());
		try {
			boolean existsInRoom = classRepository.existsByRoomAndDayAndTimeOverlap(request.getRoom().getId(),
					request.getDay(), LocalTime.parse(request.getStartTime()), LocalTime.parse(request.getEndTime()));

			if (existsInRoom) {
				throw new InternalServerException(ErrorConstants.CLASS_ALREADY_OCUPPIED);
			}

			boolean existsForTeacher = classRepository.existsByTeacherAndDayAndTimeOverlap(request.getTeacher().getId(),
					request.getDay(), LocalTime.parse(request.getStartTime()), LocalTime.parse(request.getEndTime()));

			if (existsForTeacher) {
				throw new InternalServerException(ErrorConstants.TEACHER_NOT_AVAILABLE);
			}

			ClassEntity entity = MapToClassEntity(request);
			classRepository.save(entity);

		} catch (Exception e) {
			log.error("saveClass - error - {}", e.getMessage());
			throw new InternalServerException(e.getMessage(), e);
		}
	}

	public List<DanceClass> getClasses() {
		List<ClassEntity> classes = classRepository.findAllWithRelations();

		return classes.stream().map(c -> {
			int reservationCount = reservationRepository.countByClassEntityId(c.getId());

			return new DanceClass(c.getId(), mapToStyle(c.getStyle()), mapToTeacher(c.getTeacher()),
					mapToRoom(c.getRoom()), reservationCount, c.getLevel(), c.getDay(), c.getStartTime().toString(),
					c.getEndTime().toString());
		}).collect(Collectors.toList());
	}

	public List<Style> getStyles() {
		List<StyleEntity> styles = styleRepository.findAll();

		return styles.stream().map(s -> {
			return mapToStyle(s);
		}).collect(Collectors.toList());
	}

	public List<Room> getRooms() {
		List<RoomEntity> rooms = roomRepository.findAll();

		return rooms.stream().map(r -> {
			return mapToRoom(r);
		}).collect(Collectors.toList());
	}

	@Modifying(clearAutomatically = true)
	@Transactional
	public void updateClass(DanceClass request) {
		log.info("updateClass - request: {} ", request.toString());
		try {
			boolean existsInRoom = classRepository.existsByRoomAndDayAndTimeOverlapExcludingId(
					request.getRoom().getId(), request.getDay(), LocalTime.parse(request.getStartTime()),
					LocalTime.parse(request.getEndTime()), request.getId());

			if (existsInRoom) {
				throw new InternalServerException(ErrorConstants.CLASS_ALREADY_OCUPPIED);
			}

			boolean existsForTeacher = classRepository.existsByTeacherAndDayAndTimeOverlapExcludingId(
					request.getTeacher().getId(), request.getDay(), LocalTime.parse(request.getStartTime()),
					LocalTime.parse(request.getEndTime()), request.getId());

			if (existsForTeacher) {
				throw new InternalServerException(ErrorConstants.TEACHER_NOT_AVAILABLE);
			}

			Optional<ClassEntity> optionalExistingClass = classRepository.findById(request.getId());

			if (optionalExistingClass.isPresent()) {

				ClassEntity existingClass = optionalExistingClass.get();

				existingClass.setStyle(StyleEntity.builder().id(request.getStyle().getId())
						.style(request.getStyle().getStyle()).build());

				existingClass.setTeacher(TeacherEntity.builder().id(request.getTeacher().getId())
						.name(request.getTeacher().getName()).lastname(request.getTeacher().getLastname())
						.mail(request.getTeacher().getMail()).nif(request.getTeacher().getNif()).build());

				existingClass.setRoom(RoomEntity.builder().id(request.getRoom().getId())
						.roomName(request.getRoom().getRoomName()).capacity(request.getRoom().getCapacity()).build());

				existingClass.setLevel(request.getLevel());
				existingClass.setDay(request.getDay());
				existingClass.setStartTime(LocalTime.parse(request.getStartTime()));
				existingClass.setEndTime(LocalTime.parse(request.getEndTime()));

				classRepository.save(existingClass);
				classRepository.flush();
			} else {
				throw new InternalServerException(ErrorConstants.TEACHER_NOT_AVAILABLE);
			}

		} catch (Exception e) {
			log.error("saveClass - error - {}", e.getMessage());
			throw new InternalServerException(e.getMessage(), e);
		}
	}

	public void saveReservation(ReservationRequest request) {
		log.info("saveReservation - request: {} ", request.toString());
		try {
			Optional<StudentEntity> optionalStudent = studentsRepository.findByNif(request.getStudentNif());

			if (optionalStudent.isPresent()) {
				StudentEntity studentEntity = optionalStudent.get();
				ClassEntity classEntity = classRepository.findById(request.getClassId()).get();
				int roomCapacity = classEntity.getRoom().getCapacity();
				int reservationCount = reservationRepository.countByClassEntityId(request.getClassId());

				if (reservationCount >= roomCapacity) {
					throw new InternalServerException(ErrorConstants.CLASS_IS_FULL);
				}

				List<ReservationEntity> studentReservations = reservationRepository
						.findByStudentEntityId(studentEntity.getId());

				for (ReservationEntity existingReservation : studentReservations) {
					ClassEntity existingClass = existingReservation.getClassEntity();

					if (existingClass.getDay().equals(classEntity.getDay())) {
						if (timesOverlap(existingClass.getStartTime(), existingClass.getEndTime(),
								classEntity.getStartTime(), classEntity.getEndTime())) {
							throw new InternalServerException(
									ErrorConstants.STUDENT_ALREADY_HAS_RESERVATION_IN_TIME_SLOT);
						}
					}
				}
				
				boolean alreadyReserved = reservationRepository
						.existsByClassEntityIdAndStudentEntityId(classEntity.getId(), studentEntity.getId());

				if (alreadyReserved) {
					throw new InternalServerException(ErrorConstants.STUDENT_ALREADY_RESERVED);
				}

				ReservationEntity reservationEntity = ReservationEntity.builder().classEntity(classEntity)
						.studentEntity(studentEntity).build();

				reservationRepository.save(reservationEntity);

			} else {
				throw new InternalServerException(ErrorConstants.STUDENT_NOT_EXISTS);
			}
		} catch (Exception e) {
			log.error("saveReservation - error - {}", e.getMessage());
			throw new InternalServerException(e.getMessage(), e);
		}
	}

	@Transactional
	public void deleteClass(int classId) {
		log.info("deleteClass - classId: {} ", classId);
		try {
			Optional<ClassEntity> optionalClass = classRepository.findById(classId);

			if (optionalClass.isPresent()) {
				ClassEntity classEntity = optionalClass.get();
				reservationRepository.deleteByClassEntityId(classEntity.getId());
				classRepository.delete(classEntity);
			} else {
				throw new InternalServerException(ErrorConstants.CLASS_NOT_EXISTS);
			}
		} catch (Exception e) {
			log.error("deleteClass - error - {}", e.getMessage());
			throw new InternalServerException(e.getMessage(), e);
		}
	}

	@Transactional
	public void deleteReservation(int studentId, int classId) {
		log.info("deleteClass - classId: {} ", classId);
		try {
			Optional<ClassEntity> optionalClass = classRepository.findById(classId);
			Optional<StudentEntity> optionalStudent = studentsRepository.findById(studentId);

			if (optionalClass.isPresent() && optionalStudent.isPresent()) {
				reservationRepository.deleteByClassEntityIdAndStudentEntityId(classId, studentId);
			} else {
				throw new InternalServerException(ErrorConstants.CLASS_AND_STUDENT_NOT_EXIST);
			}
		} catch (Exception e) {
			log.error("deleteClass - error - {}", e.getMessage());
			throw new InternalServerException(e.getMessage(), e);
		}
	}

	private Room mapToRoom(RoomEntity r) {
		return Room.builder().id(r.getId()).roomName(r.getRoomName()).capacity(r.getCapacity()).build();
	}

	private Teacher mapToTeacher(TeacherEntity s) {
		return Teacher.builder().id(s.getId()).name(s.getName()).lastname(s.getLastname()).mail(s.getMail())
				.nif(s.getNif()).build();
	}

	private Style mapToStyle(StyleEntity c) {
		return Style.builder().id(c.getId()).style(c.getStyle()).build();
	}

	private ClassEntity MapToClassEntity(DanceClass request) {
		return ClassEntity.builder().id(request.getId() != -1 ? request.getId() : null)
				.style(StyleEntity.builder().id(request.getStyle().getId()).style(request.getStyle().getStyle())
						.build())
				.teacher(TeacherEntity.builder().id(request.getTeacher().getId()).name(request.getTeacher().getName())
						.lastname(request.getTeacher().getLastname()).mail(request.getTeacher().getMail())
						.nif(request.getTeacher().getNif()).build())
				.room(RoomEntity.builder().id(request.getRoom().getId()).roomName(request.getRoom().getRoomName())
						.capacity(request.getRoom().getCapacity()).build())
				.level(request.getLevel()).day(request.getDay()).startTime(LocalTime.parse(request.getStartTime()))
				.endTime(LocalTime.parse(request.getEndTime())).build();
	}
	
	private boolean timesOverlap(LocalTime start1, LocalTime end1, LocalTime start2, LocalTime end2) {
	    return !start1.isAfter(end2) && !start2.isAfter(end1);
	}
}
