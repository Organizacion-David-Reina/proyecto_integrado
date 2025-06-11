package com.proyectointegrado.reina_cabrera_david.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import com.proyectointegrado.reina_cabrera_david.bean.Bonus;
import com.proyectointegrado.reina_cabrera_david.bean.Student;
import com.proyectointegrado.reina_cabrera_david.constants.ErrorConstants;
import com.proyectointegrado.reina_cabrera_david.entity.BonusEntity;
import com.proyectointegrado.reina_cabrera_david.entity.StudentEntity;
import com.proyectointegrado.reina_cabrera_david.exceptions.InternalServerException;
import com.proyectointegrado.reina_cabrera_david.repository.ReservationRepository;
import com.proyectointegrado.reina_cabrera_david.repository.StudentsRepository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.dao.DataIntegrityViolationException;

class StudentServiceTest {

    @Mock
    private StudentsRepository studentsRepository;

    @Mock
    private ReservationRepository reservationRepository;

    @InjectMocks
    private StudentService studentService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    // Helper method para crear un Student de prueba
    private Student buildStudent() {
        return Student.builder()
                .id(1)
                .name("Alice")
                .lastname("Wonderland")
                .nif("11122233X")
                .address("Magic Street 123")
                .phoneNumber("123456789")
                .dayOfBirth(LocalDate.of(2000, 1, 1))
                .bonus(Bonus.builder()
                        .id(5)
                        .bondType("Premium")
                        .price(99.99)
                        .build())
                .build();
    }

    @Test
    void saveStudent_Success() {
        Student student = buildStudent();

        when(studentsRepository.save(any(StudentEntity.class))).thenReturn(new StudentEntity());

        assertDoesNotThrow(() -> studentService.saveStudent(student));
        verify(studentsRepository).save(any(StudentEntity.class));
    }

    @Test
    void saveStudent_DataIntegrityViolationException() {
        Student student = buildStudent();

        when(studentsRepository.save(any(StudentEntity.class)))
                .thenThrow(new DataIntegrityViolationException("Duplicate"));

        InternalServerException ex = assertThrows(InternalServerException.class,
                () -> studentService.saveStudent(student));

        assertEquals(ErrorConstants.CREDENTIALS_ALREADY_REGISTERED, ex.getMessage());
    }

    @Test
    void saveStudent_OtherException() {
        Student student = buildStudent();

        when(studentsRepository.save(any(StudentEntity.class)))
                .thenThrow(new RuntimeException("Some error"));

        InternalServerException ex = assertThrows(InternalServerException.class,
                () -> studentService.saveStudent(student));

        assertEquals(ErrorConstants.INTERNAL_ERROR, ex.getMessage());
    }

    @Test
    void getAllStudents_ReturnsList() {
        StudentEntity entity = StudentEntity.builder()
                .id(1)
                .name("Alice")
                .lastname("Wonderland")
                .nif("11122233X")
                .address("Magic Street 123")
                .phoneNumber("123456789")
                .dayOfBirth(LocalDate.of(2000, 1, 1))
                .bonus(BonusEntity.builder()
                        .id(5)
                        .bondType("Premium")
                        .price(99.99)
                        .build())
                .build();

        when(studentsRepository.findAll()).thenReturn(Arrays.asList(entity));

        List<Student> students = studentService.getAllStudents();

        assertEquals(1, students.size());
        assertEquals("Alice", students.get(0).getName());
        assertEquals("Premium", students.get(0).getBonus().getBondType());
    }

    @Test
    void updateStudent_Success() {
        Student student = buildStudent();

        when(studentsRepository.save(any(StudentEntity.class))).thenReturn(new StudentEntity());

        assertDoesNotThrow(() -> studentService.updateStudent(student));
        verify(studentsRepository).save(any(StudentEntity.class));
        verify(studentsRepository).flush();
    }

    @Test
    void updateStudent_DataIntegrityViolationException() {
        Student student = buildStudent();

        when(studentsRepository.save(any(StudentEntity.class)))
                .thenThrow(new DataIntegrityViolationException("Duplicate"));

        InternalServerException ex = assertThrows(InternalServerException.class,
                () -> studentService.updateStudent(student));

        assertEquals(ErrorConstants.CREDENTIALS_ALREADY_REGISTERED, ex.getMessage());
    }

    @Test
    void updateStudent_OtherException() {
        Student student = buildStudent();

        when(studentsRepository.save(any(StudentEntity.class)))
                .thenThrow(new RuntimeException("Some error"));

        InternalServerException ex = assertThrows(InternalServerException.class,
                () -> studentService.updateStudent(student));

        assertEquals(ErrorConstants.INTERNAL_ERROR, ex.getMessage());
    }

    @Test
    void getStudentsByClassId_ReturnsList() {
        StudentEntity entity = StudentEntity.builder()
                .id(2)
                .name("Bob")
                .lastname("Builder")
                .nif("99988877Y")
                .bonus(BonusEntity.builder()
                        .id(3)
                        .bondType("Standard")
                        .price(49.99)
                        .build())
                .build();

        when(studentsRepository.findStudentsByClassId(10)).thenReturn(Collections.singletonList(entity));

        List<Student> students = studentService.getStudentsByClassId(10);

        assertEquals(1, students.size());
        assertEquals("Bob", students.get(0).getName());
        assertEquals("Standard", students.get(0).getBonus().getBondType());
    }

    @Test
    void deleteStudent_Success() {
        StudentEntity studentEntity = StudentEntity.builder().id(1).build();

        when(studentsRepository.findById(1)).thenReturn(Optional.of(studentEntity));

        assertDoesNotThrow(() -> studentService.deleteStudent(1));
        verify(reservationRepository).deleteByStudentEntityId(1);
        verify(studentsRepository).delete(studentEntity);
    }

    @Test
    void deleteStudent_StudentNotFound() {
        when(studentsRepository.findById(1)).thenReturn(Optional.empty());

        InternalServerException ex = assertThrows(InternalServerException.class,
                () -> studentService.deleteStudent(1));

        assertEquals(ErrorConstants.STUDENT_NOT_EXISTS, ex.getMessage());
    }

    @Test
    void deleteStudent_Exception() {
        when(studentsRepository.findById(1)).thenThrow(new RuntimeException("DB error"));

        InternalServerException ex = assertThrows(InternalServerException.class,
                () -> studentService.deleteStudent(1));

        assertEquals("DB error", ex.getMessage());
    }
}
