package com.proyectointegrado.reina_cabrera_david.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import com.proyectointegrado.reina_cabrera_david.bean.Teacher;
import com.proyectointegrado.reina_cabrera_david.constants.ErrorConstants;
import com.proyectointegrado.reina_cabrera_david.entity.ClassEntity;
import com.proyectointegrado.reina_cabrera_david.entity.TeacherEntity;
import com.proyectointegrado.reina_cabrera_david.exceptions.InternalServerException;
import com.proyectointegrado.reina_cabrera_david.repository.ClassRepository;
import com.proyectointegrado.reina_cabrera_david.repository.ReservationRepository;
import com.proyectointegrado.reina_cabrera_david.repository.TeachersRepository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.dao.DataIntegrityViolationException;

class TeacherServiceTest {

    @Mock
    private TeachersRepository teachersRepository;

    @Mock
    private ClassRepository classRepository;

    @Mock
    private ReservationRepository reservationRepository;

    @InjectMocks
    private TeacherService teacherService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void saveTeacher_Success() {
        Teacher teacher = buildTeacher();
        when(teachersRepository.save(any(TeacherEntity.class)))
                .thenReturn(new TeacherEntity());

        assertDoesNotThrow(() -> teacherService.saveTeacher(teacher));
        verify(teachersRepository).save(any(TeacherEntity.class));
    }

    @Test
    void saveTeacher_DataIntegrityViolationException() {
        Teacher teacher = buildTeacher();
        when(teachersRepository.save(any(TeacherEntity.class)))
                .thenThrow(new DataIntegrityViolationException("Duplicate email"));

        InternalServerException ex = assertThrows(InternalServerException.class,
                () -> teacherService.saveTeacher(teacher));
        assertEquals(ErrorConstants.CREDENTIALS_ALREADY_REGISTERED, ex.getMessage());
    }

    @Test
    void getAllTeachers_ReturnsList() {
        TeacherEntity teacherEntity = TeacherEntity.builder()
                .id(1)
                .name("John")
                .lastname("Doe")
                .nif("12345678A")
                .mail("john@example.com")
                .address("123 Street")
                .phoneNumber("123456789")
                .dayOfBirth(LocalDate.of(1990, 1, 1))
                .build();

        when(teachersRepository.findAll()).thenReturn(Arrays.asList(teacherEntity));

        List<Teacher> teachers = teacherService.getAllTeachers();
        assertEquals(1, teachers.size());
        assertEquals("John", teachers.get(0).getName());
    }

    @Test
    void updateTeacher_Success() {
        Teacher teacher = buildTeacher();
        when(teachersRepository.save(any(TeacherEntity.class)))
                .thenReturn(new TeacherEntity());

        assertDoesNotThrow(() -> teacherService.updateTeacher(teacher));
        verify(teachersRepository).save(any(TeacherEntity.class));
        verify(teachersRepository).flush();
    }

    @Test
    void updateTeacher_DataIntegrityViolationException() {
        Teacher teacher = buildTeacher();
        when(teachersRepository.save(any(TeacherEntity.class)))
                .thenThrow(new DataIntegrityViolationException("Duplicate phone"));

        InternalServerException ex = assertThrows(InternalServerException.class,
                () -> teacherService.updateTeacher(teacher));
        assertEquals(ErrorConstants.MAIL_OR_PHONE_ALREADY_REGISTERED, ex.getMessage());
    }

    @Test
    void deleteTeacher_Success() {
        TeacherEntity teacherEntity = TeacherEntity.builder().id(1).build();
        ClassEntity classEntity = ClassEntity.builder().id(1).teacher(teacherEntity).build();

        when(teachersRepository.findById(1)).thenReturn(Optional.of(teacherEntity));
        when(classRepository.findByTeacherId(1)).thenReturn(Collections.singletonList(classEntity));

        assertDoesNotThrow(() -> teacherService.deleteTeacher(1));
        verify(reservationRepository).deleteByClassEntityId(1);
        verify(classRepository).deleteByTeacherId(1);
        verify(teachersRepository).delete(teacherEntity);
    }

    @Test
    void deleteTeacher_TeacherNotFound() {
        when(teachersRepository.findById(1)).thenReturn(Optional.empty());

        InternalServerException ex = assertThrows(InternalServerException.class,
                () -> teacherService.deleteTeacher(1));
        assertEquals(ErrorConstants.TEACHER_NOT_EXISTS, ex.getMessage());
    }

    @Test
    void deleteTeacher_Exception() {
        when(teachersRepository.findById(1)).thenThrow(new RuntimeException("Database error"));

        InternalServerException ex = assertThrows(InternalServerException.class,
                () -> teacherService.deleteTeacher(1));
        assertEquals("Database error", ex.getMessage());
    }

    // Helper method
    private Teacher buildTeacher() {
        return Teacher.builder()
                .id(1)
                .name("Jane")
                .lastname("Smith")
                .nif("87654321B")
                .mail("jane@example.com")
                .address("456 Avenue")
                .phoneNumber("987654321")
                .dayOfBirth(LocalDate.of(1985, 5, 20))
                .build();
    }
}
