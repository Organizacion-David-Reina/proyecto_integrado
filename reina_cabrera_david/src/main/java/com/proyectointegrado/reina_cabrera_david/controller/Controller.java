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

@RestController
@CrossOrigin(origins = "http://localhost:4200")
@RequestMapping(ApiConstants.ENDPOINT)
public class Controller {

	private UserService userService;

	private StudentService studentService;

	private TeacherService teacherService;

	private ClassService classService;

	protected Controller(UserService userService, StudentService studentService, TeacherService teacherService,
			ClassService classService) {
		this.userService = userService;
		this.studentService = studentService;
		this.teacherService = teacherService;
		this.classService = classService;
	}

	// Users end points
	@GetMapping("login")
	public User login(@RequestParam String corporateMail, @RequestParam String password) {
		return userService.login(corporateMail, password);
	}

	@PostMapping("sign-up")
	public ResponseEntity<String> signUp(@RequestBody UserRequest request) {
		userService.signUp(request);
		return ResponseEntity.status(HttpStatus.CREATED).build();
	}

	@GetMapping("users-list")
	public List<User> getAllUsers() {
		return userService.getAllUsers();
	}

	@PutMapping("update-user")
	public ResponseEntity<String> updateUser(@RequestBody User request) {
		userService.updateUser(request);
		return ResponseEntity.status(HttpStatus.CREATED).build();
	}
	
	@DeleteMapping("delete-user/{user_id}")
	public void deleteUser(@PathVariable("user_id") int userId) {
		userService.deleteUser(userId);
	}

	// Students end points

	@PostMapping("save-student")
	public ResponseEntity<String> saveStudent(@RequestBody Student request) {
		studentService.saveStudent(request);
		return ResponseEntity.status(HttpStatus.CREATED).build();
	}

	@GetMapping("students-list")
	public List<Student> getAllStudents() {
		return studentService.getAllStudents();
	}

	@PutMapping("update-student")
	public ResponseEntity<String> updateStudent(@RequestBody Student request) {
		studentService.updateStudent(request);
		return ResponseEntity.status(HttpStatus.CREATED).build();
	}
	
	@GetMapping("students-reserved/{class_id}")
	public List<Student> getStudentsByClassId(@PathVariable("class_id") int classId) {
		return studentService.getStudentsByClassId(classId);
	}
	
	@DeleteMapping("delete-student/{student_id}")
	public void deleteStudent(@PathVariable("student_id") int studentId) {
		studentService.deleteStudent(studentId);
	}

	// Teachers end points

	@PostMapping("save-teacher")
	public ResponseEntity<String> saveTeacher(@RequestBody Teacher request) {
		teacherService.saveTeacher(request);
		return ResponseEntity.status(HttpStatus.CREATED).build();
	}

	@GetMapping("teachers-list")
	public List<Teacher> getAllTeachers() {
		return teacherService.getAllTeachers();
	}

	@PutMapping("update-teacher")
	public ResponseEntity<String> updateTeacher(@RequestBody Teacher request) {
		teacherService.updateTeacher(request);
		return ResponseEntity.status(HttpStatus.CREATED).build();
	}
	
	@DeleteMapping("delete-teacher/{teacher_id}")
	public void deleteTeacher(@PathVariable("teacher_id") int teacherId) {
		teacherService.deleteTeacher(teacherId);
	}

	// Classes end points
	
	@PostMapping("save-class")
	public ResponseEntity<String> saveClass(@RequestBody DanceClass request) {
		classService.saveClass(request);
		return ResponseEntity.status(HttpStatus.CREATED).build();
	}
	
	@PostMapping("save-reservation")
	public ResponseEntity<String> saveReservation(@RequestBody ReservationRequest request) {
		classService.saveReservation(request);
		return ResponseEntity.status(HttpStatus.CREATED).build();
	}
	
	@PutMapping("update-class")
	public ResponseEntity<String> updateClass(@RequestBody DanceClass request) {
		classService.updateClass(request);
		return ResponseEntity.status(HttpStatus.CREATED).build();
	}
	
	@DeleteMapping("delete-class/{class_id}")
	public void deleteClass(@PathVariable("class_id") int classId) {
		classService.deleteClass(classId);
	}

	@GetMapping("classes-list")
	public List<DanceClass> getClasses() {
		return classService.getClasses();
	}
	
	@GetMapping("styles-list")
	public List<Style> getStyles() {
		return classService.getStyles();
	}
	
	@GetMapping("rooms-list")
	public List<Room> getRooms() {
		return classService.getRooms();
	}
}
