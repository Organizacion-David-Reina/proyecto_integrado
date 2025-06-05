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
import com.proyectointegrado.reina_cabrera_david.bean.Room;
import com.proyectointegrado.reina_cabrera_david.bean.Student;
import com.proyectointegrado.reina_cabrera_david.bean.Style;
import com.proyectointegrado.reina_cabrera_david.bean.Teacher;
import com.proyectointegrado.reina_cabrera_david.bean.User;
import com.proyectointegrado.reina_cabrera_david.bean.UserRequest;
import com.proyectointegrado.reina_cabrera_david.constants.ApiConstants;
import com.proyectointegrado.reina_cabrera_david.service.ClassService;
import com.proyectointegrado.reina_cabrera_david.service.StudentService;
import com.proyectointegrado.reina_cabrera_david.service.TeacherService;
import com.proyectointegrado.reina_cabrera_david.service.UserService;

/**
 * The Class Controller
 */
@RestController
@CrossOrigin(origins = "http://localhost:4200")
@RequestMapping(ApiConstants.ENDPOINT)
public class Controller {

	/** The User Service */
    private UserService userService;
    
    /** The Student Service */
    private StudentService studentService;
    
    /** The Teacher Service */
    private TeacherService teacherService;
    
    /** The Class Service */
    private ClassService classService;

    /**
     * Constructor injecting the required services.
     * 
     * @param userService User management service
     * @param studentService Student management service
     * @param teacherService Teacher management service
     * @param classService Class and reservation management service
     */
    protected Controller(UserService userService, StudentService studentService, TeacherService teacherService,
                         ClassService classService) {
        this.userService = userService;
        this.studentService = studentService;
        this.teacherService = teacherService;
        this.classService = classService;
    }

    // ---------- USERS ----------

    /**
     * Logs in a user by verifying credentials.
     * 
     * @param corporateMail User's corporate email
     * @param password User's password
     * @return User object if credentials are valid
     */
    @GetMapping("login")
    public User login(@RequestParam String corporateMail, @RequestParam String password) {
        return userService.login(corporateMail, password);
    }

    /**
     * Registers a new user.
     * 
     * @param request User registration request object
     * @return HTTP 201 Created if successful
     */
    @PostMapping("sign-up")
    public ResponseEntity<String> signUp(@RequestBody UserRequest request) {
        userService.signUp(request);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    /**
     * Retrieves all registered users.
     * 
     * @return List of all users
     */
    @GetMapping("users-list")
    public List<User> getAllUsers() {
        return userService.getAllUsers();
    }

    /**
     * Updates user information.
     * 
     * @param request Updated user object
     * @return HTTP 201 Created if successful
     */
    @PutMapping("update-user")
    public ResponseEntity<String> updateUser(@RequestBody User request) {
        userService.updateUser(request);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    /**
     * Deletes a user by ID.
     * 
     * @param userId ID of the user to delete
     */
    @DeleteMapping("delete-user/{user_id}")
    public void deleteUser(@PathVariable("user_id") int userId) {
        userService.deleteUser(userId);
    }

    // ---------- STUDENTS ----------

    /**
     * Saves a new student.
     * 
     * @param request Student object to save
     * @return HTTP 201 Created if successful
     */
    @PostMapping("save-student")
    public ResponseEntity<String> saveStudent(@RequestBody Student request) {
        studentService.saveStudent(request);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    /**
     * Retrieves all students.
     * 
     * @return List of all students
     */
    @GetMapping("students-list")
    public List<Student> getAllStudents() {
        return studentService.getAllStudents();
    }

    /**
     * Updates student information.
     * 
     * @param request Updated student object
     * @return HTTP 201 Created if successful
     */
    @PutMapping("update-student")
    public ResponseEntity<String> updateStudent(@RequestBody Student request) {
        studentService.updateStudent(request);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    /**
     * Retrieves students enrolled in a specific class.
     * 
     * @param classId ID of the class
     * @return List of students enrolled in the class
     */
    @GetMapping("students-reserved/{class_id}")
    public List<Student> getStudentsByClassId(@PathVariable("class_id") int classId) {
        return studentService.getStudentsByClassId(classId);
    }

    /**
     * Deletes a student by ID.
     * 
     * @param studentId ID of the student to delete
     */
    @DeleteMapping("delete-student/{student_id}")
    public void deleteStudent(@PathVariable("student_id") int studentId) {
        studentService.deleteStudent(studentId);
    }

    // ---------- TEACHERS ----------

    /**
     * Saves a new teacher.
     * 
     * @param request Teacher object to save
     * @return HTTP 201 Created if successful
     */
    @PostMapping("save-teacher")
    public ResponseEntity<String> saveTeacher(@RequestBody Teacher request) {
        teacherService.saveTeacher(request);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    /**
     * Retrieves all teachers.
     * 
     * @return List of all teachers
     */
    @GetMapping("teachers-list")
    public List<Teacher> getAllTeachers() {
        return teacherService.getAllTeachers();
    }

    /**
     * Updates teacher information.
     * 
     * @param request Updated teacher object
     * @return HTTP 201 Created if successful
     */
    @PutMapping("update-teacher")
    public ResponseEntity<String> updateTeacher(@RequestBody Teacher request) {
        teacherService.updateTeacher(request);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    /**
     * Deletes a teacher by ID.
     * 
     * @param teacherId ID of the teacher to delete
     */
    @DeleteMapping("delete-teacher/{teacher_id}")
    public void deleteTeacher(@PathVariable("teacher_id") int teacherId) {
        teacherService.deleteTeacher(teacherId);
    }

    // ---------- CLASSES ----------

    /**
     * Saves a new dance class.
     * 
     * @param request Dance class object to save
     * @return HTTP 201 Created if successful
     */
    @PostMapping("save-class")
    public ResponseEntity<String> saveClass(@RequestBody DanceClass request) {
        classService.saveClass(request);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    /**
     * Saves a reservation for a student in a class.
     * 
     * @param request Reservation request object
     * @return HTTP 201 Created if successful
     */
    @PostMapping("save-reservation")
    public ResponseEntity<String> saveReservation(@RequestBody ReservationRequest request) {
        classService.saveReservation(request);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    /**
     * Updates a dance class.
     * 
     * @param request Updated dance class object
     * @return HTTP 201 Created if successful
     */
    @PutMapping("update-class")
    public ResponseEntity<String> updateClass(@RequestBody DanceClass request) {
        classService.updateClass(request);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    /**
     * Deletes a dance class by ID.
     * 
     * @param classId ID of the class to delete
     */
    @DeleteMapping("delete-class/{class_id}")
    public void deleteClass(@PathVariable("class_id") int classId) {
        classService.deleteClass(classId);
    }

    /**
     * Deletes a reservation linking a student to a class.
     * 
     * @param studentId ID of the student
     * @param classId ID of the class
     */
    @DeleteMapping("delete-reservation/{student_id}/{class_id}")
    public void deleteReservation(@PathVariable("student_id") int studentId, @PathVariable("class_id") int classId) {
        classService.deleteReservation(studentId, classId);
    }

    /**
     * Retrieves all dance classes.
     * 
     * @return List of all dance classes
     */
    @GetMapping("classes-list")
    public List<DanceClass> getClasses() {
        return classService.getClasses();
    }

    /**
     * Retrieves all available dance styles.
     * 
     * @return List of all styles
     */
    @GetMapping("styles-list")
    public List<Style> getStyles() {
        return classService.getStyles();
    }

    /**
     * Retrieves all available rooms.
     * 
     * @return List of all rooms
     */
    @GetMapping("rooms-list")
    public List<Room> getRooms() {
        return classService.getRooms();
    }
}
