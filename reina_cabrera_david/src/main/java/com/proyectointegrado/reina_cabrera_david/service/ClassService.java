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
import com.proyectointegrado.reina_cabrera_david.constants.Constants;
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

/**
 * The Class ClassService
 */
@Service
@Slf4j
public class ClassService {

	/** The Class Repository */
	private ClassRepository classRepository;

	/** The Reservation Repository */
	private ReservationRepository reservationRepository;

	/** The Style Repository */
	private StyleRepository styleRepository;

	/** The Room Repository */
	private RoomRepository roomRepository;

	/** The Students Repository */
	private StudentsRepository studentsRepository;
	
	/** The Email Service */
	private EmailService emailService;

	/**
	 * Constructor for ClassService with required repositories.
	 *
	 * @param classRepository       repository for dance classes
	 * @param reservationRepository repository for reservations
	 * @param styleRepository       repository for dance styles
	 * @param roomRepository        repository for rooms
	 * @param studentsRepository    repository for students
	 * @param emailService 			Service to send emails
	 */
	protected ClassService(ClassRepository classRepository, ReservationRepository reservationRepository,
			StyleRepository styleRepository, RoomRepository roomRepository, StudentsRepository studentsRepository,
			EmailService emailService) {
		this.classRepository = classRepository;
		this.reservationRepository = reservationRepository;
		this.styleRepository = styleRepository;
		this.roomRepository = roomRepository;
		this.studentsRepository = studentsRepository;
		this.emailService = emailService;
	}

	/**
	 * Saves a new dance class after validating that the room and teacher are
	 * available for the specified time and day.
	 *
	 * @param request the dance class data to save
	 * @throws InternalServerException if the room or teacher is not available
	 */
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


	/**
	 * Retrieves all dance classes including related reservation count, style,
	 * teacher, and room information.
	 *
	 * @return list of dance classes
	 */
	public List<DanceClass> getClasses() {
		List<ClassEntity> classes = classRepository.findAllWithRelations();

		return classes.stream().map(c -> {
			int reservationCount = reservationRepository.countByClassEntityId(c.getId());

			return new DanceClass(c.getId(), mapToStyle(c.getStyle()), mapToTeacher(c.getTeacher()),
					mapToRoom(c.getRoom()), reservationCount, c.getLevel(), c.getDay(), c.getStartTime().toString(),
					c.getEndTime().toString());
		}).collect(Collectors.toList());
	}

	/**
	 * Retrieves all available dance styles.
	 *
	 * @return list of styles
	 */
	public List<Style> getStyles() {
		List<StyleEntity> styles = styleRepository.findAll();

		return styles.stream().map(s -> {
			return mapToStyle(s);
		}).collect(Collectors.toList());
	}

	/**
	 * Retrieves all available rooms.
	 *
	 * @return list of rooms
	 */
	public List<Room> getRooms() {
		List<RoomEntity> rooms = roomRepository.findAll();

		return rooms.stream().map(r -> {
			return mapToRoom(r);
		}).collect(Collectors.toList());
	}

	/**
	 * Updates an existing dance class after validating room and teacher availability
	 * excluding the current class.
	 *
	 * @param request the dance class data to update
	 * @throws InternalServerException if the room or teacher is not available or
	 *                                 class not found
	 */
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

	/**
	 * Saves a new reservation for a student in a class if capacity and scheduling
	 * constraints allow.
	 *
	 * @param request reservation request containing student NIF and class ID
	 * @throws InternalServerException if student does not exist, class is full,
	 *                                 student already reserved or has conflicting
	 *                                 schedule
	 */
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
				String studentName = studentEntity.getName();
				String classStyle = classEntity.getStyle().getStyle();
				String classDay = String.valueOf(classEntity.getDay());
				String classStartTime = String.valueOf(classEntity.getStartTime());
				String personalizedHtml = String.format(
					    Constants.RESERVATION_SAVED_HTML_MAIL_TEMPLATE,
					    studentName,
					    classStyle,
					    classDay,
					    classStartTime
					);
				emailService.sendEmail(studentEntity.getMail(), "RESERVA DE CLASE", personalizedHtml);
			} else {
				throw new InternalServerException(ErrorConstants.STUDENT_NOT_EXISTS);
			}
		} catch (Exception e) {
			log.error("saveReservation - error - {}", e.getMessage());
			throw new InternalServerException(e.getMessage(), e);
		}
	}

	/**
	 * Deletes a dance class and all related reservations.
	 *
	 * @param classId the ID of the class to delete
	 * @throws InternalServerException if the class does not exist
	 */
	@Transactional
	public void deleteClass(int classId) {
	    log.info("deleteClass - classId: {} ", classId);
	    try {
	        Optional<ClassEntity> optionalClass = classRepository.findById(classId);

	        if (optionalClass.isPresent()) {
	            ClassEntity classEntity = optionalClass.get();
	            List<StudentEntity> studentsReserved = reservationRepository.findStudentsByClassId(classId);

	            for (StudentEntity student : studentsReserved) {
	                String email = student.getMail();
	                String studentName = student.getName();
	                String classStyle = classEntity.getStyle().getStyle();
	                String classDay = classEntity.getDay().toString();
	                String startTime = classEntity.getStartTime().toString();

	                String emailContent = String.format(
	                    Constants.CLASS_CANCELLED_HTML_MAIL_TEMPLATE,
	                    studentName,
	                    classStyle,
	                    classDay,
	                    startTime
	                );

	                emailService.sendEmail(
	                    email,
	                    "CANCELACIÓN DE RESERVA",
	                    emailContent
	                );
	            }

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


	/**
	 * Deletes a reservation for a student in a specific class.
	 *
	 * @param studentId the ID of the student
	 * @param classId   the ID of the class
	 * @throws InternalServerException if either the class or student does not exist
	 */
	@Transactional
	public void deleteReservation(int studentId, int classId) {
		log.info("deleteClass - classId: {} ", classId);
		try {
			Optional<ClassEntity> optionalClass = classRepository.findById(classId);
			Optional<StudentEntity> optionalStudent = studentsRepository.findById(studentId);

			if (optionalClass.isPresent() && optionalStudent.isPresent()) {
				StudentEntity studentEntity = optionalStudent.get();
				ClassEntity classEntity = optionalClass.get();
				reservationRepository.deleteByClassEntityIdAndStudentEntityId(classId, studentId);
				String studentName = studentEntity.getName();
				String classStyle = classEntity.getStyle().getStyle();
				String classDay = String.valueOf(classEntity.getDay());
				String classStartTime = String.valueOf(classEntity.getStartTime());
				String personalizedHtml = String.format(
					    Constants.RESERVATION_CANCELLED_HTML_MAIL_TEMPLATE,
					    studentName,
					    classStyle,
					    classDay,
					    classStartTime
					);
				emailService.sendEmail(studentEntity.getMail(), "RESERVA DE CLASE", personalizedHtml);
			} else {
				throw new InternalServerException(ErrorConstants.CLASS_AND_STUDENT_NOT_EXIST);
			}
		} catch (Exception e) {
			log.error("deleteClass - error - {}", e.getMessage());
			throw new InternalServerException(e.getMessage(), e);
		}
	}

	/**
	 * Maps a RoomEntity to a Room bean.
	 *
	 * @param r the RoomEntity
	 * @return the mapped Room
	 */
	private Room mapToRoom(RoomEntity r) {
		return Room.builder().id(r.getId()).roomName(r.getRoomName()).capacity(r.getCapacity()).build();
	}

	/**
	 * Maps a TeacherEntity to a Teacher bean.
	 *
	 * @param s the TeacherEntity
	 * @return the mapped Teacher
	 */
	private Teacher mapToTeacher(TeacherEntity s) {
		return Teacher.builder().id(s.getId()).name(s.getName()).lastname(s.getLastname()).mail(s.getMail())
				.nif(s.getNif()).build();
	}

	/**
	 * Maps a StyleEntity to a Style bean.
	 *
	 * @param c the StyleEntity
	 * @return the mapped Style
	 */
	private Style mapToStyle(StyleEntity c) {
		return Style.builder().id(c.getId()).style(c.getStyle()).build();
	}

	/**
	 * Maps a DanceClass bean to a ClassEntity for persistence.
	 *
	 * @param request the DanceClass bean
	 * @return the mapped ClassEntity
	 */
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
	
	/**
	 * Checks if two time intervals overlap.
	 *
	 * @param start1 start time of the first interval
	 * @param end1   end time of the first interval
	 * @param start2 start time of the second interval
	 * @param end2   end time of the second interval
	 * @return true if the intervals overlap, false otherwise
	 */
	private boolean timesOverlap(LocalTime start1, LocalTime end1, LocalTime start2, LocalTime end2) {
	    return !start1.isAfter(end2) && !start2.isAfter(end1);
	}
}
