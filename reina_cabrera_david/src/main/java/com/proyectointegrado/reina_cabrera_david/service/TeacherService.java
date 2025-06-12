package com.proyectointegrado.reina_cabrera_david.service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Service;

import com.proyectointegrado.reina_cabrera_david.bean.Teacher;
import com.proyectointegrado.reina_cabrera_david.constants.ErrorConstants;
import com.proyectointegrado.reina_cabrera_david.entity.ClassEntity;
import com.proyectointegrado.reina_cabrera_david.entity.TeacherEntity;
import com.proyectointegrado.reina_cabrera_david.exceptions.InternalServerException;
import com.proyectointegrado.reina_cabrera_david.repository.ClassRepository;
import com.proyectointegrado.reina_cabrera_david.repository.ReservationRepository;
import com.proyectointegrado.reina_cabrera_david.repository.TeachersRepository;

import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;

/**
 * The Class TeacherService
 */
@Service
@Slf4j
public class TeacherService {

	/** The Teachers Repository */
	private TeachersRepository teachersRepository;
	
	/** The Class Repository */
	private ClassRepository classRepository;
	
	/** The Reservation Repository */
	private ReservationRepository reservationRepository;

	/**
	 * Constructor to inject the required repositories.
	 *
	 * @param teachersRepository   Repository to manage TeacherEntity persistence
	 * @param classRepository      Repository to manage ClassEntity persistence
	 * @param reservationRepository Repository to manage ReservationEntity persistence
	 */
	protected TeacherService(TeachersRepository teachersRepository, ClassRepository classRepository,
			ReservationRepository reservationRepository) {
		this.teachersRepository = teachersRepository;
		this.classRepository = classRepository;
		this.reservationRepository = reservationRepository;
	}

	/**
	 * Saves a new teacher to the database.
	 *
	 * @param request Teacher object containing the information to persist
	 * @throws InternalServerException if a data integrity violation occurs (e.g. duplicate credentials)
	 *                                 or other internal errors
	 */
	public void saveTeacher(Teacher request) {
		log.info("saveTeacher - request: {} ", request.toString());
		try {
			TeacherEntity teacherEntity = mapToTeacherEntity(request);
			teachersRepository.save(teacherEntity);

		} catch (DataIntegrityViolationException e) {
			log.error("saveTeacher - error - {}", e.getMessage());
			throw new InternalServerException(ErrorConstants.CREDENTIALS_ALREADY_REGISTERED, e);
		} catch (Exception e) {
			log.error("saveTeacher - error - {}", e.getMessage());
			throw new InternalServerException(ErrorConstants.INTERNAL_ERROR, e);
		}
	}

	/**
	 * Retrieves all teachers from the database.
	 *
	 * @return List of Teacher objects representing all stored teachers
	 */
	public List<Teacher> getAllTeachers() {
		List<Teacher> teachers = teachersRepository.findAll().stream()
				.map(t -> Teacher.builder().id(t.getId()).name(t.getName()).lastname(t.getLastname()).nif(t.getNif())
						.mail(t.getMail()).address(t.getAddress()).phoneNumber(t.getPhoneNumber())
						.dayOfBirth(t.getDayOfBirth()).build())
				.collect(Collectors.toList());
		return teachers;
	}

	/**
	 * Updates an existing teacher's information.
	 *
	 * @param request Teacher object containing updated information
	 * @throws InternalServerException if there is a data integrity violation (e.g. email or phone already registered)
	 *                                 or other internal errors
	 */
	@Modifying(clearAutomatically = true)
	@Transactional
	public void updateTeacher(Teacher request) {
		log.info("updateTeacher - request: {} ", request.toString());
		try {
			TeacherEntity teacherEntity = mapToTeacherEntity(request);
			teachersRepository.save(teacherEntity);
			teachersRepository.flush();
		} catch (DataIntegrityViolationException e) {
			log.error("updateTeacher - error - {}", e.getMessage());
			throw new InternalServerException(ErrorConstants.MAIL_OR_PHONE_ALREADY_REGISTERED, e);
		} catch (Exception e) {
			log.error("updateTeacher - error - {}", e.getMessage());
			throw new InternalServerException(ErrorConstants.INTERNAL_ERROR, e);
		}
	}

	/**
	 * Deletes a teacher by their ID. Also deletes all associated classes and reservations.
	 *
	 * @param teacherId The ID of the teacher to delete
	 * @throws InternalServerException if the teacher does not exist or any other error occurs
	 */
	@Transactional
	public void deleteTeacher(int teacherId) {
		log.info("deleteTeacher - teacherId: {} ", teacherId);
		try {
			Optional<TeacherEntity> optionalTeacher = teachersRepository.findById(teacherId);

			if (optionalTeacher.isPresent()) {
				TeacherEntity teacherEntity = optionalTeacher.get();
				List<ClassEntity> classes = classRepository.findByTeacherId(teacherEntity.getId());

				for (ClassEntity clazz : classes) {
					reservationRepository.deleteByClassEntityId(clazz.getId());
				}

				classRepository.deleteByTeacherId(teacherEntity.getId());
				teachersRepository.delete(teacherEntity);
			} else {
				throw new InternalServerException(ErrorConstants.TEACHER_NOT_EXISTS);
			}
		} catch (Exception e) {
			log.error("deleteTeacher - error - {}", e.getMessage());
			throw new InternalServerException(e.getMessage(), e);
		}
	}

	/**
	 * Maps a Teacher bean to a TeacherEntity for database persistence.
	 *
	 * @param request Teacher bean to map
	 * @return TeacherEntity object mapped from the bean
	 */
	private TeacherEntity mapToTeacherEntity(Teacher request) {
		return TeacherEntity.builder().id(request.getId() != -1 ? request.getId() : null).name(request.getName())
				.lastname(request.getLastname()).nif(request.getNif()).mail(request.getMail())
				.address(request.getAddress()).phoneNumber(request.getPhoneNumber()).dayOfBirth(request.getDayOfBirth())
				.build();
	}

}
