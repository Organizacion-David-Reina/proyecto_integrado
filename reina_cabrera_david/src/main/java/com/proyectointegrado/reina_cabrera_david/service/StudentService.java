package com.proyectointegrado.reina_cabrera_david.service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Service;

import com.proyectointegrado.reina_cabrera_david.bean.Bonus;
import com.proyectointegrado.reina_cabrera_david.bean.Student;
import com.proyectointegrado.reina_cabrera_david.constants.ErrorConstants;
import com.proyectointegrado.reina_cabrera_david.entity.BonusEntity;
import com.proyectointegrado.reina_cabrera_david.entity.StudentEntity;
import com.proyectointegrado.reina_cabrera_david.exceptions.InternalServerException;
import com.proyectointegrado.reina_cabrera_david.repository.ReservationRepository;
import com.proyectointegrado.reina_cabrera_david.repository.StudentsRepository;

import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;

/**
 * The Class StudentService
 */
@Service
@Slf4j
public class StudentService {

	/** The Students Repository */
	private StudentsRepository studentsRepository;
	
	/** The Reservation Repository */
	private ReservationRepository reservationRepository;

	/**
	 * Constructor injecting required repositories.
	 * 
	 * @param studentsRepository    repository for student entities
	 * @param reservationRepository repository for reservation entities
	 */
	protected StudentService(StudentsRepository studentsRepository, ReservationRepository reservationRepository) {
		this.studentsRepository = studentsRepository;
		this.reservationRepository = reservationRepository;
	}

	/**
	 * Saves a new student in the database.
	 * 
	 * @param request the student data to save
	 * @throws InternalServerException if there is a data integrity violation or
	 *                                 internal error
	 */
	public void saveStudent(Student request) {
		log.info("saveStudent - request: {} ", request.toString());
		try {
			BonusEntity bonusEntity = mapToBonusEntity(request);
			StudentEntity studentEntity = mapToStudentEntity(request, bonusEntity);
			studentsRepository.save(studentEntity);

		} catch (DataIntegrityViolationException e) {
			log.error("saveStudent - error - {}", e.getMessage());
			throw new InternalServerException(ErrorConstants.CREDENTIALS_ALREADY_REGISTERED, e);
		} catch (Exception e) {
			log.error("saveStudent - error - {}", e.getMessage());
			throw new InternalServerException(ErrorConstants.INTERNAL_ERROR, e);
		}
	}

	/**
	 * Retrieves all students with their associated bonus information.
	 * 
	 * @return list of students
	 */
	public List<Student> getAllStudents() {
		List<Student> students = studentsRepository
				.findAll().stream().map(s -> Student.builder().id(s.getId()).name(s.getName()).lastname(s.getLastname())
						.nif(s.getNif()).address(s.getAddress()).phoneNumber(s.getPhoneNumber())
						.dayOfBirth(s.getDayOfBirth()).bonus(Bonus.builder().id(s.getBonus().getId())
								.bondType(s.getBonus().getBondType()).price(s.getBonus().getPrice()).build())
						.build())
				.collect(Collectors.toList());
		return students;
	}

	/**
	 * Updates an existing student's data.
	 * 
	 * @param request the student data to update
	 * @throws InternalServerException if there is a data integrity violation or
	 *                                 internal error
	 */
	@Modifying(clearAutomatically = true)
	@Transactional
	public void updateStudent(Student request) {
		log.info("updateStudent - user: {} ", request.toString());
		try {
			BonusEntity bonusEntity = mapToBonusEntity(request);
			StudentEntity studentEntity = mapToStudentEntity(request, bonusEntity);
			studentsRepository.save(studentEntity);
			studentsRepository.flush();
		} catch (DataIntegrityViolationException e) {
			log.error("updateStudent - error - {}", e.getMessage());
			throw new InternalServerException(ErrorConstants.CREDENTIALS_ALREADY_REGISTERED, e);
		} catch (Exception e) {
			log.error("updateStudent - error - {}", e.getMessage());
			throw new InternalServerException(ErrorConstants.INTERNAL_ERROR, e);
		}
	}

	/**
	 * Retrieves all students enrolled in a specific class by its ID.
	 * 
	 * @param classId the ID of the class
	 * @return list of students enrolled in the class
	 */
	public List<Student> getStudentsByClassId(int classId) {
		List<Student> students = studentsRepository
				.findStudentsByClassId(classId).stream().map(s -> Student.builder().id(s.getId()).name(s.getName())
						.lastname(s.getLastname()).nif(s.getNif()).bonus(Bonus.builder().id(s.getBonus().getId())
								.bondType(s.getBonus().getBondType()).price(s.getBonus().getPrice()).build())
						.build())
				.collect(Collectors.toList());
		return students;
	}

	/**
	 * Deletes a student and all their reservations.
	 * 
	 * @param studentId the ID of the student to delete
	 * @throws InternalServerException if the student does not exist or on other
	 *                                 errors
	 */
	@Transactional
	public void deleteStudent(int studentId) {
		log.info("deleteStudent - studentId: {} ", studentId);
		try {
			Optional<StudentEntity> optionalStudent = studentsRepository.findById(studentId);

			if (optionalStudent.isPresent()) {
				StudentEntity studentEntity = optionalStudent.get();
				reservationRepository.deleteByStudentEntityId(studentEntity.getId());
				studentsRepository.delete(studentEntity);
			} else {
				throw new InternalServerException(ErrorConstants.STUDENT_NOT_EXISTS);
			}
		} catch (Exception e) {
			log.error("deleteStudent - error - {}", e.getMessage());
			throw new InternalServerException(e.getMessage(), e);
		}
	}

	/**
	 * Maps a Student bean to a StudentEntity for persistence.
	 * 
	 * @param request     the Student bean
	 * @param bonusEntity the BonusEntity associated with the student
	 * @return the mapped StudentEntity
	 */
	private StudentEntity mapToStudentEntity(Student request, BonusEntity bonusEntity) {
		return StudentEntity.builder().id(request.getId() != -1 ? request.getId() : null).name(request.getName())
				.lastname(request.getLastname()).nif(request.getNif()).address(request.getAddress())
				.phoneNumber(request.getPhoneNumber()).dayOfBirth(request.getDayOfBirth()).bonus(bonusEntity).build();
	}

	/**
	 * Maps the Bonus information from a Student bean to a BonusEntity.
	 * 
	 * @param request the Student bean
	 * @return the mapped BonusEntity
	 */
	private BonusEntity mapToBonusEntity(Student request) {
		return BonusEntity.builder().id(request.getBonus().getId()).bondType(request.getBonus().getBondType())
				.price(request.getBonus().getPrice()).build();
	}
}
