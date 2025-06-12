package com.proyectointegrado.reina_cabrera_david.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.proyectointegrado.reina_cabrera_david.bean.DanceClass;
import com.proyectointegrado.reina_cabrera_david.bean.ReservationRequest;
import com.proyectointegrado.reina_cabrera_david.bean.Room;
import com.proyectointegrado.reina_cabrera_david.bean.Student;
import com.proyectointegrado.reina_cabrera_david.bean.Style;
import com.proyectointegrado.reina_cabrera_david.bean.Teacher;
import com.proyectointegrado.reina_cabrera_david.bean.User;
import com.proyectointegrado.reina_cabrera_david.bean.UserRequest;
import com.proyectointegrado.reina_cabrera_david.service.ClassService;
import com.proyectointegrado.reina_cabrera_david.service.StudentService;
import com.proyectointegrado.reina_cabrera_david.service.TeacherService;
import com.proyectointegrado.reina_cabrera_david.service.UserService;

@ExtendWith(MockitoExtension.class)
public class ControllerTest {

    @Mock
    private UserService userService;

    @Mock
    private StudentService studentService;

    @Mock
    private TeacherService teacherService;

    @Mock
    private ClassService classService;

    @InjectMocks
    private Controller controller;

    @Test
    public void loginTest() {
        when(userService.login(anyString(), anyString())).thenReturn(new User());
        User user = controller.login("user@example.com", "pass");
        assertNotNull(user);
        verify(userService).login("user@example.com", "pass");
    }

    @Test
    public void signUpTest() {
        doNothing().when(userService).signUp(any(UserRequest.class));
        ResponseEntity<String> response = controller.signUp(new UserRequest());
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        verify(userService).signUp(any(UserRequest.class));
    }

    @Test
    public void getAllUsersTest() {
        when(userService.getAllUsers()).thenReturn(Arrays.asList(new User(), new User()));
        List<User> users = controller.getAllUsers();
        assertEquals(2, users.size());
        verify(userService).getAllUsers();
    }

    @Test
    public void updateUserTest() {
        doNothing().when(userService).updateUser(any(User.class));
        ResponseEntity<String> response = controller.updateUser(new User());
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        verify(userService).updateUser(any(User.class));
    }

    @Test
    public void deleteUserTest() {
        doNothing().when(userService).deleteUser(anyInt());
        controller.deleteUser(1);
        verify(userService).deleteUser(1);
    }

    @Test
    public void saveStudentTest() {
        doNothing().when(studentService).saveStudent(any(Student.class));
        ResponseEntity<String> response = controller.saveStudent(new Student());
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        verify(studentService).saveStudent(any(Student.class));
    }

    @Test
    public void getAllStudentsTest() {
        when(studentService.getAllStudents()).thenReturn(Arrays.asList(new Student(), new Student()));
        List<Student> students = controller.getAllStudents();
        assertEquals(2, students.size());
        verify(studentService).getAllStudents();
    }

    @Test
    public void updateStudentTest() {
        doNothing().when(studentService).updateStudent(any(Student.class));
        ResponseEntity<String> response = controller.updateStudent(new Student());
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        verify(studentService).updateStudent(any(Student.class));
    }

    @Test
    public void getStudentsByClassIdTest() {
        when(studentService.getStudentsByClassId(5)).thenReturn(Arrays.asList(new Student()));
        List<Student> students = controller.getStudentsByClassId(5);
        assertEquals(1, students.size());
        verify(studentService).getStudentsByClassId(5);
    }

    @Test
    public void deleteStudentTest() {
        doNothing().when(studentService).deleteStudent(anyInt());
        controller.deleteStudent(2);
        verify(studentService).deleteStudent(2);
    }

    @Test
    public void saveTeacherTest() {
        doNothing().when(teacherService).saveTeacher(any(Teacher.class));
        ResponseEntity<String> response = controller.saveTeacher(new Teacher());
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        verify(teacherService).saveTeacher(any(Teacher.class));
    }

    @Test
    public void getAllTeachersTest() {
        when(teacherService.getAllTeachers()).thenReturn(Arrays.asList(new Teacher(), new Teacher()));
        List<Teacher> teachers = controller.getAllTeachers();
        assertEquals(2, teachers.size());
        verify(teacherService).getAllTeachers();
    }

    @Test
    public void updateTeacherTest() {
        doNothing().when(teacherService).updateTeacher(any(Teacher.class));
        ResponseEntity<String> response = controller.updateTeacher(new Teacher());
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        verify(teacherService).updateTeacher(any(Teacher.class));
    }

    @Test
    public void deleteTeacherTest() {
        doNothing().when(teacherService).deleteTeacher(anyInt());
        controller.deleteTeacher(3);
        verify(teacherService).deleteTeacher(3);
    }

    @Test
    public void saveClassTest() {
        doNothing().when(classService).saveClass(any(DanceClass.class));
        ResponseEntity<String> response = controller.saveClass(new DanceClass());
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        verify(classService).saveClass(any(DanceClass.class));
    }

    @Test
    public void saveReservationTest() {
        doNothing().when(classService).saveReservation(any(ReservationRequest.class));
        ResponseEntity<String> response = controller.saveReservation(new ReservationRequest());
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        verify(classService).saveReservation(any(ReservationRequest.class));
    }

    @Test
    public void updateClassTest() {
        doNothing().when(classService).updateClass(any(DanceClass.class));
        ResponseEntity<String> response = controller.updateClass(new DanceClass());
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        verify(classService).updateClass(any(DanceClass.class));
    }

    @Test
    public void deleteClassTest() {
        doNothing().when(classService).deleteClass(anyInt());
        controller.deleteClass(4);
        verify(classService).deleteClass(4);
    }

    @Test
    public void deleteReservationTest() {
        doNothing().when(classService).deleteReservation(anyInt(), anyInt());
        controller.deleteReservation(1, 2);
        verify(classService).deleteReservation(1, 2);
    }

    @Test
    public void getClassesTest() {
        when(classService.getClasses()).thenReturn(Arrays.asList(new DanceClass(), new DanceClass()));
        List<DanceClass> classes = controller.getClasses();
        assertEquals(2, classes.size());
        verify(classService).getClasses();
    }

    @Test
    public void getStylesTest() {
        when(classService.getStyles()).thenReturn(Arrays.asList(new Style(), new Style()));
        List<Style> styles = controller.getStyles();
        assertEquals(2, styles.size());
        verify(classService).getStyles();
    }

    @Test
    public void getRoomsTest() {
        when(classService.getRooms()).thenReturn(Arrays.asList(new Room(), new Room()));
        List<Room> rooms = controller.getRooms();
        assertEquals(2, rooms.size());
        verify(classService).getRooms();
    }
}
