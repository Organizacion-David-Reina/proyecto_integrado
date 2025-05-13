package com.proyectointegrado.reina_cabrera_david.service;

import java.util.List;
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
import com.proyectointegrado.reina_cabrera_david.repository.StudentsRepository;

import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class StudentService {

	private StudentsRepository studentsRepository;

	protected StudentService(StudentsRepository studentsRepository) {
		this.studentsRepository = studentsRepository;
	}

	public void saveStudent(Student request) {
		log.info("saveStudent - request: {} ", request.toString());
		try {
			boolean nifExists = studentsRepository.existsByNif(request.getNif());

			if (nifExists) {
				throw new DataIntegrityViolationException(ErrorConstants.NIF_ALREADY_REGISTERED);
			}

			BonusEntity bonusEntity = mapToBonusEntity(request);
			StudentEntity studentEntity = mapToStudentEntity(request, bonusEntity);
			studentsRepository.save(studentEntity);

		} catch (DataIntegrityViolationException e) {
			log.error("saveStudent - error - {}", e.getMessage());
			throw new InternalServerException(ErrorConstants.NIF_ALREADY_REGISTERED, e);
		} catch (Exception e) {
			log.error("saveStudent - error - {}", e.getMessage());
			throw new InternalServerException(ErrorConstants.INTERNAL_ERROR, e);
		}
	}

	public List<Student> getAllStudents() {
		List<Student> students = studentsRepository
				.findAll().stream().map(s -> Student.builder().id(s.getId()).name(s.getName()).lastname(s.getLastname())
						.nif(s.getNif()).bonus(Bonus.builder().id(s.getBonus().getId())
								.bondType(s.getBonus().getBondType()).price(s.getBonus().getPrice()).build())
						.build())
				.collect(Collectors.toList());
		return students;
	}

	@Modifying(clearAutomatically = true)
	@Transactional
	public void updateStudent(Student request) {
		log.info("updateStudent - user: {} ", request.toString());
		try {
			BonusEntity bonusEntity = mapToBonusEntity(request);
			StudentEntity studentEntity = StudentEntity.builder().id(request.getId()).name(request.getName())
					.lastname(request.getLastname()).nif(request.getNif()).bonus(bonusEntity).build();
			studentsRepository.save(studentEntity);
		} catch (DataIntegrityViolationException e) {
			log.error("updateStudent - error - {}", e.getMessage());
			throw new InternalServerException(ErrorConstants.NIF_ALREADY_REGISTERED, e);
		} catch (Exception e) {
			log.error("updateStudent - error - {}", e.getMessage());
			throw new InternalServerException(ErrorConstants.INTERNAL_ERROR, e);
		}
	}

	private StudentEntity mapToStudentEntity(Student request, BonusEntity bonusEntity) {
		return StudentEntity.builder().name(request.getName()).lastname(request.getLastname()).nif(request.getNif())
				.bonus(bonusEntity).build();
	}

	private BonusEntity mapToBonusEntity(Student request) {
		return BonusEntity.builder().id(request.getBonus().getId()).bondType(request.getBonus().getBondType())
				.price(request.getBonus().getPrice()).build();
	}
}
