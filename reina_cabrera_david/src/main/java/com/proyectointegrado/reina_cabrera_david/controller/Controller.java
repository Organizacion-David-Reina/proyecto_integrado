package com.proyectointegrado.reina_cabrera_david.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.proyectointegrado.reina_cabrera_david.bean.DanceClass;
import com.proyectointegrado.reina_cabrera_david.bean.ReservationRequest;
import com.proyectointegrado.reina_cabrera_david.bean.Student;
import com.proyectointegrado.reina_cabrera_david.bean.Teacher;
import com.proyectointegrado.reina_cabrera_david.bean.User;
import com.proyectointegrado.reina_cabrera_david.bean.UserRequest;
import com.proyectointegrado.reina_cabrera_david.constants.ApiConstants;
import com.proyectointegrado.reina_cabrera_david.service.ClassService;
import com.proyectointegrado.reina_cabrera_david.service.StudentService;
import com.proyectointegrado.reina_cabrera_david.service.TeacherService;
import com.proyectointegrado.reina_cabrera_david.service.UserService;

/**
 * The Class Controller.
 * REST controller to handle user, student, teacher, and class related endpoints.
 */
@RestController
@CrossOrigin(origins = "http://localhost:4200")
@RequestMapping(ApiConstants.ENDPOINT)
public class Controller {

	/** The User Service */
    private UserService userService;

    /** The Student Service */
    private StudentService studentService;

    /** The Teachers Service */
    private TeacherService teacherService;

    /** The Class Service */
    private ClassService classService;

    /**
     * Constructor for Controller.
     *
     * @param userService the user service
     * @param studentService the student service
     * @param teacherService the teacher service
     * @param classService the class service
     */
    protected Controller(UserService userService, StudentService studentService, TeacherService teacherService,
            ClassService classService) {
        this.userService = userService;
        this.studentService = studentService;
        this.teacherService = teacherService;
        this.classService = classService;
    }

    // Users end points

    /**
     * Logs in a user by corporate mail and password.
     *
     * @param corporateMail the corporate mail
     * @param password the password
     * @return the authenticated User object
     */
    @GetMapping("login")
    public User login(@RequestParam String corporateMail, @RequestParam String password) {
        return userService.login(corporateMail, password);
    }

    /**
     * Signs up a new user.
     *
     * @param request the user request body containing sign up data
     * @return ResponseEntity with HTTP CREATED status
     */
    @PostMapping("sign-up")
    public ResponseEntity<String> signUp(@RequestBody UserRequest request) {
        userService.signUp(request);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    /**
     * Gets a list of all users.
     *
     * @return list of User objects
     */
    @GetMapping("users-list")
    public List<User> getAllUsers() {
        return userService.getAllUsers();
    }

    /**
     * Updates an existing user.
     *
     * @param request the user data to update
     * @return ResponseEntity with HTTP CREATED status
     */
    @PutMapping("update-user")
    public ResponseEntity<String> updateUser(@RequestBody User request) {
        userService.updateUser(request);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    /**
     * Deletes a user by ID.
     *
     * @param userId the ID of the user to delete
     */
    @DeleteMapping("delete-user/{user_id}")
    public void deleteUser(@PathVariable("user_id") int userId) {
        userService.deleteUser(userId);
    }

    // Students end points

    /**
     * Saves a new student.
     *
     * @param request the student data to save
     * @return ResponseEntity with HTTP CREATED status
     */
    @PostMapping("save-student")
    public ResponseEntity<String> saveStudent(@RequestBody Student request) {
        studentService.saveStudent(request);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    /**
     * Gets all students.
     *
     * @return list of Student objects
     */
    @GetMapping("students-list")
    public List<Student> getAllStudents() {
        return studentService.getAllStudents();
    }

    /**
     * Updates an existing student.
     *
     * @param request the student data to update
     * @return ResponseEntity with HTTP CREATED status
     */
    @PutMapping("update-student")
    public ResponseEntity<String> updateStudent(@RequestBody Student request) {
        studentService.updateStudent(request);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    /**
     * Gets students reserved for a specific class.
     *
     * @param classId the ID of the class
     * @return list of Student objects reserved for the class
     */
    @GetMapping("students-reserved/{class_id}")
    public List<Student> getStudentsByClassId(@PathVariable("class_id") int classId) {
        return studentService.getStudentsByClassId(classId);
    }

    /**
     * Deletes a student by ID.
     *
     * @param studentId the ID of the student to delete
     */
    @DeleteMapping("delete-student/{student_id}")
    public void deleteStudent(@PathVariable("student_id") int studentId) {
        studentService.deleteStudent(studentId);
    }

    // Teachers end points

    /**
     * Saves a new teacher.
     *
     * @param request the teacher data to save
     * @return ResponseEntity with HTTP CREATED status
     */
    @PostMapping("save-teacher")
    public ResponseEntity<String> saveTeacher(@RequestBody Teacher request) {
        teacherService.saveTeacher(request);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    /**
     * Gets all teachers.
     *
     * @return list of Teacher objects
     */
    @GetMapping("teachers-list")
    public List<Teacher> getAllTeachers() {
        return teacherService.getAllTeachers();
    }

    /**
     * Updates an existing teacher.
     *
     * @param request the teacher data to update
     * @return ResponseEntity with HTTP CREATED status
     */
    @PutMapping("update-teacher")
    public ResponseEntity<String> updateTeacher(@RequestBody Teacher request) {
        teacherService.updateTeacher(request);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    /**
     * Deletes a teacher by ID.
     *
     * @param teacherId the ID of the teacher to delete
     */
    @DeleteMapping("delete-teacher/{teacher_id}")
    public void deleteTeacher(@PathVariable("teacher_id") int teacherId) {
        teacherService.deleteTeacher(teacherId);
    }

    // Classes end points

    /**
     * Saves a new dance class.
     *
     * @param request the dance class data to save
     * @return ResponseEntity with HTTP CREATED status
     */
    @PostMapping("save-class")
    public ResponseEntity<String> saveClass(@RequestBody DanceClass request) {
        classService.saveClass(request);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    /**
     * Saves a new reservation for a class.
     *
     * @param request the reservation request data
     * @return ResponseEntity with HTTP CREATED status
     */
    @PostMapping("save-reservation")
    public ResponseEntity<String> saveReservation(@RequestBody ReservationRequest request) {
        classService.saveReservation(request);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    /**
     * Updates an existing dance class.
     *
     * @param request the dance class data to update
     * @return ResponseEntity with HTTP CREATED status
     */
    @PutMapping("update-class")
    public ResponseEntity<String> updateClass(@RequestBody DanceClass request) {
        classService.updateClass(request);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    /**
     * Deletes a dance class by ID.
     *
     * @param classId the ID of the class to delete
     */
    @DeleteMapping("delete-class/{class_id}")
    public void deleteClass(@PathVariable("class_id") int classId) {
        classService.deleteClass(classId);
    }

    /**
     * Deletes a reservation for a student and class.
     *
     * @param studentId the student ID
     * @param classId the class ID
     */
    @DeleteMapping("delete-reservation/{student_id}/{class_id}")
    public void deleteReservation(@PathVariable("student_id") int studentId, @PathVariable("class_id") int classId) {
        classService.deleteReservation(studentId, classId);
    }

    /**
     * Gets the list of all dance classes.
     *
     * @return list of DanceClass objects
     */
    @GetMapping("classes-list")
    public List<DanceClass> getClasses() {
        return classService.getClasses();
    }

}