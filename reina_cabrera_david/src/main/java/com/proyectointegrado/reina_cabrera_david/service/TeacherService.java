package com.proyectointegrado.reina_cabrera_david.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Service;

import com.proyectointegrado.reina_cabrera_david.bean.Teacher;
import com.proyectointegrado.reina_cabrera_david.constants.ErrorConstants;
import com.proyectointegrado.reina_cabrera_david.entity.TeacherEntity;
import com.proyectointegrado.reina_cabrera_david.exceptions.InternalServerException;
import com.proyectointegrado.reina_cabrera_david.repository.TeachersRepository;

import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class TeacherService {
	
	private TeachersRepository teachersRepository;

	protected TeacherService(TeachersRepository teachersRepository) {
		this.teachersRepository = teachersRepository;
	}

	public void saveTeacher(Teacher request) {
		log.info("saveTeacher - request: {} ", request.toString());
		try {
			boolean nifExists = teachersRepository.existsByNif(request.getNif());
			boolean mailExists = teachersRepository.existsByMail(request.getMail());

			if (nifExists || mailExists) {
				throw new DataIntegrityViolationException(ErrorConstants.NIF_MAIL_ALREADY_REGISTERED);
			}

			TeacherEntity teacherEntity = mapToTeacherEntity(request);
			teachersRepository.save(teacherEntity);

		} catch (DataIntegrityViolationException e) {
			log.error("saveTeacher - error - {}", e.getMessage());
			throw new InternalServerException(ErrorConstants.NIF_MAIL_ALREADY_REGISTERED, e);
		} catch (Exception e) {
			log.error("saveTeacher - error - {}", e.getMessage());
			throw new InternalServerException(ErrorConstants.INTERNAL_ERROR, e);
		}
	}

	public List<Teacher> getAllTeachers() {
		List<Teacher> teachers = teachersRepository
				.findAll().stream().map(t -> Teacher.builder().id(t.getId()).name(t.getName()).lastname(t.getLastname())
						.nif(t.getNif()).mail(t.getMail())
						.build())
				.collect(Collectors.toList());
		return teachers;
	}

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
			throw new InternalServerException(ErrorConstants.MAIL_ALREADY_REGISTERED, e);
		} catch (Exception e) {
			log.error("updateTeacher - error - {}", e.getMessage());
			throw new InternalServerException(ErrorConstants.INTERNAL_ERROR, e);
		}
	}

	private TeacherEntity mapToTeacherEntity(Teacher request) {
		return TeacherEntity.builder().id(request.getId()).name(request.getName()).lastname(request.getLastname()).nif(request.getNif())
				.mail(request.getMail()).build();
	}

}
